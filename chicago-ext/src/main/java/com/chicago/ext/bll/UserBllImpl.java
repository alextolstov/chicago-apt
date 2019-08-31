package com.chicago.ext.bll;

import com.chicago.common.util.PasswordUtil;
import com.chicago.dto.OrganizationOuterClass;
import com.chicago.dto.UserOuterClass;
import com.chicago.ext.dal.OrganizationDal;
import com.chicago.ext.dal.PasswordNotMatchException;
import com.chicago.ext.dal.PermissionDal;
import com.chicago.ext.dal.UserDal;
import com.chicago.ext.dal.UserNotFoundException;
import com.chicago.ext.util.Pair;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Service
public class UserBllImpl implements UserBll
{
    private static final Logger LOG = LoggerFactory.getLogger(UserBllImpl.class);
    @Inject
    private UserDal _userDal;
    @Inject
    private PermissionDal _permissionDal;
    @Inject
    private OrganizationDal _organizationDal;

    @Override
    public UserOuterClass.User createAdminUser(UserOuterClass.User user) throws Exception
    {
        if (_userDal.isCellPhoneExists(user.getCellPhone()))
        {
            throw new Exception("User with cell phone " + user.getCellPhone() + " already exists");
        }
        if (_userDal.isEmailExists(user.getEmail()))
        {
            throw new Exception("User with email " + user.getEmail() + " already exists");
        }

        OrganizationOuterClass.Organization holding = OrganizationOuterClass.Organization.newBuilder()
                .setType(OrganizationOuterClass.OrganizationType.HOLDING)
                .setName("New holding")
                .setDescription("New holding")
                .build();
        String holdingId = _organizationDal.createOrganization(holding);
        LOG.info("New holding {} created for user {}", holdingId, user.getEmail());

        // Admin user is user #1, he needs company as well
        OrganizationOuterClass.Organization company = OrganizationOuterClass.Organization.newBuilder()
                .setType(OrganizationOuterClass.OrganizationType.COMPANY)
                .setParentOrganizationId(holdingId)
                .setName("New company")
                .setDescription("New company")
                .build();
        String organizationId = _organizationDal.createOrganization(company);
        LOG.info("New company {} created for user with email {} and cell phone {}", organizationId, user.getEmail(), user.getCellPhone());

        // Now set new company for user
        user = UserOuterClass.User.newBuilder(user)
                .setOrganizationId(organizationId)
                .build();
        UserOuterClass.User newUser = createUser(user, false);
        LOG.info("New admin user created userId {}", newUser.getUserId());

        _organizationDal.addUserToOrganization(newUser.getUserId(), organizationId );
        LOG.info("New admin user added to organizationId {}", organizationId );

        // Finally turn user to system admin and return with permissions
        Set<String> permissionNames = _permissionDal.setSystemAdminRole(newUser.getUserId());
        return newUser.toBuilder()
                .addAllPermissionNames(permissionNames)
                .build();
    }

    @Override
    public UserOuterClass.User createStandardUser(UserOuterClass.User newUser) throws Exception
    {
        UserOuterClass.User updatedUser = createUser(newUser, true);
        _organizationDal.addUserToOrganization(updatedUser.getUserId(), updatedUser.getOrganizationId());
        LOG.info("New standard user added to orgnizationId {}", updatedUser.getOrganizationId());
        return updatedUser;
    }

    @Override
    public void setUserAvatar(UserOuterClass.UserAvatar avatar) throws Exception
    {
        LOG.info("Set avatar for user: {}", avatar.getUserId());
        _userDal.setUserAvatar(avatar);
    }

    @Override
    public UserOuterClass.User authUser(String cellPhone, String email, String password) throws Exception
    {
        LOG.info("Called for user with cell phone: {} and email {}", cellPhone, email);
        Pair<String, Pair<String, byte[]>> result = null;
        try
        {
            result = _userDal.getHashSaltByCell(cellPhone);
        } catch (UserNotFoundException ex)
        {
            LOG.info("User with cell phone {} not found will try email {}", cellPhone, email);
            result = _userDal.getHashSaltByEmail(email);
        }

        String userId = result.getKey();
        Pair<String, byte[]> hashSalt = result.getValue();
        String hashPassword = PasswordUtil.getSecurePassword(password, hashSalt.getValue());

        if (!hashPassword.equals(hashSalt.getKey()))
        {
            throw new PasswordNotMatchException("Wrong password for userId " + userId);
        }

        return _userDal.getUserById(userId);
    }

    @Override
    public void setUserPassword(UserOuterClass.UserPassword password) throws Exception
    {
        byte[] passwordSalt = PasswordUtil.getSalt();
        String passwordHash = PasswordUtil.getSecurePassword(password.getPassword(), passwordSalt);
        _userDal.setUserPassword(password.getUserId(), passwordHash, passwordSalt);
    }

    @Override
    public List<UserOuterClass.User> getUsers(String organizationId) throws Exception
    {
        return _userDal.getUsers(organizationId);
    }

    @Override
    public UserOuterClass.User getUserByEmail(String email) throws Exception
    {
        return _userDal.getUserByEmail(email);
    }

    @Override
    public UserOuterClass.User getUserByCell(String cellPhone) throws Exception
    {
        return _userDal.getUserByCell(cellPhone);
    }

    @Override
    public UserOuterClass.User getUserById(String userId) throws Exception
    {
        return _userDal.getUserById(userId);
    }

    @Override
    public void updateUser(UserOuterClass.User user) throws Exception
    {
        _userDal.updateUser(user);
    }

    private UserOuterClass.User createUser(UserOuterClass.User user, boolean sendCredentials) throws Exception
    {
        if (_userDal.isCellPhoneExists(user.getCellPhone()))
        {
            throw new Exception("User with cell phone " + user.getCellPhone() + " already exists");
        }
        if (_userDal.isEmailExists(user.getEmail()))
        {
            throw new Exception("User with email " + user.getEmail() + " already exists");
        }

        String password = user.getPassword();

        if (sendCredentials)
        {
            password = generateRandomString(12);
            // TODO email password
            LOG.info("Password was generated for user {} and sent", user.getEmail());
        }

        byte[] passwordSalt = PasswordUtil.getSalt();
        String passwordHash = PasswordUtil.getSecurePassword(password, passwordSalt);
        String userId = _userDal.createUser(user, passwordHash, passwordSalt);
        return UserOuterClass.User.newBuilder(user)
                .setUserId(userId)
                .build();
    }

    private static String generateRandomString(int length)
    {
        return new Random().ints(48, 123)
                .filter(i -> (i < 57 || i > 65) && (i < 90 || i > 97))
                .mapToObj(i -> (char) i)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }
}

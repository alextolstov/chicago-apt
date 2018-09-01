package com.chicago.ext.bll;

import com.chicago.common.util.PasswordUtil;
import com.chicago.dto.Organization;
import com.chicago.dto.UserOuterClass;
import com.chicago.ext.dal.OrganizationDal;
import com.chicago.ext.dal.UserDal;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class UserBllImpl implements UserBll
{
    private static final Logger _LOG = LoggerFactory.getLogger(UserBllImpl.class);
    @Inject
    private UserDal _userDal;
    @Inject
    private OrganizationDal _organizationDal;

    public void setUserPermissions(String userId, List<Integer> roles, List<Integer> extraPermissions) throws Exception
    {
        if (roles == null)
        {
            roles = new ArrayList<>();
        }
        if (extraPermissions == null)
        {
            extraPermissions = new ArrayList<>();
        }
        UserOuterClass.UserPermissions permissions = UserOuterClass.UserPermissions.newBuilder()
                .setUserId(userId)
                .addAllRoles(roles)
                .addAllExtraPermissions(extraPermissions)
                .build();
        _userDal.setUserPermissions(permissions);
    }

    public UserOuterClass.User createAdminUser(UserOuterClass.User user) throws Exception
    {
        if (_userDal.isUserExists(user.getEmail()))
        {
            throw new Exception("User " + user.getEmail() + " already exists");
        }
        // Admin user is user #1, he needs company as well
        Organization.Company company = Organization.Company.newBuilder()
                .setName("New company")
                .setDescription("New company")
                .build();
        String companyId = _organizationDal.createCompany(company);
        _LOG.info("New company {} created for user {}", companyId, user.getEmail());

        // Now set new company for user
        user = UserOuterClass.User.newBuilder(user)
                .setCompanyId(companyId)
                .build();
        UserOuterClass.User newUser = createUser(user, false);
        _LOG.info("New user created userId {}", newUser.getUserId());

        return newUser;
    }

    public UserOuterClass.User createStandardUser(UserOuterClass.User newUser) throws Exception
    {
        return createUser(newUser, true);
    }

    public void authUser(String userName, String password) throws Exception
    {
        _LOG.info("Called for user: {}", userName);
        _userDal.authUser(userName, password);
    }

    private UserOuterClass.User createUser(UserOuterClass.User user, boolean sendCredentials) throws Exception
    {
        if (_userDal.isUserExists(user.getEmail()))
        {
            throw new Exception("User " + user.getEmail() + " already exists");
        }

        String password = user.getPassword();

        if (sendCredentials)
        {
            password = generateRandomString(12);
            // TODO email password
            _LOG.info("Password was generated for user {} and sent", user.getEmail());
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

package com.chicago.ext.dal.cassandra;

import com.chicago.common.util.TimeUtil;
import com.chicago.dto.PermissionOuterClass;
import com.chicago.dto.UserOuterClass;
import com.chicago.ext.dal.DbConnector;
import com.chicago.ext.dal.PositionDal;
import com.chicago.ext.dal.UserDal;
import com.chicago.ext.dal.UserNotFoundException;
import com.chicago.ext.dal.UserPermissionsNotFoundException;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.utils.UUIDs;
import com.google.protobuf.ByteString;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static com.chicago.ext.dal.cassandra.CassandraConstants.KEYSPACE;
import static com.chicago.ext.dal.cassandra.CassandraConstants.ORGANIZATIONS_TABLE;
import static com.chicago.ext.dal.cassandra.CassandraConstants.USERS_BY_CELL_PHONE_TABLE;
import static com.chicago.ext.dal.cassandra.CassandraConstants.USERS_BY_EMAIL_TABLE;
import static com.chicago.ext.dal.cassandra.CassandraConstants.USERS_BY_ID_TABLE;
import static com.chicago.ext.dal.cassandra.CassandraConstants.USER_PERMISSIONS_TABLE;
import static com.chicago.ext.dal.cassandra.CassandraUtil.convertToUuidSet;

public class UserDalImpl implements UserDal
{
    private static final Logger LOG = LoggerFactory.getLogger(UserDalImpl.class);

    @Inject
    private DbConnector<Session> _cassandraConnector;
    @Inject
    private PositionDal _positionDal;

    @Override
    public String createUser(UserOuterClass.User newUser, String passwordHash, byte[] passwordSalt)
    {
        // Create and associate user with new company
        UUID newUserId = UUIDs.random();
        Statement query = QueryBuilder.insertInto(KEYSPACE, USERS_BY_ID_TABLE)
                .value("user_id", newUserId)
                .value("email", newUser.getEmail())
                .value("password_hash", passwordHash)
                .value("password_salt", ByteBuffer.wrap(passwordSalt))
                .value("first_name", newUser.getFirstName())
                .value("middle_name", newUser.getMiddleName())
                .value("last_name", newUser.getLastName())
                .value("nick_name", newUser.getNickName())
                .value("cell_phone", newUser.getCellPhone())
                .value("home_phone", newUser.getHomePhone())
                .value("work_phone", newUser.getWorkPhone())
                .value("passport_number", newUser.getPassportNumber())
                .value("date_of_birth", newUser.getDateOfBirth())
                .value("employment_date", newUser.getEmploymentDate())
                .value("actual_employment_date", newUser.getActualEmploymentDate())
                .value("dismissal_date", newUser.getDismissalDate())
                .value("actual_dismissal_date", newUser.getActualDismissalDate())
                .value("tax_payer_id", newUser.getTaxPayerId())
                .value("diploma_number", newUser.getDiplomaNumber())
                .value("diploma_date", newUser.getDiplomaDate())
                .value("retirement_id_number", newUser.getRetirementIdNumber())
                .value("retirement_date", newUser.getRetirementDate())
                .value("medical_book", newUser.getMedicalBook())
                .value("medical_book_date", newUser.getMedicalBookDate())
                .value("employment_book_number", newUser.getEmploymentBookNumber())
                .value("organization_id", UUID.fromString(newUser.getOrganizationId()))
                .value("create_datetime", TimeUtil.getUtcNowInMilliseconds());
        _cassandraConnector.getSession().execute(query);
        LOG.info("New user {} created", newUser.getEmail());
        return newUserId.toString();
    }

    @Override
    public void setUserAvatar(UserOuterClass.UserAvatar avatar)
    {
        Statement query = QueryBuilder.update(KEYSPACE, USERS_BY_ID_TABLE)
                .with(QueryBuilder.set("avatar", avatar.getAvatar()))
                .where(QueryBuilder.eq("user_id", UUID.fromString(avatar.getUserId())));
        _cassandraConnector.getSession().execute(query);
    }

    @Override
    public void setUserAddress(String addressId, String userId)
    {
        Statement query = QueryBuilder.update(KEYSPACE, USERS_BY_ID_TABLE)
                .with(QueryBuilder.set("address_id", UUID.fromString(addressId)))
                .where(QueryBuilder.eq("user_id", UUID.fromString(userId)));
        _cassandraConnector.getSession().execute(query);
    }

    @Override
    public UserOuterClass.UserPermissions getUserPermissions(String userId) throws Exception
    {
        // Get all premissions
        Statement query = QueryBuilder.select()
                .from(KEYSPACE, USER_PERMISSIONS_TABLE)
                .where(QueryBuilder.eq("user_id", UUID.fromString(userId)));
        ResultSet result = _cassandraConnector.getSession().execute(query);
        Row row = result.one();
        if (row == null)
        {
            throw new UserPermissionsNotFoundException("No permissions found for user with user Id " + userId);
        }

        Set<UUID> roleIds = row.getSet("role_ids", UUID.class);
        List<PermissionOuterClass.Role> roles = new ArrayList<>();
        for (UUID roleId : roleIds)
        {
            roles.add(PermissionOuterClass.Role.newBuilder().setRoleId(roleId.toString()).build());
        }

        Set<Integer> extraPermissionIds = row.getSet("extra_permission_ids", Integer.class);
        List<PermissionOuterClass.Permission> permissions = new ArrayList<>();
        for (int permissionId : extraPermissionIds)
        {
            permissions.add(PermissionOuterClass.Permission.newBuilder().setPermissionId(permissionId).build());
        }

        return UserOuterClass.UserPermissions.newBuilder()
                .setUserId(row.getUUID("user_id").toString())
                .addAllRoles(roles)
                .addAllExtraPermissions(permissions)
                .build();
    }

    @Override
    public UserOuterClass.User getUserByEmail(String email) throws Exception
    {
        UUID userId = getUserIdByParameter(USERS_BY_EMAIL_TABLE, "email", email);
        return getUserByParameter(USERS_BY_ID_TABLE, "user_id", userId);
    }

    @Override
    public UserOuterClass.User getUserByCell(String cellPhone) throws Exception
    {
        UUID userId = getUserIdByParameter(USERS_BY_CELL_PHONE_TABLE, "cell_phone", cellPhone);
        return getUserByParameter(USERS_BY_ID_TABLE, "user_id", userId);
    }

    @Override
    public UserOuterClass.User getUserById(String userId) throws Exception
    {
        return getUserByParameter(USERS_BY_ID_TABLE, "user_id", UUID.fromString(userId));
    }

    private  UUID getUserIdByParameter(String table, String column, String value) throws Exception
    {
        Statement query = QueryBuilder.select()
                .from(KEYSPACE, table)
                .where(QueryBuilder.eq(column, value));
        ResultSet result = _cassandraConnector.getSession().execute(query);
        Row row = result.one();
        if (row == null)
        {
            throw new UserNotFoundException("No user with " + column + " " + value + " found");
        }

        // Populate protobuf
        return row.getUUID("user_id");
    }

    private  <T> UserOuterClass.User getUserByParameter(String table, String column, T value) throws Exception
    {
        Statement query = QueryBuilder.select()
                .from(KEYSPACE, table)
                .where(QueryBuilder.eq(column, value));
        ResultSet result = _cassandraConnector.getSession().execute(query);
        Row row = result.one();
        if (row == null)
        {
            throw new UserNotFoundException("No user with " + column + " " + value + " found");
        }

        // Populate protobuf
        return buildUser(row);
    }

    @Override
    public Pair<String, Pair<String, byte[]>> getHashSaltByEmail(String email) throws Exception
    {
        if (email.length() == 0)
        {
            throw new UserNotFoundException("Email field is empty");
        }
        return getHashSaltByParameter(USERS_BY_EMAIL_TABLE, "email", email);
    }

    @Override
    public Pair<String, Pair<String, byte[]>> getHashSaltByCell(String cellPhone) throws Exception
    {
        if (cellPhone.length() == 0)
        {
            throw new UserNotFoundException("Cell phone field is empty");
        }
        return getHashSaltByParameter(USERS_BY_CELL_PHONE_TABLE, "cell_phone", cellPhone);
    }

    private Pair<String, Pair<String, byte[]>> getHashSaltByParameter(String table, String column, String value) throws Exception
    {
        Statement query = QueryBuilder.select()
                .from(KEYSPACE, table)
                .where(QueryBuilder.eq(column, value));
        ResultSet result = _cassandraConnector.getSession().execute(query);
        Row row = result.one();
        if (row == null)
        {
            throw new UserNotFoundException("No user with " + column + " " + value + " found");
        }

        UUID userId = row.getUUID("user_id");
        LOG.info("Found userId {} for search value {}", userId, value);
        query = QueryBuilder.select("password_hash", "password_salt")
                .from(KEYSPACE, USERS_BY_ID_TABLE)
                .where(QueryBuilder.eq("user_id", userId));
        result = _cassandraConnector.getSession().execute(query);
        row = result.one();

        if (row == null)
        {
            throw new UserNotFoundException("No user with userId " + userId + " found");
        }
        Pair<String, byte[]> hashSalt = new Pair<>(row.getString("password_hash"), row.getBytes("password_salt").array());
        return new Pair<>(userId.toString(), hashSalt);
    }

    @Override
    // We dont care what organization type, just dig inside 3 levels. Cassandra give use very cheap read
    public List<UserOuterClass.User> getUsers(String organizationId) throws Exception
    {
        List<UUID> usersId = getOrganizationUsers(organizationId);

        Statement query = QueryBuilder.select()
                .from(KEYSPACE, USERS_BY_ID_TABLE)
                .where(QueryBuilder.in("user_id", usersId));
        ResultSet result = _cassandraConnector.getSession().execute(query);
        Row row;
        List<UserOuterClass.User> users = new ArrayList<>();

        while ((row = result.one()) != null)
        {
            users.add(buildUser(row));
        }

        return users;
    }

    private List<UUID> getOrganizationUsers(String organizationId)
    {
        Statement query = QueryBuilder.select("users")
                .from(KEYSPACE, ORGANIZATIONS_TABLE)
                .where(QueryBuilder.eq("organization_id", UUID.fromString(organizationId)));
        ResultSet result = _cassandraConnector.getSession().execute(query);
        Row row = result.one();
        List<UUID> users = new ArrayList<>();

        if (row != null)
        {
            Set<UUID> usersSet;
            if ((usersSet = row.getSet("users", UUID.class)) != null)
            {
                users = new ArrayList<>(usersSet);
            }
        }
        LOG.info("Found {} users of organizationId {} ", users.size(), organizationId);
        return users;
    }

    @Override
    public boolean isEmailExists(String email)
    {
        if (email.length() == 0)
        {
            return false;
        }
        return checkParameter(USERS_BY_EMAIL_TABLE, "email", email);
    }

    @Override
    public boolean isCellPhoneExists(String cellPhone)
    {
        if (cellPhone.length() == 0)
        {
            return false;
        }

        return checkParameter(USERS_BY_CELL_PHONE_TABLE, "cell_phone", cellPhone);
    }

    private boolean checkParameter(String table, String column, String value)
    {
        Statement query = QueryBuilder.select()
                .countAll()
                .from(KEYSPACE, table)
                .where(QueryBuilder.eq(column, value));
        ResultSet result = _cassandraConnector.getSession().execute(query);
        return (result.one().getLong("count") > 0);
    }

    @Override
    public void updateUser(UserOuterClass.User user)
    {
        Statement query = QueryBuilder.update(KEYSPACE, USERS_BY_ID_TABLE)
                .with(QueryBuilder.set("first_name", user.getFirstName()))
                .and(QueryBuilder.set("middle_name", user.getMiddleName()))
                .and(QueryBuilder.set("last_name", user.getLastName()))
                .and(QueryBuilder.set("nick_name", user.getNickName()))
                .and(QueryBuilder.set("cell_phone", user.getCellPhone()))
                .and(QueryBuilder.set("home_phone", user.getHomePhone()))
                .and(QueryBuilder.set("work_phone", user.getWorkPhone()))
                .and(QueryBuilder.set("passport_number", user.getPassportNumber()))
                .and(QueryBuilder.set("date_of_birth", user.getDateOfBirth()))
                .and(QueryBuilder.set("employment_date", user.getEmploymentDate()))
                .and(QueryBuilder.set("actual_employment_date", user.getActualEmploymentDate()))
                .and(QueryBuilder.set("dismissal_date", user.getDismissalDate()))
                .and(QueryBuilder.set("actual_dismissal_date", user.getActualDismissalDate()))
                .and(QueryBuilder.set("tax_payer_id", user.getTaxPayerId()))
                .and(QueryBuilder.set("diploma_number", user.getDiplomaNumber()))
                .and(QueryBuilder.set("diploma_date", user.getDiplomaDate()))
                .and(QueryBuilder.set("retirement_id_number", user.getRetirementIdNumber()))
                .and(QueryBuilder.set("retirement_date", user.getRetirementDate()))
                .and(QueryBuilder.set("medical_book", user.getMedicalBook()))
                .and(QueryBuilder.set("medical_book_date", user.getMedicalBookDate()))
                .and(QueryBuilder.set("employment_book_number", user.getEmploymentBookNumber()))
                // Only keys are interesting
                .and(QueryBuilder.set("positions", convertToUuidSet(user.getPositionsMap())))
                .where(QueryBuilder.eq("user_id", UUID.fromString(user.getUserId())));
        _cassandraConnector.getSession().execute(query);
    }

    @Override
    public void setUserPassword(String user_id, String passwordHash, byte[] passwordSalt)
    {
        Statement query = QueryBuilder.update(KEYSPACE, USERS_BY_ID_TABLE)
                .with(QueryBuilder.set("password_hash", passwordHash))
                .and(QueryBuilder.set("password_salt", passwordSalt))
                .where(QueryBuilder.eq("user_id", UUID.fromString(user_id)));
        _cassandraConnector.getSession().execute(query);
    }

    private UserOuterClass.User buildUser(Row row) throws Exception
    {
        UserOuterClass.User.Builder builder = UserOuterClass.User.newBuilder();
        // Always non empty
        builder.setUserId(row.getUUID("user_id").toString());
        builder.setCreateDatetime(row.getTimestamp("create_datetime").getTime());
        // Could be empty
        if (row.getString("email") != null)
            builder.setEmail(row.getString("email"));
        if (row.getBytes("avatar") != null)
            builder.setAvatar(ByteString.copyFrom(row.getBytes("avatar").array()));
        if (row.getString("first_name") != null)
            builder.setFirstName(row.getString("first_name"));
        if (row.getString("middle_name") != null)
            builder.setMiddleName(row.getString("middle_name"));
        if (row.getString("nick_name") != null)
            builder.setNickName(row.getString("nick_name"));
        if (row.getString("last_name") != null)
            builder.setLastName(row.getString("last_name"));
        if (row.getString("cell_phone") != null)
            builder.setCellPhone(row.getString("cell_phone"));
        if (row.getString("home_phone") != null)
            builder.setHomePhone(row.getString("home_phone"));
        if (row.getString("work_phone") != null)
            builder.setWorkPhone(row.getString("work_phone"));
        if (row.getString("passport_number") != null)
            builder.setPassportNumber(row.getString("passport_number"));
        if (row.getTimestamp("date_of_birth") != null)
            builder.setDateOfBirth(row.getTimestamp("date_of_birth").getTime());
        if (row.getTimestamp("employment_date") != null)
            builder.setEmploymentDate(row.getTimestamp("employment_date").getTime());
        if (row.getTimestamp("actual_employment_date") != null)
            builder.setActualEmploymentDate(row.getTimestamp("actual_employment_date").getTime());
        if (row.getTimestamp("dismissal_date") != null)
            builder.setDismissalDate(row.getTimestamp("dismissal_date").getTime());
        if (row.getTimestamp("actual_dismissal_date") != null)
            builder.setActualDismissalDate(row.getTimestamp("actual_dismissal_date").getTime());
        if (row.getString("tax_payer_id") != null)
            builder.setTaxPayerId(row.getString("tax_payer_id"));
        if (row.getString("diploma_number") != null)
            builder.setDiplomaNumber(row.getString("diploma_number"));
        if (row.getTimestamp("diploma_date") != null)
            builder.setDiplomaDate(row.getTimestamp("diploma_date").getTime());
        if (row.getString("retirement_id_number") != null)
            builder.setRetirementIdNumber(row.getString("retirement_id_number"));
        if (row.getTimestamp("retirement_date") != null)
            builder.setRetirementDate(row.getTimestamp("retirement_date").getTime());
        if (row.getString("medical_book") != null)
            builder.setMedicalBook(row.getString("medical_book"));
        if (row.getTimestamp("medical_book_date") != null)
            builder.setMedicalBookDate(row.getTimestamp("medical_book_date").getTime());
        if (row.getString("employment_book_number") != null)
            builder.setEmploymentBookNumber(row.getString("employment_book_number"));
        if (row.getUUID("organization_id") != null)
            builder.setOrganizationId(row.getUUID("organization_id").toString());
        if (row.getUUID("address_id") != null)
            builder.setAddressId(row.getUUID("address_id").toString());

        // Positions as set and convert to map position to description
        Set<UUID> positions;
        if ((positions = row.getSet("positions", UUID.class)) != null)
        {
            Map<UUID, String > positionMap = _positionDal.getPositions(row.getUUID("organization_id").toString());
            for(UUID position : positions)
            {
                String description = positionMap.get(position);
                if (description != null)
                {
                    builder.putPositions(position.toString(), description);
                }
            }
        }

        return builder.build();
    }
}
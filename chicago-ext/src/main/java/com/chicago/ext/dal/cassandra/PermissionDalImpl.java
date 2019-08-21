package com.chicago.ext.dal.cassandra;

import com.chicago.dto.PermissionOuterClass;
import com.chicago.dto.UserOuterClass;
import com.chicago.ext.dal.DbConnector;
import com.chicago.ext.dal.PermissionDal;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.*;

import static com.chicago.ext.dal.cassandra.CassandraConstants.*;

public class PermissionDalImpl implements PermissionDal
{
    private static final Logger LOG = LoggerFactory.getLogger(PermissionDalImpl.class);

    @Inject
    private DbConnector<Session> _cassandraConnector;

    @Override
    public Set<String> setSystemAdminRole(String userId) throws Exception
    {
        // Get sysadmin row
        Statement query = QueryBuilder.select()
                .from(KEYSPACE, ROLES_BY_NAME_TABLE)
                .where(QueryBuilder.eq("role_name", "system:admin"));
        ResultSet result = _cassandraConnector.getSession().execute(query);
        Row row = result.one();
        if (row == null)
        {
            throw new Exception("Roles table is empty");
        }
        Set<UUID> roleIds = new HashSet<>();
        roleIds.add(row.getUUID("role_id"));
        Set<Integer> permissionIds = new HashSet<>(row.getSet("permission_ids", Integer.class));

        // Expand Role to permission set
        query = QueryBuilder.select()
                .from(KEYSPACE, PERMISSIONS_TABLE)
                .where(QueryBuilder.in("permission_id", permissionIds));
        result = _cassandraConnector.getSession().execute(query);
        Set<String> permissionNames = new HashSet<>();
        while ((row = result.one()) != null)
        {
            permissionNames.add(row.getString("permission_name"));
        }

        // Update user permission set in user table
        query = QueryBuilder.update(KEYSPACE, USERS_BY_ID_TABLE)
                .with(QueryBuilder.set("permissions", permissionNames))
                .where(QueryBuilder.eq("user_id", UUID.fromString(userId)));
        _cassandraConnector.getSession().execute(query);

        // Update user roles
        query = QueryBuilder.update(KEYSPACE, USER_PERMISSIONS_TABLE)
                .with(QueryBuilder.set("role_ids", roleIds))
                .where(QueryBuilder.eq("user_id", UUID.fromString(userId)));
        _cassandraConnector.getSession().execute(query);

        return permissionNames;
    }

    @Override
    public void setUserPermissions(String userId, List<PermissionOuterClass.Role> roles, List<PermissionOuterClass.Permission> extraPermissions)
    {
        Set<UUID> roleIds = new HashSet<>();
        for (PermissionOuterClass.Role role : roles)
        {
            roleIds.add(UUID.fromString(role.getRoleId()));
        }
        // Expand roles we dont trust permission names from client
        Statement query = QueryBuilder.select()
                .from(KEYSPACE, ROLES_TABLE)
                .where(QueryBuilder.in("role_id", roleIds));
        ResultSet result = _cassandraConnector.getSession().execute(query);

        // Collect extra permissions
        Set<Integer> extraPermissionIds = new HashSet<>();
        Set<Integer> permissionIds = new HashSet<>();
        for (PermissionOuterClass.Permission ePermission : extraPermissions)
        {
            permissionIds.add(ePermission.getPermissionId());
            extraPermissionIds.add(ePermission.getPermissionId());
        }

        // Merge permissions from roles with extra permissions
        Row row;
        while ((row = result.one()) != null)
        {
            Set<Integer> rolePermissionsIds = row.getSet("permission_ids", Integer.class);
            permissionIds.addAll(rolePermissionsIds);
        }
        // Get permissions names
        query = QueryBuilder.select()
                .from(KEYSPACE, PERMISSIONS_TABLE)
                .where(QueryBuilder.in("permission_id", permissionIds));
        result = _cassandraConnector.getSession().execute(query);

        Set<String> permissionNames = new HashSet<>();

        while ((row = result.one()) != null)
        {
            permissionNames.add(row.getString("permission_name"));
        }

        // Update user permissions now
        // Cassandra update same as insert if condition not true
        query = QueryBuilder.update(KEYSPACE, USER_PERMISSIONS_TABLE)
                .with(QueryBuilder.set("role_ids", roleIds))
                .and(QueryBuilder.set("extra_permission_ids", extraPermissionIds))
                .where(QueryBuilder.eq("user_id", UUID.fromString(userId)));
        _cassandraConnector.getSession().execute(query);

        query = QueryBuilder.update(KEYSPACE, USERS_BY_ID_TABLE).with(QueryBuilder.set("permissions", permissionNames))
                .where(QueryBuilder.eq("user_id", UUID.fromString(userId)));
        _cassandraConnector.getSession().execute(query);
    }

    @Override
    public UserOuterClass.UserPermissions getUserPermissions(String userId)
    {
        Statement query = QueryBuilder.select()
                .from(KEYSPACE, USER_PERMISSIONS_TABLE)
                .where(QueryBuilder.eq("user_id", UUID.fromString(userId)));

        ResultSet result = _cassandraConnector.getSession().execute(query);
        UserOuterClass.UserPermissions.Builder builder = UserOuterClass.UserPermissions.newBuilder();
        Row row = result.one();

        if (row != null)
        {
            builder.setUserId(userId);
            Set<UUID> roleIds = row.getSet("role_ids", UUID.class);
            List<PermissionOuterClass.Role> rolesList = new ArrayList<>();
            for (UUID roleId : roleIds)
            {
                PermissionOuterClass.Role role = getRole(roleId);
                if (role != null)
                {
                    rolesList.add(role);
                }else
                {
                    LOG.error("Role with id: {} not found", roleId);
                }
            }
            builder.addAllRoles(rolesList);

            Set<Integer> extraPermissionIds = row.getSet("extra_permission_ids", Integer.class);
            List<PermissionOuterClass.Permission> permissionsList = new ArrayList<>();
            for (int extraPermissionId : extraPermissionIds)
            {
                PermissionOuterClass.Permission permission = PermissionOuterClass.Permission.newBuilder()
                        .setPermissionId(extraPermissionId)
                        .build();
                permissionsList.add(permission);
            }
            builder.addAllExtraPermissions(permissionsList);
        }

        return builder.build();
    }

    @Override
    public List<PermissionOuterClass.Role> getSystemRoles()
    {
        Statement query = QueryBuilder.select().from(KEYSPACE, ROLES_TABLE);
        ResultSet rolesResult = _cassandraConnector.getSession().execute(query);
        List<PermissionOuterClass.Role> rolesList = new ArrayList<>();
        Row roleRow;

        while ((roleRow = rolesResult.one()) != null)
        {
            UUID roleId = roleRow.getUUID("role_id");
            String roleName = roleRow.getString("role_name");
            String roleDescription = roleRow.getString("description");
            Set<Integer> permissionIds = roleRow.getSet("permission_ids", Integer.class);
            List<PermissionOuterClass.Permission> permissions = getPermissions(permissionIds);
            PermissionOuterClass.Role role = PermissionOuterClass.Role.newBuilder()
                    .setRoleId(roleId.toString())
                    .setRoleName(roleName)
                    .setRoleDescription(roleDescription)
                    .addAllPermissions(permissions)
                    .build();
            rolesList.add(role);
        }
        return rolesList;
    }

    private PermissionOuterClass.Role getRole(UUID roleId)
    {
        Statement query = QueryBuilder.select().from(KEYSPACE, ROLES_TABLE)
                .where(QueryBuilder.in("role_id", roleId));
        ResultSet roleResult = _cassandraConnector.getSession().execute(query);
        Row roleRow = roleResult.one();

        if (roleRow != null)
        {
            Set<Integer> permissionIds = roleRow.getSet("permission_ids", Integer.class);
            List<PermissionOuterClass.Permission> permissions = getPermissions(permissionIds);
            PermissionOuterClass.Role role = PermissionOuterClass.Role.newBuilder()
                    .setRoleId(roleId.toString())
                    .setRoleName(roleRow.getString("role_name"))
                    .setRoleDescription(roleRow.getString("description"))
                    .addAllPermissions(permissions)
                    .build();
            return role;
        }
        return null;
    }

    private List<PermissionOuterClass.Permission> getPermissions(Set<Integer> permissionsIds)
    {
        Statement query = QueryBuilder.select().from(KEYSPACE, PERMISSIONS_TABLE)
                .where(QueryBuilder.in("permission_id", permissionsIds));
        ResultSet permissionResult = _cassandraConnector.getSession().execute(query);

        Row permissionRow;
        List<PermissionOuterClass.Permission> permissionsArr = new ArrayList<>();
        while ((permissionRow = permissionResult.one()) != null)
        {
            PermissionOuterClass.Permission permission = PermissionOuterClass.Permission.newBuilder()
                    .setPermissionId(permissionRow.getInt("permission_id"))
                    .setPermissionName(permissionRow.getString("permission_name"))
                    .setDescription(permissionRow.getString("description"))
                    .build();
            permissionsArr.add(permission);
        }
        return permissionsArr;
    }
}

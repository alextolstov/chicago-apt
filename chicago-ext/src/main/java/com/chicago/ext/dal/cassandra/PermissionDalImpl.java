package com.chicago.ext.dal.cassandra;

import com.chicago.dto.PermissionOuterClass;
import com.chicago.dto.UserOuterClass;
import com.chicago.ext.dal.PermissionDal;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
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
    private CassandraConnector _cassandraConnector;

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
            Set<UUID> roleIds = row.getSet("role_ids", UUID.class);
            List<PermissionOuterClass.Role> rolesList = new ArrayList<>();
            for (UUID roleId : roleIds)
            {
                PermissionOuterClass.Role role = PermissionOuterClass.Role.newBuilder()
                        .setRoleId(roleId.toString())
                        .build();
                rolesList.add(role);
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
    public PermissionOuterClass.Roles getSystemPermissions()
    {
        Statement query = QueryBuilder.select().from(KEYSPACE, ROLES_TABLE);
        ResultSet rolesResult = _cassandraConnector.getSession().execute(query);
        List<PermissionOuterClass.Role> rolesArr = new ArrayList<>();
        Row roleRow;

        while ((roleRow = rolesResult.one()) != null)
        {
            UUID roleId = roleRow.getUUID("role_id");
            String roleName = roleRow.getString("role_name");
            String roleDescription = roleRow.getString("description");
            Set<Integer> permissionIds = roleRow.getSet("permission_ids", Integer.class);
            query = QueryBuilder.select().from(KEYSPACE, PERMISSIONS_TABLE)
                    .where(QueryBuilder.in("permission_id", permissionIds));
            ResultSet permissionsResult = _cassandraConnector.getSession().execute(query);

            Row permissionRow;
            List<PermissionOuterClass.Permission> permissionsArr = new ArrayList<>();
            while ((permissionRow = permissionsResult.one()) != null)
            {
                PermissionOuterClass.Permission permission = PermissionOuterClass.Permission.newBuilder()
                        .setPermissionId(permissionRow.getInt("permission_id"))
                        .setPermissionName(permissionRow.getString("permission_name"))
                        .setPermissionDescription(permissionRow.getString("description"))
                        .build();
                permissionsArr.add(permission);
            }
            PermissionOuterClass.Role role = PermissionOuterClass.Role.newBuilder()
                    .setRoleId(roleId.toString())
                    .setRoleName(roleName)
                    .setRoleDescription(roleDescription)
                    .addAllPermissions(permissionsArr)
                    .build();
            rolesArr.add(role);
        }
        return PermissionOuterClass.Roles.newBuilder()
                .addAllRole(rolesArr)
                .build();
    }
}

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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.chicago.ext.dal.cassandra.CassandraConstants.*;

public class PermissionDalImpl implements PermissionDal
{
    private static final Logger LOG = LoggerFactory.getLogger(PermissionDalImpl.class);

    @Inject
    private CassandraConnector _cassandraConnector;

    @Override
    public void setUserPermissions(UserOuterClass.UserPermissions permissions)
    {
        Statement query = QueryBuilder.update(KEYSPACE, USER_PERMISSIONS_TABLE)
                .with(QueryBuilder.set("role_ids", permissions.getRolesList()))
                .and(QueryBuilder.set("extra_permission_ids", permissions.getExtraPermissionsList()))
                .where(QueryBuilder.eq("user_id", UUID.fromString(permissions.getUserId())));
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
            Set<UUID> permissionIds = roleRow.getSet("permission_ids", UUID.class);
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
        PermissionOuterClass.Roles roles = PermissionOuterClass.Roles.newBuilder()
                .addAllRole(rolesArr)
                .build();

        return roles;
    }
}

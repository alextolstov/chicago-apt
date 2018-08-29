package com.chicago.ext.dal.cassandra;

import com.chicago.dto.PositionOuterClass;
import com.chicago.ext.dal.PositionDal;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.Delete;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.utils.UUIDs;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.ServiceLocatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.chicago.ext.dal.cassandra.CassandraConstants.KEYSPACE;
import static com.chicago.ext.dal.cassandra.CassandraConstants.POSITIONS_TABLE;

public class PositionDalImpl implements PositionDal {
    private static final Logger _LOG = LoggerFactory.getLogger(PositionDalImpl.class);

    private ServiceLocator _locator;

    public PositionDalImpl() {
        _locator = ServiceLocatorFactory.getInstance().find("servicelocator");
    }

    @Override
    public PositionOuterClass.Position createPosition(PositionOuterClass.Position position) throws Exception
    {
        UUID newPositionId = UUIDs.random();
        Statement query = QueryBuilder.update(KEYSPACE, POSITIONS_TABLE)
                .where(QueryBuilder.eq("organization_id", UUID.fromString(position.getOrganizationId())))
                .with(QueryBuilder.put("positions", newPositionId, position.getDescription()));
        _locator.getService(CassandraConnector.class).getSession().execute(query);

        return PositionOuterClass.Position.newBuilder()
                .setOrganizationId(position.getOrganizationId())
                .setPositionId(newPositionId.toString())
                .build();
    }

    @Override
    public void updatePosition(PositionOuterClass.Position position) throws Exception
    {
        Statement query = QueryBuilder.update(KEYSPACE, POSITIONS_TABLE)
                .where(QueryBuilder.eq("organization_id", UUID.fromString(position.getOrganizationId())))
                .with(QueryBuilder.put("positions", UUID.fromString(position.getPositionId()), position.getDescription()));

        _locator.getService(CassandraConnector.class).getSession().execute(query);
    }

    @Override
    public void deletePosition(PositionOuterClass.Position position) throws Exception
    {
        Statement query = QueryBuilder.delete()
                .mapElt("positions", UUID.fromString(position.getPositionId()))
                .from(KEYSPACE, POSITIONS_TABLE)
                .where(QueryBuilder.eq("organization_id", UUID.fromString(position.getOrganizationId())));

        _locator.getService(CassandraConnector.class).getSession().execute(query);
    }

    @Override
    public PositionOuterClass.Positions getPositions(String organizationId) throws Exception
    {
        return null;
    }
}

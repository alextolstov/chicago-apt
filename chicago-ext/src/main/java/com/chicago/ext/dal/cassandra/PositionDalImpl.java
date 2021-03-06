package com.chicago.ext.dal.cassandra;

import com.chicago.dto.PositionOuterClass;
import com.chicago.ext.dal.DbConnector;
import com.chicago.ext.dal.PositionDal;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.utils.UUIDs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.chicago.ext.dal.cassandra.CassandraConstants.KEYSPACE;
import static com.chicago.ext.dal.cassandra.CassandraConstants.POSITIONS_TABLE;

public class PositionDalImpl implements PositionDal
{
    private static final Logger LOG = LoggerFactory.getLogger(PositionDalImpl.class);

    @Inject
    private DbConnector<Session> _cassandraConnector;

    @Override
    public PositionOuterClass.Position createPosition(PositionOuterClass.Position position)
    {
        UUID newPositionId = UUIDs.random();
        Statement query = QueryBuilder.update(KEYSPACE, POSITIONS_TABLE)
                .where(QueryBuilder.eq("organization_id", UUID.fromString(position.getOrganizationId())))
                .with(QueryBuilder.put("positions", newPositionId, position.getDescription()));
        _cassandraConnector.getSession().execute(query);

        return PositionOuterClass.Position.newBuilder(position)
                .setPositionId(newPositionId.toString())
                .build();
    }

    @Override
    public void updatePosition(PositionOuterClass.Position position)
    {
        Statement query = QueryBuilder.update(KEYSPACE, POSITIONS_TABLE)
                .where(QueryBuilder.eq("organization_id", UUID.fromString(position.getOrganizationId())))
                .with(QueryBuilder.put("positions", UUID.fromString(position.getPositionId()), position.getDescription()));

        _cassandraConnector.getSession().execute(query);
    }

    @Override
    public void deletePosition(PositionOuterClass.Position position)
    {
        Statement query = QueryBuilder.delete()
                .mapElt("positions", UUID.fromString(position.getPositionId()))
                .from(KEYSPACE, POSITIONS_TABLE)
                .where(QueryBuilder.eq("organization_id", UUID.fromString(position.getOrganizationId())));

        _cassandraConnector.getSession().execute(query);
    }

    @Override
    public Map<UUID, String> getPositions(String organizationId)
    {
        Statement query = QueryBuilder.select()
                .from(KEYSPACE, POSITIONS_TABLE)
                .where(QueryBuilder.eq("organization_id", UUID.fromString(organizationId)));
        ResultSet result = _cassandraConnector.getSession().execute(query);
        Row row = result.one();
        if (row == null)
        {
            return new HashMap<>();
        }

        return row.getMap("positions", UUID.class, String.class);
    }
}

package com.chicago.ext.dal.cassandra;

import com.chicago.dto.PositionOuterClass;
import com.chicago.ext.dal.PositionDal;
import com.datastax.driver.core.Statement;
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
//        Statement query = QueryBuilder.select()
//                .from(KEYSPACE, POSITIONS_TABLE)
//                .where(QueryBuilder.eq("entity_id", position.getEntityId()));
//        ResultSet result = _locator.getService(CassandraConnector.class).getSession().execute(query);
//
//        Row positionsRow;
//        Map<UUID, String> positionsMap;
//        UUID newPositionId = UUIDs.random();
//
//        if((positionsRow = result.one()) != null)
//        {
//            positionsMap = positionsRow.getMap("positions", UUID.class, String.class);
//        }
//        else
//        {
//            positionsMap = new HashMap<UUID, String>();
//        }
//
        Map<UUID, String> positionsMap = new HashMap<UUID, String>();
        UUID newPositionId = UUIDs.random();
        positionsMap.put(newPositionId, position.getDescription());
        // Create company first
        Statement query = QueryBuilder.insertInto(KEYSPACE, POSITIONS_TABLE)
                .value("entity_id", UUID.fromString(position.getEntityId()))
                .value("positions", positionsMap);
        _locator.getService(CassandraConnector.class).getSession().execute(query);

//        query = QueryBuilder.select()
//                .from(KEYSPACE, POSITIONS_TABLE)
//                .where(QueryBuilder.eq("entity_id", position.getEntityId()));
//        ResultSet result = _locator.getService(CassandraConnector.class).getSession().execute(query);
//        Row positionsRow = result.one();
//        Map<String, String> newMap = positionsRow.getMap("positions", UUID.class, String.class).entrySet().stream()
//                .collect(Collectors.toMap(e -> (e.getKey().toString()), Map.Entry::getValue));
        return PositionOuterClass.Position.newBuilder()
                .setEntityId(position.getEntityId())
                .setPositionId(newPositionId.toString())
                .build();
    }

    @Override
    public void updatePosition(PositionOuterClass.Position position) throws Exception
    {
        Map<UUID, String> positionsMap = new HashMap<UUID, String>();
        positionsMap.put(UUID.fromString(position.getPositionId()), position.getDescription());
    }

    @Override
    public PositionOuterClass.Position deleetPosition(PositionOuterClass.Position position) throws Exception
    {
        return null;
    }

    @Override
    public PositionOuterClass.Positions getPositions(String entityId) throws Exception
    {
        return null;
    }
}

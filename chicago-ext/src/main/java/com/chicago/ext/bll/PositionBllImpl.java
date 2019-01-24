package com.chicago.ext.bll;

import com.chicago.dto.PositionOuterClass;
import com.chicago.ext.dal.PositionDal;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PositionBllImpl implements PositionBll
{
    private static final Logger LOG = LoggerFactory.getLogger(PositionBllImpl.class);
    @Inject
    private PositionDal _positionDal;

    @Override
    public PositionOuterClass.Position createPosition(PositionOuterClass.Position position) throws Exception
    {
        return _positionDal.createPosition(position);
    }

    @Override
    public void updatePosition(PositionOuterClass.Position position) throws Exception
    {
        _positionDal.updatePosition(position);
    }

    @Override
    public void deletePosition(PositionOuterClass.Position position) throws Exception
    {
        _positionDal.deletePosition(position);
    }

    @Override
    public PositionOuterClass.Positions getPositions(String organizationId) throws Exception
    {
        // Protobuf doesnt support UUID, need to convert to string
        Map<UUID, String> result = _positionDal.getPositions(organizationId);
        Map<String, String> newResult = result.entrySet().stream()
                .collect(Collectors.toMap(entry -> entry.getKey().toString(), Map.Entry::getValue));
        return PositionOuterClass.Positions.newBuilder()
                .putAllPositions(newResult)
                .build();
    }
}

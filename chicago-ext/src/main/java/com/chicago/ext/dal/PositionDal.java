package com.chicago.ext.dal;

import com.chicago.dto.PositionOuterClass;

import java.util.Map;
import java.util.UUID;

public interface PositionDal {
    PositionOuterClass.Position createPosition(PositionOuterClass.Position position)
            throws Exception;

    void updatePosition(PositionOuterClass.Position position)
            throws Exception;

    void deletePosition(PositionOuterClass.Position position)
            throws Exception;

    Map<UUID, String> getPositions(String organizationId)
            throws Exception;
}

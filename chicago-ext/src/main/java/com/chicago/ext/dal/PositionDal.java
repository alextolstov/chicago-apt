package com.chicago.ext.dal;

import com.chicago.dto.PositionOuterClass;

public interface PositionDal {
    PositionOuterClass.Position createPosition(PositionOuterClass.Position position)
            throws Exception;

    void updatePosition(PositionOuterClass.Position position)
            throws Exception;

    void deletePosition(PositionOuterClass.Position position)
            throws Exception;

    PositionOuterClass.Positions getPositions(String organizationId)
            throws Exception;
}

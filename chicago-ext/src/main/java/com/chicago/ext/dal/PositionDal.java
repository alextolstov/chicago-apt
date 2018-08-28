package com.chicago.ext.dal;

import com.chicago.dto.PositionOuterClass;

public interface PositionDal {
    PositionOuterClass.Position createPosition(PositionOuterClass.Position position)
            throws Exception;

    void updatePosition(PositionOuterClass.Position position)
            throws Exception;

    PositionOuterClass.Position deleetPosition(PositionOuterClass.Position position)
            throws Exception;

    PositionOuterClass.Positions getPositions(String entityId)
            throws Exception;
}

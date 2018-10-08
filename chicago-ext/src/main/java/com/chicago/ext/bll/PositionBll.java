package com.chicago.ext.bll;

import com.chicago.dto.PositionOuterClass;
import org.jvnet.hk2.annotations.Contract;

@Contract
public interface PositionBll
{
    PositionOuterClass.Position createPosition(PositionOuterClass.Position position) throws Exception;

    void updatePosition(PositionOuterClass.Position position) throws Exception;

    void deletePosition(PositionOuterClass.Position position) throws Exception;

    PositionOuterClass.Positions getPositions(String organizationId) throws Exception;
}



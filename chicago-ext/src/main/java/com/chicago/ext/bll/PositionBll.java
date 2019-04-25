package com.chicago.ext.bll;

import com.chicago.dto.PositionOuterClass;
import org.jvnet.hk2.annotations.Contract;

import java.util.List;

@Contract
public interface PositionBll
{
    PositionOuterClass.Position createPosition(PositionOuterClass.Position position) throws Exception;

    void updatePosition(PositionOuterClass.Position position) throws Exception;

    void deletePosition(PositionOuterClass.Position position) throws Exception;

    List<PositionOuterClass.Position> getPositions(String organizationId) throws Exception;
}



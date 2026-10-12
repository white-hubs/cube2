package com.guhecloud.cube.biz.ouaa.server.converters;

import com.guhecloud.cube.biz.ouaa.server.entity.Position;
import com.guhecloud.cube.biz.ouaa.server.vo.UserVO;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.List;

public final class PositionConverterForUserVOPosition {
    private PositionConverterForUserVOPosition() {}

    public static UserVO.Position toUserVOPosition(Position position) {
        var userVOPosition = new UserVO.Position();
        userVOPosition.setId(position.getId());
        userVOPosition.setName(position.getName());
        return userVOPosition;
    }

    public static List<UserVO.Position> toUserVOPositions(List<Position> positions) {
        if (CollectionUtils.isEmpty(positions)) {
            return CollectionUtils.emptyList();
        }
        return positions.stream().map(PositionConverterForUserVOPosition::toUserVOPosition).toList();
    }
}

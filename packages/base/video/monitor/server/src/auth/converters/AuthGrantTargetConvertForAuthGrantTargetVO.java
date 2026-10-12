package com.guhecloud.cube.biz.vmonitor.server.auth.converters;

import com.guhecloud.cube.biz.vmonitor.server.auth.po.AuthGrantTargetPO;
import com.guhecloud.cube.biz.vmonitor.server.auth.vo.AuthGrantTargetVO;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class AuthGrantTargetConvertForAuthGrantTargetVO {

    private AuthGrantTargetConvertForAuthGrantTargetVO() {}

    public static List<AuthGrantTargetVO> toAuthGrantTypeTargetVOS(List<AuthGrantTargetPO> authGrantTargetPOS) {
        if (CollectionUtils.isEmpty(authGrantTargetPOS)) {
            return Collections.emptyList();
        }
        var authGroupMap = authGrantTargetPOS.stream()
                .collect(Collectors.groupingBy(AuthGrantTargetPO::getAuthGroupId));
        List<AuthGrantTargetVO> authGrantTargetVOS = new ArrayList<>();
        authGroupMap.forEach((id, targets) -> {
            var authGrantTargetMap = targets.stream()
                    .collect(Collectors.groupingBy(AuthGrantTargetPO::getTargetType));
            authGrantTargetMap.forEach((targetType, targetPOs) -> {
                var authGrantTargetVO = new AuthGrantTargetVO();
                authGrantTargetVO.setAuthGroupId(id);
                authGrantTargetVO.setTargetType(targetType);
                authGrantTargetVO.setTargetIds(targetPOs.stream().map(AuthGrantTargetPO::getTargetId).collect(Collectors.toSet()));
                authGrantTargetVOS.add(authGrantTargetVO);
            });
        });
        return authGrantTargetVOS;
    }
}

package com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.vo;

import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.entity.ElectricityConnection;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.entity.ElectricityNode;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ElectricitySupplyDetailVO {
    private List<ElectricityNodeVO> nodeList;
    private List<ElectricityConnectionVO> connectionList;

    public void setCovertNodeList(List<ElectricityNode> poList) {
        if (CollectionUtils.isEmpty(poList)) {
            return;
        }
        nodeList = poList.stream()
                .map(this::poToVo).collect(Collectors.toList());
    }

    public void setCovertConnectionList(List<ElectricityConnection> poList) {
        if (CollectionUtils.isEmpty(poList)) {
            return;
        }
        connectionList = poList.stream()
                .map(this::poToVo).collect(Collectors.toList());
    }

    private ElectricityNodeVO poToVo(ElectricityNode po) {
        ElectricityNodeVO vo = new ElectricityNodeVO();
        vo.setId(po.getId());
        vo.setCode(po.getCode());
        vo.setName(po.getName());
        vo.setType(po.getType());
        vo.setDeviceIds(po.getDeviceIds());
        vo.setChildrenIds(po.getChildrenIds());
        vo.setLocation(po.getLocation());
        vo.setDescription(po.getDescription());
        vo.setWebStyle(po.getWebStyle());
        vo.setCreateTime(po.getCreateTime());
        vo.setCreateBy(po.getCreateBy());
        return vo;
    }

    private ElectricityConnectionVO poToVo(ElectricityConnection po) {
        ElectricityConnectionVO vo = new ElectricityConnectionVO();
        vo.setId(po.getId());
        vo.setLineName(po.getLineName());
        vo.setFromNodeId(po.getFromNodeId());
        vo.setToNodeId(po.getToNodeId());
        vo.setWebStyle(po.getWebStyle());
        vo.setCreateTime(po.getCreateTime());
        vo.setCreateBy(po.getCreateBy());
        return vo;
    }
}

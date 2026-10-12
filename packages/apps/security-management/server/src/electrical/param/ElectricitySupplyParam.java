package com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.param;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.entity.ElectricityConnection;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.entity.ElectricityNode;
import com.guhecloud.platform.base.util.CollectionUtils;
import jakarta.validation.Valid;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ElectricitySupplyParam {
    @Valid
    private List<ElectricityNodeParam> nodeList;
    private List<ElectricityConnectionParam> connectionList;

    public List<ElectricityNode> covertNodePoList() {
        if (CollectionUtils.isEmpty(nodeList)) {
            return CollectionUtils.emptyList();
        }
        var list = new ArrayList<ElectricityNode>();
        nodeList.forEach(paramNode -> list.add(paramToNode(paramNode)));
        return list;
    }

    public List<ElectricityConnection> covertConnectionPoList() {
        if (CollectionUtils.isEmpty(connectionList)) {
            return CollectionUtils.emptyList();
        }
        var list = new ArrayList<ElectricityConnection>();
        connectionList.forEach(paramConnection -> list.add(paramToConnection(paramConnection)));
        return list;
    }

    private ElectricityNode paramToNode(ElectricityNodeParam param) {
        var po = new ElectricityNode();
        po.setId(param.getId() == null ? IdWorker.getId() : param.getId());
        po.setCode(param.getCode());
        po.setName(param.getName());
        po.setType(param.getType());
        po.setDeviceIds(param.getDeviceIds());
        po.setChildrenIds(param.getChildrenIds());
        po.setLocation(param.getLocation());
        po.setDescription(param.getDescription());
        po.setWebStyle(param.getWebStyle());
        if (param.getCreateTime() != null) {
            po.setCreateTime(param.getCreateTime());
            po.setCreateBy(param.getCreateBy());
        }
        return po;
    }

    private ElectricityConnection paramToConnection(ElectricityConnectionParam param) {
        var po = new ElectricityConnection();
        po.setId(param.getId() == null ? IdWorker.getId() : param.getId());
        po.setLineName(param.getLineName());
        po.setFromNodeId(param.getFromNodeId());
        po.setToNodeId(param.getToNodeId());
        po.setWebStyle(param.getWebStyle());
        if (param.getCreateTime() != null) {
            po.setCreateTime(param.getCreateTime());
            po.setCreateBy(param.getCreateBy());
        }
        return po;
    }
}

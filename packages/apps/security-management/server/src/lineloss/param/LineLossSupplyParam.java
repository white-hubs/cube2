package com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.param;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.entity.LineLossConnection;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.entity.LineLossNode;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class LineLossSupplyParam {
    private List<LineLossNodeParam> nodeList;
    private List<LineLossConnectionParam> connectionList;

    public List<LineLossNode> covertNodePoList() {
        if (CollectionUtils.isEmpty(nodeList)) {
            return CollectionUtils.emptyList();
        }
        var list = new ArrayList<LineLossNode>();
        nodeList.forEach(paramNode -> list.add(paramToNode(paramNode)));
        return list;
    }

    public List<LineLossConnection> covertConnectionPoList() {
        if (CollectionUtils.isEmpty(connectionList)) {
            return CollectionUtils.emptyList();
        }
        var list = new ArrayList<LineLossConnection>();
        connectionList.forEach(paramConnection -> list.add(paramToConnection(paramConnection)));
        return list;
    }

    private LineLossNode paramToNode(LineLossNodeParam param) {
        var po = new LineLossNode();
        po.setId(param.getId());
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

    private LineLossConnection paramToConnection(LineLossConnectionParam param) {
        var po = new LineLossConnection();
        po.setId(IdWorker.getId());
        po.setLineName(param.getLineName());
        po.setFromNodeId(param.getFromNodeId());
        po.setToNodeId(param.getToNodeId());
        po.setFromDeviceId(param.getFromDeviceId());
        po.setToDeviceId(param.getToDeviceId());
        po.setWebStyle(param.getWebStyle());
        if (param.getCreateTime() != null) {
            po.setCreateTime(param.getCreateTime());
            po.setCreateBy(param.getCreateBy());
        }
        return po;
    }
}

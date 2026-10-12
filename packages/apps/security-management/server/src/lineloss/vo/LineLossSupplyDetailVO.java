package com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.vo;

import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseArchivesInfoDTO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.entity.LineLossConnection;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.entity.LineLossNode;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
public class LineLossSupplyDetailVO {
    private List<LineLossNodeVO> nodeList;
    private List<LineLossConnectionVO> connectionList;

    public void setCovertNodeList(List<LineLossNode> poList, Map<Long, DeviceBaseArchivesInfoDTO> deviceMap) {
        if (CollectionUtils.isEmpty(poList)) {
            return;
        }
        nodeList = poList.stream()
                .map(po -> this.poToVo(po, deviceMap)).collect(Collectors.toList());
    }

    public void setCovertConnectionList(List<LineLossConnection> poList, Map<Long, LineLossAlarmRuleVO> lineRuleMap,
                                        Map<Long, DeviceBaseArchivesInfoDTO> deviceMap
                                       ) {
        if (CollectionUtils.isEmpty(poList)) {
            return;
        }
        connectionList = poList.stream()
                .map(po -> poToVo(po, lineRuleMap, deviceMap)).collect(Collectors.toList());
    }

    private LineLossNodeVO poToVo(LineLossNode po, Map<Long, DeviceBaseArchivesInfoDTO> deviceMap) {
        LineLossNodeVO vo = new LineLossNodeVO();
        vo.setId(po.getId());
        vo.setCode(po.getCode());
        vo.setName(po.getName());
        vo.setType(po.getType());
        vo.setDeviceIds(po.getDeviceIds());
        if (StringUtils.isNotBlank(po.getDeviceIds())) {
            vo.setDeviceList(StringUtils.split(po.getDeviceIds(), ",").stream()
                                     .map(deviceId -> deviceMap.get(Long.valueOf(deviceId))).filter(Objects::nonNull)
                                     .collect(Collectors.toList()));
        }
        vo.setChildrenIds(po.getChildrenIds());
        vo.setLocation(po.getLocation());
        vo.setDescription(po.getDescription());
        vo.setWebStyle(po.getWebStyle());
        vo.setCreateTime(po.getCreateTime());
        vo.setCreateBy(po.getCreateBy());
        return vo;
    }

    private LineLossConnectionVO poToVo(LineLossConnection po, Map<Long, LineLossAlarmRuleVO> lineRuleMap,
                                        Map<Long, DeviceBaseArchivesInfoDTO> deviceMap
                                       ) {
        LineLossConnectionVO vo = new LineLossConnectionVO();
        vo.setId(po.getId());
        vo.setLineName(po.getLineName());
        vo.setFromNodeId(po.getFromNodeId());
        vo.setToNodeId(po.getToNodeId());
        vo.setFromDeviceId(po.getFromDeviceId());
        vo.setFromDevice(deviceMap.get(po.getFromDeviceId()));
        vo.setToDeviceId(po.getToDeviceId());
        vo.setToDevice(deviceMap.get(po.getToDeviceId()));
        vo.setLineLossRate(po.getLineLossRate());
        vo.setWebStyle(po.getWebStyle());
        vo.setCreateTime(po.getCreateTime());
        vo.setCreateBy(po.getCreateBy());
        vo.setCondition(lineRuleMap.getOrDefault(po.getId(), new LineLossAlarmRuleVO()).getLossAlarmCondition().getCondition());
        return vo;
    }
}

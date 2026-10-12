package com.guhecloud.cube.biz.subsystem.parking.server.Illegal.interfaces.converters;

import com.guhecloud.cube.base.flow.client.dto.ProcessInstanceDTO;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.domain.po.IllegalParkingRecordPO;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.interfaces.vo.IllegalParkingRecordVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/6 10:03
 * @Description:
 */
public final class IllegalParkingRecordConverts {
    public static List<IllegalParkingRecordVO> convertToRecordVO(List<ProcessInstanceDTO> records,
                                                                 Map<String, IllegalParkingRecordPO> illegalParkingRecordPOMap
                                                                ) {
        List<IllegalParkingRecordVO> recordVOS = new ArrayList<>();
        for (ProcessInstanceDTO record : records) {
            IllegalParkingRecordVO recordVO = new IllegalParkingRecordVO();
            recordVO.setStartUserInfo(record.getStartUserInfo());
            recordVO.setFormNo(record.getFormNo());
            recordVO.setProcessInstanceId(record.getProcessInstanceId());
            recordVO.setStartUserId(record.getStartUserId());
            recordVO.setStartUserName(record.getStartUserName());
            recordVO.setSubmitedTime(record.getSubmitedTime());
            recordVO.setLastTaskCompletedTime(record.getLastTaskCompletedTime());
            recordVO.setStatus(record.getStatus());
            recordVO.setActiveNodeName(record.getActiveNodeName());
            recordVO.setActiveNodeAssignee(record.getActiveNodeAssignee());
            recordVO.setCurrentTaskId(record.getCurrentTaskId());
            recordVO.setVariables(record.getVariables());
            recordVO.setInstanceName(record.getInstanceName());
            recordVO.setBusinessType(record.getBusinessType());
            recordVO.setCreateTime(record.getCreateTime());
            recordVO.setUpdateTime(record.getUpdateTime());
            IllegalParkingRecordPO recordPO = illegalParkingRecordPOMap.getOrDefault(record.getProcessInstanceId(), new IllegalParkingRecordPO());
            recordVO.setCode(recordPO.getCode());
            recordVO.setName(recordPO.getName());
            recordVO.setPlateNumber(recordPO.getPlateNumber());
            recordVO.setOwnerPhone(recordPO.getOwnerPhone());
            recordVO.setOwnerName(recordPO.getOwnerName());
            recordVO.setIllegalLevel(recordPO.getIllegalLevel());
            recordVOS.add(recordVO);
        }
        return recordVOS;
    }

}

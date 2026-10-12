package com.guhecloud.cube.biz.work.center.server.coverts;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.work.center.server.entity.HandoverRecord;
import com.guhecloud.cube.biz.work.center.server.enumeration.HandoverStatusEnum;
import com.guhecloud.cube.biz.work.center.server.vo.HandoverRecordDetailVO;
import com.guhecloud.cube.biz.work.center.server.vo.HandoverRecordVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/10/30 17:22
 * @Description:
 */
public class HandoverRecordCovertToVO {

    public HandoverRecordCovertToVO() {

    }

    public static Pagination<HandoverRecordVO> toHandoverRecordVOPage(Page<HandoverRecord> handoverRecordPage,
                                                                      Map<Long, String> userNameMap
                                                                     ) {
        List<HandoverRecordVO> result = new ArrayList<>();
        Long currentUser = SecurityContextHolder.getPrincipal().getId();
        for (HandoverRecord record : handoverRecordPage.getRecords()) {
            //判断当前是接班人
            boolean takerUser = Objects.equals(currentUser, record.getTakeOverUser());
            HandoverRecordVO vo = new HandoverRecordVO();
            vo.setId(record.getId());
            vo.setRecordName(record.getRecordName());
            vo.setTaskNum(record.getTaskNum());
            vo.setStatus(record.getStatus());
            if (HandoverStatusEnum.HAND_OVERED.equals(record.getStatus())) {
                vo.setStatus(takerUser ? HandoverStatusEnum.TAKE_OVERED : record.getStatus());
            }
            vo.setShiftOverUserName(userNameMap.get(record.getShiftOverUser()));
            vo.setShiftOverTime(record.getShiftOverTime());
            vo.setTakeOverUserId(record.getTakeOverUser());
            vo.setTakeOverUserName(userNameMap.get(record.getTakeOverUser()));
            vo.setTakeOverTime(record.getTakeOverTime());
            vo.setRevoke(Objects.equals(record.getShiftOverUser(), SecurityContextHolder.getPrincipal().getId()));
            result.add(vo);
        }
        return PagingUtils.convert(handoverRecordPage, result);
    }

    public static HandoverRecordDetailVO toHandoverRecordDetailVO(HandoverRecord handoverRecord) {
        if (Objects.isNull(handoverRecord)) {
            return new HandoverRecordDetailVO();
        }
        HandoverRecordDetailVO vo = new HandoverRecordDetailVO();
        vo.setId(handoverRecord.getId());
        vo.setShiftOverUser(handoverRecord.getShiftOverUser());
        vo.setRemarks(handoverRecord.getRemarks());
        return vo;
    }
}

package com.guhecloud.cube.srv.gen.visitor.management.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.visitor.management.server.flow.po.VisitorInvitePO;
import com.guhecloud.cube.srv.gen.visitor.management.server.po.VisitorInviteRecordPO;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.param.VisitorInviteRecordParam;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.result.VisitorInviteRecordResult;
import com.guhecloud.platform.base.model.Pagination;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/2/24 16:55
 * @Description:
 */
public interface VisitorInviteRecordService extends IService<VisitorInviteRecordPO> {

    /**
     * 邀约列表
     *
     * @param param
     *
     * @return
     */
    Pagination<VisitorInviteRecordResult> visitorInviteRecordPage(VisitorInviteRecordParam param);

    /**
     * 保存或更新邀约记录
     *
     * @param visitorInvitePO
     * @param processInsId
     */
    void saveOrUpdateVisitorInviteRecord(VisitorInvitePO visitorInvitePO, String processInsId, String status, String barCode);
}

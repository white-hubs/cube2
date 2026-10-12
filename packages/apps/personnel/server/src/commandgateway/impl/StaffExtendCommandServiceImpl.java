package com.guhecloud.cube.srv.gen.personnel.management.server.commandgateway.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.personnel.management.server.commandgateway.StaffExtendCommandService;
import com.guhecloud.cube.srv.gen.personnel.management.server.commandgateway.command.SubmitStaffCommand;
import com.guhecloud.cube.srv.gen.personnel.management.server.dao.StaffExtendMapper;
import com.guhecloud.cube.srv.gen.personnel.management.server.po.StaffExtendPO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/3/20 11:49
 * @Description:
 */
@Service
public class StaffExtendCommandServiceImpl extends ServiceImpl<StaffExtendMapper, StaffExtendPO> implements StaffExtendCommandService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(SubmitStaffCommand submitStaffCommand) {
        StaffExtendPO extendPO = this.getById(submitStaffCommand.getId());
        if (extendPO == null) {
            StaffExtendPO staffExtendPO = new StaffExtendPO();
            staffExtendPO.setId(submitStaffCommand.getId());
            staffExtendPO.setCornet(submitStaffCommand.getCornet());
            this.save(staffExtendPO);
        } else {
            extendPO.setCornet(submitStaffCommand.getCornet());
            this.saveOrUpdate(extendPO);
        }
    }
}

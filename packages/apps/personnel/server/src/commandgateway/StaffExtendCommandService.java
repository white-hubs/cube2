package com.guhecloud.cube.srv.gen.personnel.management.server.commandgateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.personnel.management.server.commandgateway.command.SubmitStaffCommand;
import com.guhecloud.cube.srv.gen.personnel.management.server.po.StaffExtendPO;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/3/20 11:48
 * @Description:
 */
public interface StaffExtendCommandService extends IService<StaffExtendPO> {

    /**
     * 新增或更新员工里扩展信息
     *
     * @param submitStaffCommand
     */
    void saveOrUpdate(SubmitStaffCommand submitStaffCommand);

}

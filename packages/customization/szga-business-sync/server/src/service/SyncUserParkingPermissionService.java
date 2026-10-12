package com.guhecloud.cube.srv.gen.business.info.sync.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.business.info.sync.server.entity.UserParkingPermission;
import com.guhecloud.cube.srv.gen.business.info.sync.server.rocketmq.vo.VechicleApprovalUpdateVO;
import com.guhecloud.cube.srv.gen.business.info.sync.server.rocketmq.vo.VechicleApprovalVO;
import com.guhecloud.cube.srv.gen.business.info.sync.server.rocketmq.vo.VechicleDeleteVO;

/**
 * @author 15561
 * @description 针对表【user_parking_permission(停车权限)】的数据库操作Service
 * @createDate 2024-12-17 16:14:05
 */
public interface SyncUserParkingPermissionService extends IService<UserParkingPermission> {

    void vehicleInsert(VechicleApprovalVO vechicleApprovalVO);

    void vehicleUpdate(VechicleApprovalUpdateVO vechicleApprovalUpdateVO);

    void vehicleDelete(VechicleDeleteVO vechicleDeleteVO);
}

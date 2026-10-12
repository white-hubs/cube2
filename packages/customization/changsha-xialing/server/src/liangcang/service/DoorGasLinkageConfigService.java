package com.guhecloud.cube.customization.xialing.server.liangcang.service;

import com.guhecloud.cube.customization.xialing.server.liangcang.entity.DoorGasLinkageConfig;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.DoorGasLinkageConfigDetailResponse;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.DoorGasLinkageConfigPageRequest;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.DoorGasLinkageConfigPageResponse;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.DoorGasLinkageConfigSaveRequest;
import com.guhecloud.platform.base.model.Pagination;
import com.baomidou.mybatisplus.extension.service.IService;

/** 门禁气体联动配置业务。 */
public interface DoorGasLinkageConfigService extends IService<DoorGasLinkageConfig> {

    /** 分页查询门禁气体联动配置。 */
    Pagination<DoorGasLinkageConfigPageResponse> page(DoorGasLinkageConfigPageRequest request);

    /** 查询门禁气体联动配置详情。 */
    DoorGasLinkageConfigDetailResponse detail(Long doorDeviceId);

    /** 保存门禁气体联动配置。 */
    Boolean saveConfig(Long doorDeviceId, DoorGasLinkageConfigSaveRequest request);
}

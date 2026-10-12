package com.guhecloud.cube.biz.subsystem.parking.server.guidance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.subsystem.parking.server.guidance.entity.VehicleGuideBoardConfig;
import com.guhecloud.cube.biz.subsystem.parking.server.guidance.param.VehicleGuideBoardConfigParam;
import com.guhecloud.cube.biz.subsystem.parking.server.guidance.param.VehicleGuideBoardConfigQueryParam;
import com.guhecloud.cube.biz.subsystem.parking.server.guidance.vo.VehicleGuideBoardConfigVO;
import com.guhecloud.platform.base.model.Pagination;

/**
 * 车辆引导牌配置表 Service
 */
public interface VehicleGuideBoardConfigService extends IService<VehicleGuideBoardConfig> {

    /**
     * 新增车辆引导牌配置（包含内容配置）
     *
     * @param param 配置参数
     * @return 是否成功
     */
    Boolean save(VehicleGuideBoardConfigParam param);

    /**
     * 更新车辆引导牌配置（包含内容配置）
     *
     * @param param 配置参数
     * @return 是否成功
     */
    Boolean update(VehicleGuideBoardConfigParam param);

    /**
     * 删除车辆引导牌配置（同时删除关联的内容配置）
     *
     * @param id 配置id
     * @return 是否成功
     */
    Boolean delete(Long id);

    /**
     * 分页查询车辆引导牌配置
     *
     * @param param 查询参数
     * @return 分页结果
     */
    Pagination<VehicleGuideBoardConfigVO> page(VehicleGuideBoardConfigQueryParam param);

    /**
     * 获取车辆引导牌配置详情（包含内容配置）
     *
     * @param id 配置id
     * @return 配置详情
     */
    VehicleGuideBoardConfigVO getDetail(Long id);
}


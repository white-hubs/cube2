package com.guhecloud.cube.customization.xialing.server.liangcang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.customization.xialing.server.liangcang.entity.WarehouseDeviceBinding;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.ApplicationRelatedDevicePageParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.LiangcangWarehousePageQueryParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.RadarBindingDetailParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.RadarBoundPageParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.RadarDeviceBindParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.RadarDeviceEditParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseBoundDevicePageParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseStatisticsParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.param.WarehouseUnbindParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.ApplicationRelatedDeviceVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.BoundWarehouseSimpleVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.LiangcangLocationCardInfoVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.LiangcangWarehouseListItemVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.RadarBindingDetailVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.RadarBoundPageVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseBoundDeviceVO;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.WarehouseStatisticsVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 粮仓与设备绑定
 * 智慧仓数据、货量统计、货位卡等聚合业务接口层。
 */
public interface WarehouseDeviceBindingService extends IService<WarehouseDeviceBinding> {

    /**
     * 绑定雷达与业务设备
     *
     * @param param 智慧仓 id、雷达设备 id、待绑定业务设备 id 列表
     * @return 是否成功
     */
    Boolean bindDevices(RadarDeviceBindParam param);

    /**
     * 编辑雷达与业务设备绑定
     *
     * @param param 原仓/原雷达、新仓/新雷达、编辑后的业务设备 id 列表
     * @return 是否成功
     */
    Boolean editDevices(RadarDeviceEditParam param);

    /**
     * 粮仓业务设备解绑
     *
     * @param param 设备解绑参数
     * @return 是否成功
     */
    Boolean unbindDevices(WarehouseUnbindParam param);

    /**
     * 粮仓绑定状态分页
     *
     * @param param 绑定状态、分页、可选仓名/雷达名搜索；默认已绑定
     * @return 粮仓绑定状态分页数据
     */
    Pagination<RadarBoundPageVO> boundWarehouseInfoPage(RadarBoundPageParam param);

    /**
     * 粮仓绑定详情
     *
     * @param param 智慧仓 id
     * @return 三方仓档案与已绑雷达、业务设备
     */
    RadarBindingDetailVO bindingDetail(RadarBindingDetailParam param);

    /**
     * 仓库已绑定业务设备分页
     *
     * @param param 智慧仓 id、分页、产品/区域/名称筛选参数
     * @return 已绑定业务设备分页
     */
    Pagination<WarehouseBoundDeviceVO> boundDevicePage(WarehouseBoundDevicePageParam param);

    /**
     * 货位卡信息
     *
     * @param param 智慧仓 id
     * @return 货位卡综合 VO
     */
    LiangcangLocationCardInfoVO locationCardInfo(RadarBindingDetailParam param);

    /**
     * 仓与雷达列表
     *
     * @param param 透传智慧仓分页入参，可为空走默认
     * @return 仓×雷达行列表
     */
    List<LiangcangWarehouseListItemVO> warehouseList(LiangcangWarehousePageQueryParam param);

    /**
     * 已绑定仓简要列表
     *
     * @return 仓 id、名称、分类
     */
    List<BoundWarehouseSimpleVO> boundWarehouseSimpleList();

    /**
     * 货量与周转统计
     *
     * @param param 统计维度、时间区间、可选单仓与仓种类
     * @return 多段时间桶与可选逐次测点、同比环比等
     */
    WarehouseStatisticsVO warehouseStatistics(WarehouseStatisticsParam param);

    /**
     * 可关联业务设备分页
     *
     * @return 设备分页（本模块已绑 id 从规则中排除）
     */
    Pagination<ApplicationRelatedDeviceVO> applicationRelatedDevicePage(ApplicationRelatedDevicePageParam param);
}

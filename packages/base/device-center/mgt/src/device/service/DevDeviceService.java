package com.guhecloud.cube.iot.mgt.device.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.iot.mgt.core.data.vo.cache.DeviceDigestVo;
import com.guhecloud.cube.iot.mgt.core.data.vo.cache.DevicePropValueVo;
import com.guhecloud.cube.iot.mgt.core.data.vo.cache.ProductPropSchema;
import com.guhecloud.cube.iot.mgt.device.model.DevDevice;
import com.guhecloud.cube.iot.mgt.device.param.BatchSyncDeviceParam;
import com.guhecloud.cube.iot.mgt.device.param.BatchUpdateDeviceParam;
import com.guhecloud.cube.iot.mgt.device.param.DeviceCreateParam;
import com.guhecloud.cube.iot.mgt.device.param.DeviceCreateParamNew;
import com.guhecloud.cube.iot.mgt.device.param.DeviceListParam;
import com.guhecloud.cube.iot.mgt.device.param.DevicePageParam;
import com.guhecloud.cube.iot.mgt.device.param.DeviceUpdateParam;
import com.guhecloud.cube.iot.mgt.device.param.CommandHistoryPageParam;
import com.guhecloud.cube.iot.mgt.device.vo.CommandHistoryVo;
import com.guhecloud.cube.iot.mgt.device.vo.CommandLogVo;
import com.guhecloud.cube.iot.mgt.device.vo.DeviceThinPairVo;
import com.guhecloud.cube.iot.mgt.device.vo.DeviceVo;
import com.guhecloud.platform.base.model.Pagination;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface DevDeviceService extends IService<DevDevice> {

    /**
     * 设备详情
     *
     * @param deviceId 设备ID
     *
     * @return DeviceVo
     */
    DeviceVo deviceDetail(Long deviceId);

    Pagination<DeviceVo> listPage(DevicePageParam devicePageParam);

    void saveDevice(DeviceCreateParam deviceCreateParam);

    /**
     * 批量删除
     *
     * @param ids 设备ID
     */
    void batchDel(List<Long> ids);

    /**
     * 根据产品ID查询设备
     *
     * @param productId 产品ID
     *
     * @return List<DeviceThinPairVo>
     */
    List<DeviceThinPairVo> listDeviceByProductId(Long productId);

    String queryProdCategoryIdByDeviceId(Long deviceId);

    /**
     * 根据产品ID查询设备数量
     *
     * @param productId 产品ID
     *
     * @return Integer
     */
    Long countByProdId(Long productId);

    String queryDeviceSecret(String deviceId);

    /**
     * 枚举所有网关设备
     *
     * @return List<DeviceVo>
     */
    List<DeviceVo> listVisibleGateway();

    /**
     * 添加设备
     *
     * @param deviceCreateParam 设备信息
     *
     * @return 设备id
     */
    Long addDevice(DeviceCreateParam deviceCreateParam);

    /**
     * 批量上报设备
     *
     * @param modelList 设备列表
     * @param parentId  父设备id
     * @param productId 产品id
     *
     * @return List<String>
     */
    Collection<String> batchSaveDevice(List<DeviceCreateParamNew> modelList, Long parentId, Long productId);

    DeviceDigestVo queryDeviceDigest(Long deviceId);

    List<ProductPropSchema> queryProductPropSchema(Long prodId);

    Optional<DevicePropValueVo> queryDevicePropCache(Long deviceId, String propCode);

    void updateDevice(DeviceUpdateParam deviceUpdateParam);

    /**
     * @param param 修改参数
     */
    String batchUpdate(BatchUpdateDeviceParam param);

    /**
     * 从缓存同步设备信息
     *
     * @param syncDeviceParam 设备同步信息
     *
     * @return 同步结果
     */
    List<String> synDeviceByCache(BatchSyncDeviceParam syncDeviceParam);

    List<DeviceVo> listInfoByIds(DeviceListParam param);

    /**
     * 分页查询设备命令下发历史记录
     *
     * @param param 分页及过滤参数
     * @return 分页结果
     */
    Pagination<CommandHistoryVo> getCommandHistoryPage(CommandHistoryPageParam param);

    /**
     * 查询指定命令的详情日志
     *
     * @param instanceId 实例ID
     * @return 日志列表
     */
    List<CommandLogVo> getCommandLogs(String instanceId);
}

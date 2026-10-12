package com.guhecloud.cube.base.device.center.core.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.device.center.core.dto.DeviceListDto;
import com.guhecloud.cube.base.device.center.core.dto.DevicePageDto;
import com.guhecloud.cube.base.device.center.core.model.BizDevice;
import com.guhecloud.cube.base.device.center.core.param.DeviceListQueryParam;
import com.guhecloud.cube.base.device.center.core.param.DevicePageParam;
import com.guhecloud.cube.base.device.center.core.vo.DeviceIdAndCode;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseAndIotPropertyInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseDetailsDTO;
import com.guhecloud.cube.base.device.center.metadata.param.QueryDeviceByTagParam;
import com.guhecloud.cube.base.device.center.metadata.std.dto.AreaDeviceNumberDTO;
import com.guhecloud.cube.base.device.center.metadata.std.dto.DeviceStateCountDTO;
import com.guhecloud.cube.base.device.center.metadata.std.vo.DeviceOfflineCountVO;
import com.guhecloud.cube.base.device.center.metadata.vo.AreaPointVO;
import com.guhecloud.cube.base.device.center.metadata.vo.DeviceAreaSeqVO;
import com.guhecloud.cube.base.device.center.metadata.vo.DeviceUsefulLifeExpireStatisticsVO;
import com.guhecloud.cube.base.device.center.metadata.vo.DevicesVO;
import com.guhecloud.cube.base.device.center.metadata.vo.GenreVO;
import com.guhecloud.cube.base.device.center.metadata.vo.ProductCategoryOffLineCountVO;
import com.guhecloud.cube.base.device.center.open.vo.DeviceBaseInfoAndNumberVO;
import com.guhecloud.cube.base.device.center.open.vo.DeviceFireSafetyReportVO;
import com.guhecloud.cube.base.device.center.open.vo.ProductDeviceCountVO;
import com.guhecloud.cube.base.device.center.statistics.param.DeviceStatisticsQueryParam;
import com.guhecloud.cube.base.device.center.statistics.vo.DeviceStatisticsNumVO;
import com.guhecloud.cube.base.device.center.statistics.vo.DeviceStatisticsVO;
import com.guhecloud.platform.base.model.Period;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Mapper
public interface CenterBizDeviceMapper extends BaseMapper<BizDevice> {

    boolean existDeviceId(@Param("iotId") Long iotId);

    long pageCount(@Param("pageParam") DevicePageParam pageParam);

    Page<DevicePageDto> page(Page page, @Param("pageParam") DevicePageParam pageParam);

    List<DeviceListDto> queryDeviceDetailsList(@Param("listParam") DeviceListQueryParam listQueryParam);

    List<DeviceListDto> queryDeviceDetailsNewList(@Param("listParam") DeviceListQueryParam listQueryParam);

    List<Long> queryDeviceIds(@Param("listParam") DeviceListQueryParam listQueryParam);

    @InterceptorIgnore(tenantLine = "true")
    Integer updateDeviceStateNoTenant(@Param("iotDeviceId") Long iotDeviceId, @Param("state") Integer state,
                                      @Param("updateTime") LocalDateTime updateTime
                                     );

    @InterceptorIgnore(tenantLine = "true")
    void updateBySelective(@Param("bizDevice") BizDevice bizDevice);

    Integer queryNormalDevice(@Param("tagId") Long tagId);

    DeviceStateCountDTO selectNumberOfGroupByStatusByTags(@Param("tagCodes") List<String> tagCodes);

    List<DevicesVO> listDevicesVo(@Param("tagId") Long tagId);

    List<DeviceOfflineCountVO> queryDeviceOffline(@Param("tagId") Long tagId);

    List<AreaPointVO> queryAreaPoint(@Param("tagId") Long tagId);

    List<AreaDeviceNumberDTO> queryDeviceAreaNum(@Param("tagId") Long tagId);

    List<DeviceAreaSeqVO> queryDeviceAreaSeqNumGroupByAreaLevel(@Param("tagIds") List<Long> tagIds);

    List<GenreVO> queryGenreDevicesNum(@Param("tagId") Long tagId);

    Integer queryTotalDevice();

    @InterceptorIgnore(tenantLine = "true")
    void updateThirdPartyId(@Param("deviceId") Long deviceId, @Param("thirdPartyId") String thirdPartyId);

    @InterceptorIgnore(tenantLine = "true")
    void updateDeviceCfg(@Param("deviceId") Long deviceId, @Param("cfg") String cfg);

    void logicDeletedBatchIds(@Param("userId") String userId, @Param("userType") String userType,
                              @Param("deviceIds") List<Long> deviceIds,
                              @Param("updateTime") LocalDateTime updateTime
                             );

    @InterceptorIgnore(tenantLine = "true")
    Integer checkDeviceExist(@Param("deviceName") String deviceName, @Param("deviceCode") String deviceCode,
                             @Param("iotDeviceId") Long iotDeviceId, @Param("productId") Long productId,
                             @Param("tenant") Integer tenant
                            );

    @InterceptorIgnore(tenantLine = "true")
    BizDevice getDeviceInfoNoTenant(@Param("deviceId") Long id);

    @InterceptorIgnore(tenantLine = "true")
    Long countAllDeviceId();

    @InterceptorIgnore(tenantLine = "true")
    List<Long> selectDeviceIdPage(@Param("offset") int offset, @Param("pageSize") int pageSize);

    @InterceptorIgnore(tenantLine = "true")
    void syncDeviceOnlineTime(@Param("deviceId") Long deviceId, @Param("onlineTime") LocalDateTime date, @Param("isOnline") int isOnline);

    Long queryProductIdByDeviceId(@Param("deviceId") Long deviceId);

    @InterceptorIgnore(tenantLine = "true")
    void syncDeviceOnState(@Param("deviceId") Long deviceId, @Param("isOnline") int isOnline);

    Page<DeviceStatisticsVO> deviceStatisticsPage(Page page, @Param("query") DeviceStatisticsQueryParam query);

    List<DeviceStatisticsVO> deviceStatistics(@Param("query") DeviceStatisticsQueryParam query);

    List<ProductCategoryOffLineCountVO> getTopProductCategoryStatisticsByTagCode(@Param("tagId") Long tagId);

    List<DeviceBaseAndIotPropertyInfoDTO> getDeviceBaseInfoByProductTag(@Param("tagId") Long tagId);

    @Update("update center_biz_device set area_name = #{areaPathName} where area_id = #{areaSequence}")
    Integer updateCacheAreaInfo(@Param("areaSequence") String areaSequence, @Param("areaPathName") String areaPathName);

    @InterceptorIgnore(tenantLine = "true")
    String queryThirdPartyIdByDeviceId(@Param("deviceId") Long deviceId);

    List<DeviceStatisticsNumVO> getDevicesNumber(@Param("period") String period);

    List<DeviceBaseInfoAndNumberVO> selectDeviceNumberGroupOnStatusByTagCode(@Param("tagCodes") List<String> tagCodes,
                                                                             @Param("period") Period period
                                                                            );

    Long selectDeviceNumberByTagCode(@Param("tagCodes") List<String> tagCodes, @Param("period") Period period);

    List<DeviceFireSafetyReportVO> selectFireDeviceNumberByTagCode(@Param("tagCode") String tagCode, @Param("period") Period period);

    List<DeviceBaseDetailsDTO> getThirdPartyIdListByTagCode(@Param("tagCode") Object tagCode);

    Set<Long> getDeviceIdsByTagCode(@Param("tagCode") String tagCode);

    List<ProductDeviceCountVO> getProductDeviceCountByTagCode(@Param("tagIds") List<Long> tagIds);

    DeviceUsefulLifeExpireStatisticsVO getDeviceExpireStatistics(@Param("tagIds") List<Long> tagIds);

    List<ProductCategoryOffLineCountVO> getProductCategoryStatisticsByTagCodes(@Param("param") QueryDeviceByTagParam param);

    List<DeviceBaseDetailsDTO> selectDeviceDetailsByDeviceIds(@Param("deviceIds") Set<Long> deviceIds);

    /**
     * 按 center_biz_device.thirdparty_id 精确查一条，列与 {@link #selectDeviceDetailsByDeviceIds} 相同。
     */
    DeviceBaseDetailsDTO selectDeviceDetailsByThirdPartyId(@Param("thirdPartyId") String thirdPartyId);

    Set<String> deviceCodesByDeviceIds(@Param("deviceIds") Set<Long> deviceIds);

    List<DeviceIdAndCode> queryDeviceCodeByIds(@Param("deviceIds") Set<Long> deviceIds);

    /**
     * 检查三维模型编码是否已存在（排除指定设备）
     *
     * @param deviceCode      三维模型编码
     * @param excludeDeviceId 排除的设备ID
     *
     * @return 匹配到的设备数量
     */
    int existDeviceCode(@Param("deviceCode") String deviceCode, @Param("excludeDeviceId") Long excludeDeviceId);

    /**
     * 批量更新设备状态（无租户拦截）
     *
     * @param devices 设备状态列表
     *
     * @return 更新的记录数
     */
    @InterceptorIgnore(tenantLine = "true")
    Integer batchUpdateDeviceStateNoTenant(@Param("devices") List<BizDevice> devices);
}

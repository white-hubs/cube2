package com.guhecloud.cube.srv.gen.securitymanagement.server.common.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceProductInfoDTO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.dao.dto.SecurityDeviceInfoDTO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.entity.SecurityDevice;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.param.DeviceDeletedParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.param.DeviceFilterPageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.query.DeviceInfoPageQuery;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.AreaDeviceNumVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.DeviceCollectionVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.DeviceProdCategoryVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.ModelVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.SecurityDeviceApplicationCodeStatisticsVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.SecurityDeviceApplicationStatusStatisticsVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.perimeter.vo.AreaOfflineVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SecurityCommonMapper extends BaseMapper<SecurityDevice> {
    List<DeviceProdCategoryVo> getGenreVo(@Param("applicationCode") String applicationCode);

    List<ModelVo> getModelVo(@Param("applicationCode") String applicationCode, @Param("genre") String genre);

    List<AreaDeviceNumVO> getDeviceNum(@Param("applicationCode") String applicationCode);

    List<SecurityDeviceApplicationCodeStatisticsVO> selectSecurityDeviceApplicationStatistics();

    List<SecurityDeviceApplicationStatusStatisticsVO> selectSecurityDeviceApplicationStatusStatistics();

    List<DeviceProdCategoryVo> getApplicationGenreVo(@Param("applicationCode") String applicationCode);

    List<ModelVo> getApplicationModels(@Param("applicationCode") String applicationCode);

    List<DeviceProdCategoryVo> getStatisticsGenreVo(@Param("list") List<Long> list);

    List<DeviceCollectionVo> getDeviceCollection(@Param("applicationCode") String applicationCode, @Param("userId") Long userId);

    void delDeviceInstance(@Param("list") List<Long> list, @Param("delTime") Long delTime);

    void delCollectionDeviceInstance(@Param("list") List<Long> list, @Param("delTime") Long delTime);

    void delCollectionSecurityDeviceInstance(@Param("id") Long id, @Param("applicationCode") String applicationCode,
                                             @Param("delTime") Long delTime
                                            );

    void delAppDevices(@Param("applicationCode") String applicationCode, @Param("delTime") Long delTime);

    void delAppSecurityDeviceInstance(@Param("list") List<Long> list, @Param("applicationCode") String applicationCode,
                                      @Param("delTime") Long delTime
                                     );

    Integer insertBatch(@Param("securityDevices") List<SecurityDevice> securityDevices);

    void updateBatch(@Param("param") List<SecurityDevice> securityDevices);

    void updateDeviceStatus(@Param("deviceId") Long deviceId, @Param("deviceStatus") Boolean deviceStatus,
                            @Param("communicationStatus") String communicationStatus
                           );

    //获取系统总览
    Integer getEquipmentOverview(@Param("list") List<Long> list);

    Page<SecurityDeviceInfoDTO> selectDeviceInfoPage(Page page, @Param("param") DeviceInfoPageQuery deviceInfoPageQuery);

    List<AreaOfflineVo> getAreaStatusNum(@Param("ids") List<Long> ids);

    void updateDeviceBatch(@Param("list") List<SecurityDevice> list);

    void updateProduct(@Param("productInfo") DeviceProductInfoDTO productInfoDTO);

    List<Long> selectConditionDeleteSecurityDeviceIds(@Param("param") DeviceDeletedParam param);

    List<Long> selectDeviceIds(@Param("applicationCode") String applicationCode);

    List<SecurityDevice> selectSecurityDeviceByIds(@Param("param") DeviceFilterPageParam param);
}

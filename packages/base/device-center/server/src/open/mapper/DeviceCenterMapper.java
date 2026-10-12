package com.guhecloud.cube.base.device.center.open.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.device.center.core.dto.DevicePageDto;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseArchivesInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseArchivesInfoWithFullAreaDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceGenreDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceIdTypeDTO;
import com.guhecloud.cube.base.device.center.metadata.lang.Pair;
import com.guhecloud.cube.base.device.center.metadata.param.DeviceBaseInfoPageQry;
import com.guhecloud.cube.base.device.center.metadata.param.QueryDeviceByAreasOrGroupsAndTagParam;
import com.guhecloud.cube.base.device.center.metadata.param.QueryDeviceCustomInfoParam;
import com.guhecloud.cube.base.device.center.metadata.param.QueryDeviceCustomInfoParamPage;
import com.guhecloud.cube.base.device.center.metadata.param.QueryDeviceSelfInfoParam;
import com.guhecloud.cube.base.device.center.metadata.param.QueryDeviceSelfInfoParamPage;
import com.guhecloud.cube.base.device.center.metadata.std.dto.AreaDeviceNumberDTO;
import com.guhecloud.cube.base.device.center.metadata.std.dto.DeviceStateCountDTO;
import com.guhecloud.cube.base.device.center.metadata.std.param.DeviceFilterPageParam;
import com.guhecloud.cube.base.device.center.metadata.std.param.DeviceFilterParam;
import com.guhecloud.cube.base.device.center.metadata.std.vo.DeviceExpandVO;
import com.guhecloud.cube.base.device.center.metadata.std.vo.DeviceInfoVO;
import com.guhecloud.cube.base.device.center.metadata.std.vo.DeviceOfflineCountVO;
import com.guhecloud.cube.base.device.center.metadata.vo.AreaPointVO;
import com.guhecloud.cube.base.device.center.metadata.vo.DeviceDetailThinVO;
import com.guhecloud.cube.base.device.center.metadata.vo.DeviceInstanceDetailVO;
import com.guhecloud.cube.base.device.center.metadata.vo.DeviceSelfInfoVO;
import com.guhecloud.cube.base.device.center.metadata.vo.DevicesVO;
import com.guhecloud.cube.base.device.center.metadata.vo.GenreVO;
import com.guhecloud.cube.base.device.center.open.interfaces.thirdparty.vo.HeadquartersDataGatherDeviceVO;
import com.guhecloud.cube.base.device.center.open.interfaces.web.query.DevicePageQry;
import com.guhecloud.cube.base.device.center.open.interfaces.web.vo.DevicePageInfoVO;
import com.guhecloud.cube.base.device.center.open.vo.AreaEmptyDeviceTotalVO;
import com.guhecloud.cube.base.device.center.open.vo.DeviceManagerVO;
import com.guhecloud.cube.base.device.center.open.vo.ExpiredDeviceVO;
import com.guhecloud.cube.base.device.center.visualization.param.DeviceUsefulLifeExpireParam;
import com.guhecloud.cube.base.device.center.visualization.vo.DeviceAreaGroupVo;
import com.guhecloud.cube.base.device.center.visualization.vo.DeviceStatusGroupVo;
import com.guhecloud.cube.base.device.center.visualization.vo.DeviceStatusTotalData;
import com.guhecloud.cube.base.device.center.visualization.vo.DeviceTypeGroupVo;
import com.guhecloud.cube.base.device.center.visualization.vo.DeviceUsefulLifeExpireVO;
import com.guhecloud.platform.base.model.Period;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Mapper
public interface DeviceCenterMapper {

    List<DeviceSelfInfoVO> queryDeviceSelfInfo(@Param("param") QueryDeviceSelfInfoParam param);

    Page<DeviceSelfInfoVO> queryDeviceSelfInfo(Page<?> page, @Param("param") QueryDeviceSelfInfoParamPage param);

    List<DeviceInfoVO> queryCustomDeviceInfo(@Param("param") QueryDeviceCustomInfoParam param);

    Page<DeviceInfoVO> queryCustomDeviceInfo(Page<?> page, @Param("param") QueryDeviceCustomInfoParamPage param);

    DeviceInstanceDetailVO selectDetail(Long deviceId);

    DeviceDetailThinVO selectDetailThin(Long deviceId);

    Page<DevicePageInfoVO> deviceInfoPage(Page<?> page, @Param("param") DeviceFilterPageParam param,
                                          @Param("tagCodeIds") List<Long> tagCodeIds,
                                          @Param("order") String order
                                         );

    Page<DeviceBaseArchivesInfoDTO> pageOfBaseInfo(Page page, @Param("param") DeviceBaseInfoPageQry deviceBaseInfoPageQry);

    Page<DeviceBaseArchivesInfoWithFullAreaDTO> pageOfBaseInfoWithFullArea(Page page,
                                                                           @Param("param") DeviceBaseInfoPageQry deviceBaseInfoPageQry
                                                                          );

    List<DeviceInfoVO> deviceInfoList(@Param("param") DeviceFilterParam param, @Param("tagCodeIds") List<Long> tagCodeIds);

    Set<Long> selectDeviceIds(@Param("param") DeviceFilterParam param, @Param("tagCodeIds") List<Long> tagCodeIds);

    AreaEmptyDeviceTotalVO getAlarmEmptyDeviceTotal(@Param("param") DeviceFilterParam param);

    DeviceInfoVO deviceInfoSingle(@Param("deviceId") Long deviceId);

    List<Long> selectRelatedDeviceIds(Long deviceId);

    Long deviceExits(@Param("deviceInstanceId") Long deviceInstanceId);

    List<Pair<String, String>> getCodeByDeviceId(@Param("deviceIds") Set<Long> deviceIds);

    Integer selectCountNormalDevice(@Param("deviceIds") Set<Long> deviceIds);

    DeviceStateCountDTO selectNumberOfGroupByStatus(@Param("deviceIds") List<Long> deviceIds);

    List<DevicesVO> selectDevicesVo(@Param("deviceIds") List<Long> deviceIds, @Param("tenant") Integer tenant);

    List<GenreVO> selectGenreDevicesNum(@Param("deviceIds") List<Long> deviceIds, @Param("tenant") Integer tenant);

    List<AreaDeviceNumberDTO> selectDeviceAreaNum(@Param("deviceIds") List<Long> deviceIds);

    List<Long> selectDeviceIdsByAll();

    List<AreaPointVO> selectAreaPoint(@Param("deviceIds") List<Long> deviceIds);

    List<AreaPointVO> selectApplicationAreaPoint(@Param("deviceIds") List<Long> deviceIds);

    List<DeviceOfflineCountVO> selectDeviceOffline(@Param("deviceIds") Set<Long> deviceIds);

    List<DeviceGenreDTO> selectDeviceListFilterableByGenre(@Param("name") String name,
                                                           @Param("filteredDeviceIds") Set<Long> filteredDeviceIds
                                                          );

    List<DeviceGenreDTO> selectDeviceListByGenre(@Param("deviceType") Long deviceType, @Param("name") String name);

    List<DeviceManagerVO> getManagerByDevice(@Param("deviceIds") Set<Long> deviceInstanceIds);

    List<Long> listByManager(@Param("managerId") Long managerId);

    List<ExpiredDeviceVO> expiredDevices();

    int getDeviceTotal(@Param("deviceTypes") List<Long> deviceTypes, @Param("st") LocalDateTime st, @Param("et") LocalDateTime et);

    List<DeviceTypeGroupVo> getDeviceTypeGroup(@Param("deviceTypes") List<Long> deviceTypes, @Param("st") LocalDateTime st,
                                               @Param("et") LocalDateTime et
                                              );

    List<DeviceStatusGroupVo> getDeviceStatusGroup(@Param("deviceTypes") List<Long> deviceTypes, @Param("st") LocalDateTime st,
                                                   @Param("et") LocalDateTime et
                                                  );

    List<DeviceAreaGroupVo> getDeviceAreaGroup(@Param("deviceTypes") List<Long> deviceTypes, @Param("st") LocalDateTime st,
                                               @Param("et") LocalDateTime et
                                              );

    List<DeviceBaseArchivesInfoDTO> getDeviceBaseArchivesInfoListByDeviceId(@Param("deviceIds") Set<Long> deviceIds);

    List<DeviceBaseArchivesInfoDTO> getListByManager(@Param("managerId") Long managerId);

    Set<DeviceBaseArchivesInfoDTO> getAssociatedVideos(@Param("deviceId") Long deviceId, @Param("tagId") Long tagId);

    List<DeviceBaseArchivesInfoDTO> getListByTagCodes(@Param("tagCodes") Set<String> tagCodes);

    Page<DevicePageDto> page(Page page, @Param("pageParam") DevicePageQry pageParam);

    Set<Long> getDeviceIdsByProductIds(@Param("productIds") Set<Long> productIds);

    Set<Long> getDeviceIdsByProdCategoryCode(@Param("prodCategoryCodeConcat") String prodCategoryCodeConcat);

    DeviceBaseArchivesInfoDTO getDeviceBaseArchivesInfoByDeviceId(@Param("deviceId") Long deviceId);

    DeviceBaseArchivesInfoDTO getDeviceBaseArchivesInfoByDeviceCode(@Param("deviceCode") String deviceCode);

    String getProdCategoryCodeByProductId(@Param("productId") Long productId);

    Page<DeviceUsefulLifeExpireVO> selectUsefulLifeExpiredDevicePage(Page page, @Param("param") DeviceUsefulLifeExpireParam param);

    Integer selectUsefulLifeExpiredDeviceCount(@Param("param") DeviceUsefulLifeExpireParam param);

    @InterceptorIgnore(tenantLine = "on")
    List<HeadquartersDataGatherDeviceVO> selectThirdPartyHeadquartersDataGather(@Param("period") Period period,
                                                                                @Param("tenant") Integer tenant
                                                                               );

    DeviceStatusTotalData getStatusTotalData(@Param("areaId") Long areaId, @Param("tagCode") String tagCode);

    List<DeviceExpandVO> getSpatialCoordinatesDevices(@Param("areaId") Long areaId);

    Set<DeviceBaseArchivesInfoDTO> getVideosByIds(@Param("deviceIds") Set<Long> deviceIds, @Param("tagId") Long tagId);

    Integer deviceCountByAreasOrGroupsAndTag(@Param("param") QueryDeviceByAreasOrGroupsAndTagParam param);

    Set<Long> deviceIdsByAreasOrGroupsAndTag(@Param("param") QueryDeviceByAreasOrGroupsAndTagParam param);

    List<DeviceIdTypeDTO> selectDeviceIdTypes(@Param("param") DeviceFilterParam deviceFilterParam, List<Long> tagCodeIds);

    List<Pair<Long, Long>> countByAreaIds(@Param("areaIds") List<Long> areaIds);
}

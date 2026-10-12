package com.guhecloud.cube.base.device.center.core.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.device.center.core.model.BizTag;
import com.guhecloud.cube.base.device.center.metadata.param.QueryDeviceSelfInfoParamPage;
import com.guhecloud.cube.base.device.center.metadata.std.param.CustomerTagPageParam;
import com.guhecloud.cube.base.device.center.metadata.std.param.CustomerTagParam;
import com.guhecloud.cube.base.device.center.metadata.std.param.QueryDeviceByAreaParam;
import com.guhecloud.cube.base.device.center.metadata.std.vo.CustomerTagVO;
import com.guhecloud.cube.base.device.center.metadata.std.vo.DeviceTagFullVO;
import com.guhecloud.cube.base.device.center.metadata.vo.DeviceRefTagVO;
import com.guhecloud.cube.base.device.center.metadata.vo.DeviceSelfInfoVO;
import com.guhecloud.cube.base.device.center.open.vo.SecurityDeviceApplicationCodeStatisticsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface CenterBizTagMapper extends BaseMapper<BizTag> {
    int deleteByPrimaryKey(Long id);

    int insertSelective(BizTag record);

    BizTag selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BizTag record);

    int updateByPrimaryKey(BizTag record);

    int batchInsert(@Param("list") List<BizTag> list);

    List<Long> queryDeviceIdsByTagCode(@Param("tagCode") String tagCode);

    List<Long> queryDeviceIdsByTagCodeAndArea(@Param("tagCode") String tagCode, @Param("param") QueryDeviceByAreaParam param);

    Page<DeviceSelfInfoVO> queryDeivceSelfInfoPage(Page<Object> convert,
                                                   @Param("param") QueryDeviceSelfInfoParamPage queryDeviceSelfInfoParamPage,
                                                   @Param("tagId") Long tagId
                                                  );

    Integer getEquipmentOverview(@Param("ids") Set<Long> ids);

    List<SecurityDeviceApplicationCodeStatisticsVO> selectSecurityDeviceApplicationStatistics();

    List<Long> getIds(@Param("code") String code);

    @InterceptorIgnore(tenantLine = "true")
    Integer findTenantIdByDeviceId(@Param("deviceId") Long deviceId);

    List<DeviceTagFullVO> selectDeviceTags(@Param("deviceIds") List<Long> deviceIds);

    List<BizTag> selectDeviceTagsByCodeLeftLike(@Param("tagCode") String tagCode);

    List<BizTag> queryByTagCode(@Param("tagCode") String tagCode);

    void updateTageName(CustomerTagParam createTagCmd);

    void updateEnableState(@Param("tagId") Long tagId, @Param("status") Integer status);

    Page<CustomerTagVO> customerTagPage(Page<Object> convert, @Param("param") CustomerTagPageParam param, @Param("prefix") String prefix);

    void realDeleteById(@Param("tagId") Long tagId);

    List<CustomerTagVO> deviceTagList(@Param("devId") Long devId, @Param("tagPrefix") String tagPrefix);

    List<DeviceRefTagVO> deviceAllTag(@Param("deviceIds") Set<Long> deviceIds);

    List<CustomerTagVO> customerTagList(@Param("prefix") String prefix);

    @InterceptorIgnore(tenantLine = "true")
    BizTag getTagByNameAndCodePrefix(@Param("tagName") String tagName, @Param("codePrefix") String codePrefix);

    @InterceptorIgnore(tenantLine = "true")
    void recoverTag(@Param("tagId") Long tagId);
}
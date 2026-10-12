package com.guhecloud.cube.base.device.center.core.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.base.device.center.core.model.BizTag;
import com.guhecloud.cube.base.device.center.core.model.BizTagRef;
import com.guhecloud.cube.base.device.center.core.vo.TagCountInfo;
import com.guhecloud.cube.base.device.center.metadata.lang.Pair;
import com.guhecloud.cube.base.device.center.metadata.std.vo.CustomerTagVO;
import com.guhecloud.cube.base.device.center.open.vo.DeviceTag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Mapper
public interface CenterBizTagRefMapper extends BaseMapper<BizTagRef> {

    void logicDeleteByDeviceIds(@Param("userId") String userId, @Param("userType") String userType,
                                @Param("deviceIds") List<Long> deviceIds, @Param("updateTime")
                                LocalDateTime updateTime
                               );

    void logicDeletedByDeviceIds(@Param("userId") String userId,
                                 @Param("userType") String username,
                                 @Param("userType") String userType,
                                 @Param("deviceIds") List<Long> deviceIds,
                                 @Param("updateTime") LocalDateTime updateTime, @Param("deleted") Long deleted
                                );

    List<Pair<String, String>> queryDeviceCode(@Param("tagId") Long tagId);

    TagCountInfo tagCountInfo(@Param("tagId") Long tagId);

    List<DeviceTag> getDeviceTagCode(@Param("deviceId") Long deviceId);

    List<DeviceTag> getTagCodeByBusinessKey(@Param("businessKey") Long businessKey);

    Integer insertBatch(@Param("bizTagRefs") List<BizTagRef> bizTagRefs);

    void logicDelete(@Param("deviceId") Long deviceId, @Param("tagId") Long tagId, @Param("updateTime") LocalDateTime updateTime);

    void logicBatchDelete(@Param("tagId") Long tagId, @Param("deviceIds") Set<Long> deviceIds,
                          @Param("updateTime") LocalDateTime updateTime
                         );

    void logicTagAllDelete(@Param("tagId") Long tagId, @Param("updateTime") LocalDateTime updateTime);

    @InterceptorIgnore(tenantLine = "true")
    void physicalDeleteByTagDeviceIds(@Param("tagId") Long tagId, @Param("deviceIds") Set<Long> deviceIds);

    @InterceptorIgnore(tenantLine = "true")
    int recover(@Param("deviceId") Long deviceId, @Param("tagId") Long tagId, @Param("tenant") Integer tenant);

    @InterceptorIgnore(tenantLine = "true")
    void updateBySelective(@Param("tagRef") BizTagRef tagRef, @Param("bizTag") BizTag bizTag);

    Set<Long> queryDeviceTagId(@Param("deviceId") Long deviceId);

    void realDeleteByTagId(@Param("tagId") Long tagId);

    List<Long> getTagRefIds(@Param("deviceId") Long deviceId, @Param("prefix") String deviceCenterCustomerTagType);

    Set<Long> tagRefDeviceIds(@Param("tagCodes") List<String> tagCodes);

    List<CustomerTagVO> deviceRefTagList(@Param("deviceIds") Set<Long> deviceIds, @Param("prefixCodes") Set<String> prefixCodes);

    @InterceptorIgnore(tenantLine = "true")
    BizTagRef queryByDeviceAndTag(@Param("deviceId") Long deviceId, @Param("tagId") Long tagId, @Param("tenant") Integer tenant);
}
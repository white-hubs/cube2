package com.guhecloud.cube.base.device.center.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.base.device.center.core.model.BizGroupRef;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Mapper
public interface CenterBizGroupRefMapper extends BaseMapper<BizGroupRef> {

    Integer insertBatch(@Param("bizGroupRefs") List<BizGroupRef> bizGroupRefs);

    void logicBatchDelete(@Param("groupId") Long groupId, @Param("updateTime") LocalDateTime updateTime, @Param("userId") String userId,
                          @Param("username") String username,
                          @Param("userType") String userType
                         );

    void logicBatchDeleteByDeviceIds(@Param("groupId") Long groupId,
                                     @Param("deviceIds") Set<Long> deviceIds,
                                     @Param("updateTime") LocalDateTime updateTime,
                                     @Param("userId") String userId,
                                     @Param("username") String username,
                                     @Param("userType") String userType
                                    );
}

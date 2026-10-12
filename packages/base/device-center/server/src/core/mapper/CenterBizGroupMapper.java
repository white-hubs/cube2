package com.guhecloud.cube.base.device.center.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.base.device.center.core.model.BizGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.Set;

@Mapper
public interface CenterBizGroupMapper extends BaseMapper<BizGroup> {

    void logicDeleteByIds(@Param("userId") String userId,
                          @Param("username") String username,
                          @Param("userType") String userType,
                          @Param("ids") Set<Long> ids,
                          @Param("updateTime") LocalDateTime updateTime,
                          @Param("deleted") Long deleted
                         );
}

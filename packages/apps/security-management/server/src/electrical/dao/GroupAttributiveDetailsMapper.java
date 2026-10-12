package com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.entity.GroupAttributiveDetails;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.vo.AttributiveGroupVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.vo.GroupAttributiveInfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GroupAttributiveDetailsMapper extends BaseMapper<GroupAttributiveDetails> {
    List<GroupAttributiveInfoVo> getGroupAttributive(@Param("applicationCode") String applicationCode, @Param("groupName") String groupName
                                                    );

    List<AttributiveGroupVo> getGroupAttributives(@Param("applicationCode") String applicationCode, @Param("list") List<String> list);
}

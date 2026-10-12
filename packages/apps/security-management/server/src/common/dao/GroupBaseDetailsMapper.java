package com.guhecloud.cube.srv.gen.securitymanagement.server.common.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.param.GroupControlOrderParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.entity.GroupBaseDetails;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.CommonStat;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.GroupBaseDetailsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GroupBaseDetailsMapper extends BaseMapper<GroupBaseDetails> {
    List<GroupBaseDetailsVo> getByGroupId(Long id);

    List<GroupBaseDetailsVo> getGroupBaseDetails(@Param("param") GroupControlOrderParam param);

    List<CommonStat> getHlInGroupStat();
}

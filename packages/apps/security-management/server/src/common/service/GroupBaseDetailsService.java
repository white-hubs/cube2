package com.guhecloud.cube.srv.gen.securitymanagement.server.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.param.GroupControlOrderParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.entity.GroupBaseDetails;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.GroupBaseDetailsVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.HlInGroupStatVo;

import java.util.List;

public interface GroupBaseDetailsService extends IService<GroupBaseDetails> {

    List<GroupBaseDetailsVo> getGroupBaseDetails(GroupControlOrderParam param);

    HlInGroupStatVo getHlInGroupStat(GroupControlOrderParam param);
}

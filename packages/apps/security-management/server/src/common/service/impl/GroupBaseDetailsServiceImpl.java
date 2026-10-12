package com.guhecloud.cube.srv.gen.securitymanagement.server.common.service.impl;

import com.guhecloud.platform.base.util.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.param.GroupControlOrderParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.dao.GroupBaseDetailsMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.entity.GroupBaseDetails;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.service.GroupBaseDetailsService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.CommonStat;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.GroupBaseDetailsVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.HlInGroupStatVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GroupBaseDetailsServiceImpl extends ServiceImpl<GroupBaseDetailsMapper, GroupBaseDetails> implements GroupBaseDetailsService {
    private final GroupBaseDetailsMapper groupBaseDetailsMapper;

    @Override
    public List<GroupBaseDetailsVo> getGroupBaseDetails(GroupControlOrderParam param) {

        return groupBaseDetailsMapper.getGroupBaseDetails(param);
    }

    @Override
    public HlInGroupStatVo getHlInGroupStat(GroupControlOrderParam param) {
        List<CommonStat> hlInGroupStat = groupBaseDetailsMapper.getHlInGroupStat();
        HlInGroupStatVo data = new HlInGroupStatVo();
        if (CollectionUtils.isNotEmpty(hlInGroupStat)) {
            data.setDetailStat(hlInGroupStat);
            //设置总数
            data.setTotalOnGroup(hlInGroupStat.stream()
                                         .mapToInt(CommonStat::getNum)
                                         .sum());
        }
        return data;
    }
}

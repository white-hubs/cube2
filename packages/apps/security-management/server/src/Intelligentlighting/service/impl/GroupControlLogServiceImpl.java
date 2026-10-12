package com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.service.impl;

import com.guhecloud.platform.base.util.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.dao.GroupControlLogMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.entity.GroupControlLog;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.param.GroupControlLogQueryParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.service.GroupControlLogService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.vo.GroupControlLogVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.CommonStat;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.HlInGroupStatVo;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.database.entity.BasicEntity;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GroupControlLogServiceImpl extends ServiceImpl<GroupControlLogMapper, GroupControlLog> implements GroupControlLogService {

    @Override
    public Pagination<GroupControlLogVo> getGroupControlLogs(Long id, GroupControlLogQueryParam param) {
        Page<GroupControlLog> page = this.page(PagingUtils.convert(param), Wrappers.<GroupControlLog>lambdaQuery()
                .eq(GroupControlLog::getGroupControlId, id)
                .orderByDesc(BasicEntity::getCreateTime));
        return PagingUtils.convert(page, GroupControlLogVo.class);
    }

    @Override
    public List<HlInGroupStatVo> getGroupControlStat(GroupControlLogQueryParam param) {
        List<CommonStat> groupControlStat = this.baseMapper.getGroupControlStat(param);
        List<HlInGroupStatVo> finalDataList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(groupControlStat)) {
            groupControlStat.stream().collect(Collectors.groupingBy(CommonStat::getName)).forEach((groupName, list) -> {
                finalDataList.add(new HlInGroupStatVo(groupName, list));
            });
        }
        return finalDataList;
    }
}

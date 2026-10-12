package com.guhecloud.cube.srv.gen.securitymanagement.server.surgicaldemonstration.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.util.SecurityManagementCommonUtil;
import com.guhecloud.cube.srv.gen.securitymanagement.server.surgicaldemonstration.dao.SurgicalDemonstrationMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.surgicaldemonstration.entity.SurgicalDemonstration;
import com.guhecloud.cube.srv.gen.securitymanagement.server.surgicaldemonstration.param.SurgicalDemonstrationParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.surgicaldemonstration.service.SurgicalDemonstrationService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.surgicaldemonstration.vo.OverviewVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.surgicaldemonstration.vo.SurgicalDemonstrationVo;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SurgicalDemonstrationServiceImpl extends ServiceImpl<SurgicalDemonstrationMapper, SurgicalDemonstration> implements SurgicalDemonstrationService {
    private final SurgicalDemonstrationMapper surgicalDemonstrationMapper;

    @Override
    public List<OverviewVo> getOverview(SurgicalDemonstrationParam param) {
        List<OverviewVo> overview = surgicalDemonstrationMapper.getOverview(param);
        return overview;
    }

    @Override
    public Map<String, Object> teachingLiveBroadcastSessions(SurgicalDemonstrationParam param) {
        Map<String, Object> dateFormat = SecurityManagementCommonUtil.getDateFormat(param.getType(), param.getPeriod().getBegin(), param
                .getPeriod().getEnd());
        String timeFlag = (String) dateFormat.get("timeFlag");
        List<String> xtitle = (List<String>) dateFormat.get("xtitle");
        List<OverviewVo> overviewVos = surgicalDemonstrationMapper.teachingLiveBroadcastSessions(timeFlag, param.getPeriod()
                .getBegin(), param.getPeriod().getEnd());
        Map<String, Object> remap = new HashMap<>();
        remap.put("xtitle", xtitle);
        remap.put("overviewVos", overviewVos);
        return remap;
    }

    @Override
    public List<OverviewVo> mostPopularVideos(SurgicalDemonstrationParam param) {
        List<OverviewVo> overviewVos = surgicalDemonstrationMapper.mostPopularVideos(param);
        return overviewVos;
    }

    @Override
    public Pagination<SurgicalDemonstrationVo> SurgicalDemonstrationPage(SurgicalDemonstrationParam param) {
        LocalDateTime begin = null;
        LocalDateTime end = null;
        if (Objects.nonNull(param.getPeriod())) {
            begin = param.getPeriod().getBegin();
            end = param.getPeriod().getEnd();
        }
        Page<SurgicalDemonstration> page = this.page(PagingUtils.convert(param), Wrappers.<SurgicalDemonstration>lambdaQuery()
                .like(StringUtils.isNotEmpty(param.getKeyword()), SurgicalDemonstration::getName, param.getKeyword())
                .between(Objects.nonNull(param.getPeriod()), SurgicalDemonstration::getDateOperation, begin, end));
        return PagingUtils.convert(page, SurgicalDemonstrationVo.class);
    }
}

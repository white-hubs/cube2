package com.guhecloud.cube.srv.gen.business.info.sync.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.business.info.sync.server.controller.vo.OrganizationInfoVO;
import com.guhecloud.cube.srv.gen.business.info.sync.server.entity.Organization;
import com.guhecloud.cube.srv.gen.business.info.sync.server.mapper.SyncOrganizationMapper;
import com.guhecloud.cube.srv.gen.business.info.sync.server.service.SyncOrganizationService;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author 15561
 * @description 针对表【organization(组织机构信息表)】的数据库操作Service实现
 * @createDate 2024-12-17 16:11:33
 */
@Service
public class SyncOrganizationServiceImpl extends ServiceImpl<SyncOrganizationMapper, Organization>
        implements SyncOrganizationService {

    @Override
    public List<OrganizationInfoVO> getOrganizationInfoListAll(String keyword, Period period) {
        var begin = Objects.nonNull(period) ? period.getBegin() : null;
        var end = Objects.nonNull(period) ? period.getEnd() : null;
        List<Organization> organizationList = this.lambdaQuery()
                .like(StringUtils.isNotBlank(keyword), Organization::getName, keyword)
                .between(Objects.nonNull(begin) && Objects.nonNull(end), Organization::getUpdateTime, begin, end)
                .list();
        organizationList.forEach(t -> {
            if (StringUtils.isNotBlank(t.getOrgPathId())) {
                t.setOrgPathId(t.getOrgPathId().substring(0, t.getOrgPathId().length() - 1));
            }
        });
        return ObjectUtils.copying(organizationList, OrganizationInfoVO.class);
    }

}





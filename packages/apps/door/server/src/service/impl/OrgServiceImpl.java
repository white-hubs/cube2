package com.guhecloud.cube.biz.door.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.door.server.dao.OrgMapper;
import com.guhecloud.cube.biz.door.server.entity.Org;
import com.guhecloud.cube.biz.door.server.service.OrgService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service("door_org_service")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrgServiceImpl extends ServiceImpl<OrgMapper, Org> implements OrgService {
    @Override
    public void insertOrUpdate(Org org) {
        try {
            Org temp = this.baseMapper.selectOne(Wrappers.<Org>lambdaQuery()
                                                         .eq(Org::getOrgCode, org.getOrgCode()));
            if (temp != null) {
                temp.setOrgName(org.getOrgName());
                temp.setOrgPath(org.getOrgPath());
                temp.setParentId(org.getParentId());
                this.baseMapper.updateById(temp);
            } else {
                this.baseMapper.insert(org);
            }
        } catch (Exception e) {
            log.error("门禁系统组织同步异常：", e);
        }
    }
}

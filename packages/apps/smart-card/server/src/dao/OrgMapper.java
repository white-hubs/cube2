package com.guhecloud.cube.biz.smartcard.server.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.smartcard.server.entity.Org;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("smartcard_org_mapper")
public interface OrgMapper extends BaseMapper<Org> {
    @InterceptorIgnore(illegalSql = "true", tenantLine = "true")
    void insertOrUpdateBatch(List<Org> dataList);
}

package com.guhecloud.cube.biz.meattrade.server.pork.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkArrangeAudit;
import com.guhecloud.cube.biz.meattrade.server.pork.param.PorkArrangeAuditQueryParam;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.PorkArrangeAuditVO;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.PorkStatArrangeAuditVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("meattrade-porkArrangeAuditMapper")
public interface PorkArrangeAuditMapper extends BaseMapper<PorkArrangeAudit> {

    Page<PorkArrangeAudit> pagePorkArrangeAudit(Page<?> page, @Param("param") PorkArrangeAuditQueryParam queryParam);

    PorkStatArrangeAuditVO selectSupplierArrangeAudit(@Param("supplierId") Long supplierId, @Param("tenant") Integer tenant);

    Page<PorkArrangeAuditVO> selectPorkArrangeAuditPage(Page<?> page, @Param("param") PorkArrangeAuditQueryParam queryParam);
}

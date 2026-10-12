package com.guhecloud.cube.biz.meattrade.server.pork.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkArrangeAudit;
import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.PorkArrangeAuditEnum;
import com.guhecloud.cube.biz.meattrade.server.pork.param.PorkArrangeAuditModifyParam;
import com.guhecloud.cube.biz.meattrade.server.pork.param.PorkArrangeAuditQueryParam;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.PorkArrangeAuditVO;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.PorkStatArrangeAuditVO;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface PorkArrangeAuditService extends IService<PorkArrangeAudit> {

    Long addPorkArrangeAudit(Long id, PorkArrangeAuditModifyParam param);

    void auditPorkArrangeAudit(PorkArrangeAudit porkArrangeAudit, PorkArrangeAuditEnum status, String remark);

    void rescindPorkArrangeAudit(PorkArrangeAudit porkArrangeAudit);

    PorkArrangeAudit getById(Serializable id);

    PorkArrangeAudit getByPorkReport(Long porkReportId, Integer tenant);

    List<PorkArrangeAudit> listByPorkArranges(Collection<? extends Serializable> ids, Integer tenant);

    List<PorkArrangeAudit> listByPorkReportIds(Collection<? extends Serializable> porkReportIds, Integer tenant);

    List<PorkArrangeAudit> listBySpotChecks(Collection<? extends Serializable> spotCheckIds, Integer tenant);

    List<PorkArrangeAudit> listByPratiqueRgisters(Collection<? extends Serializable> pratiqueRegisterIds, Integer tenant);

    Page<PorkArrangeAuditVO> pagePorkArrangeAudit(PorkArrangeAuditQueryParam queryParam);

    PorkStatArrangeAuditVO getSupplierArrangeAudit(Long supplierId, Integer tenant);
}


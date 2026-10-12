package com.guhecloud.cube.biz.meattrade.server.pork.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkReport;
import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.PorkReportEnum;
import com.guhecloud.cube.biz.meattrade.server.pork.param.PorkReportModifyParam;
import com.guhecloud.cube.biz.meattrade.server.pork.param.PorkReportQueryParam;
import com.guhecloud.cube.biz.meattrade.server.pork.param.StatQueryParam;
import com.guhecloud.cube.biz.meattrade.server.pork.param.SupplierTallyInsertParam;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.PorkReportStatAuditVO;
import com.guhecloud.cube.biz.meattrade.server.pork.vo.PorkSupplyStatisticsVO;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @author lzy
 * @date 2021/12/4
 */
public interface PorkReportService extends IService<PorkReport> {

    PorkReport addPorkReport(PorkReportModifyParam param);

    void saveSupplierAndPorkArrange(SupplierTallyInsertParam param);

    void auditPorkReport(Long id, PorkReportEnum status, String auditRemark);

    void rescindPorkReport(Long id);

    PorkReport getById(Serializable id);

    PorkReport getByCode(Serializable code, Integer tenant);

    List<PorkReport> listByIds(Collection<? extends Serializable> ids);

    List<PorkReport> listByCodes(Collection<? extends Serializable> codes, Integer tenant);

    List<PorkReport> listByAbattoir(Serializable abattoirId, Integer tenant);

    List<PorkReport> listPorkReportBySupplier(Serializable supplierId, Integer tenant);

    List<PorkReport> listPorkReportBySuppliers(Collection<? extends Serializable> supplierIds, Integer tenant);

    Integer getSupplySupplierCount(StatQueryParam queryParam);

    PorkSupplyStatisticsVO getSupplyStatistics(StatQueryParam queryParam);

    List<PorkReport> getStatPorkReports(StatQueryParam queryParam);

    Page<PorkReport> pagePorkReport(PorkReportQueryParam queryParam);

    Page<PorkReport> pageMyPorkReport(PorkReportQueryParam queryParam);

    PorkReportStatAuditVO getSupplierReportAudit(Long supplierId, Integer tenant);
}

package com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.repo.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.entity.ReportCompany;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 报表公司
 */
@Mapper
public interface DetectionReportCompanyMapper extends BaseMapper<ReportCompany> {
    @InterceptorIgnore(tenantLine = "on")
    ReportCompany getNoTenantReportCompany(@Param("reportId") Long reportId);
}

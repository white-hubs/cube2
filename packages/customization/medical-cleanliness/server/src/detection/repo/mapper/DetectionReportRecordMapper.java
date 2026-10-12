package com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.repo.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.entity.ReportRecord;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.param.DetectionReportQueryParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.vo.DetectionReportVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DetectionReportRecordMapper extends BaseMapper<ReportRecord> {

    /**
     * admin端报告详情
     *
     * @param id 主键id
     *
     * @return 详情
     */
    @InterceptorIgnore(tenantLine = "on")
    ReportRecord getAdminReportDetail(@Param("id") Long id);

    /**
     * admin端检测报告记录分页列表
     *
     * @param param 分页条件
     *
     * @return 报告记录
     */
    @InterceptorIgnore(tenantLine = "on")
    Page<DetectionReportVO> getAdminReportPage(@Param("page") Page<ReportRecord> page, @Param("param") DetectionReportQueryParam param);

    @InterceptorIgnore(tenantLine = "on")
    String getRecordSourceList(Long recordId);
}
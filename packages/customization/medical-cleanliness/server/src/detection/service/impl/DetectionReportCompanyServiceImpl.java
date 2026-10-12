package com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.biz.ouaa.client.TenantClient;
import com.guhecloud.cube.biz.ouaa.client.dto.TenantDTO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.entity.ReportCompany;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.repo.mapper.DetectionReportCompanyMapper;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.service.IDetectionReportCompanyService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.vo.ReportCompanyVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DetectionReportCompanyServiceImpl extends ServiceImpl<DetectionReportCompanyMapper, ReportCompany> implements IDetectionReportCompanyService {
    private final TenantClient tenantClient;
    private final FileClient fileClient;

    /**
     * 获取报表公司信息
     *
     * @param reportId 报表id
     *
     * @return 报表公司
     */
    @Override
    public ReportCompanyVO getReportCompany(Long reportId) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        TenantDTO tenantDTO = tenantClient.getByCode(tenant);
        ReportCompany reportCompany = this.baseMapper.getNoTenantReportCompany(reportId);
        ReportCompanyVO vo = new ReportCompanyVO();
        Assert.isNotFound(reportCompany, "数据异常，请重新生成");
        ObjectUtils.copying(reportCompany, vo);
        vo.setTenantName(tenantDTO.getName());
        vo.setTenantId(tenant);
        vo.setFileVO(vo.getDetectionStampFileId() != null ? fileClient.getById(vo.getDetectionStampFileId()) : null);
        LocalDateTime calibrationExpireTime = vo.getCalibrationExpireTime();
        if (calibrationExpireTime != null) {
            vo.setCalibrationExpireDay((int) ChronoUnit.DAYS.between(ZonedDateTime.now().toLocalDateTime(), calibrationExpireTime));
        }
        return vo;
    }
}

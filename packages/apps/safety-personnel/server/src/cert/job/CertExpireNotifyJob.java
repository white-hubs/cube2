package com.guhecloud.cube.srv.gen.safety.personnel.server.cert.job;

import com.guhecloud.cube.base.message.sdk.api.constants.MessageSendStatusCodeConstants;
import com.guhecloud.cube.base.message.sdk.api.enumeration.MessageNotifyTargetEnum;
import com.guhecloud.cube.base.message.sdk.api.model.MessageNotifyTarget;
import com.guhecloud.cube.base.message.sdk.api.request.MessageSceneSendRequest;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.domain.CertType;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.domain.EmployeeCert;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.service.CertTypeService;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.service.EmployeeCertService;
import com.guhecloud.cube.srv.gen.safety.personnel.server.common.SafetyPersonnelConstants;
import com.guhecloud.cube.srv.gen.safety.personnel.server.common.enumeration.CertStatusEnum;
import com.guhecloud.cube.srv.gen.safety.personnel.server.common.facade.SafetyPersonnelMessageFacadeService;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 证书到期提醒任务。
 */
@Slf4j
@Component("safetyPersonnelCertExpireNotifyJob")
@RequiredArgsConstructor
public class CertExpireNotifyJob {

    private final EmployeeCertService employeeCertService;
    private final CertTypeService certTypeService;
    private final SafetyPersonnelMessageFacadeService messageFacade;

    /**
     * 扫描临期证书并发送一次到期提醒。
     *
     * @param param 调度参数，暂未使用。
     */
    @JobAnnotation(name = "safetyPersonnelCertExpireNotifyJob", cron = "0 0 8 * * ?", desc = "安全人员证书到期提醒")
    public void notifyExpiringCertificates(String param) {
        LocalDate today = LocalDate.now();
        List<EmployeeCert> certs = employeeCertService.lambdaQuery()
                .eq(EmployeeCert::getCertStatus, CertStatusEnum.VALID.getCode())
                .eq(EmployeeCert::getWarnNotified, Boolean.FALSE)
                .isNotNull(EmployeeCert::getValidEnd)
                .list();
        for (EmployeeCert cert : certs) {
            if (!shouldNotify(cert, today)) {
                continue;
            }
            CertType type = certTypeService.getById(cert.getTypeId());
            String certTypeName = type == null ? cert.getCertName() : type.getTypeName();
            var response = messageFacade.sceneSend(MessageSceneSendRequest.builder()
                    .msgSceneCode(messageFacade.certWarnSceneCode())
                    .applicationCode(SafetyPersonnelConstants.MESSAGE_APPLICATION_CODE)
                    .applicationName(SafetyPersonnelConstants.MESSAGE_APPLICATION_NAME)
                    .businessId(String.valueOf(cert.getId()))
                    .businessPrincipal(cert.getCertName())
                    .tmpParams(Map.of(
                            "certTypeName", Objects.toString(certTypeName, ""),
                            "certName", Objects.toString(cert.getCertName(), ""),
                            "validEnd", Objects.toString(cert.getValidEnd(), ""),
                            "remainingDays", String.valueOf(ChronoUnit.DAYS.between(today, cert.getValidEnd()))
                    ))
                    .messageNotifyTargets(List.of(MessageNotifyTarget.builder()
                            .notifyTargetType(MessageNotifyTargetEnum.USER)
                            .ids(Set.of(cert.getUserId()))
                            .build()))
                    .tenant(cert.getTenant())
                    .build());
            if (response != null && MessageSendStatusCodeConstants.SEND_SUCCESS.equals(response.getCode())) {
                cert.setWarnNotified(Boolean.TRUE);
                cert.setWarnNotifyTime(java.time.LocalDateTime.now());
                employeeCertService.updateById(cert);
            } else {
                log.warn("证书到期提醒发送失败，certId={}，response={}", cert.getId(), response);
            }
        }
    }

    private boolean shouldNotify(EmployeeCert cert, LocalDate today) {
        Integer warnDays = cert.getWarnDaysSnapshot();
        if (warnDays == null || cert.getValidEnd() == null) {
            return false;
        }
        LocalDate warnDate = cert.getValidEnd().minusDays(warnDays);
        return !today.isBefore(warnDate) && !today.isAfter(cert.getValidEnd());
    }
}

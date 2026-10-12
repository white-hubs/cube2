package com.guhecloud.cube.srv.gen.safety.personnel.server.cert.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.domain.CertCategory;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.domain.CertType;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.domain.EmployeeCert;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.service.CertCategoryService;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.mapper.EmployeeCertMapper;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.param.EmployeeCertModifyParam;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.param.EmployeeCertQueryParam;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.service.CertTypeService;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.service.EmployeeCertChangeLogService;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.service.EmployeeCertService;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.vo.EmployeeCertExportRowVO;
import com.guhecloud.cube.srv.gen.safety.personnel.server.cert.vo.EmployeeCertVO;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import jakarta.servlet.http.HttpServletResponse;
import com.guhecloud.cube.srv.gen.safety.personnel.server.common.SafetyPersonnelConstants;
import com.guhecloud.cube.srv.gen.safety.personnel.server.common.enumeration.CertExpireStatusEnum;
import com.guhecloud.cube.srv.gen.safety.personnel.server.common.enumeration.CertStatusEnum;
import com.guhecloud.cube.srv.gen.safety.personnel.server.common.facade.SafetyPersonnelFileFacadeService;
import com.guhecloud.cube.srv.gen.safety.personnel.server.common.service.SafetyPersonnelUserNameService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guhecloud.cube.srv.gen.safety.personnel.server.person.domain.Person;
import com.guhecloud.cube.srv.gen.safety.personnel.server.person.mapper.PersonMapper;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 人员证书服务实现。
 */
@Service("safetyPersonnelEmployeeCertService")
@RequiredArgsConstructor
public class EmployeeCertServiceImpl extends ServiceImpl<EmployeeCertMapper, EmployeeCert> implements EmployeeCertService {

    private final CertTypeService certTypeService;
    private final CertCategoryService certCategoryService;
    private final PersonMapper personMapper;
    private final EmployeeCertChangeLogService changeLogService;
    private final SafetyPersonnelUserNameService userNameService;
    private final SafetyPersonnelFileFacadeService fileFacadeService;

    @Override
    public Pagination<EmployeeCertVO> pageCerts(EmployeeCertQueryParam param) {
        Page<EmployeeCert> page = buildCertQuery(param)
                .orderByDesc(EmployeeCert::getCreateTime)
                .page(PagingUtils.convert(param));
        return PagingUtils.convert(page, toVOList(page.getRecords()));
    }

    @Override
    public void exportCerts(EmployeeCertQueryParam param, HttpServletResponse response) throws IOException {
        List<EmployeeCertExportRowVO> rows = toVOList(buildCertQuery(param)
                .orderByDesc(EmployeeCert::getCreateTime)
                .list()).stream()
                .map(this::toExportRow)
                .toList();
        writeCertExport(response, rows);
    }

    @Override
    public EmployeeCertVO detail(Long id) {
        return toVO(getRequired(id));
    }

    @Override
    @Transactional
    public EmployeeCertVO create(EmployeeCertModifyParam param) {
        CertType type = getActiveType(param.getTypeId());
        assertTypeBelongsCategory(type, param.getCategoryId());
        EmployeeCert cert = new EmployeeCert();
        cert.setId(IdWorker.getId());
        fill(cert, param, type, true);
        cert.setWarnNotified(Boolean.FALSE);
        save(cert);
        bindAttachment(cert);
        changeLogService.log(cert.getId(), "CREATE", JSON.toJSONString(cert));
        return toVO(cert);
    }

    @Override
    @Transactional
    public EmployeeCertVO updateCert(Long id, EmployeeCertModifyParam param) {
        EmployeeCert cert = getRequired(id);
        Long previousAttachmentFileId = cert.getAttachmentFileId();
        CertType type = getActiveType(param.getTypeId());
        assertTypeBelongsCategory(type, param.getCategoryId());
        boolean typeChanged = !Objects.equals(cert.getTypeId(), param.getTypeId());
        boolean shouldResetWarnNotify = shouldResetWarnNotify(cert, param, type, typeChanged);
        fill(cert, param, type, typeChanged);
        if (shouldResetWarnNotify) {
            cert.setWarnNotified(Boolean.FALSE);
            cert.setWarnNotifyTime(null);
        }
        updateById(cert);
        if (!Objects.equals(previousAttachmentFileId, cert.getAttachmentFileId())) {
            fileFacadeService.removeAssociation(cert.getId(), previousAttachmentFileId);
            bindAttachment(cert);
        }
        changeLogService.log(cert.getId(), "UPDATE", JSON.toJSONString(cert));
        return toVO(cert);
    }

    @Override
    @Transactional
    public void deleteCert(Long id) {
        EmployeeCert cert = getRequired(id);
        fileFacadeService.removeAssociation(cert.getId(), cert.getAttachmentFileId());
        DatabaseContextHolder.softDelete(this, cert);
        changeLogService.log(cert.getId(), "DELETE", JSON.toJSONString(cert));
    }

    @Override
    public List<EmployeeCertVO> listByUserId(Long userId) {
        return toVOList(lambdaQuery()
                .eq(EmployeeCert::getUserId, userId)
                .orderByDesc(EmployeeCert::getCreateTime)
                .list());
    }

    private void fill(EmployeeCert cert, EmployeeCertModifyParam param, CertType type, boolean snapshotType) {
        cert.setUserId(param.getUserId());
        cert.setUserName(resolveRequiredUserName(param));
        cert.setCategoryId(param.getCategoryId());
        cert.setTypeId(param.getTypeId());
        cert.setCertName(param.getCertName().trim());
        cert.setCertNo(param.getCertNo());
        cert.setIssueOrg(param.getIssueOrg());
        cert.setIssueDate(param.getIssueDate());
        cert.setValidStart(param.getValidStart());
        cert.setValidEnd(Objects.requireNonNullElse(param.getValidEnd(), computeValidEnd(param.getValidStart(), type)));
        cert.setReviewStatus(param.getReviewStatus());
        cert.setAttachmentFileId(resolveAttachmentFileId(param.getAttachmentFileId()));
        cert.setCertStatus(CertStatusEnum.normalizeOrDefault(param.getCertStatus(), CertStatusEnum.VALID));
        if (snapshotType) {
            cert.setValidPeriodSnapshot(type.getValidPeriod());
            cert.setValidPeriodUnitSnapshot(type.getValidPeriodUnit());
            cert.setWarnDaysSnapshot(type.getWarnDays());
            cert.setNeedReview(Boolean.TRUE.equals(type.getNeedReview()));
            cert.setReviewPeriodSnapshot(type.getReviewPeriod());
            cert.setReviewPeriodUnitSnapshot(type.getReviewPeriodUnit());
        }
    }

    private Long resolveAttachmentFileId(Long attachmentFileId) {
        FileVO file = fileFacadeService.requireFile(attachmentFileId);
        return file == null ? null : file.getId();
    }

    private void bindAttachment(EmployeeCert cert) {
        fileFacadeService.induceSingle(
                SafetyPersonnelConstants.SCENE_CERT_ATTACHMENT,
                cert.getId(),
                cert.getAttachmentFileId());
    }

    private List<EmployeeCertVO> toVOList(List<EmployeeCert> certs) {
        if (certs.isEmpty()) {
            return List.of();
        }
        Map<Long, FileVO> fileMap = fileFacadeService.mapFiles(certs.stream()
                .map(EmployeeCert::getAttachmentFileId)
                .filter(Objects::nonNull)
                .toList());
        Map<Long, Person> personMap = loadPersonMap(certs);
        Map<Long, CertType> typeMap = loadTypeMap(certs);
        Map<Long, CertCategory> categoryMap = loadCategoryMap(typeMap.values());
        return certs.stream()
                .map(cert -> buildVO(
                        cert,
                        fileMap.get(cert.getAttachmentFileId()),
                        personMap.get(cert.getUserId()),
                        typeMap.get(cert.getTypeId()),
                        categoryMap.get(cert.getCategoryId())))
                .toList();
    }

    private EmployeeCertVO toVO(EmployeeCert cert) {
        CertType type = certTypeService.getById(cert.getTypeId());
        CertCategory category = type == null ? null : certCategoryService.getById(cert.getCategoryId());
        Person person = personMapper.selectOne(
                Wrappers.<Person>lambdaQuery().eq(Person::getUserId, cert.getUserId()).last("limit 1"));
        return buildVO(cert, fileFacadeService.getFile(cert.getAttachmentFileId()), person, type, category);
    }

    private EmployeeCertVO buildVO(EmployeeCert cert, FileVO attachmentFile, Person person, CertType type, CertCategory category) {
        EmployeeCertVO vo = EmployeeCertVO.of(cert, attachmentFile);
        fillExpire(vo, cert);
        if (person != null) {
            vo.setPhone(person.getPhone());
            vo.setDeptName(person.getDeptName());
            vo.setPostName(person.getPostName());
        }
        if (type != null) {
            vo.setTypeName(type.getTypeName());
        }
        if (category != null) {
            vo.setCategoryName(category.getName());
        }
        return vo;
    }

    private LambdaQueryChainWrapper<EmployeeCert> buildCertQuery(EmployeeCertQueryParam param) {
        return lambdaQuery()
                .eq(Objects.nonNull(param.getUserId()), EmployeeCert::getUserId, param.getUserId())
                .eq(Objects.nonNull(param.getCategoryId()), EmployeeCert::getCategoryId, param.getCategoryId())
                .eq(Objects.nonNull(param.getTypeId()), EmployeeCert::getTypeId, param.getTypeId())
                .and(StringUtils.isNotBlank(param.getKeyword()), wrapper -> wrapper
                        .like(EmployeeCert::getUserName, param.getKeyword())
                        .or()
                        .like(EmployeeCert::getCertName, param.getKeyword())
                        .or()
                        .like(EmployeeCert::getCertNo, param.getKeyword()))
                .eq(StringUtils.isNotBlank(param.getCertStatus()), EmployeeCert::getCertStatus, param.getCertStatus())
                .eq(StringUtils.isNotBlank(param.getReviewStatus()), EmployeeCert::getReviewStatus, param.getReviewStatus())
                .and(StringUtils.isNotBlank(param.getExpireStatus()), wrapper -> applyExpireStatusFilter(wrapper, param.getExpireStatus()));
    }

    private EmployeeCertExportRowVO toExportRow(EmployeeCertVO vo) {
        EmployeeCertExportRowVO row = new EmployeeCertExportRowVO();
        row.setTypeName(vo.getTypeName());
        row.setUserName(vo.getUserName());
        row.setPhone(vo.getPhone());
        row.setCertName(vo.getCertName());
        row.setReviewStatus(vo.getReviewStatus());
        row.setExpireStatusLabel(CertExpireStatusEnum.getDescription(vo.getExpireStatus()));
        row.setRemainingDaysLabel(formatRemainingDays(vo));
        return row;
    }

    private String formatRemainingDays(EmployeeCertVO vo) {
        if (vo.getValidEnd() == null || vo.getRemainingDays() == null) {
            return null;
        }
        long days = vo.getRemainingDays();
        if (days < 0) {
            return "已过期" + Math.abs(days) + "天";
        }
        return days + "天";
    }

    private void writeCertExport(HttpServletResponse response, List<EmployeeCertExportRowVO> rows) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("证书管理", StandardCharsets.UTF_8).replace("+", "%20");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ExcelTypeEnum.XLSX.getValue());
        EasyExcel.write(response.getOutputStream(), EmployeeCertExportRowVO.class)
                .sheet("证书管理")
                .doWrite(rows);
    }

    private void fillExpire(EmployeeCertVO vo, EmployeeCert cert) {
        LocalDate today = LocalDate.now();
        if (cert.getValidEnd() == null) {
            vo.setExpireStatus(CertExpireStatusEnum.NORMAL.getCode());
            return;
        }
        long remainingDays = ChronoUnit.DAYS.between(today, cert.getValidEnd());
        vo.setRemainingDays(remainingDays);
        if (remainingDays < 0) {
            vo.setExpireStatus(CertExpireStatusEnum.EXPIRED.getCode());
        } else if (cert.getWarnDaysSnapshot() != null && remainingDays <= cert.getWarnDaysSnapshot()) {
            vo.setExpireStatus(CertExpireStatusEnum.EXPIRING.getCode());
        } else {
            vo.setExpireStatus(CertExpireStatusEnum.NORMAL.getCode());
        }
    }

    private void applyExpireStatusFilter(LambdaQueryWrapper<EmployeeCert> wrapper, String expireStatus) {
        String normalized = CertExpireStatusEnum.normalize(expireStatus);
        LocalDate today = LocalDate.now();
        if (CertExpireStatusEnum.EXPIRED.getCode().equals(normalized)) {
            wrapper.isNotNull(EmployeeCert::getValidEnd).lt(EmployeeCert::getValidEnd, today);
            return;
        }
        if (CertExpireStatusEnum.EXPIRING.getCode().equals(normalized)) {
            wrapper.isNotNull(EmployeeCert::getValidEnd)
                    .ge(EmployeeCert::getValidEnd, today)
                    .apply("DATEDIFF(valid_end, CURDATE()) <= IFNULL(warn_days_snapshot, 0)");
            return;
        }
        if (CertExpireStatusEnum.NORMAL.getCode().equals(normalized)) {
            wrapper.and(normal -> normal.isNull(EmployeeCert::getValidEnd)
                    .or(expired -> expired.ge(EmployeeCert::getValidEnd, today)
                            .apply("(warn_days_snapshot IS NULL OR DATEDIFF(valid_end, CURDATE()) > warn_days_snapshot)")));
        }
    }

    private Map<Long, Person> loadPersonMap(List<EmployeeCert> certs) {
        List<Long> userIds = certs.stream().map(EmployeeCert::getUserId).filter(Objects::nonNull).distinct().toList();
        if (userIds.isEmpty()) {
            return Map.of();
        }
        return personMapper.selectList(Wrappers.<Person>lambdaQuery().in(Person::getUserId, userIds)).stream()
                .collect(Collectors.toMap(Person::getUserId, Function.identity(), (left, right) -> left));
    }

    private Map<Long, CertType> loadTypeMap(List<EmployeeCert> certs) {
        List<Long> typeIds = certs.stream().map(EmployeeCert::getTypeId).filter(Objects::nonNull).distinct().toList();
        if (typeIds.isEmpty()) {
            return Map.of();
        }
        return certTypeService.listByIds(typeIds).stream()
                .collect(Collectors.toMap(CertType::getId, Function.identity(), (left, right) -> left));
    }

    private Map<Long, CertCategory> loadCategoryMap(java.util.Collection<CertType> types) {
        List<Long> categoryIds = types.stream().map(CertType::getCategoryId).filter(Objects::nonNull).distinct().toList();
        if (categoryIds.isEmpty()) {
            return Map.of();
        }
        return certCategoryService.listByIds(categoryIds).stream()
                .collect(Collectors.toMap(CertCategory::getId, Function.identity(), (left, right) -> left));
    }

    private String resolveRequiredUserName(EmployeeCertModifyParam param) {
        String userName = userNameService.resolveUserName(param.getUserId(), param.getUserName());
        Assert.isParameterError(StringUtils.isBlank(userName), "用户姓名不能为空");
        return userName;
    }

    private LocalDate computeValidEnd(LocalDate validStart, CertType type) {
        if (validStart == null || type.getValidPeriod() == null || StringUtils.isBlank(type.getValidPeriodUnit())) {
            return null;
        }
        return switch (type.getValidPeriodUnit()) {
            case "DAY" -> validStart.plusDays(type.getValidPeriod());
            case "MONTH" -> validStart.plusMonths(type.getValidPeriod());
            case "YEAR" -> validStart.plusYears(type.getValidPeriod());
            default -> validStart.plusDays(type.getValidPeriod());
        };
    }

    private CertType getActiveType(Long typeId) {
        CertType type = certTypeService.getById(typeId);
        Assert.isNotFound(type, "证书类型不存在");
        return type;
    }

    private void assertTypeBelongsCategory(CertType type, Long categoryId) {
        Assert.isParameterError(!Objects.equals(type.getCategoryId(), categoryId), "证书类型不属于所选分类");
    }

    private boolean shouldResetWarnNotify(EmployeeCert cert, EmployeeCertModifyParam param, CertType type, boolean typeChanged) {
        LocalDate validEnd = Objects.requireNonNullElse(param.getValidEnd(), computeValidEnd(param.getValidStart(), type));
        return typeChanged
                || !Objects.equals(cert.getValidStart(), param.getValidStart())
                || !Objects.equals(cert.getValidEnd(), validEnd);
    }

    private EmployeeCert getRequired(Long id) {
        EmployeeCert cert = getById(id);
        Assert.isNotFound(cert, "人员证书不存在");
        return cert;
    }
}

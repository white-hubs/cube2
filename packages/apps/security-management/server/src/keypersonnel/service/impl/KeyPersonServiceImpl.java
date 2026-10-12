package com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.dao.KeyPersonMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.entity.KeyPerson;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.enumeration.KeyPersonRiskLevelEnum;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.enumeration.KeyPersonStatusEnum;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.facade.FileFacadeService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.facade.KeyPersonSensitiveConfigService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.facade.MdataAreaFacadeService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.facade.OuaaUserFacadeService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.param.KeyPersonPageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.param.KeyPersonReportPageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.param.KeyPersonSaveParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.service.KeyPersonReportService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.service.KeyPersonService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.vo.AreaOptionVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.vo.KeyPersonAreaCountVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.vo.KeyPersonDetailVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.vo.KeyPersonOptionVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.vo.KeyPersonPageVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.vo.KeyPersonRiskCountVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.vo.KeyPersonStatisticsVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.vo.SupervisorOptionVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.BizCodeUtils;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DesensitizeUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.MaskedValueUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.entity.BasicEntity;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class KeyPersonServiceImpl extends ServiceImpl<KeyPersonMapper, KeyPerson> implements KeyPersonService {
    private static final int STATISTICS_TOP_LIMIT = 5;

    private final MdataAreaFacadeService mdataAreaFacadeService;
    private final OuaaUserFacadeService ouaaUserFacadeService;
    private final FileFacadeService fileFacadeService;
    private final KeyPersonSensitiveConfigService sensitiveConfigService;
    private final KeyPersonReportService keyPersonReportService;

    @Override
    public Pagination<KeyPersonPageVO> getPage(KeyPersonPageParam param) {
        KeyPersonPageParam queryParam = param == null ? new KeyPersonPageParam() : param;
        String status = KeyPersonStatusEnum.normalizeOrDefault(queryParam.getStatus(), KeyPersonStatusEnum.ACTIVE);
        String riskLevel = StringUtils.isBlank(queryParam.getRiskLevel()) ? null : KeyPersonRiskLevelEnum.normalize(queryParam.getRiskLevel());
        String keyword = trimToNull(queryParam.getKeyword());
        String areaSequence = trimToNull(queryParam.getAreaSequence());

        Page<KeyPerson> page = this.page(PagingUtils.convert(queryParam), Wrappers.<KeyPerson>lambdaQuery()
                .eq(KeyPerson::getStatus, status)
                .eq(StringUtils.isNotBlank(riskLevel), KeyPerson::getRiskLevel, riskLevel)
                .likeRight(StringUtils.isNotBlank(areaSequence), KeyPerson::getAreaSequence, areaSequence)
                .and(StringUtils.isNotBlank(keyword), wrapper -> wrapper
                        .like(KeyPerson::getName, keyword)
                        .or()
                        .like(KeyPerson::getPhone, keyword))
                .orderByDesc(BasicEntity::getCreateTime));
        KeyPersonSensitiveConfigService.Config config = sensitiveConfigService.getConfig();
        Page<KeyPersonPageVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        voPage.setRecords(page.getRecords().stream().map(person -> toPageVO(person, config)).toList());
        return PagingUtils.convert(voPage);
    }

    @Override
    public List<KeyPersonOptionVO> listOptions(String keyword) {
        String trimKeyword = trimToNull(keyword);
        List<KeyPerson> persons = this.list(Wrappers.<KeyPerson>lambdaQuery()
                .eq(KeyPerson::getStatus, KeyPersonStatusEnum.ACTIVE.name())
                .and(StringUtils.isNotBlank(trimKeyword), wrapper -> wrapper
                        .like(KeyPerson::getName, trimKeyword)
                        .or()
                        .like(KeyPerson::getPhone, trimKeyword))
                .orderByDesc(BasicEntity::getCreateTime));
        return ObjectUtils.copying(persons, KeyPersonOptionVO.class);
    }

    @Override
    public KeyPersonDetailVO getDetail(Long id) {
        KeyPerson person = this.getById(id);
        Assert.isNotFound(person, "重点人员不存在");
        KeyPersonSensitiveConfigService.Config config = sensitiveConfigService.getConfig();
        KeyPersonDetailVO vo = ObjectUtils.copying(toPageVO(person, config), KeyPersonDetailVO.class);
        vo.setIdCard(config.idCardEnabled() ? DesensitizeUtils.desensitizeIdCard(person.getIdCard()) : person.getIdCard());
        vo.setPhotoFileId(person.getPhotoFileId());
        vo.setPhotoFile(fileFacadeService.getFile(person.getPhotoFileId()));
        List<Long> personAttachmentFileIds = parseFileIds(person.getPersonAttachmentFileIds());
        vo.setPersonAttachmentFileIds(personAttachmentFileIds);
        vo.setPersonAttachmentFiles(fileFacadeService.getFiles(personAttachmentFileIds));
        vo.setReports(keyPersonReportService.getReportPage(id, new KeyPersonReportPageParam()).getRecords());
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long create(KeyPersonSaveParam param) {
        assertRequiredForSave(param, true);
        AreaDTO area = mdataAreaFacadeService.getBySequence(param.getAreaSequence());
        UserDTO supervisor = ouaaUserFacadeService.getTenantUser(param.getSupervisorUserId());
        Long photoFileId = fileFacadeService.validateSingle(param.getPhotoFileId());
        List<Long> attachmentFileIds = fileFacadeService.normalizeAndValidate(param.getPersonAttachmentFileIds());

        KeyPerson person = new KeyPerson();
        person.setPersonCode(generatePersonCode());
        applyPlainFields(person, param, area, supervisor);
        person.setPhone(param.getPhone().trim());
        person.setIdCard(trimToNull(param.getIdCard()));
        person.setStatus(KeyPersonStatusEnum.ACTIVE.name());
        person.setPhotoFileId(photoFileId);
        person.setPersonAttachmentFileIds(JsonUtils.serialize(attachmentFileIds));
        this.save(person);
        fileFacadeService.induceSingle(FileFacadeService.SCENE_KEY_PERSON_PHOTO, person.getId(), photoFileId);
        fileFacadeService.induce(FileFacadeService.SCENE_KEY_PERSON_ATTACHMENT, person.getId(), attachmentFileIds);
        return person.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, KeyPersonSaveParam param) {
        KeyPerson person = this.getById(id);
        Assert.isNotFound(person, "重点人员不存在");
        assertRequiredForSave(param, false);

        AreaDTO area = mdataAreaFacadeService.getBySequence(param.getAreaSequence());
        UserDTO supervisor = ouaaUserFacadeService.getTenantUser(param.getSupervisorUserId());
        applyPlainFields(person, param, area, supervisor);
        applySensitiveEdit(person, param);

        if (Boolean.TRUE.equals(param.getClearPhoto())) {
            person.setPhotoFileId(null);
        } else if (param.getPhotoFileId() != null) {
            person.setPhotoFileId(fileFacadeService.validateSingle(param.getPhotoFileId()));
        }
        if (param.getPersonAttachmentFileIds() != null) {
            List<Long> attachmentFileIds = fileFacadeService.normalizeAndValidate(param.getPersonAttachmentFileIds());
            person.setPersonAttachmentFileIds(JsonUtils.serialize(attachmentFileIds));
        }

        this.updateById(person);
        fileFacadeService.induceSingle(FileFacadeService.SCENE_KEY_PERSON_PHOTO, person.getId(), person.getPhotoFileId());
        fileFacadeService.induce(FileFacadeService.SCENE_KEY_PERSON_ATTACHMENT, person.getId(), parseFileIds(person.getPersonAttachmentFileIds()));
    }

    @Override
    public void delete(Long id) {
        KeyPerson person = this.getById(id);
        Assert.isNotFound(person, "重点人员不存在");
        DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    public void withdraw(Long id) {
        changeStatus(id, KeyPersonStatusEnum.INACTIVE);
    }

    @Override
    public void activate(Long id) {
        changeStatus(id, KeyPersonStatusEnum.ACTIVE);
    }

    @Override
    public KeyPersonStatisticsVO getStatistics() {
        List<KeyPersonRiskCountVO> riskCounts = this.baseMapper.countByRiskLevel(KeyPersonStatusEnum.ACTIVE.name());
        Map<String, Long> riskCountMap = riskCounts == null ? Collections.emptyMap() :
                riskCounts.stream().collect(Collectors.toMap(KeyPersonRiskCountVO::getRiskLevel, KeyPersonRiskCountVO::getCount));
        KeyPersonStatisticsVO vo = new KeyPersonStatisticsVO();
        long high = riskCountMap.getOrDefault(KeyPersonRiskLevelEnum.HIGH.name(), 0L);
        long medium = riskCountMap.getOrDefault(KeyPersonRiskLevelEnum.MEDIUM.name(), 0L);
        long low = riskCountMap.getOrDefault(KeyPersonRiskLevelEnum.LOW.name(), 0L);
        vo.setHighRiskCount(high);
        vo.setMediumRiskCount(medium);
        vo.setLowRiskCount(low);
        vo.setTotalCount(high + medium + low);
        List<KeyPersonAreaCountVO> areaSupervisorCounts = this.baseMapper.countAreaSupervisors(KeyPersonStatusEnum.ACTIVE.name(), STATISTICS_TOP_LIMIT);
        List<KeyPersonAreaCountVO> areaHighRiskCounts = this.baseMapper.countAreaHighRisk(KeyPersonStatusEnum.ACTIVE.name(), STATISTICS_TOP_LIMIT);
        vo.setAreaSupervisorCounts(areaSupervisorCounts == null ? Collections.emptyList() : areaSupervisorCounts);
        vo.setAreaHighRiskCounts(areaHighRiskCounts == null ? Collections.emptyList() : areaHighRiskCounts);
        return vo;
    }

    @Override
    public List<AreaOptionVO> listAreaOptions(String keyword) {
        return mdataAreaFacadeService.listOptions(keyword);
    }

    @Override
    public List<SupervisorOptionVO> listSupervisorOptions(String keyword) {
        return ouaaUserFacadeService.listOptions(keyword);
    }

    private void changeStatus(Long id, KeyPersonStatusEnum status) {
        KeyPerson person = this.getById(id);
        Assert.isNotFound(person, "重点人员不存在");
        person.setStatus(status.name());
        this.updateById(person);
    }

    private void assertRequiredForSave(KeyPersonSaveParam param, boolean creating) {
        Assert.isParameterError(param == null, "参数不能为空");
        Assert.isParameterError(StringUtils.isBlank(param.getName()), "姓名不能为空");
        Assert.isParameterError(creating && StringUtils.isBlank(param.getPhone()), "手机号不能为空");
        Assert.isParameterError(StringUtils.isBlank(param.getAreaSequence()), "区域不能为空");
        Assert.isParameterError(StringUtils.isBlank(param.getRiskLevel()), "风险等级不能为空");
        Assert.isParameterError(param.getSupervisorUserId() == null, "监管人员不能为空");
        Assert.isParameterError(StringUtils.isBlank(param.getSupervisionReason()), "监管原因不能为空");
    }

    private void applyPlainFields(KeyPerson person, KeyPersonSaveParam param, AreaDTO area, UserDTO supervisor) {
        person.setName(param.getName().trim());
        person.setAreaId(area.getId());
        person.setAreaName(area.getName());
        person.setAreaSequence(area.getSequence());
        person.setRiskLevel(KeyPersonRiskLevelEnum.normalize(param.getRiskLevel()));
        person.setSupervisorUserId(supervisor.getId());
        person.setSupervisorName(supervisor.getName());
        person.setSupervisionReason(param.getSupervisionReason().trim());
    }

    private void applySensitiveEdit(KeyPerson person, KeyPersonSaveParam param) {
        if (param.getPhone() != null) {
            MaskedValueUtils.UpdateDecision decision = decideMaskedValue(
                    DesensitizeUtils.desensitizeMobile(person.getPhone()), param.getPhone(), false, "手机号脱敏值不能作为新值提交");
            if (decision == MaskedValueUtils.UpdateDecision.UPDATE) {
                Assert.isParameterError(StringUtils.isBlank(param.getPhone()), "手机号不能为空");
                person.setPhone(param.getPhone().trim());
            }
        }
        if (param.getIdCard() != null) {
            MaskedValueUtils.UpdateDecision decision = decideMaskedValue(
                    DesensitizeUtils.desensitizeIdCard(person.getIdCard()), param.getIdCard(), true, "身份证号脱敏值不能作为新值提交");
            if (decision == MaskedValueUtils.UpdateDecision.CLEAR) {
                person.setIdCard(null);
            } else if (decision == MaskedValueUtils.UpdateDecision.UPDATE) {
                person.setIdCard(param.getIdCard().trim());
            }
        }
    }

    private MaskedValueUtils.UpdateDecision decideMaskedValue(String currentMasked, String incoming, boolean blankMeansClear, String errorMessage) {
        try {
            return MaskedValueUtils.decide(currentMasked, incoming, blankMeansClear);
        } catch (IllegalArgumentException ex) {
            Assert.isParameterError(true, errorMessage);
            return MaskedValueUtils.UpdateDecision.UNCHANGED;
        }
    }

    private String generatePersonCode() {
        String prefix = BizCodeUtils.dailyPrefix("KP-", LocalDate.now(), "-");
        return BizCodeUtils.nextSequenceCode(prefix, this.baseMapper.selectMaxPersonCodeByPrefix(prefix), 3);
    }

    private KeyPersonPageVO toPageVO(KeyPerson person, KeyPersonSensitiveConfigService.Config config) {
        KeyPersonPageVO vo = ObjectUtils.copying(person, KeyPersonPageVO.class);
        vo.setPhone(config.phoneEnabled() ? DesensitizeUtils.desensitizeMobile(person.getPhone()) : person.getPhone());
        vo.setRiskLevelName(KeyPersonRiskLevelEnum.getDescription(person.getRiskLevel()));
        vo.setStatusName(KeyPersonStatusEnum.getDescription(person.getStatus()));
        return vo;
    }

    private List<Long> parseFileIds(String json) {
        if (StringUtils.isBlank(json)) {
            return Collections.emptyList();
        }
        List<Long> result = JsonUtils.deserialize(json, new TypeReference<List<Long>>() {});
        return result == null ? Collections.emptyList() : result;
    }

    private String trimToNull(String value) {
        return StringUtils.isBlank(value) ? null : value.trim();
    }
}

package com.guhecloud.cube.srv.gen.personnel.management.server.common.commandgateway.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.dictionary.client.DataDictionaryClient;
import com.guhecloud.cube.base.dictionary.dto.DataDictionaryDTO;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryaramByItemAndCodeParam;
import com.guhecloud.cube.base.flow.client.FlowClient;
import com.guhecloud.cube.base.flow.client.FlowResult;
import com.guhecloud.cube.base.flow.client.param.FormDataParam;
import com.guhecloud.cube.srv.gen.personnel.management.server.commandgateway.StaffCommandService;
import com.guhecloud.cube.srv.gen.personnel.management.server.commandgateway.command.SubmitStaffCommand;
import com.guhecloud.cube.srv.gen.personnel.management.server.common.approval.context.PersonnelApprovalContext;
import com.guhecloud.cube.srv.gen.personnel.management.server.common.approval.po.InductionPO;
import com.guhecloud.cube.srv.gen.personnel.management.server.common.approval.po.LeaveOfficePO;
import com.guhecloud.cube.srv.gen.personnel.management.server.common.approval.po.RegularWorkerPO;
import com.guhecloud.cube.srv.gen.personnel.management.server.common.approval.po.RotationPostPO;
import com.guhecloud.cube.srv.gen.personnel.management.server.common.approval.service.PersonnelApprovalService;
import com.guhecloud.cube.srv.gen.personnel.management.server.common.approval.vo.PersonnelLeaveConfigVO;
import com.guhecloud.cube.srv.gen.personnel.management.server.common.commandgateway.FlowFormDataCommandService;
import com.guhecloud.cube.srv.gen.personnel.management.server.common.commandgateway.command.SubmitFlowFormDataCommand;
import com.guhecloud.cube.srv.gen.personnel.management.server.common.dao.FlowFormDataDetailsMapper;
import com.guhecloud.cube.srv.gen.personnel.management.server.common.dao.FlowFormDataMapper;
import com.guhecloud.cube.srv.gen.personnel.management.server.common.enumeration.PersonnelApprovalBizEnum;
import com.guhecloud.cube.srv.gen.personnel.management.server.common.po.FlowFormDataDetailsPO;
import com.guhecloud.cube.srv.gen.personnel.management.server.common.po.FlowFormDataPO;
import com.guhecloud.cube.srv.gen.personnel.management.server.enumeration.GenderEnum;
import com.guhecloud.cube.srv.gen.personnel.management.server.enumeration.StaffStatusEnum;
import com.guhecloud.cube.srv.gen.personnel.management.server.event.message.UpdateStaffMessage;
import com.guhecloud.cube.srv.gen.personnel.management.server.interfaces.vo.StaffDetailVO;
import com.guhecloud.cube.srv.gen.personnel.management.server.po.StaffPO;
import com.guhecloud.cube.srv.gen.personnel.management.server.querygateway.StaffQueryService;
import com.guhecloud.cube.srv.gen.personnel.management.server.tool.PersonnelAbility;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import instance.command.SubmitInitFormCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author bq
 * @date 2023/02/22
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FlowFormDataCommandServiceImpl extends ServiceImpl<FlowFormDataMapper, FlowFormDataPO> implements FlowFormDataCommandService {
    public static final String APP_BUSINESS_CONFIG = "APP_BUSINESS_CONFIG";
    public static final String PERSONNEL_LEAVE_CONFIG = "personnel::leave_config";

    private final StaffQueryService staffQueryService;
    private final StaffCommandService staffCommandService;
    private final FlowFormDataMapper flowFormDataMapper;
    private final FlowFormDataDetailsMapper flowFormDataDetailsMapper;
    private final DataDictionaryClient dataDictionaryClient;
    private final FlowClient flowClient;
    private final PersonnelApprovalContext personnelApprovalContext;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(SubmitFlowFormDataCommand command) {
        SessionContextHolder.requestTenant(command.getTenant());
        FlowFormDataPO formDataPO = ObjectUtils.copying(command, FlowFormDataPO.class);
        // 查询员工信息
        StaffDetailVO staffDetailVO = staffQueryService.get(formDataPO.getApplicantId());
        log.info("人事审批通过查询员工信息，" + staffDetailVO);
        if (Objects.nonNull(staffDetailVO)) {
            formDataPO.setId(IdWorker.getId());
            formDataPO.setApplicantName(staffDetailVO.getName());
            formDataPO.setApplicantOrganizationId(staffDetailVO.getOrganizationId());
            formDataPO.setApplicantOrganizationName(staffDetailVO.getOrganizationName());
            formDataPO.setApplicantPostId(staffDetailVO.getAppointmentId());
            formDataPO.setApplicantPostName(staffDetailVO.getAppointmentName());
        }
        log.info("人事审批通过后，得到FlowFormData，" + formDataPO);
        try {
            // 更新业务操作不影响监听数据的保存
            formDataPO.setUpdateBusiness(this.updateBusinessData(formDataPO));
            this.save(formDataPO);
        } catch (Exception e) {
            log.error("人事流程监听更新业务数据失败，数据：" + formDataPO, e);
        } finally {
            SessionContextHolder.clearSessionContext();
        }
    }

    @Override
    public void jobUpdateRegularWorkerData() {
        List<FlowFormDataPO> formDataPOS = this.queryNoUpdateFlowFormData(PersonnelApprovalBizEnum.PERSONNEL_REGULAR_WORKER);
        formDataPOS.forEach(po -> {
            this.lambdaUpdate().eq(FlowFormDataPO::getId, po.getId())
                    .set(FlowFormDataPO::getUpdateBusiness, this.handleRegularWorker(po.getJsonForm())).update();
        });
    }

    @Override
    public void jobUpdateRotationPostData() {
        List<FlowFormDataPO> formDataPOS = this.queryNoUpdateFlowFormData(PersonnelApprovalBizEnum.PERSONNEL_ROTATION_POST);
        List<RotationPostPO> rotationList = new ArrayList<>();
        for (FlowFormDataPO po : formDataPOS) {
            try {
                JSONArray array = JSON.parseArray(po.getJsonForm());
                for (Object ex : array) {
                    RotationPostPO rotationPostPO = JSONObject.parseObject(ex.toString(), RotationPostPO.class);
                    rotationPostPO.setFlowFormDataId(po.getId());
                    rotationPostPO.setTenant(po.getTenant());
                    rotationPostPO.setFlowFormDataId(po.getId());
                    rotationList.add(rotationPostPO);
                }
            } catch (Exception e) {
                log.error("轮岗申请通过后，集合解析失败：", e);
                log.info("轮岗申请通过后，解析对象");
                RotationPostPO rotationPostPO = JSONObject.parseObject(po.getJsonForm(), RotationPostPO.class);
                rotationPostPO.setFlowFormDataId(po.getId());
                rotationPostPO.setTenant(po.getTenant());
                rotationPostPO.setFlowFormDataId(po.getId());
                rotationList.add(rotationPostPO);
            }
            rotationList.stream().forEach(RotationPostPO::parseStartAndEnd);
            rotationList.stream().sorted(Comparator.comparing(RotationPostPO::getStartDate)).forEach(p -> {
                boolean b = this.handleRotationPost(p);
                if (!b) {
                    log.error("轮岗结果：{}", p);
                }
                this.lambdaUpdate().eq(FlowFormDataPO::getId, p.getFlowFormDataId()).set(FlowFormDataPO::getUpdateBusiness, b).update();
            });
        }
    }

    @Override
    public void jobUpdateLeaveOfficeData() {
        List<FlowFormDataPO> formDataPOS = this.queryNoUpdateFlowFormData(PersonnelApprovalBizEnum.PERSONNEL_LEAVE_OFFICE);
        formDataPOS.forEach(po -> {
            LeaveOfficePO leaveOfficePO = JSONObject.parseObject(po.getJsonForm(), LeaveOfficePO.class);
            leaveOfficePO.setTenant(po.getTenant());
            boolean leaveOffice = this.handleLeaveOffice(leaveOfficePO, PersonnelApprovalBizEnum.PERSONNEL_LEAVE_OFFICE.name());
            this.lambdaUpdate().eq(FlowFormDataPO::getId, po.getId()).set(FlowFormDataPO::getUpdateBusiness, leaveOffice).update();
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String submitApproval(SubmitInitFormCommand command) {
        PersonnelApprovalBizEnum approvalBizEnum = PersonnelApprovalBizEnum.getApprovalBizEnum(command.getBusinessType());
        //解析表单信息
        FormDataParam data = new FormDataParam(command.getFormDefinitionId(), command.getData());
        String fromString;
        Object personnelFlowEntity = null;
        try {
            fromString = flowClient.formParse(data);
            personnelFlowEntity = JSONObject.parseObject(fromString, approvalBizEnum.getClazz());
        } catch (Exception exception) {
            throw new ServiceException("表单解析失败,请检查流程配置信息");
        }
        PersonnelApprovalService<Object> approvalService = personnelApprovalContext.dispatch(approvalBizEnum);
        approvalService.preBusinessHandle(personnelFlowEntity);
        return flowClient.submitFirst(command);

    }

    // 查询未更新的流程表单数据
    private List<FlowFormDataPO> queryNoUpdateFlowFormData(PersonnelApprovalBizEnum businessType) {
        return this.lambdaQuery()
                .eq(FlowFormDataPO::getUpdateBusiness, false)
                .eq(FlowFormDataPO::getBusinessType, businessType)
                .eq(FlowFormDataPO::getStatus, FlowResult.PASS)
                .list();
    }

    // 审批通过后更新业务数据
    private boolean updateBusinessData(FlowFormDataPO formDataPO) {
        if (FlowResult.REJECT.name().equals(formDataPO.getStatus())) {
            return false;
        }
        String businessType = formDataPO.getBusinessType();
        String jsonForm = formDataPO.getJsonForm();
        if (PersonnelApprovalBizEnum.PERSONNEL_REGULAR_WORKER.name().equals(businessType)) {
            return this.handleRegularWorker(jsonForm);
        } else if (PersonnelApprovalBizEnum.PERSONNEL_ROTATION_POST.name().equals(businessType)) {
            log.info("轮岗申请通过后，解析集合：" + jsonForm);
            List<RotationPostPO> rotationList = new ArrayList<>();
            try {
                JSONArray array = JSON.parseArray(jsonForm);
                for (Object ex : array) {
                    RotationPostPO rotationPostPO = JSONObject.parseObject(ex.toString(), RotationPostPO.class);
                    rotationPostPO.setTenant(formDataPO.getTenant());
                    rotationPostPO.setFlowFormDataId(formDataPO.getId());
                    rotationList.add(rotationPostPO);
                }
            } catch (Exception e) {
                log.error("轮岗申请通过后，集合解析失败：", e);
                log.info("轮岗申请通过后，解析对象");
                RotationPostPO rotationPostPO = JSONObject.parseObject(jsonForm, RotationPostPO.class);
                rotationPostPO.setTenant(formDataPO.getTenant());
                rotationPostPO.setFlowFormDataId(formDataPO.getId());
                rotationList.add(rotationPostPO);
            }
            for (RotationPostPO ex : rotationList) {
                if (!this.handleRotationPost(ex)) {
                    return false;
                }
            }
            return true;
        } else if (PersonnelApprovalBizEnum.PERSONNEL_LEAVE_OFFICE_PROCEDURES.name().equals(businessType) ||
                   PersonnelApprovalBizEnum.PERSONNEL_LEAVE_OFFICE.name().equals(businessType)) {
            LeaveOfficePO leaveOfficePO = JSONObject.parseObject(jsonForm, LeaveOfficePO.class);
            leaveOfficePO.setTenant(formDataPO.getTenant());
            return this.handleLeaveOffice(leaveOfficePO, businessType);
        } else if (PersonnelApprovalBizEnum.PERSONNEL_INDUCTION.name().equals(businessType)) {
            return this.handleInduction(formDataPO);
        }
        return false;
    }

    /**
     * 员工入职审批处理
     *
     * @param formDataPO
     *
     * @return
     */
    private boolean handleInduction(FlowFormDataPO formDataPO) {
        try {
            InductionPO inductionPO = JSONObject.parseObject(formDataPO.getJsonForm(), InductionPO.class);
            SubmitStaffCommand submitStaffCommand = ObjectUtils.copying(inductionPO, SubmitStaffCommand.class);
            submitStaffCommand.setName(inductionPO.getStaffName());
            submitStaffCommand.setOrganizationId(
                    !CollectionUtils.isEmpty(inductionPO.getOrganizationIdList()) ? Long.parseLong(inductionPO.getOrganizationIdList()
                                                                                                           .get(0)) : null);
            submitStaffCommand.setAppointmentIds(
                    Objects.nonNull(inductionPO.getAppointment()) ? Collections.singletonList(inductionPO.getAppointment()) : null);
            submitStaffCommand.setPostIds(
                    Objects.nonNull(inductionPO.getPostId()) ? Collections.singletonList(inductionPO.getPostId()) : null);
            submitStaffCommand.setRoleIds(
                    !CollectionUtils.isEmpty(inductionPO.getRoleId()) ? inductionPO.getRoleId() : null);
            submitStaffCommand.setMobile(inductionPO.getMobile());
            submitStaffCommand.setIdCard(inductionPO.getIdCard());
            submitStaffCommand.setStatus(PersonnelAbility.getStaffStatusByFlow());
            submitStaffCommand.setGender(GenderEnum.NONE.name());
            Long aLong = staffCommandService.saveOrUpdate(submitStaffCommand);
            StaffDetailVO staffDetailVO = staffQueryService.get(aLong);
            if (Objects.nonNull(staffDetailVO)) {
                formDataPO.setId(IdWorker.getId());
                formDataPO.setApplicantName(staffDetailVO.getName());
                formDataPO.setApplicantOrganizationId(staffDetailVO.getOrganizationId());
                formDataPO.setApplicantOrganizationName(staffDetailVO.getOrganizationName());
                formDataPO.setApplicantPostId(staffDetailVO.getAppointmentId());
                formDataPO.setApplicantPostName(staffDetailVO.getAppointmentName());
            }
        } catch (Exception e) {
            log.error("员工入职审批解析失败：", e);
            //todo 补偿机制
            return false;
        }
        return true;
    }

    // 处理离职
    private boolean handleLeaveOffice(LeaveOfficePO leaveOfficePO, String businessType) {
        List<Long> flowFormDataIds = new ArrayList<>();
        String leaveOfficeTimeStr;
        // 离职审批或者离职手续通过审批后都要检查一次，最终更新离职是以两种手续都通过
        List<String> staffIds = leaveOfficePO.getStaffIds();
        String staffId = staffIds.get(0);
        //判断是否只需离职不需离职手续
        PersonnelLeaveConfigVO personnelConfig = getPersonnelConfig();

        if (PersonnelApprovalBizEnum.PERSONNEL_LEAVE_OFFICE.name().equals(businessType)) {
            // 查找离职手续是否已经审批通过
            List<LeaveOfficePO> noUpdateLeaveOffices = flowFormDataMapper.getNoUpdateLeaveOffice(staffId, PersonnelApprovalBizEnum.PERSONNEL_LEAVE_OFFICE_PROCEDURES.name());
            if (CollectionUtils.isEmpty(noUpdateLeaveOffices) && !personnelConfig.getOnlyLeaveFlag()) {
                return false;
            }
            leaveOfficeTimeStr = leaveOfficePO.getTime();
            if (!CollectionUtils.isEmpty(noUpdateLeaveOffices)) {
                flowFormDataIds.addAll(noUpdateLeaveOffices.stream().map(LeaveOfficePO::getFlowFormDataId).collect(Collectors.toList()));
            }
        } else {
            // 查找离职审批是否已经审批通过
            List<LeaveOfficePO> noUpdateLeaveOffices = flowFormDataMapper.getNoUpdateLeaveOffice(staffId, PersonnelApprovalBizEnum.PERSONNEL_LEAVE_OFFICE.name());
            if (CollectionUtils.isEmpty(noUpdateLeaveOffices)) {
                return false;
            }
            // 如果有多个离职审批，按照离职时间倒序排列，取最晚的时间作为离职时间
            List<LeaveOfficePO> leaveOfficePOS = noUpdateLeaveOffices.stream()
                    .sorted(Comparator.comparing(l -> DateUtils.parseDate(l.getTime()), Comparator.reverseOrder()))
                    .toList();
            leaveOfficeTimeStr = leaveOfficePOS.get(0).getTime();
            flowFormDataIds.addAll(noUpdateLeaveOffices.stream().map(LeaveOfficePO::getFlowFormDataId).toList());
        }
        LocalDateTime leaveOfficeTime = null;
        SessionContextHolder.requestTenant(leaveOfficePO.getTenant());
        try {
            leaveOfficeTime = DateUtils.parseDate(leaveOfficeTimeStr);
        } catch (Exception e) {
            log.error("离职时间格式化失败，时间：" + leaveOfficeTimeStr, e);
            // 如果离职时间格式化失败，只更新状态，离职时间为null
            staffCommandService.updateStaffLeaveOffice(Long.valueOf(staffId), null);
            flowFormDataIds.forEach(id -> this.lambdaUpdate().eq(FlowFormDataPO::getId, id).set(FlowFormDataPO::getUpdateBusiness, true)
                    .update());
            return true;
        }
        if (DateUtils.compare(leaveOfficeTime, DateUtils.parseDate(DateUtils.formatDate(LocalDateTime.now()))) <= 0) {
            staffCommandService.updateStaffLeaveOffice(Long.valueOf(staffId), leaveOfficeTime);
            flowFormDataIds.forEach(id -> this.lambdaUpdate().eq(FlowFormDataPO::getId, id).set(FlowFormDataPO::getUpdateBusiness, true)
                    .update());
            return true;
        }
        return false;
    }

    /**
     * 获取离职手续配置
     *
     * @return
     */
    private PersonnelLeaveConfigVO getPersonnelConfig() {
        DataDictionaryaramByItemAndCodeParam dictionaryaram = new DataDictionaryaramByItemAndCodeParam();
        dictionaryaram.setItemCode(APP_BUSINESS_CONFIG);
        dictionaryaram.setCode(PERSONNEL_LEAVE_CONFIG);
        DataDictionaryDTO dataDictionaryByItemAndCode = dataDictionaryClient.getDataDictionaryByItemAndCode(dictionaryaram);
        if (Objects.isNull(dataDictionaryByItemAndCode) || StringUtils.isBlank(dataDictionaryByItemAndCode.getVariable())) {
            return new PersonnelLeaveConfigVO();
        }
        PersonnelLeaveConfigVO leaveConfigVO;
        try {
            leaveConfigVO = JsonUtils.deserialize(dataDictionaryByItemAndCode.getVariable(), PersonnelLeaveConfigVO.class);
            if (Objects.isNull(leaveConfigVO) || Objects.isNull(leaveConfigVO.getOnlyLeaveFlag())) {
                return new PersonnelLeaveConfigVO();
            }
        } catch (Exception e) {
            log.error("leave config deserialize error", e);
            leaveConfigVO = new PersonnelLeaveConfigVO();
        }
        return leaveConfigVO;
    }

    // 处理轮岗
    private boolean handleRotationPost(RotationPostPO rotationPostPO) {
        log.info("更新轮岗业务数据，参数:{}", rotationPostPO);
        LocalDateTime startDate;
        LocalDateTime endDate;
        try {
            rotationPostPO.parseStartAndEnd();
            startDate = rotationPostPO.getStartDate();
            endDate = rotationPostPO.getEndDate();
            // 如果轮岗结束时间小于当前时间，不做后续操作
            if (DateUtils.compare(endDate, LocalDateTime.now()) <= 0) {
                log.info("轮岗时间已结束：{}", endDate);
                return false;
            }
            // 若轮岗开始时间小于等于当前时间直接更新后续，若大于当前时间则由xxl-job定时更新
            if (DateUtils.compare(startDate, DateUtils.parseDate(DateUtils.formatDate(LocalDateTime.now()))) > 0) {
                log.info("轮岗时间未开始：{}", startDate);
                return false;
            }
            SessionContextHolder.requestTenant(rotationPostPO.getTenant());
            rotationPostPO.getStaffIds().stream().forEach(ex -> {
                this.addDetails(rotationPostPO, Long.parseLong(ex));
                staffCommandService.updateStaffOrganization(Long.parseLong(ex), rotationPostPO.getRotationOrganizationId());
            });
            return true;
        } catch (Exception e) {
            log.error("轮岗申请通过后更新业务失败,数据：" + rotationPostPO, e);
            return false;
        }
    }

    // 处理转正
    private boolean handleRegularWorker(String jsonForm) {
        RegularWorkerPO regularWorkerPO = JSONObject.parseObject(jsonForm, RegularWorkerPO.class);
        List<String> staffIds = regularWorkerPO.getStaffIds();
        String staffId = staffIds.getFirst();
        String regularWorkerTimeStr = regularWorkerPO.getRegularWorkerTime();
        LocalDateTime regularWorkerTime = null;
        StaffPO staffPO = staffCommandService.getById(staffId);
        try {
            regularWorkerTime = DateUtils.parseDate(regularWorkerTimeStr);
        } catch (Exception e) {
            log.error("转正时间格式化失败，时间：" + regularWorkerTimeStr, e);
            // 如果转正时间格式化失败，只更新状态，转正时间为null
            staffCommandService.lambdaUpdate().eq(StaffPO::getId, staffId).set(StaffPO::getStatus, StaffStatusEnum.FORMAL.name()).update();
            // 发送修改消息
            RocketMQContextHolder.send(UpdateStaffMessage.TOPIC, UpdateStaffMessage.builder()
                    .staffId(Long.valueOf(staffId))
                    .userId(staffPO != null ? staffPO.getUserId() : null)
                    .status(StaffStatusEnum.FORMAL.name())
                    .tenant(staffPO.getTenant())
                    .build());
            return true;
        }
        if (DateUtils.compare(regularWorkerTime, DateUtils.parseDate(DateUtils.formatDate(LocalDateTime.now()))) <= 0) {
            staffCommandService.lambdaUpdate().eq(StaffPO::getId, staffId).set(StaffPO::getStatus, StaffStatusEnum.FORMAL.name())
                    .set(StaffPO::getRegularWorkerDate, regularWorkerTime).update();
            // 发送修改消息
            RocketMQContextHolder.send(UpdateStaffMessage.TOPIC, UpdateStaffMessage.builder()
                    .staffId(Long.valueOf(staffId))
                    .userId(staffPO != null ? staffPO.getUserId() : null)
                    .status(StaffStatusEnum.FORMAL.name())
                    .tenant(staffPO.getTenant())
                    .build());
            return true;
        }
        return false;
    }

    /**
     * 添加转岗详情
     *
     * @param staffId        转岗员工id
     * @param rotationPostPO 转岗信息
     */
    public Boolean addDetails(RotationPostPO rotationPostPO, Long staffId) {
        log.info("添加转岗详情");
        FlowFormDataDetailsPO dataDetails = new FlowFormDataDetailsPO();
        dataDetails.setStaffId(staffId);
        dataDetails.setFlowFormDataId(rotationPostPO.getFlowFormDataId());
        dataDetails.setRotationOrganizationId(rotationPostPO.getRotationOrganizationId());
        dataDetails.setOrganizationId(rotationPostPO.getOrganizationId());
        rotationPostPO.parseStartAndEnd();
        StringBuffer buffer = new StringBuffer();
        buffer.append(DateUtils.formatDate(rotationPostPO.getStartDate()));
        buffer.append("~");
        buffer.append(DateUtils.formatDate(rotationPostPO.getEndDate()));
        dataDetails.setRotationTime(buffer.toString());
        return flowFormDataDetailsMapper.insert(dataDetails) > 0;
    }
}

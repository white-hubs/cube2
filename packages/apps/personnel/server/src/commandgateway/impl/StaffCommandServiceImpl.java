package com.guhecloud.cube.srv.gen.personnel.management.server.commandgateway.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.PositionClient;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.PositionDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.param.UserModifyParam;
import com.guhecloud.cube.biz.ouaa.metadata.enumeration.Gender;
import com.guhecloud.cube.srv.gen.personnel.management.server.commandgateway.StaffCommandService;
import com.guhecloud.cube.srv.gen.personnel.management.server.commandgateway.StaffExtendCommandService;
import com.guhecloud.cube.srv.gen.personnel.management.server.commandgateway.StaffTypeCommandService;
import com.guhecloud.cube.srv.gen.personnel.management.server.commandgateway.command.ChangeStaffCommand;
import com.guhecloud.cube.srv.gen.personnel.management.server.commandgateway.command.SaveToBePerfectCommand;
import com.guhecloud.cube.srv.gen.personnel.management.server.commandgateway.command.SubmitStaffCommand;
import com.guhecloud.cube.srv.gen.personnel.management.server.dao.StaffMapper;
import com.guhecloud.cube.srv.gen.personnel.management.server.enumeration.ArchiveStatus;
import com.guhecloud.cube.srv.gen.personnel.management.server.enumeration.StaffStatusEnum;
import com.guhecloud.cube.srv.gen.personnel.management.server.event.message.DelStaffMessage;
import com.guhecloud.cube.srv.gen.personnel.management.server.event.message.IncreaseStaffMessage;
import com.guhecloud.cube.srv.gen.personnel.management.server.event.message.UpdateStaffMessage;
import com.guhecloud.cube.srv.gen.personnel.management.server.interfaces.vo.StaffExcelVO;
import com.guhecloud.cube.srv.gen.personnel.management.server.po.StaffPO;
import com.guhecloud.cube.srv.gen.personnel.management.server.po.StaffTypePO;
import com.guhecloud.cube.srv.gen.personnel.management.server.tool.ExcelBeanUtil;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author bq
 * @date 2022/11/08
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StaffCommandServiceImpl extends ServiceImpl<StaffMapper, StaffPO> implements StaffCommandService {

    private final UserClient userClient;
    private final OrganizationClient organizationClient;
    private final PositionClient positionClient;
    private final StaffMapper staffMapper;
    private final StaffTypeCommandService staffTypeCommandService;
    private final StaffExtendCommandService staffExtendCommandService;
    private final AreaClient areaClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveOrUpdate(SubmitStaffCommand submitStaffCommand) {
        if (!CollectionUtils.isEmpty(submitStaffCommand.getAreaIdList())) {
            submitStaffCommand.setAreaIds(submitStaffCommand.getAreaIdList().stream().map(String::valueOf)
                                                  .collect(Collectors.joining(",")));
        }
        //校验身份证号
        //校验身份证号唯一
        if (StringUtils.isNotBlank(submitStaffCommand.getIdCard())) {
            List<StaffPO> staffPOList = this.lambdaQuery().eq(StaffPO::getIdCard, submitStaffCommand.getIdCard())
                    .list();
            Assert.isConflict(
                    !CollectionUtils.isEmpty(staffPOList) && !Objects.equals(staffPOList.get(0).getId(), submitStaffCommand.getId())
                    , "身份证号已存在");
        }
        Long id = submitStaffCommand.getId();
        //更新
        if (Objects.nonNull(id)) {
            StaffPO staffPO = this.getById(id);
            Assert.isNotFound(Objects.isNull(staffPO), "员工不存在");
            //新增或更新ouaa用户信息
            Long userId = this.saveOrUpdateOuaaUser(submitStaffCommand, staffPO);
            // 修改后需要通知其他业务的参数
            ObjectUtils.copying(submitStaffCommand, staffPO);
            staffPO.setArchiveStatus(ArchiveStatus.PERFECTED);
            if (ObjectUtils.isNotEmpty(submitStaffCommand.getAppointmentIds())) {
                staffPO.setAppointmentId(submitStaffCommand.getAppointmentIds().stream().map(String::valueOf)
                                                 .collect(Collectors.joining(",")));
            }
            staffPO.setAreaIds(submitStaffCommand.getAreaIds());
            staffPO.setIdCard(submitStaffCommand.getIdCard());

            this.updateById(staffPO);
            //新增或更新扩展表
            submitStaffCommand.setId(id);
            staffExtendCommandService.saveOrUpdate(submitStaffCommand);
            // 发送修改消息
            RocketMQContextHolder.send(UpdateStaffMessage.TOPIC, UpdateStaffMessage.builder()
                    .staffId(staffPO.getId())
                    .userId(userId)
                    .name(staffPO.getName())
                    .mobile(staffPO.getMobile())
                    .organizationId(staffPO.getOrganizationId())
                    .status(submitStaffCommand.getStatus())
                    .tenant(SessionContextHolder.getRequester().getTenant())
                    .cornet(submitStaffCommand.getCornet())
                    .idCard(staffPO.getIdCard())
                    .build());
        } else {
            List<StaffPO> staffPOS = this.lambdaQuery().eq(StaffPO::getMobile, submitStaffCommand.getMobile()).list();
            Assert.isConflict(!CollectionUtils.isEmpty(staffPOS), "员工信息已存在请勿重复添加");
            StaffPO staffPO = ObjectUtils.copying(submitStaffCommand, StaffPO.class);
            if (ObjectUtils.isNotEmpty(submitStaffCommand.getAppointmentIds())) {
                staffPO.setAppointmentId(submitStaffCommand.getAppointmentIds().stream().map(String::valueOf)
                                                 .collect(Collectors.joining(",")));
            }
            staffPO.setArchiveStatus(ArchiveStatus.PERFECTED);
            UserDTO userDTO = userClient.getUserByMobile(submitStaffCommand.getMobile(), SessionContextHolder.getRequester().getTenant());
            Long userId = Objects.nonNull(userDTO) ? userDTO.getId() : IdWorker.getId();
            staffPO.setUserId(userId);
            staffPO.setId(userId);
            this.save(staffPO);
            // 同步到用户管理
            this.saveOrUpdateOuaaUser(submitStaffCommand, staffPO);
            //新增或更新扩展表
            submitStaffCommand.setId(staffPO.getId());
            staffExtendCommandService.saveOrUpdate(submitStaffCommand);
            // 发出新增员工消息
            RocketMQContextHolder.send(IncreaseStaffMessage.TOPIC, IncreaseStaffMessage.builder()
                                               .paySocialInsuranceFirstDate(staffPO.getPaySocialInsuranceFirstDate())
                                               .employeeId(staffPO.getUserId())
                                               .name(staffPO.getName())
                                               .mobile(staffPO.getMobile())
                                               .organizationId(staffPO.getOrganizationId())
                                               .inductionDate(staffPO.getInductionDate())
                                               .status(staffPO.getStatus())
                                               .gender(staffPO.getGender())
                                               .idCard(staffPO.getIdCard())
                                               .cornet(submitStaffCommand.getCornet())
                                               .tenant(SessionContextHolder.getRequester().getTenant())
                                               .build()
                                      );
            id = staffPO.getId();

        }
        return id;
    }

    /**
     * 新增或更新OUAA用户信息
     *
     * @param submitStaffCommand 新增员工请求
     * @param staffPO            员工信息
     *
     * @return
     */
    private Long saveOrUpdateOuaaUser(SubmitStaffCommand submitStaffCommand, StaffPO staffPO) {

        Long userId = staffPO.getUserId();
        // 更新用户
        UserDTO userDTO = userClient.getUserByMobile(staffPO.getMobile(), SessionContextHolder.getRequester().getTenant());
        if (Objects.nonNull(userDTO)) {
            userClient.updateOfNormalByTenant(userDTO.getId(), userInfoConvert(userDTO, submitStaffCommand));
            userId = userDTO.getId();
        } else {
            // 同步到用户管理
            UserModifyParam userModifyParam = userInfoConvert(null, submitStaffCommand);
            userModifyParam.setId(userId);
            userId = userClient.saveOfNormalByTenant(userModifyParam);
            if (!Objects.equals(userId, staffPO.getId())) {
                userModifyParam.setId(userId);
                userClient.updateOfNormalByTenant(userId, userModifyParam);
            }
        }
        this.lambdaUpdate().eq(StaffPO::getId, staffPO.getId()).set(StaffPO::getUserId, userId).update();
        return userId;
    }

    @Override
    public Long saveToBePerfect(SaveToBePerfectCommand saveToBePerfectCommand) {
        StaffPO staffPO = ObjectUtils.copying(saveToBePerfectCommand, StaffPO.class);
        staffPO.setId(saveToBePerfectCommand.getUserId());
        staffPO.setPositionIds(CollectionUtils.isEmpty(saveToBePerfectCommand.getPostIds()) ? ""
                                                                                            : saveToBePerfectCommand.getPostIds().stream()
                                       .map(String::valueOf).collect(Collectors.joining(",")));
        staffPO.setArchiveStatus(ArchiveStatus.WAITING_FOR_PERFECT);
        staffPO.setAppointmentId(CollectionUtils.isEmpty(saveToBePerfectCommand.getAppointmentIds()) ? "" : saveToBePerfectCommand
                .getAppointmentIds().stream().map(String::valueOf).collect(Collectors.joining(",")));
        try {
            this.save(staffPO);
        } catch (DuplicateKeyException e) {
            log.error("监听ouaa新增员工,主键重复", e);
            StaffPO exist = this.getById(staffPO.getId());
            // 如果保存提示主键重复，但是查询又不存在，说明是已经逻辑删除的员工，但是ouaa的删除之后新建的用户还是保留了老的ID，我们这边同步也只能恢复删除的员工(如果以后改成id和userId不一致时就可以不用这样处理)
            if (exist == null) {
                staffMapper.updatePartInfoByDeleted(staffPO);
            }
        }
        // 发出新增员工消息
        RocketMQContextHolder.send(IncreaseStaffMessage.TOPIC, IncreaseStaffMessage.builder()
                .paySocialInsuranceFirstDate(staffPO.getPaySocialInsuranceFirstDate())
                .employeeId(staffPO.getId()).name(staffPO.getName()).mobile(staffPO.getMobile())
                .idCard(staffPO.getIdCard())
                .tenant(SessionContextHolder.getRequester().getTenant())
                .organizationId(staffPO.getOrganizationId()).build());
        return staffPO.getId();
    }

    @Override
    public void changeStaffByUser(ChangeStaffCommand changeStaffCommand) {
        StaffPO staffPO = super.getOne(Wrappers.<StaffPO>lambdaQuery().eq(StaffPO::getUserId, changeStaffCommand.getUserId()));
        Assert.isNotFound(staffPO, "员工不存在");
        if (StaffStatusEnum.LEAVE_OFFICE.name().equals(staffPO.getStatus())) {
            return;
        }
        String positionIds = CollectionUtils.isEmpty(changeStaffCommand.getPostIds()) ? "" : changeStaffCommand.getPostIds().stream()
                .map(String::valueOf).collect(Collectors.joining(","));
        String appointmentId = CollectionUtils.isEmpty(changeStaffCommand.getAppointmentIds()) ? "" : changeStaffCommand.getAppointmentIds()
                .stream().map(String::valueOf).collect(Collectors.joining(","));
        this.lambdaUpdate()
                .eq(StaffPO::getId, staffPO.getId())
                .set(StaffPO::getPositionIds, positionIds)
                .set(StaffPO::getName, changeStaffCommand.getName())
                .set(StaffPO::getMobile, changeStaffCommand.getMobile())
                .set(StaffPO::getOrganizationId, changeStaffCommand.getOrganizationId())
                .set(StaffPO::getGender, changeStaffCommand.getGender())
                .set(StaffPO::getAppointmentId, appointmentId)
                .set(StaffPO::getIdCard, changeStaffCommand.getIdCard())
                .update();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ExcelBeanUtil.ExcelMsg> importTemplate(MultipartFile file) {
        Assert.isNotFound(file.isEmpty(), "必须上传excel文件");
        int dataFirstRow = 2;
        ExcelBeanUtil<StaffExcelVO> excelBeanUtil = new ExcelBeanUtil<>(StaffExcelVO.class);
        List<StaffExcelVO> dataByExcel = new ArrayList<>();
        try {
            dataByExcel = excelBeanUtil.getDataByExcel(file.getInputStream(), "", dataFirstRow);
        } catch (Exception e) {
            log.error("导入花名册异常", e);
            throw new ServiceException("导入文件异常,请联系管理员");
        }
        // 基础格式校验（非空、日期、长度）
        List<ExcelBeanUtil.ExcelMsg> excelMsgs = excelBeanUtil.getExcelMsgs();
        if (excelMsgs.size() > 0) {
            return excelMsgs;
        }
        // 业务数据校验
        excelMsgs = this.checkDataByImportExcel(dataByExcel, dataFirstRow);
        if (excelMsgs.size() > 0) {
            return excelMsgs;
        }
        List<SubmitStaffCommand> staffPOS = ObjectUtils.copying(dataByExcel, SubmitStaffCommand.class);
        for (int i = 0; i < staffPOS.size(); i++) {
            SubmitStaffCommand po = staffPOS.get(i);
            StaffPO staffPO = this.lambdaQuery().eq(StaffPO::getMobile, po.getMobile()).one();
            if (Objects.isNull(staffPO)) {
                po.setId(null);
            } else {
                po.setId(staffPO.getId());
            }
            try {
                this.saveOrUpdate(po);
            } catch (Exception e) {
                log.error("导入花名册，保存失败", e);
                throw new ServiceException("第" + (i + 3) + "行数据导入失败");
            }
        }
        return excelMsgs;
    }

    @Override
    public void updateStaffOrganization(Long staffId, Long organizationId) {
        StaffPO staffPO = this.getById(staffId);
        if (Objects.isNull(staffPO)) {
            return;
        }
        this.lambdaUpdate().eq(StaffPO::getId, staffId).set(StaffPO::getOrganizationId, organizationId).update();
        UserDTO user = userClient.getUser(staffPO.getUserId());
        if (Objects.nonNull(user)) {
            UserModifyParam userModifyParam = new UserModifyParam();
            ObjectUtils.copying(user, userModifyParam);
            userModifyParam.setOrganizationIds(Collections.singletonList(organizationId));
            String positionIds = staffPO.getPositionIds();
            if (StringUtils.isNotEmpty(positionIds)) {
                userModifyParam.setPositionIds(Arrays.stream(positionIds.split(",")).map(Long::parseLong).collect(Collectors.toList()));
            }
            userModifyParam.setSendMessage(false);
            userClient.updateOfNormalByTenant(staffPO.getUserId(), userModifyParam);
        }
        // 发送修改消息
        RocketMQContextHolder.send(UpdateStaffMessage.TOPIC, UpdateStaffMessage.builder()
                .staffId(staffId)
                .organizationId(organizationId)
                .mobile(staffPO.getMobile())
                .tenant(SessionContextHolder.getRequester().getTenant())
                .build());
    }

    @Override
    public void updateStaffLeaveOffice(Long staffId, LocalDateTime leaveOfficeTime) {
        StaffPO staffPO = this.getById(staffId);
        if (Objects.isNull(staffPO)) {
            return;
        }
        this.lambdaUpdate().eq(StaffPO::getId, staffId)
                .set(Objects.nonNull(leaveOfficeTime), StaffPO::getQuitDate, leaveOfficeTime)
                .set(StaffPO::getStatus, StaffStatusEnum.LEAVE_OFFICE.name())
                .update();
        // 禁用ouaa账户
        UserDTO user = userClient.getUser(staffPO.getUserId());
        if (Objects.nonNull(user)) {
            UserModifyParam userModifyParam = new UserModifyParam();
            ObjectUtils.copying(user, userModifyParam);
            userModifyParam.setOrganizationIds(Collections.singletonList(staffPO.getOrganizationId()));
            userModifyParam.setPositionIds(user.getPositions().stream().map(UserDTO.Position::getId).collect(Collectors.toList()));
            userModifyParam.setEnabled(false);
            userModifyParam.setSendMessage(false);
            userClient.updateOfNormalByTenant(staffPO.getUserId(), userModifyParam);
        }
        // 发送修改消息
        RocketMQContextHolder.send(UpdateStaffMessage.TOPIC, UpdateStaffMessage.builder()
                .staffId(staffId)
                .userId(staffPO.getUserId())
                .mobile(staffPO.getMobile())
                .leaveOffice(true)
                .status(StaffStatusEnum.LEAVE_OFFICE.name())
                .tenant(SessionContextHolder.getRequester().getTenant())
                .build());
    }

    @Override
    public void remove(Long staffId) {
        StaffPO staffPO = this.getById(staffId);
        DatabaseContextHolder.softDelete(this, staffId);
        // 发出删除员工消息
        RocketMQContextHolder.send(DelStaffMessage.TOPIC, new DelStaffMessage(staffId,
                                                                               staffPO == null ? null : staffPO.getUserId(),
                                                                               staffPO == null ? null : staffPO.getTenant()));
    }

    private List<ExcelBeanUtil.ExcelMsg> checkDataByImportExcel(List<StaffExcelVO> dataByExcel, int dataFirstRow) {
        List<ExcelBeanUtil.ExcelMsg> errMsgList = new ArrayList<>();
        Map<String, String> staffNameMap = staffTypeCommandService.lambdaQuery().list().stream()
                .collect(Collectors.toMap(StaffTypePO::getName, StaffTypePO::getCode, (k1, k2) -> k1));
        for (int i = 0; i < dataByExcel.size(); i++) {
            StaffExcelVO excelVO = dataByExcel.get(i);
            excelVO.setEnumName();
            excelVO.setType(staffNameMap.get(excelVO.getTypeName()));
            if (StringUtils.isNotBlank(excelVO.getPostNames())) {
                List<PositionDTO> positionDTOS = positionClient.getByName(excelVO.getPostNames());
                if (CollectionUtils.isEmpty(positionDTOS)) {
                    errMsgList.add(new ExcelBeanUtil.ExcelMsg(i + dataFirstRow + 1, 7, "岗位", "岗位不存在,请下载最新的模板"));
                } else {
                    excelVO.setPositionIds(positionDTOS.get(0).getId().toString());
                    excelVO.setPostIds(Collections.singletonList(positionDTOS.get(0).getId()));
                }
            }
            OrganizationDTO organization = organizationClient.getOrganizationsByName(excelVO.getOrganizationName());
            if (organization == null) {
                errMsgList.add(new ExcelBeanUtil.ExcelMsg(i + dataFirstRow + 1, 8, "部门", "部门不存在,请下载最新的模板"));
            } else {
                excelVO.setOrganizationId(organization.getId());
            }
            if (StringUtils.isNotBlank(excelVO.getAreaNameList())) {
                List<AreaDTO> areaByName = areaClient.getAreaByName(excelVO.getAreaNameList());
                excelVO.setAreaIds(CollectionUtils.isEmpty(areaByName) ? null : areaByName.stream().map(t -> String.valueOf(t.getId()))
                        .collect(Collectors.joining(",")));
            }
            if (StringUtils.isNotBlank(excelVO.getStatusName())) {
                excelVO.setStatus(StaffStatusEnum.getNameByValues(excelVO.getStatusName()));
            }
        }
        return errMsgList;
    }

    /**
     * 参数转换
     *
     * @param userDTO
     * @param submitStaffCommand
     *
     * @return
     */
    private UserModifyParam userInfoConvert(UserDTO userDTO, SubmitStaffCommand submitStaffCommand) {
        UserModifyParam userModifyParam =
                Objects.nonNull(userDTO) ? ObjectUtils.copying(userDTO, UserModifyParam.class) : new UserModifyParam();
        userModifyParam.setName(submitStaffCommand.getName());
        userModifyParam.setMobile(submitStaffCommand.getMobile());
        userModifyParam.setEnabled(!StaffStatusEnum.LEAVE_OFFICE.name().equals(submitStaffCommand.getStatus()));
        userModifyParam.setGender(
                StringUtils.isNotBlank(submitStaffCommand.getGender()) ? Gender.valueOf(submitStaffCommand.getGender()) : Gender.NONE);
        userModifyParam.setOrganizationIds(Collections.singletonList(submitStaffCommand.getOrganizationId()));
        userModifyParam.setPositionIds(
                CollectionUtils.isEmpty(submitStaffCommand.getPostIds()) ? new ArrayList<>() : submitStaffCommand.getPostIds());
        userModifyParam.setAppointmentIds(submitStaffCommand.getAppointmentIds());
        userModifyParam.setRoleIds(submitStaffCommand.getRoleIds());
        userModifyParam.setIdCard(submitStaffCommand.getIdCard());
        userModifyParam.setTenant(SessionContextHolder.getRequester().getTenant());
        userModifyParam.setSendMessage(false);
        return userModifyParam;
    }

}

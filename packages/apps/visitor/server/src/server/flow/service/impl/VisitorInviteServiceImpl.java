package com.guhecloud.cube.srv.gen.visitor.management.server.flow.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.flow.client.FlowClient;
import com.guhecloud.cube.base.flow.client.FlowResult;
import com.guhecloud.cube.base.flow.client.dto.StartUserInfo;
import com.guhecloud.cube.base.flow.client.dto.TaskVariableDTO;
import com.guhecloud.cube.base.flow.client.param.FormDataParam;
import com.guhecloud.cube.base.message2.client.SatSendClient;
import com.guhecloud.cube.base.message2.client.param.MsgSendTargetParam;
import com.guhecloud.cube.base.message2.client.param.SatMsgChannelSendParam;
import com.guhecloud.cube.base.message2.client.param.SatMsgSendParam;
import com.guhecloud.cube.biz.ouaa.client.TerminalClient;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.TerminalDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.param.UserModifyParam;
import com.guhecloud.cube.biz.ouaa.metadata.enumeration.Gender;
import com.guhecloud.cube.srv.gen.visitor.management.server.enums.VisitorInviteStausEnum;
import com.guhecloud.cube.srv.gen.visitor.management.server.flow.po.FollowVisitorPO;
import com.guhecloud.cube.srv.gen.visitor.management.server.flow.po.VisitorInvitePO;
import com.guhecloud.cube.srv.gen.visitor.management.server.flow.service.VisitorInviteService;
import com.guhecloud.cube.srv.gen.visitor.management.server.mapper.ReservationRecordMapper;
import com.guhecloud.cube.srv.gen.visitor.management.server.po.ReservationRecordPO;
import com.guhecloud.cube.srv.gen.visitor.management.server.po.VisitAreaPO;
import com.guhecloud.cube.srv.gen.visitor.management.server.po.VisitorInviteRecordPO;
import com.guhecloud.cube.srv.gen.visitor.management.server.service.ReservationPassService;
import com.guhecloud.cube.srv.gen.visitor.management.server.service.VisitAreaService;
import com.guhecloud.cube.srv.gen.visitor.management.server.service.VisitorInviteRecordService;
import com.guhecloud.cube.srv.gen.visitor.management.server.third.lamp.utils.LampVisitorHttpUtils;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.param.ReservationParam;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.result.ReservationResult;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import instance.command.BackTaskCommand;
import instance.command.CompleteTaskCommand;
import instance.command.SubmitInitFormCommand;
import instance.event.NodePassMessage;
import instance.event.ProcessBackMessage;
import instance.event.ProcessCompletedMessage;
import instance.event.ProcessStartedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vo.FormBizFieldMapVO;
import vo.FormFlowBaseVO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/2/13 9:26
 * @Description:
 */

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VisitorInviteServiceImpl implements VisitorInviteService {

    public static final String SOURCE_PROCESS_ID = "sourceProcessId";

    public static final String START_USER_INFO = "startUserInfo";

    public static final String VISITOR_INVITE_NOTICE_USER = "VISITOR_INVITE_NOTICE_USER";

    /**
     * 小程序URLLink默认H5Url
     */
    public static final String VISITOR_APPLET_PATH = "/pages/web-page/web-page";
    public static final String VISITOR_APPLET_QUERY = "type=streamengineH5&processId=%s";

    /**
     * 获取微信URL_LINK
     */
    public static final String URL_LINK_URL = "https://api.weixin.qq.com/wxa/generate_urllink";

    /**
     * 获取access_token
     */
    public static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";

    private final FlowClient flowClient;

    private final UserClient userClient;

    private final SatSendClient satSendClient;

    private final TerminalClient terminalClient;

    private final ReservationPassService reservationPassService;

    private final ReservationRecordMapper reservationRecordMapper;

    private final VisitorInviteRecordService visitorInviteRecordService;

    private final VisitAreaService visitAreaService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String visitorInviteSubmit(SubmitInitFormCommand command) {
        VisitorInvitePO visitorInvitePO = analyzeFormData(command.getFormDefinitionId(), command.getData());
        this.verification(visitorInvitePO);
        String processInsId = flowClient.submitFirst(command);
        visitorInviteRecordService.saveOrUpdateVisitorInviteRecord(visitorInvitePO, processInsId,
                                                                   VisitorInviteStausEnum.PENDING.name(), null);
        return processInsId;
    }

    /**
     * 解析表单内容
     *
     * @param data
     *
     * @return
     */
    private VisitorInvitePO analyzeFormData(Long formDefinitionId, String data) {
        FormDataParam formDataParam = new FormDataParam(formDefinitionId, data);
        VisitorInvitePO visitorInvitePO = null;
        try {
            String fromString = flowClient.formParse(formDataParam);
            visitorInvitePO = JSONObject.parseObject(fromString, VisitorInvitePO.class);
        } catch (Exception exception) {
            throw new ServiceException("表单解析失败,请检查流程配置信息");
        }
        return visitorInvitePO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void visitorInviteEventStart(ProcessStartedMessage message) {
        //流程变量
        Map<String, Object> variables = new HashMap<>();
        FormFlowBaseVO formFlowBaseVO = flowClient.getFormFlowBaseInfoByBusinessType(message.getBusinessType());
        //构建流程参数
        String buildData = this.buildData(formFlowBaseVO, message.getData(), variables);
        VisitorInvitePO visitorInvitePO = JsonUtils.deserialize(message.getData(), VisitorInvitePO.class);
        this.verification(visitorInvitePO);
        SubmitInitFormCommand command = new SubmitInitFormCommand();
        command.setType("FLOW_FORM");
        command.setApplication(formFlowBaseVO.getApplication());
        command.setApplicationName(formFlowBaseVO.getApplicationName());
        command.setBusinessType(message.getBusinessType());
        command.setFlowKey(formFlowBaseVO.getFlowKey());
        command.setFormKey(formFlowBaseVO.getFormKey());
        command.setFormDefinitionId(formFlowBaseVO.getFormDefinitionId());
        command.setFormDefinitionVersion(formFlowBaseVO.getFormDefinitionVersion());
        //判断用户是否存在，不存在进行初始化
        UserDTO user = userClient.getUserByMobile(visitorInvitePO.getVisitorPhone());
        if (Objects.isNull(user)) {
            UserModifyParam userModifyParam = new UserModifyParam();
            userModifyParam.setName(visitorInvitePO.getVisitorName());
            userModifyParam.setMobile(visitorInvitePO.getVisitorPhone());
            userModifyParam.setGender(Gender.NONE);
            userModifyParam.setSendMessage(false);
            userModifyParam.setSource("WECHAT");
            Long userId = userClient.saveByTenant(userModifyParam);
            user = new UserDTO();
            user.setMobile(visitorInvitePO.getVisitorPhone());
            user.setName(visitorInvitePO.getVisitorName());
            user.setId(userId);
            user.setUsername(visitorInvitePO.getVisitorName());
        }
        StartUserInfo startUserInfo = new StartUserInfo();
        startUserInfo.setStartUserId(message.getStartUserId());
        startUserInfo.setStartUserName("系统");
        command.setStartUserInfo(startUserInfo);
        command.setBackLogTitle("系统发起的" + formFlowBaseVO.getBusinessTypeName());
        command.setData(buildData);
        variables.put(SOURCE_PROCESS_ID, message.getProcessInstanceId());
        command.setVariables(variables);
        //发起流程
        String processId = flowClient.submitFirst(command);
        //发送短信通知访客
        try {
            Object object = message.getProcessVariables().get(START_USER_INFO);
            UserDTO deserialize = new UserDTO();
            if (Objects.nonNull(object)) {
                deserialize = JSONObject.parseObject(object.toString(), UserDTO.class);
                deserialize = Objects.nonNull(deserialize) ? deserialize : new UserDTO();
            }
            Map<String, String> msgParams = new HashMap<>();
            msgParams.put("visitorName", visitorInvitePO.getVisitorName());
            msgParams.put("inviteName", deserialize.getName());
            msgParams.put("invitePhone", deserialize.getMobile());
            msgParams.put("inviteDepartment", deserialize.getOrganization().getName());
            msgParams.put("url", getWxLinkUrl(processId));
            List<SatMsgChannelSendParam> satMsgChannelSendParams = new ArrayList<>();
            MsgSendTargetParam sendTargetParam = MsgSendTargetParam.builder().userId(user.getId())
                    .userName(user.getUsername())
                    .userPhone(user.getMobile())
                    .receiver(user.getMobile()).build();
            satMsgChannelSendParams.add(new SatMsgChannelSendParam(null, ChannelEnum.CHANNEL_SMS, SessionContextHolder.getRequester()
                    .getTenant(), Collections.singletonList(sendTargetParam)));
            this.satSendClient.sendMsg(SatMsgSendParam.builder().businessId(processId).applicationCode(command.getApplication())
                                               .applicationName(command.getApplicationName()).msgSceneCode(VISITOR_INVITE_NOTICE_USER)
                                               .msgParams(msgParams)
                                               .satMsgChannelSendParams(satMsgChannelSendParams)
                                               .tenant(SessionContextHolder.getRequester().getTenant()).build());
            log.info("访客邀约消息:{},发送成功", VISITOR_INVITE_NOTICE_USER);
        } catch (Exception e) {
            log.error("消息发送失败:{}", e.getMessage());
        }
    }

    private String buildData(FormFlowBaseVO formFlowBaseVO, String data, Map<String, Object> variables) {
        Map<String, Object> dataMap = new HashMap<>();

        //构建发起data
        List<FormBizFieldMapVO> formBizFieldMapList = formFlowBaseVO.getFormBizFieldMapList();
        //源发起参数
        Map<String, Object> sourceDataMap = JsonUtils.deserialize(data, new TypeReference<Map<String, Object>>() {
        });
        dataMap.put("sponsorId", "100000");
        for (FormBizFieldMapVO formBizFieldMapVO : formBizFieldMapList) {
            Object values = sourceDataMap.get(formBizFieldMapVO.getBusinessField());
            if (values instanceof List) {
                List<Object> valueLists = (List<Object>) values;
                List<Object> listMap = new ArrayList<>();
                for (Object object : valueLists) {
                    if (object instanceof Map) {
                        Map<String, Object> map = new HashMap<>();
                        Map<String, Object> valueMap = (Map<String, Object>) object;
                        for (FormBizFieldMapVO bizFieldMapVO : formBizFieldMapList) {
                            if (!valueMap.containsKey(bizFieldMapVO.getBusinessField())) {
                                continue;
                            }
                            map.put(bizFieldMapVO.getControlId(), valueMap.get(bizFieldMapVO.getBusinessField()));
                        }
                        listMap.add(map);
                    } else {
                        listMap.add(object);
                    }
                }
                //区域需要单独处理解析
                if ("areaId".equals(formBizFieldMapVO.getBusinessField())) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", values);
                    List<Long> values1 = (List<Long>) values;
                    VisitAreaPO areaServiceById = visitAreaService.getById(values1.get(0));
                    map.put("name", Objects.nonNull(areaServiceById) ? areaServiceById.getAreaName() : null);
                    dataMap.put(formBizFieldMapVO.getControlId(), map);
                } else {
                    dataMap.put(formBizFieldMapVO.getControlId(), listMap);
                }
            } else {
                dataMap.put(formBizFieldMapVO.getControlId(), sourceDataMap.get(formBizFieldMapVO.getBusinessField()));
            }
            //电话变量 用户查询审批人
            if ("visitorPhone".equals(formBizFieldMapVO.getBusinessField())) {
                variables.put(formBizFieldMapVO.getControlId(), sourceDataMap.get(formBizFieldMapVO.getBusinessField()));
            }
        }
        return JsonUtils.serialize(dataMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void visitorInviteEventNodePass(NodePassMessage message) {
        //流程变量
        Map<String, Object> variables = new HashMap<>();
        FormFlowBaseVO formFlowBaseVO = flowClient.getFormFlowBaseInfoByBusinessType(message.getBusinessType());
        //构建流程参数
        String buildData = this.buildData(formFlowBaseVO, message.getData(), variables);
        VisitorInvitePO visitorInvitePO = JsonUtils.deserialize(message.getData(), VisitorInvitePO.class);
        List<TaskVariableDTO> variableDTOS = this.getRunTask(message.getProcessInstanceId(), message.getData(), message.getSourceProcessId(), message.getStartUserId());
        if (CollectionUtils.isEmpty(variableDTOS)) {
            return;
        }
        variables.put(SOURCE_PROCESS_ID, message.getProcessInstanceId());
        //系统审批
        variableDTOS.forEach(t -> {
            Principal principal = Principal.builder()
                    .id(t.getAssignee())
                    .type(Principal.Type.USER)
                    .build();
            SecurityContextHolder.setPrincipal(principal);
            SessionContextHolder.requestTenant(t.getTenantId());
            CompleteTaskCommand completeTaskCommand = CompleteTaskCommand.builder()
                    .account(SecurityContextHolder.getPrincipal().getId().toString())
                    .taskId(t.getId())
                    .variables(variables)
                    .result(FlowResult.PASS.name())
                    .data(buildData)
                    .innerServer(true)
                    .build();
            flowClient.completeTask(completeTaskCommand);
        });
        List<VisitorInviteRecordPO> visitorInviteRecordPOS = visitorInviteRecordService.lambdaQuery()
                .eq(VisitorInviteRecordPO::getProcessInsId, message.getSourceProcessId())
                .list();
        String sourceId = message.getSourceProcessId();
        if (CollectionUtils.isEmpty(visitorInviteRecordPOS)) {
            sourceId = message.getProcessInstanceId();
        }
        visitorInviteRecordService.saveOrUpdateVisitorInviteRecord(visitorInvitePO, sourceId,
                                                                   VisitorInviteStausEnum.PROCESSING.name(), null);
    }

    private List<TaskVariableDTO> getRunTask(String processInstanceId, String data, String sourceProcessId, Long startUserId) {

        VisitorInviteRecordPO inviteRecordPO = visitorInviteRecordService
                .lambdaQuery().and(t -> t.eq(VisitorInviteRecordPO::getProcessInsId, processInstanceId)
                        .or().eq(VisitorInviteRecordPO::getProcessInsId, sourceProcessId))
                .last("limit 1").one();
        if (Objects.isNull(inviteRecordPO)) {
            return new ArrayList<>();
        }
        //先根据发起的processId查询，查询到就审批通过，查询不到根据sourceProcessId查询，当前网络的processId
        List<TaskVariableDTO> variableDTOS = flowClient
                .gerRunTaskByProcessInsIdAndAssignee(inviteRecordPO.getProcessInsId(),
                                                     null);
        if (!CollectionUtils.isEmpty(variableDTOS)) {
            return variableDTOS;
        }
        Map<String, String> processIdBySourceIdMap = flowClient.getProcessIdBySourceId(Collections.singletonList(inviteRecordPO.getProcessInsId()));
        if (MapUtils.isEmpty(processIdBySourceIdMap)) {
            return new ArrayList<>();
        }
        return flowClient
                .gerRunTaskByProcessInsIdAndAssignee(processIdBySourceIdMap.get(inviteRecordPO.getProcessInsId()),
                                                     null);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void visitorInviteEventNodeBack(ProcessBackMessage backMessage) {

        List<TaskVariableDTO> variableDTOS = this.getRunTask(backMessage.getProcessInstanceId(), backMessage.getData(), backMessage.getSourceProcessId(), backMessage.getStartUserId());

        if (CollectionUtils.isEmpty(variableDTOS)) {
            return;
        }
        variableDTOS.forEach(t -> {
            Principal principal = Principal.builder()
                    .id(t.getAssignee())
                    .type(Principal.Type.USER)
                    .build();
            SecurityContextHolder.setPrincipal(principal);
            SessionContextHolder.requestTenant(t.getTenantId());
            BackTaskCommand completeTaskCommand = BackTaskCommand.builder()
                    .account(SecurityContextHolder.getPrincipal().getId().toString())
                    .taskId(t.getId())
                    .innerServer(true)
                    .build();
            flowClient.back(completeTaskCommand);
        });
        visitorInviteRecordService
                .lambdaUpdate().eq(VisitorInviteRecordPO::getProcessInsId, backMessage.getSourceProcessId())
                .set(VisitorInviteRecordPO::getStatus, VisitorInviteStausEnum.REJECTED.name())
                .update();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void visitorInviteEventNodeComplete(ProcessCompletedMessage completedMessage) {
        VisitorInvitePO visitorInvitePO = JsonUtils.deserialize(completedMessage.getData(), VisitorInvitePO.class);
        if (Objects.isNull(visitorInvitePO)) {
            return;
        }
        try {
            ReservationParam reservationParam = buildReservationParam(visitorInvitePO, completedMessage.getStartUserId());
            ReservationResult result = reservationPassService.reservation(reservationParam);
            List<VisitorInviteRecordPO> visitorInviteRecordPOS = visitorInviteRecordService.lambdaQuery()
                    .eq(VisitorInviteRecordPO::getProcessInsId, completedMessage.getSourceProcessId())
                    .list();
            String sourceId = completedMessage.getSourceProcessId();
            if (CollectionUtils.isEmpty(visitorInviteRecordPOS)) {
                sourceId = completedMessage.getProcessInstanceId();
            }
            reservationRecordMapper.update(null, Wrappers.<ReservationRecordPO>lambdaUpdate()
                    .set(ReservationRecordPO::getProcessId, sourceId)
                    .eq(ReservationRecordPO::getId, result.getId()));
            visitorInviteRecordService
                    .lambdaUpdate().eq(VisitorInviteRecordPO::getProcessInsId, sourceId)
                    .set(VisitorInviteRecordPO::getStatus, VisitorInviteStausEnum.COMPLETE.name())
                    .set(VisitorInviteRecordPO::getReservationRecordId, result.getId())
                    .set(VisitorInviteRecordPO::getBarCode, result.getBarCode())
                    .update();
        } catch (Exception exception) {
            log.error("reservation fail ,reason:{}", exception);
        }
    }

    private static ReservationParam buildReservationParam(VisitorInvitePO visitorInvitePO, Long startUserId) {
        ReservationParam reservationParam = new ReservationParam();

        reservationParam.setAreaId(visitorInvitePO.getAreaId());
        reservationParam.setPeriod(visitorInvitePO.getAccessPeriod());
        reservationParam.setName(visitorInvitePO.getVisitorName());
        reservationParam.setPhone(visitorInvitePO.getVisitorPhone());
        reservationParam.setDayStr(visitorInvitePO.getAccessDate());
        reservationParam.setTimeStr(visitorInvitePO.getTimeStr().toString());
        reservationParam.setReason(visitorInvitePO.getReason());
        reservationParam.setCertificateNumber(visitorInvitePO.getVisitorIdCard());
        reservationParam.setPlateNumber(visitorInvitePO.getPlateNumber());
        if (!CollectionUtils.isEmpty(visitorInvitePO.getFollowVisitors())) {
            List<FollowVisitorPO> followVisitorPOS = visitorInvitePO.getFollowVisitors().stream()
                    .filter(t -> StringUtils.isNotBlank(t.getFollowName()))
                    .collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(followVisitorPOS)) {
                reservationParam.setFollowVisitors(visitorInvitePO.getFollowVisitors());
            }
        }
        reservationParam.setUserId(startUserId);
        return reservationParam;
    }

    /**
     * 获取微信linkUrl
     *
     * @param processId
     *
     * @return
     */
    private String getWxLinkUrl(String processId) {
        TerminalDTO terminal = terminalClient.getTerminalByCode("bimops-manage-app");
        Assert.isParameterError(StringUtils.isBlank(terminal.getAppId())
                                || StringUtils.isBlank(terminal.getAppSecret()), "wx appid or appSecret");
        //获取accessToken
        Map<String, String> param = new HashMap<>();
        param.put("grant_type", "client_credential");
        param.put("appid", terminal.getAppId());
        param.put("secret", terminal.getAppSecret());
        String response = LampVisitorHttpUtils.doGet(ACCESS_TOKEN_URL, null, param);
        Map<String, String> accessTokenMap = JsonUtils.deserialize(response, new TypeReference<Map<String, String>>() {
        });
        Assert.isParameterError(MapUtils.isEmpty(accessTokenMap)
                                || StringUtils.isBlank(accessTokenMap.get("access_token")), "get access token failed");
        //根据accessToken获取UrlLink
        Map<String, Object> urlLinkParam = new HashMap<>();
        Map<String, String> params = new HashMap<>();

        params.put("access_token", accessTokenMap.get("access_token"));
        urlLinkParam.put("path", VISITOR_APPLET_PATH);
        urlLinkParam.put("query", String.format(VISITOR_APPLET_QUERY, processId));

        String urlLinkResponse = LampVisitorHttpUtils
                .doPostJson(URL_LINK_URL, null, params, JsonUtils.serialize(urlLinkParam));
        Map<String, String> urlLinkMap = JsonUtils.deserialize(urlLinkResponse, new TypeReference<Map<String, String>>() {
        });
        return " " + urlLinkMap.get("url_link") + " ";
    }

    /**
     * 参数校验
     *
     * @param visitorInvitePO
     */
    private void verification(VisitorInvitePO visitorInvitePO) {
        Assert.isParameterError(StringUtils.isEmpty(visitorInvitePO.getVisitorName()), "流程配置访客姓名未配置");
        Assert.isParameterError(StringUtils.isEmpty(visitorInvitePO.getVisitorPhone()), "流程配置联系电话未配置");
        Assert.isParameterError(Objects.isNull(visitorInvitePO.getAreaId()), "流程配置访问区域未配置");
    }
}

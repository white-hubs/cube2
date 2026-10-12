package com.guhecloud.cube.srv.gen.visitor.management.server.third.lamp.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.visitor.management.server.enums.ReservationStatusEnum;
import com.guhecloud.cube.srv.gen.visitor.management.server.enums.VisitorInviteStausEnum;
import com.guhecloud.cube.srv.gen.visitor.management.server.mapper.PassRecordMapper;
import com.guhecloud.cube.srv.gen.visitor.management.server.mapper.ReservationRecordMapper;
import com.guhecloud.cube.srv.gen.visitor.management.server.mapper.VisitorStatusChangeRecordMapper;
import com.guhecloud.cube.srv.gen.visitor.management.server.po.PassRecordPO;
import com.guhecloud.cube.srv.gen.visitor.management.server.po.ReservationRecordPO;
import com.guhecloud.cube.srv.gen.visitor.management.server.po.SystemLoginInfoPO;
import com.guhecloud.cube.srv.gen.visitor.management.server.po.VisitorInviteRecordPO;
import com.guhecloud.cube.srv.gen.visitor.management.server.po.VisitorStatusChangeRecordPO;
import com.guhecloud.cube.srv.gen.visitor.management.server.service.VisitorInviteRecordService;
import com.guhecloud.cube.srv.gen.visitor.management.server.third.lamp.service.LampLoginService;
import com.guhecloud.cube.srv.gen.visitor.management.server.third.lamp.service.LampReservationPassService;
import com.guhecloud.cube.srv.gen.visitor.management.server.third.lamp.utils.LampVisitorHttpUtils;
import com.guhecloud.cube.srv.gen.visitor.management.server.third.lamp.utils.LampVisitorServiceUtils;
import com.guhecloud.cube.srv.gen.visitor.management.server.utils.QRCodeUtil;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.param.EventLampCallbackParam;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.param.PassRecordParam;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.param.ReservationParam;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.param.ReservationRecordParam;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.param.VisitorStatusCallbackParam;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.result.PassRecordResult;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.result.ReservationRecordResult;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.result.ReservationResult;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.BeanUtils;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 第三方预约通行
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LampReservationPassServiceImpl implements LampReservationPassService {
    private final LampLoginService lampLoginService;
    private final SqlSessionFactory sqlSessionFactory;
    private final PassRecordMapper passRecordMapper;
    private final ReservationRecordMapper reservationRecordMapper;
    private final VisitorInviteRecordService visitorInviteRecordService;
    /**
     * 通行记录分页列表接口url
     */
    private static final String PASS_RECORD_PAGE_URL = "/evo-apigw/evo-visitor/1.0.0/card/accessControl/swingCardRecord/visitor/bycondition/combined";
    /**
     * 预约记录分页列表接口(预约人员信息)url
     */
    private static final String RESERVATION_RECORD_PAGE_URL = "/evo-apigw/evo-visitor/1.0.0/card/visitor/getVisitorData";
    /**
     * 预约记录详情url
     */
    private static final String RESERVATION_RECORD_DETAIL_URL = "/evo-apigw/evo-visitor/1.0.0/card/visitor/getVisitorSingleRecordById";
    /**
     * 修改预约状态url
     */
    private static final String CHANGE_RESERVATION_URL = "/evo-apigw/evo-visitor/1.0.0/card/visitor/changeBookStatusByIds";
    /**
     * 预约url
     */
    private static final String RESERVATION_URL = "/evo-apigw/evo-visitor/1.0.0/card/visitor/appointment";

    /**
     * 通行记录
     *
     * @param param 入参
     *
     * @return 记录
     */
    @Override
    public Pagination<PassRecordResult> passRecordPage(PassRecordParam param) {
        SystemLoginInfoPO loginInfoPO = lampLoginService.loginToken();
        Map<String, Object> map = new HashMap<>();
        map.put("pageNum", param.getPageNumber());
        map.put("pageSize", param.getPageSize());
        if (StringUtils.isNotBlank(param.getName())) {
            map.put("personName", param.getName());
        }
        if (param.getPeriod() != null) {
            map.put("startSwingTime", param.getPeriod().getBegin());
            map.put("endSwingTime", param.getPeriod().getEnd());
        }

        Object obj = LampVisitorHttpUtils.sendThirdPost(loginInfoPO.getBaseUrl() + PASS_RECORD_PAGE_URL, map, loginInfoPO);
        if (ObjectUtils.isEmpty(obj)) {
            return Pagination.empty();
        }
        JSONObject data = (JSONObject) obj;
        List<PassRecord> recordList = LampVisitorServiceUtils.getObjArrList(data, PassRecord.class);
        List<PassRecordResult> list = new ArrayList<>();
        for (PassRecord passRecord : recordList) {
            PassRecordResult result = new PassRecordResult();
            BeanUtils.copyProperties(passRecord, result);
            if (StringUtils.isNotBlank(passRecord.getSwingTime())) {
                result.setTime(DateUtils.parse(passRecord.getSwingTime()));
            }
        }
        return new Pagination<>(data.getLongValue("totalRows"), data.getLongValue("totalPage"), list);
    }

    /**
     * 预约
     *
     * @param param 入参
     *
     * @return 预约信息
     */
    @Override
    public ReservationResult reservation(ReservationParam param) {
        ReservationResult result = new ReservationResult();

        SystemLoginInfoPO loginInfoPO = lampLoginService.loginToken();
        Map<String, Object> map = new HashMap<>();
        map.put("userId", 1); // 跳过审核
        map.put("isv_id", getUserId(loginInfoPO, param.getUserId())); // 被访人id
        map.put("v_name", param.getName());
        map.put("v_phone", param.getPhone());
        map.put("v_dw", "单位");
        map.put("v_address", "住址");
        //来访人数
        Integer personNum = 1;
        if (CollectionUtils.isNotEmpty(param.getFollowVisitors())) {
            personNum = personNum + param.getFollowVisitors().size();
            List<Map<String, Object>> followVisitorsList = param.getFollowVisitors().stream()
                    .map(t -> {
                        Map<String, Object> followVisitor = new HashMap<>();
                        followVisitor.put("v_name", t.getFollowName());
                        followVisitor.put("v_phone", t.getFollowPhone());
                        followVisitor.put("v_certificateNumber", t.getFollowIdCard());
                        followVisitor.put("v_plateNumber", t.getFollowPlateNumber());
                        return followVisitor;
                    }).collect(Collectors.toList());

            map.put("followVisitors", followVisitorsList);
        }
        map.put("v_personSum", personNum);
        map.put("v_timeStr", DateUtils.format(param.getPeriod().getBegin()));
        map.put("v_lvTimeStr", DateUtils.format(param.getPeriod().getEnd()));
        map.put("visitorType", 1);
        map.put("secondVisitorType", loginInfoPO.getSecondVisitorType());
        map.put("areaIdList", param.getAreaId());
        map.put("v_reason", param.getReason());
        map.put("timeType", 1);//访问类型:1临时访问、2长期访问
        map.put("v_certificateNumber", param.getCertificateNumber());
        map.put("v_certificateType", "身份证");
        map.put("v_plateNumber", param.getPlateNumber());

        Object obj = LampVisitorHttpUtils.sendThirdPost(loginInfoPO.getBaseUrl() + RESERVATION_URL, map, loginInfoPO);
        Assert.isNotAcceptable(ObjectUtils.isEmpty(obj), "预约失败");
        JSONObject data = (JSONObject) obj;

        VisitorResData resData = data.toJavaObject(VisitorResData.class);
        result.setId(resData.getId());
        result.setBarCode(resData.getV_barCode());
        result.setAppointmentCode(resData.getAppointmentCode());
        if (StringUtils.isBlank(result.getBarCode())) {
            result.setBarCode(resData.v_barCode);
        }

        return result;
    }

    /**
     * 预约记录
     *
     * @param param 入参
     *
     * @return 记录列表
     *
     * @deprecated sdfsfsfdsf
     */
    @Override
    @Deprecated
    public Pagination<ReservationRecordResult> reservationRecordPage(ReservationRecordParam param) {
        SystemLoginInfoPO loginInfoPO = lampLoginService.loginToken();
        Map<String, Object> map = new HashMap<>();
        map.put("pageNum", param.getPageNumber());
        map.put("pageSize", param.getPageSize());
        if (param.getPeriod() != null) {
            map.put("comeVisit_startTime", DateUtils.format(param.getPeriod().getBegin()));
            map.put("comeVisit_endTime", DateUtils.format(param.getPeriod().getEnd()));
        }
        if (StringUtils.isNotBlank(param.getName())) {
            map.put("v_name", param.getName());
        }
        if (param.getStatusList() != null && !param.getStatusList().isEmpty()) {
            map.put("statusList", param.getStatusList());
        }
        Object obj = LampVisitorHttpUtils.sendThirdPost(loginInfoPO.getBaseUrl() + RESERVATION_RECORD_PAGE_URL, map, loginInfoPO);
        if (obj == null) {
            return Pagination.empty();
        }
        JSONObject data = (JSONObject) obj;
        List<ReservationRecord> reservationRecords = LampVisitorServiceUtils.getObjArrList(data, ReservationRecord.class);

        List<ReservationRecordResult> list = new ArrayList<>();
        for (ReservationRecord reservationRecord : reservationRecords) {
            ReservationRecordResult result = new ReservationRecordResult();
            result.setId(reservationRecord.getId());
            result.setName(reservationRecord.v_name);
            result.setStartTime(reservationRecord.v_time);
            result.setEndTime(reservationRecord.v_lvTime);
            result.setStartTime(DateUtils.parse(reservationRecord.v_timeStr));
            result.setEndTime(DateUtils.parse(reservationRecord.v_lvTimeStr));
            result.setStatus(reservationRecord.getStatus());
            result.setStatusName(reservationRecord.getStatusName());
        }

        return new Pagination<>(data.getLongValue("totalRows"), data.getLongValue("totalPage"), list);
    }

    /**
     * 查看预约码
     *
     * @param id 记录id（访客id）
     *
     * @return 预约码
     */
    @Override
    public String getReservationCode(Long id) {
        SystemLoginInfoPO loginInfoPO = lampLoginService.loginToken();
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        Object obj = LampVisitorHttpUtils.sendThirdGet(loginInfoPO.getBaseUrl() + RESERVATION_RECORD_DETAIL_URL, map, loginInfoPO);
        if (obj == null) {
            return "";
        }
        JSONObject data = (JSONObject) obj;
        ReservationRecord reservationRecord = data.toJavaObject(ReservationRecord.class);
        String qr = "";
        try {
            qr = Base64.getEncoder().encodeToString(QRCodeUtil.generateQRCode(reservationRecord.getV_barCode()).toByteArray());
        } catch (Exception e) {
            log.error("访客管理二维码转换失败！;{0}", e);
        }

        return "data:image/png;base64," + qr;
    }

    /**
     * 修改预约状态
     *
     * @param ids    预约id
     * @param status 状态 0：待审核 1：预约 2：在访 3：离访 4:预约取消
     */
    @Override
    public void updateReservation(String ids, Integer status) {
        SystemLoginInfoPO loginInfoPO = lampLoginService.loginToken();
        Map<String, Object> map = new HashMap<>();
        map.put("ids", ids);
        map.put("status", status);
        LampVisitorHttpUtils.sendThirdPost(loginInfoPO.getBaseUrl() + CHANGE_RESERVATION_URL, map, loginInfoPO);
    }

    /**
     * 访客业务事件回调
     *
     * @param param 入参
     */
    @Override
    public void visitorBusinessEventCallback(EventLampCallbackParam param) {
        if (param == null || param.getInfo() == null) {
            return;
        }
        // 通行记录
        String passEventStr = "visitor.record";
        // 访客状态变更
        String statusEventStr = "visitor.status";
        if (statusEventStr.equals(param.getMethod())) {
            this.reservationStatusCallback(param.getInfo().toJavaObject(VisitorStatusCallbackParam.class));
        } else if (passEventStr.equals(param.getMethod())) {
            this.passRecordCallback(param.getInfo().toJavaObject(PassRecordPO.class));
        }
    }

    /**
     * 预约状态回调
     *
     * @param callbackParam param
     */
    private void reservationStatusCallback(VisitorStatusCallbackParam callbackParam) {
        if (callbackParam == null || callbackParam.getVisitors() == null || callbackParam.getVisitors().isEmpty()) {
            return;
        }
        List<VisitorStatusChangeRecordPO> changeRecordPOList = callbackParam.getVisitors();
        /* 设置租户 */
        Long visitorId = changeRecordPOList.get(0).getId();
        this.setTenantByReservation(visitorId);
        // 关闭session的自动提交
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        try {
            // 插入记录
            VisitorStatusChangeRecordMapper recordMapper = sqlSession.getMapper(VisitorStatusChangeRecordMapper.class);
            changeRecordPOList.forEach(r -> {
                r.setReservationId(r.getId());
                r.setId(IdWorker.getId());
                recordMapper.insert(r);
                //使用或取消更新邀约记录
                if (StringUtils.equalsAny(r.getStatus(),
                                          ReservationStatusEnum.DEPARTURE.getStatus().toString(), ReservationStatusEnum.CANCELLATION
                                                  .getStatus().toString())) {
                    visitorInviteRecordService.lambdaUpdate()
                            .eq(VisitorInviteRecordPO::getReservationRecordId, r.getId())
                            .set(VisitorInviteRecordPO::getStatus, VisitorInviteStausEnum.EXPIRED.name());
                }
            });
            // 更新状态
            ReservationRecordMapper sqlSessionReservationMapper = sqlSession.getMapper(ReservationRecordMapper.class);
            changeRecordPOList.forEach(r -> {
                // 默认状态表示新增，不需要修改状态。(1.这里的0是默认状态不是待审核的意思，2.新增是预约接口插入数据不在这里插入。
                if (!String.valueOf(ReservationStatusEnum.TO_BE_REVIEWED.getStatus()).equals(r.getStatus())) {
                    sqlSessionReservationMapper.update(null, Wrappers.<ReservationRecordPO>lambdaUpdate()
                            .set(ReservationRecordPO::getStatus, r.getStatus())
                            .eq(ReservationRecordPO::getId, r.getReservationId()));
                }
            });
            // 提交数据
            sqlSession.commit();
        } catch (Exception e) {
            sqlSession.rollback();
            log.error("访客状态变更回调失败：", e);
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 通行记录回调
     *
     * @param passRecord 通行信息
     */
    private void passRecordCallback(PassRecordPO passRecord) {
        if (passRecord != null) {
            /* 设置租户 */
            Long visitorId = passRecord.getVisitorId();
            if (visitorId != null) {
                this.setTenantByReservation(visitorId);
            }
            // 插入数据
            passRecord.setId(IdWorker.getId());
            passRecordMapper.insert(passRecord);
        }

    }

    /**
     * 通过预约id设置租户
     *
     * @param visitorId 访客预约id
     */
    private void setTenantByReservation(Long visitorId) {
        if (visitorId != null) {
            ReservationRecordPO reservationPO = reservationRecordMapper.selectById(visitorId);
            if (reservationPO != null) {
                SessionContextHolder.requestTenant(reservationPO.getTenant());
            }
        }
    }

    /**
     * 获取人员id
     *
     * @param loginInfoPO 登录信息
     *
     * @return 人员id
     */
    private Long getUserId(SystemLoginInfoPO loginInfoPO, Long code) {
        String getUserUrl = "/evo-apigw/evo-brm/1.2.0/person/subsystem/page";
        Map<String, Object> map = new HashMap<>();
        List<Integer> statusList = new ArrayList<>();
        statusList.add(1);
        map.put("statusList", statusList);
        map.put("searchKey", code);
        Object obj = LampVisitorHttpUtils.sendThirdPost(loginInfoPO.getBaseUrl() + getUserUrl, map, loginInfoPO);
        if (obj == null) {
            return 2L;
        }
        JSONObject data = (JSONObject) obj;
        List<UserInfo> userInfos = LampVisitorServiceUtils.getObjArrList(data, UserInfo.class);
        if (!userInfos.isEmpty()) {
            return userInfos.get(0).getId();
        }
        // 获取全局用户id
        String getIdUrl = "/evo-apigw/evo-brm/1.0.0/person/generate-id";
        Map<String, Object> idMap = new HashMap<>();
        Object idObj = LampVisitorHttpUtils.sendThirdGet(loginInfoPO.getBaseUrl() + getIdUrl, idMap, loginInfoPO);
        if (idObj == null) {
            return 2L;
        }
        JSONObject idJson = (JSONObject) idObj;
        Long userId = idJson.getLong("id");

        // 添加人员
        String addUserUrl = "/evo-apigw/evo-brm/1.2.0/person/subsystem/add";
        Map<String, Object> addMap = new HashMap<>();
        addMap.put("service", "evo-thirdParty");
        addMap.put("id", userId);
        addMap.put("name", "默认人员");
        addMap.put("code", "MRRY001");
        addMap.put("paperType", -1);
        addMap.put("paperNumber", "111111111111111111");
        addMap.put("paperAddress", "证件地址");
        addMap.put("departmentId", 1L);
        LampVisitorHttpUtils.sendThirdPost(loginInfoPO.getBaseUrl() + addUserUrl, addMap, loginInfoPO);

        return userId;
    }

    /**
     * 人员类型id
     *
     * @param loginInfoPO 登录信息
     *
     * @return 人员类型id
     */
    private Long getUserTypeId(SystemLoginInfoPO loginInfoPO) {
        String url = "/evo-apigw/evo-visitor/1.0.0/visitor-type/page";
        Map<String, Object> map = new HashMap<>();
        map.put("pageSize", 1);
        map.put("pageNum", 10);
        Object obj = LampVisitorHttpUtils.sendThirdPost(loginInfoPO.getBaseUrl() + url, map, loginInfoPO);
        if (obj == null) {
            return 1L;
        }
        JSONObject data = (JSONObject) obj;
        List<UserType> list = LampVisitorServiceUtils.getObjArrList(data, UserType.class);
        if (!list.isEmpty()) {
            return list.get(0).getId();
        }
        return null;
    }

    @Data
    static class PassRecord {
        private Long id;
        private String deviceCode;
        private String deviceName;
        private String channelCode;
        private String channelName;
        private String swingTime;
        private String personName;
        private String personId;
        private String personCode;
        private String cardNumber;
        private Integer openType; //通行方式
        private Integer openResult; //开门结果：0 - 失败，1 - 成功
        private Integer cardStatus; //卡状态：0 - 正常，1 - 挂失
        private Integer enterOrExit; //进出门类型：0 - 开门，1 - 进门，2 - 出门

    }

    @Data
    static class UserInfo {
        private Long id;
        private String code;
        private String name;
    }

    @Data
    static class UserType {
        private Long id;
        private String typeName;
    }

    @Data
    static class VisitorResData {
        private Long id;
        private String v_barCode;
        private String appointmentCode;
    }

    @Data
    static class ReservationRecord {
        private Long id;
        private String v_name;
        private String v_phone;
        private Integer status;
        private String statusName;
        private LocalDateTime v_time; // 预约开始时间
        private LocalDateTime v_lvTime; // 预约结束时间
        private String v_timeStr; // 预约开始时间
        private String v_lvTimeStr; // 预约结束时间

        private String appointmentCode;
        private String v_barCode;
    }

}

package com.guhecloud.cube.biz.adapter.park.server.application.dahua.service;

import com.alibaba.fastjson2.JSON;
import com.dahuatech.hutool.http.Method;
import com.dahuatech.icc.oauth.model.v202010.GeneralResponse;
import com.dahuatech.icc.oauth.model.v202010.OauthConfigUserPwdInfo;
import com.dahuatech.icc.oauth.utils.HttpUtils;
import com.guhecloud.cube.biz.adapter.park.server.application.AbstractParkingService;
import com.guhecloud.cube.biz.adapter.park.server.application.dahua.model.DaHuaParkingLot;
import com.guhecloud.cube.biz.adapter.park.server.application.dahua.model.DaHuaResponse;
import com.guhecloud.cube.biz.adapter.park.server.application.dahua.model.PageModel;
import com.guhecloud.cube.biz.adapter.park.server.application.dahua.param.DaHuaParkingInRecordParam;
import com.guhecloud.cube.biz.adapter.park.server.application.dahua.param.DaHuaParkingOutRecordParam;
import com.guhecloud.cube.biz.adapter.park.server.application.dahua.utils.Utils;
import com.guhecloud.cube.biz.adapter.park.server.facade.event.ParkingInRecordReportEvent;
import com.guhecloud.cube.biz.adapter.park.server.facade.event.ParkingOutRecordReportEvent;
import com.guhecloud.cube.biz.adapter.park.server.facade.event.ParkingPayRecordReportEvent;
import com.guhecloud.cube.biz.adapter.park.server.facade.request.ParkingInRecordReportRequest;
import com.guhecloud.cube.biz.adapter.park.server.facade.request.ParkingOutRecordReportRequest;
import com.guhecloud.cube.biz.adapter.park.server.facade.request.ParkingPayRecordReportRequest;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingInRecordQueryParam;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingOutRecordQueryParam;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingPayRecordQueryParam;
import com.guhecloud.cube.biz.adapter.park.server.vo.DaHuaLoginInfoVO;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingInRecordVO;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingInfoVO;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingOutRecordVO;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingPayRecordVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.subsystem.config.constant.SubsystemTypeConstants;
import com.guhecloud.subsystem.config.listener.nacos.entity.SubsystemAdapterConfig;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * 大华停车场对接
 * 文档地址：<a href="https://open-icc.dahuatech.com/#/home?url=%3Fnav%3Dwiki%2Fipms%2Fparkinglot.html&version=enterprisebase/5.0.16&blank=true">...</a>
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DaHuaParkingService extends AbstractParkingService {
    private static final String PARKING_INFO_URL = "/evo-apigw/ipms/parkinglot/query";

    private final SubsystemRedisUtils subsystemRedisUtils;
    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * 获取车场信息
     *
     * @return 车场列表
     */
    @Override
    public List<ParkingInfoVO> getParkingInfo() throws UnsupportedEncodingException, URISyntaxException {
        List<ParkingInfoVO> result = new ArrayList<>();
        // 获取平台配置
        SubsystemAdapterConfig adapterConfig = this.getBasicConfig();
        // 处理登录信息
        DaHuaLoginInfoVO loginInfoVO = JsonUtils.deserialize(adapterConfig.getManufacturerJson(), DaHuaLoginInfoVO.class);
        OauthConfigUserPwdInfo oauthConfig = new OauthConfigUserPwdInfo(loginInfoVO.getHost(), loginInfoVO.getClientId(), loginInfoVO.getClientSecret(), loginInfoVO.getUsername(), loginInfoVO.getPassword(), loginInfoVO.getIsHttp(), loginInfoVO.getPort());
        int totalPage = 0;
        int currentPage = 1;
        int pageSize = 999;
        do {
            String url = PARKING_INFO_URL + "?pageNum=" + currentPage + "&pageSize=" + pageSize + "&returnPage=1";
            //执行请求
            PageModel pageModel = new PageModel();
            try {
                GeneralResponse response = HttpUtils.executeForm(url, null, null, Method.GET, oauthConfig, GeneralResponse.class);
                if (response.isSuccess() && "200".equals(response.getCode())) {
                    DaHuaResponse daHuaResponse = JsonUtils.deserialize(response.getResult(), DaHuaResponse.class);
                    if (Boolean.TRUE.equals(daHuaResponse.getSuccess()) && 200 == daHuaResponse.getCode()) {
                        pageModel = JsonUtils.deserialize(daHuaResponse.getData(), PageModel.class);
                    }
                }
            } catch (Exception e) {
                log.error("获取大华车场列表{}失败({})：{}", currentPage, JSON.toJSONString(loginInfoVO), e.getMessage());
            }
            if (pageModel != null && pageModel.getPageData() != null && !pageModel.getPageData().isEmpty()) {
                List<Object> objectList = JsonUtils.deserialize(pageModel.getPageData(), List.class);
                objectList.forEach(object -> result.add(this.daHuaParkingLotToGuHeParking(object)));
            }
            if (pageModel != null && pageModel.getTotalPage() != null) {
                totalPage = Integer.parseInt(pageModel.getTotalPage());
            }
            currentPage++;
        } while (currentPage <= totalPage);

        return result;
    }

    /**
     * 获取入场记录
     * 此方法不使用(数据来源方式为三方回调入库)
     *
     * @param param 入参对象
     *
     * @return 返回
     */
    @Deprecated
    @Override
    public List<ParkingInRecordVO> getParkingInRecords(ParkingInRecordQueryParam param) {
        return new ArrayList<>();
    }

    /**
     * 获取出场记录
     * 此方法不使用(数据来源方式为三方回调入库)
     *
     * @param param 入参对象
     *
     * @return 返回
     */
    @Deprecated
    @Override
    public List<ParkingOutRecordVO> getParkingOutRecords(ParkingOutRecordQueryParam param) {
        return new ArrayList<>();
    }

    /**
     * 获取缴费记录
     * 此方法不使用(数据来源方式为三方回调入库)
     *
     * @param param 入参对象
     *
     * @return 返回
     */
    @Deprecated
    @Override
    public List<ParkingPayRecordVO> getParkingPayRecords(ParkingPayRecordQueryParam param) {
        return new ArrayList<>();
    }

    /**
     * 大华车辆入场回调
     *
     * @param request     请求
     * @param tenant      租户
     * @param requestBody 内容
     *
     * @return 结果
     */
    public String daHuaParkingInRecord(HttpServletRequest request, Integer tenant, String requestBody) {
        log.info("大华车辆入场回调开始");
        String str = request.getHeader("User-Agent");
        log.info("大华车辆入场回调请求头参数:{}。内容:{}", str, requestBody);
        if (StringUtils.isBlank(str)) {
            log.error("大华车辆入场事件User-Agent is empty!");
            return Utils.getSuccessResult();
        }
        String[] strArr = str.split("_");
        if (strArr.length < 2) {
            log.error("大华车辆入场事件User-Agent长度为{}!", strArr.length);
            return Utils.getSuccessResult();
        }
        String md5 = strArr[3];
        DaHuaParkingInRecordParam param = null;
        try {
            int index = Integer.parseInt(String.valueOf(md5.charAt(Utils.getLastDigitIndex(md5))));
            if (index < 0) {
                log.error("大华车辆入场事件md5无数字!{}", md5);
                return Utils.getSuccessResult();
            }
            param = Utils.requestBodyToParkingRecord(tenant, md5, index, requestBody, DaHuaParkingInRecordParam.class);
            param.setDate(DateUtils.formatDate(DateUtils.parse(param.getArriveTime()
                                                                       .substring(0, param.getArriveTime().length() - 6), "yyyyMMdd")));
            param.setTime(DateUtils.formatDate(DateUtils.parse(param.getArriveTime()
                                                                       .substring(param.getArriveTime().length() - 6), "HHmmss")));
        } catch (Exception e) {
            log.error("大华车辆入场事件解密出错!md5:{}。入参：{}", md5, requestBody);
            e.printStackTrace();
        }
        if (param == null) {
            return Utils.getSuccessResult();
        }
        // 发送事件
        ParkingInRecordReportRequest inRecordReportRequest = ObjectUtils.copying(param, ParkingInRecordReportRequest.class);
        ParkingInRecordReportEvent parkingInRecordReportEvent = new ParkingInRecordReportEvent(this, inRecordReportRequest);
        this.applicationEventPublisher.publishEvent(parkingInRecordReportEvent);
        return Utils.getSuccessResult();
    }

    /**
     * 大华车辆出场回调
     *
     * @param request     请求
     * @param tenant      租户
     * @param requestBody 内容
     *
     * @return 结果
     */
    public String daHuaParkingOutRecord(HttpServletRequest request, Integer tenant, String requestBody) {
        log.info("大华车辆出场回调开始");
        String str = request.getHeader("User-Agent");
        log.info("大华车辆出场回调请求头参数:{}。内容:{}", str, requestBody);
        if (StringUtils.isBlank(str)) {
            log.error("大华车辆出场事件User-Agent is empty!");
            return Utils.getSuccessResult();
        }
        String[] strArr = str.split("_");
        if (strArr.length < 2) {
            log.error("大华车辆出场事件User-Agent长度为{}!", strArr.length);
            return Utils.getSuccessResult();
        }
        String md5 = strArr[3];
        DaHuaParkingOutRecordParam param = null;
        try {
            int index = Integer.parseInt(String.valueOf(md5.charAt(Utils.getLastDigitIndex(md5))));
            if (index < 0) {
                log.error("大华车辆入场事件md5无数字!{}", md5);
                return Utils.getSuccessResult();
            }
            param = Utils.requestBodyToParkingRecord(tenant, md5, index, requestBody, DaHuaParkingOutRecordParam.class);
            param.setDate(DateUtils.formatDate(
                                  DateUtils.parse(
                                          param.getLeaveTime().substring(0, param.getLeaveTime().length() - 6),
                                          "yyyyMMdd")
                                              )
                         );
            param.setTime(DateUtils.formatDate(DateUtils.parse(param.getLeaveTime()
                                                                       .substring(param.getLeaveTime().length() - 6), "HHmmss")));
        } catch (Exception e) {
            log.error("大华车辆出场事件解密出错!md5:{}。入参：{}", md5, requestBody);
        }
        if (param == null) {
            return Utils.getSuccessResult();
        }
        // 发送出场事件
        ParkingOutRecordReportRequest outRecordReportRequest = ObjectUtils.copying(param, ParkingOutRecordReportRequest.class);
        ParkingOutRecordReportEvent parkingOutRecordReportEvent = new ParkingOutRecordReportEvent(this, outRecordReportRequest);
        this.applicationEventPublisher.publishEvent(parkingOutRecordReportEvent);

        // 发送缴费事件
        if (StringUtils.isNotBlank(outRecordReportRequest.getParkingFee())) {
            ParkingPayRecordReportRequest payRecordReportRequest = ObjectUtils.copying(outRecordReportRequest, ParkingPayRecordReportRequest.class);
            payRecordReportRequest.setParkingFee(outRecordReportRequest.getParkingFee());
            payRecordReportRequest.setParkingDiscFee("0.00");
            ParkingPayRecordReportEvent parkingPayRecordReportEvent = new ParkingPayRecordReportEvent(this, payRecordReportRequest);
            this.applicationEventPublisher.publishEvent(parkingPayRecordReportEvent);
        }
        return Utils.getSuccessResult();
    }

    /**
     * 获取基础配置
     *
     * @return 基础配置
     */
    private SubsystemAdapterConfig getBasicConfig() {
        String key =
                SubsystemTypeConstants.PARKING_SERVER + ":" + "daHua" + ":" + SessionContextHolder.getRequester().getTenant().toString();
        return subsystemRedisUtils.getSubAdapterRefreshConfig(key);
    }

    /**
     * 大华车场对象转古河云车场对象
     *
     * @param daHuaParkingLot 大华车场对象
     *
     * @return 古河云车场对象
     */
    private ParkingInfoVO daHuaParkingLotToGuHeParking(Object daHuaParkingLot) {
        ParkingInfoVO vo = new ParkingInfoVO();
        if (daHuaParkingLot != null) {
            DaHuaParkingLot lot = JsonUtils.deserialize(daHuaParkingLot, DaHuaParkingLot.class);
            vo.setCode(lot.getParkingLotCode());
            vo.setName(lot.getParkingLot());
            vo.setTotalNum(lot.getTotalLot());
            vo.setTotalInNum(lot.getUsedLot());
            vo.setTotalSpaceNum(lot.getIdleLot());
        }
        return vo;
    }

}

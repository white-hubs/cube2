package com.guhecloud.cube.biz.adapter.park.server.application.jscloud.controller;

import com.guhecloud.cube.biz.adapter.park.server.application.jscloud.model.JsCloudBwListResult;
import com.guhecloud.cube.biz.adapter.park.server.application.jscloud.model.JsCloudResponse;
import com.guhecloud.cube.biz.adapter.park.server.application.jscloud.param.JsCloudBwListParam;
import com.guhecloud.cube.biz.adapter.park.server.application.jscloud.param.JsCloudParkInPushQo;
import com.guhecloud.cube.biz.adapter.park.server.application.jscloud.param.JsCloudParkInQo;
import com.guhecloud.cube.biz.adapter.park.server.application.jscloud.param.JsCloudParkInfoPushQo;
import com.guhecloud.cube.biz.adapter.park.server.application.jscloud.param.JsCloudParkInfoQo;
import com.guhecloud.cube.biz.adapter.park.server.application.jscloud.param.JsCloudParkChargePushQo;
import com.guhecloud.cube.biz.adapter.park.server.application.jscloud.param.JsCloudParkChargeQo;
import com.guhecloud.cube.biz.adapter.park.server.application.jscloud.param.JsCloudParkOutPushQo;
import com.guhecloud.cube.biz.adapter.park.server.application.jscloud.param.JsCloudParkOutQo;
import com.guhecloud.cube.biz.adapter.park.server.application.jscloud.service.JsCloudParkingService;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingSubscribeParam;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingInfoVO;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * 捷顺云停车场接口
 * 参考文档: https://jsopen.jslife.com.cn/cloudopen/index.html#/documentCenter?id=241
 * 黑名单接口: 文档 id=457，签名见对接说明图2，黑白灰名单见图3第一点
 *
 * @author appet
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JsCloudParkingController {
    private final JsCloudParkingService jsCloudParkingService;

    /**
     * 获取车场信息
     *
     * @return
     *
     * @throws Exception
     */
    @GetMapping("/jscloud/parking-info")
    public List<ParkingInfoVO> getParkingInfo() throws UnsupportedEncodingException, URISyntaxException {
        return this.jsCloudParkingService.getParkingInfo();
    }

    /**
     * 车场数据订阅
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/jscloud/parking-subscribe")
    public Boolean subscribe(@RequestBody @Valid ParkingSubscribeParam param) {
        return this.jsCloudParkingService.parkingSubscribe(param);
    }

    /**
     * 车场数据取消订阅
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/jscloud/parking-un-subscribe")
    public Boolean unSubscribe(@RequestBody @Valid ParkingSubscribeParam param) {
        return this.jsCloudParkingService.parkingUnSubscribe(param);
    }

    /**
     * 车位信息上报，保存到 Redis
     * 接口协议: 数据中心平台主动推送车位数据到第三方平台
     * 接收地址: http://serverUrl/park/parkSpace
     * 请求方式: POST
     *
     * @param tenant 租户
     * @param param  请求体，包含 dataItems 车场车位信息列表
     *
     * @return resultCode: 返回码(200成功), message: 返回信息
     */
    @PostMapping("/public/jscloud/{tenant}/park/parkSpace")
    public JsCloudResponse reportParkingInfoRecord(@PathVariable Integer tenant,
                                                   @Valid @RequestBody JsCloudParkInfoPushQo param
                                                  ) {
        log.info("捷顺云停车场，车位上报：{}", param);

        var dataItems = JsonUtils.deserializeToList(param.getDataItems(), JsCloudParkInfoQo.class);
        if (CollectionUtils.isEmpty(dataItems)) {
            throw new ServiceException("捷顺云停车场，车位上报数据为空");
        }
        return this.jsCloudParkingService.reportParkingInfoRecordService(dataItems, tenant);
    }

    /**
     * 入场记录上报
     * 接口协议: 有车辆入场时，数据中心平台主动推送入场数据到第三方平台
     * 接收地址: http://serverUrl/park/parkIn
     * 请求方式: POST
     *
     * @param tenant 租户
     * @param param  请求体，包含 dataItems 入场记录列表
     *
     * @return resultCode: 返回码(200成功), message: 返回信息
     */
    @PostMapping("/public/jscloud/{tenant}/park/parkIn")
    public JsCloudResponse reportParkingInRecord(@PathVariable Integer tenant,
                                                 @Valid @RequestBody JsCloudParkInPushQo param
                                                ) {
        log.info("捷顺云停车场，入场记录上报：{}", param);

        var dataItems = JsonUtils.deserializeToList(param.getDataItems(), JsCloudParkInQo.class);
        if (CollectionUtils.isEmpty(dataItems)) {
            throw new ServiceException("捷顺云停车场，入场记录数据为空");
        }
        return this.jsCloudParkingService.reportParkingInRecordService(dataItems, tenant);
    }

    /**
     * 出场记录上报
     * 接口协议: 有车辆出场时，数据中心平台主动推送出场数据到第三方平台
     * 接收地址: http://serverUrl/park/parkOut
     * 请求方式: POST
     *
     * @param tenant 租户
     * @param param  请求体，包含 dataItems 出场记录列表
     * @return resultCode: 返回码(200成功), message: 返回信息
     */
    @PostMapping("/public/jscloud/{tenant}/park/parkOut")
    public JsCloudResponse reportParkingOutRecord(@PathVariable Integer tenant,
                                                  @Valid @RequestBody JsCloudParkOutPushQo param) {
        log.info("捷顺云停车场，出场记录上报：{}", param);

        var dataItems = JsonUtils.deserializeToList(param.getDataItems(), JsCloudParkOutQo.class);
        if (CollectionUtils.isEmpty(dataItems)) {
            throw new ServiceException("捷顺云停车场，出场记录数据为空");
        }
        return this.jsCloudParkingService.reportParkingOutRecordService(dataItems, tenant);
    }

    /**
     * 收费记录上报（支付信息）
     * 接口协议: 数据中心平台主动推送收费记录数据到第三方平台，包含线上线下临停收费记录
     * 接收地址: http://serverUrl/park/parkCharge
     * 请求方式: POST
     *
     * @param tenant 租户
     * @param param  请求体，包含 dataItems 收费记录列表（JSON 字符串）
     * @return resultCode: 返回码(200成功), message: 返回信息
     */
    @PostMapping("/public/jscloud/{tenant}/park/parkCharge")
    public JsCloudResponse reportParkingChargeRecord(@PathVariable Integer tenant,
                                                     @Valid @RequestBody JsCloudParkChargePushQo param) {
        log.info("捷顺云停车场，收费记录上报：{}", param);

        var dataItems = JsonUtils.deserializeToList(param.getDataItems(), JsCloudParkChargeQo.class);
        if (CollectionUtils.isEmpty(dataItems)) {
            throw new ServiceException("捷顺云停车场，收费记录数据为空");
        }
        return this.jsCloudParkingService.reportParkingChargeRecordService(dataItems, tenant);
    }

    /**
     * 黑白灰名单接口（对接捷顺云，method=tq.jzgk.monthCardApi.addCarBlack）
     * 请求参数严格按文档：parkCode、carNo(带横杠如粤-B88888)、carNoColor、type(0黑1白2灰)、beginTime、endTime、isDeleted、remark
     * 响应严格按文档：request_id、code、msg、biz_response(respCode、respMsg、success)
     */
    @PostMapping("/jscloud/blacklist")
    public JsCloudBwListResult addCarBlack(@RequestBody @Valid JsCloudBwListParam param) {
        return jsCloudParkingService.addCarBlack(param);
    }
}

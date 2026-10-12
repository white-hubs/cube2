package com.guhecloud.cube.biz.adapter.park.server.application.reformer.controller;

import com.guhecloud.cube.biz.adapter.park.server.application.reformer.config.ReformerConfig;
import com.guhecloud.cube.biz.adapter.park.server.application.reformer.param.ReformerParkingCarChargeParam;
import com.guhecloud.cube.biz.adapter.park.server.application.reformer.param.ReformerParkingInOutRecordReportParam;
import com.guhecloud.cube.biz.adapter.park.server.application.reformer.service.ReformerParkingService;
import com.guhecloud.cube.biz.adapter.park.server.application.reformer.vo.ReformerParkingResult;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingSubscribeParam;
import com.guhecloud.cube.biz.adapter.park.server.utils.AESUtils;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingInfoVO;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.subsystem.config.constant.SubsystemTypeConstants;
import com.guhecloud.subsystem.config.listener.nacos.entity.SubsystemAdapterConfig;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import jakarta.servlet.http.HttpServletRequest;
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
 * 立方停车场接口
 *
 * @author xiaofeng
 * @date 2024/09/11 16:45
 * {
 * "url":"http://127.0.0.1:8080/serviceName",
 * "aesKey":"cmVmb3JtZXJyZWZvcm1lcg=="
 * "stationNoList":"999997,999998"
 * }
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ReformerParkingController {
    private final ReformerParkingService reformerParkingService;

    private final SubsystemRedisUtils subsystemRedisUtils;

    /**
     * 获取车场信息
     */
    @GetMapping("/reformer/parking-info")
    public List<ParkingInfoVO> getIntercityParkingInfo() throws UnsupportedEncodingException, URISyntaxException {
        return this.reformerParkingService.getParkingInfo();
    }

    /**
     * 车场数据订阅
     */
    @PostMapping("/reformer/parking-subscribe")
    public Boolean subscribe(@RequestBody @Valid ParkingSubscribeParam param) {
        return this.reformerParkingService.parkingSubscribe(param);
    }

    /**
     * 车场数据取消订阅
     */
    @PostMapping("/reformer/parking-un-subscribe")
    public Boolean unSubscribe(@RequestBody @Valid ParkingSubscribeParam param) {
        return this.reformerParkingService.parkingUnSubscribe(param);
    }

    /**
     * 上报车场进出记录
     */
    @PostMapping("/public/reformer/report/{tenant}/parkingOutInRecord")
    public ReformerParkingResult reportParkingOutInRecord(@PathVariable Integer tenant,
                                                          @RequestBody String reportDataEncrypted,
                                                          HttpServletRequest request
                                                         ) {
        String ipAddr = request.getRemoteAddr();
        log.info("立方停车场，进场调用者IP地址：" + ipAddr);
        log.info("立方停车场，进出记录上报：" + reportDataEncrypted);
        ReformerParkingInOutRecordReportParam reportData = new ReformerParkingInOutRecordReportParam();
        try {
            SubsystemAdapterConfig subAdapterRefreshConfig = this.getBasicConfig();

            ReformerConfig config = JsonUtils.deserialize(subAdapterRefreshConfig.getManufacturerJson(), ReformerConfig.class);

            byte[] decryptData = AESUtils.decrypt(AESUtils.parseHexStr2Byte(reportDataEncrypted), config.getAesKey().getBytes());
            String reportDataJson = new String(decryptData, "utf-8");
            reportData = JsonUtils.deserialize(reportDataJson, ReformerParkingInOutRecordReportParam.class);
        } catch (Exception e) {
            log.error("立方停车场，进出记录上报,解码异常", e);
            return ReformerParkingResult.builder().resCode(500).resMsg("立方停车场，进出记录上报,解码异常").build();
        }
        try {
            this.reformerParkingService.reportParkingOutInRecord(reportData, tenant);
            return ReformerParkingResult.builder().resCode(0).resMsg("成功").build();
        } catch (Exception e) {
            log.error("立方停车场-进入场上报，异常：", e);
            return ReformerParkingResult.builder().resCode(500).resMsg("失败").build();
        }
    }

    /**
     * 上报车场缴费记录
     */
    @PostMapping("/public/reformer/report/{tenant}/parkingCarCharge")
    public ReformerParkingResult reportParkingCarCharge(@PathVariable Integer tenant,
                                                        @RequestBody String reportDataEncrypted,
                                                        HttpServletRequest request
                                                       ) {
        String ipAddr = request.getRemoteAddr();
        log.info("立方停车场，缴费记录调用者IP地址：" + ipAddr);
        log.info("立方停车场，缴费记录上报：" + reportDataEncrypted);
        ReformerParkingCarChargeParam reportData = new ReformerParkingCarChargeParam();
        try {
            SubsystemAdapterConfig subAdapterRefreshConfig = this.getBasicConfig();

            ReformerConfig config = JsonUtils.deserialize(subAdapterRefreshConfig.getManufacturerJson(), ReformerConfig.class);

            byte[] decryptData = AESUtils.decrypt(AESUtils.parseHexStr2Byte(reportDataEncrypted), config.getAesKey().getBytes());
            String reportDataJson = new String(decryptData, "utf-8");
            reportData = JsonUtils.deserialize(reportDataJson, ReformerParkingCarChargeParam.class);

        } catch (Exception e) {
            log.error("立方停车场，缴费记录上报,解码异常", e);
            return ReformerParkingResult.builder().resCode(500).resMsg("立方停车场，缴费记录上报,解码异常").build();
        }
        try {
            this.reformerParkingService.reportParkingCarCharge(reportData, tenant);
            return ReformerParkingResult.builder().resCode(0).resMsg("成功").build();
        } catch (Exception e) {
            log.error("立方停车场-缴费记录上报，异常：", e);
            return ReformerParkingResult.builder().resCode(500).resMsg("失败").build();
        }
    }

    /**
     * 获取基础配置
     *
     * @return
     */
    private SubsystemAdapterConfig getBasicConfig() {
        String key =
                SubsystemTypeConstants.PARKING_SERVER + ":" + "reformer" + ":" + SessionContextHolder.getRequester().getTenant().toString();
        return subsystemRedisUtils.getSubAdapterRefreshConfig(key);
    }
}

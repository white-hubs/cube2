package com.guhecloud.subsystem.requester.sdk.client;

import com.guhecloud.subsystem.config.constant.SubsystemTypeConstants;
import com.guhecloud.subsystem.requester.sdk.config.DynamicConfiguration;
import com.guhecloud.subsystem.requester.sdk.param.parking.ParkingBlackParam;
import com.guhecloud.subsystem.requester.sdk.param.parking.ParkingInRecordQueryParam;
import com.guhecloud.subsystem.requester.sdk.param.parking.ParkingOutRecordQueryParam;
import com.guhecloud.subsystem.requester.sdk.param.parking.ParkingPayRecordQueryParam;
import com.guhecloud.subsystem.requester.sdk.param.parking.ParkingPayTotalQueryParam;
import com.guhecloud.subsystem.requester.sdk.param.parking.ParkingSubscribeParam;
import com.guhecloud.subsystem.requester.sdk.vo.parking.ParkingBlackResultVO;
import com.guhecloud.subsystem.requester.sdk.vo.parking.ParkingInRecordVO;
import com.guhecloud.subsystem.requester.sdk.vo.parking.ParkingInfoVO;
import com.guhecloud.subsystem.requester.sdk.vo.parking.ParkingOutRecordVO;
import com.guhecloud.subsystem.requester.sdk.vo.parking.ParkingPayRecordVO;
import com.guhecloud.subsystem.requester.sdk.vo.parking.ParkingPayTotalVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author lanchun
 * @date 2022/9/14 14:14
 */
@FeignClient(name = SubsystemTypeConstants.PARKING_SERVER, configuration = DynamicConfiguration.class)
public interface ParkingClient {

    /**
     * 获取车场信息
     *
     * @return
     */
    @GetMapping("/parking-info")
    List<ParkingInfoVO> getParkingInfo();

    /**
     * 获取车场缴费统计
     *
     * @return
     */
    @GetMapping("/parking-pay-total")
    ParkingPayTotalVO getParkingPayTotal(ParkingPayTotalQueryParam param);

    /**
     * 车场数据订阅
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/parking-subscribe")
    Boolean subscribe(@RequestBody ParkingSubscribeParam param);

    /**
     * 车场数据取消订阅
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/parking-un-subscribe")
    Boolean unSubscribe(@RequestBody ParkingSubscribeParam param);

    /**
     * 获取车辆入场记录列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/parking-in-records")
    List<ParkingInRecordVO> getParkingInRecords(@SpringQueryMap ParkingInRecordQueryParam param);

    /**
     * 获取车辆出场记录列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/parking-out-records")
    List<ParkingOutRecordVO> getParkingOutRecords(@SpringQueryMap ParkingOutRecordQueryParam param);

    /**
     * 获取车辆缴费记录列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/parking-pay-records")
    List<ParkingPayRecordVO> getParkingPayRecords(@SpringQueryMap ParkingPayRecordQueryParam param);

    /**
     * 下发黑白灰名单
     *
     * @param param 黑白灰名单参数
     *
     * @return 下发结果
     */
    @PostMapping("/blacklist")
    ParkingBlackResultVO addParkingBlack(@RequestBody ParkingBlackParam param);

}

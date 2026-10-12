package com.guhecloud.cube.srv.gen.visitor.management.client;

import com.guhecloud.cube.srv.gen.visitor.management.client.param.ReservationRecordParam;
import com.guhecloud.cube.srv.gen.visitor.management.client.vo.ReservationRecordResult;
import com.guhecloud.platform.base.model.Pagination;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/12/9 9:33
 * @Description: 预约记录feign
 */
@FeignClient(name = "visitorManagement")
public interface ReservationRecordClient {

    /**
     * 获取预约记录的人信息
     */
    @GetMapping("/visitor/reservation/personnel/page")
    Pagination<ReservationRecordResult> reservationPersonnelPage(@SpringQueryMap ReservationRecordParam param);

    /**
     * 获取预约人数
     */
    @GetMapping("/visitor/reservation/count")
    Integer reservationRecordCount(@SpringQueryMap ReservationRecordParam param);

}
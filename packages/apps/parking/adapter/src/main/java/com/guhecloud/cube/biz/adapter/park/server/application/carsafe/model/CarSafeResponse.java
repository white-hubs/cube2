package com.guhecloud.cube.biz.adapter.park.server.application.carsafe.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarSafeResponse {
    private Boolean status;
    private String message;
    // 200-成功
    //500-失败
    //510-缴费订单已超时或取消，需要重新下单计费
    //511-交易重复，需要重新下单计费
    //520-上传车安通失败
    //530-受访者不存在
    //531-受访者不唯一，请重新选定条件
    //532-访客在黑名单中（证件号或手机号在访客信息中被禁用）
    //100001-传入参数格式错误
    //100002-系统异常
    //100003-认证错误
    private int code;
    private List<ParkingInfo> data;
    private Integer rowindex;
    private Integer total;
    private String time;

    @Data
    public static class ParkingInfo {
        //区域ID
        private String areaID;
        //区域名称
        private String areaName;
        //总车位数
        private String total;
        //剩余车位数
        private String remain;
    }
}

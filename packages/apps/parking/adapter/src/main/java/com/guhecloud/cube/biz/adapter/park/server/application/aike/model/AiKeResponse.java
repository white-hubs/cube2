package com.guhecloud.cube.biz.adapter.park.server.application.aike.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiKeResponse {
    private int status;
    //只有状态码为2时才生效，状态码不为2时业务结果为0;
    // 901-系统发生异常,902-远程服务器未响应,903-运营商不存在,904-停车场不存在,905-未找到在场车辆,906-账单不存在,907-账单已同步,908-其它错误,909-请求参数错误,910-找不到授权商下面的停车场,911-无权限操作该停车场,912-查费已超时，请重新查费,1801-找不到指定车辆
    private int resultCode;
    private String message;

    //停车场信息返回对象
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ParkingListResponse extends AiKeResponse {
        private ParkingData data;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ParkingData {
        private Integer total;
        private List<ParkingInfo> parkList;
    }

    @Data
    public static class ParkingInfo {
        private String parkCode;//车场编号
        private String parkName;//车场名称
        private String address;//车场地址
        private String longitude;//经度
        private String latitude;//纬度
        private String desc;//描述
        private Integer totalCarports;//总车位数
        private Integer eptCarports;//空车位数
    }
}

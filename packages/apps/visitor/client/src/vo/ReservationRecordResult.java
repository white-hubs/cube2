package com.guhecloud.cube.srv.gen.visitor.management.client.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/12/9 11:51
 * @Description:
 */

@Data
public class ReservationRecordResult implements Serializable {

    private static final long serialVersionUID = -8800024276038227530L;
    private Long id;
    private String name;
    private String phone;
    private String dw; // 单位
    private String address; // 住址
    private Integer personSum; // 来访人数
    private String reason; // 来访理由（非必填）
    private String faceFile; // 人脸图片（非必填） base64，须加前缀data:image/jpeg;base64,图片大小5k~4M,500*500,五官清晰

    private Long areaId; // 预约区域id
    private String areaCode;
    private String areaName;
    private LocalDateTime startTime; // 预约开始时间
    private LocalDateTime endTime; // 预约结束时间
    private String dayStr; // 预约日期字符串
    private String timeStr; // 预约时段字符串

    private Integer status;
    private String statusName; // 预约状态名称
    private String appointmentCode; //预约码
    private LocalDateTime createTime;

}

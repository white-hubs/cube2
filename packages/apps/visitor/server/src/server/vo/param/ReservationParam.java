package com.guhecloud.cube.srv.gen.visitor.management.server.vo.param;

import com.guhecloud.cube.srv.gen.visitor.management.server.flow.po.FollowVisitorPO;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.List;

/**
 * 预约入参
 */
@Data
public class ReservationParam {

    private static final long serialVersionUID = 6435243049651951526L;
    @NotNull(message = "预约区域不能为空")
    private List<Long> areaId; // 预约区域id
    private String areaCode; // 预约区域code
    private String areaName; // 预约区域名称
    @NotNull(message = "预约时段不能为空")
    private Period period;
    @NotEmpty(message = "预约人名称不能为空")
    private String name; // 预约人名称
    @NotEmpty(message = "预约人手机不能为空")
    @Pattern(regexp = "^1[3|4|5|7|8|9][0-9]{9}$", message = "手机号格式不正确！")
    private String phone; // 预约人手机

    private String dayStr; // 预约日期字符串
    /**
     * 预约时段字符串
     */
    private String timeStr;
    /**
     * 跨天标识
     */
    private Boolean nextDayBool;
    private String reason; // 来访理由
    private String dw; // 来访人单位
    private Integer personSum; // 来访人数
    private Integer certificateType; // 证件类型
    private String certificateNumber; // 证件号
    private String faceFile; // 人脸图片base64
    private Long faceFileId; // 人脸图片文件id
    private Boolean auditBool; // 只有为true才需要审核
    private String plateNumber;
    private Long userId;//被访人id
    private List<FollowVisitorPO> followVisitors;
}

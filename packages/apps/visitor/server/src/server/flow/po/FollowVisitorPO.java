package com.guhecloud.cube.srv.gen.visitor.management.server.flow.po;

import annotation.FormField;
import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/2/19 11:19
 * @Description: 随访人员
 */
@Data
public class FollowVisitorPO {
    @FormField(label = "姓名")
    private String followName;
    @FormField(label = "电话")
    private String followPhone;
    @FormField(label = "身份证号")
    private String followIdCard;
    @FormField(label = "车牌号码")
    private String followPlateNumber;
}

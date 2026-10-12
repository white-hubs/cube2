package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.dto;

import annotation.FormField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe 隐患排查2.0
 * @date 2023/11/8 20:59
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class HiddenDangerNewDTO {
    @FormField(label = "对象类型")
    private String objType;
    @FormField(label = "隐患对象")
    private HiddenObj hiddenObj;
    @FormField(label = "排查对象")
    private String checkObj;
    @FormField(label = "隐患类型")
    private Long hiddenType;
    @FormField(label = "隐患级别")
    private Long urgencyLevel;
    @FormField(label = "隐患描述")
    private String hiddenDescribe;
    @FormField(label = "确认结果")
    private String confirm;
    @FormField(label = "处理描述")
    private String confirmDescribe;
    @FormField(label = "解决结果")
    private String result;
    @FormField(label = "解决描述")
    private String solveDescribe;

    @Data
    public static class HiddenObj {
        private Long id;
        private String name;
        private String type;
    }
}

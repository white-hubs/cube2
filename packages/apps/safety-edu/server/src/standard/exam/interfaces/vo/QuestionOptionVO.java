package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo;

import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/11/26 10:30
 * @Description: 考题选项模型
 */
@Data
public class QuestionOptionVO {

    /**
     * 序号
     */
    private Integer sort;

    /**
     * 选项标题
     */
    private String optionTitle;

    /**
     * 选项Id
     */
    private String optionID;

    /**
     * 是否正确
     */
    private Boolean isRight = false;

    /**
     * 是否校验
     */
    private Boolean isCheck = false;
}

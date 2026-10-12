package com.guhecloud.cube.srv.gen.maintain.dto;

import com.guhecloud.cube.srv.gen.maintain.enumeration.HiddenDangerStatusEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
public class HiddenDangerCommandDTO {
    /**
     * 隐患单号
     */
    private String code;

    /**
     * 隐患名称
     */
    private String name;

    /**
     * 隐患对象主键
     */
    @NotNull(message = "隐患对象主键不能为空")
    private Long hiddenObjId;

    /**
     * 隐患对象名称
     */
    @NotEmpty(message = "隐患对象名称不能为空")
    private String hiddenObjName;

    /**
     * 隐患对象类别
     */
    @NotEmpty(message = "隐患对象类别不能为空")
    private String hiddenObjType;

    /**
     * 提交人
     */
    private String submitter;

    /**
     * 提交时间
     */
    private LocalDateTime submitTime;

    /**
     * 状态
     */
    private HiddenDangerStatusEnum status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 文件图片主键集合
     */
    private Collection<Long> fileIds;
}

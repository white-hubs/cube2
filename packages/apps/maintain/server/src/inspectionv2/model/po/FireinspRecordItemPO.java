package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 巡检项记录
 * </p>
 *
 * @author zwb
 * @since 2021-09-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("fireinsp_record_item")
public class FireinspRecordItemPO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String id;

    /**
     * 项目ID
     */
    private String projectId;

    /**
     * 记录ID
     */
    private String recordId;

    /**
     * 标准ID
     */
    private String standardId;

    /**
     * 标准名
     */
    private String standardName;

    /**
     * 标准类型
     */
    private String standardType;

    /**
     * 标准目标类型
     */
    private String standardTargetType;

    /**
     * 巡检检查项名称
     */
    private String name;

    /**
     * 巡检项结果类型
     */
    private String resultType;

    /**
     * 巡检项结果
     */
    private String result;

    /**
     * 结果正常异常
     */
    private Boolean status;

    /**
     * 时间
     */
    private LocalDateTime time;

    /**
     * 自定义数据
     */
    private String config;

}

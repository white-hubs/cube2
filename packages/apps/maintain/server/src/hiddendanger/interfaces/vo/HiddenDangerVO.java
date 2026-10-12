package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.enumeration.HiddenDangerConfirmEnum;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.enumeration.HiddenDangerStatusEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lzy
 * @date 2022/4/26
 */
@Data
public class HiddenDangerVO {

    /**
     * 主键
     */
    private Long id;
    /**
     * 流程实例id
     */
    private String processInsId;
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
    private Long hiddenObjId;

    /**
     * 隐患对象名称
     */
    private String hiddenObjName;

    /**
     * 隐患对象类别
     */
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
     * 隐患分类主键
     */
    private Long hiddenDangerCategoryId;

    /**
     * 隐患分类名称
     */
    private String hiddenDangerCategoryName;

    /**
     * 隐患程度主键
     */
    private Long hiddenDangerLevelId;

    /**
     * 隐患程度名称
     */
    private String hiddenDangerLevelName;

    /**
     * 状态
     */
    private HiddenDangerStatusEnum status;

    /**
     * 隐患流程全状态
     */
    private String hiddenDangerStatus;

    /**
     * 全程处理结果
     */
    private String result;

    /**
     * 确认结果
     */
    private HiddenDangerConfirmEnum confirmResult;

    /**
     * 备注
     */
    private String remark;

    /**
     * 文件图片集合
     */
    private List<FileVO> files;

    /**
     * 是否有确认按钮
     */
    private Boolean isButton;

    /**
     * 任务主键
     */
    private String taskId;

    //工单Id
    private Long tId;

    //确认信息
    private HiddenDangerConfirmVO hiddenDangerConfirmVO;

    //工单信息
    private HiddenDangerTicketVO ticketVO;

    //关闭信息
    private HiddenDangerClosedVO closedByHiddenDanger;

    private Integer tenant;

    //隐患管理
    private String manager;

}

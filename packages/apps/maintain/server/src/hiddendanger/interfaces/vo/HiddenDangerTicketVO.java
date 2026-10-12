package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.vo;

import com.guhecloud.cube.srv.gen.maintain.server.common.enumeration.TicketStatusEnum;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.enumeration.HiddenDangerResultEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author lzy
 * @date 2022/4/28
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HiddenDangerTicketVO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 任务主键
     */
    private String taskId;

    /**
     * 隐患单号
     */
    private String code;

    /**
     * 隐患名称
     */
    private String name;

    /**
     * 工单主键
     */
    private Long ticketId;

    /**
     * 工单编号
     */
    private String ticketCode;

    /**
     * 隐患对象主键
     */
    private Long hiddenObjId;

    /**
     * 隐患对象名称
     */
    private String hiddenObjName;

    /**
     * 隐患分类主键
     */
    private Long hiddenDangerCategoryId;

    /**
     * 隐患分类名称
     */
    private String hiddenDangerCategoryName;

    /**
     * 隐患类型主键
     */
    private Long hiddenDangerTypeId;

    /**
     * 隐患类型信息
     */
    private String hiddenDangerTypeName;

    /**
     * 隐患程度主键
     */
    private Long hiddenDangerLevelId;

    /**
     * 隐患程度名称
     */
    private String hiddenDangerLevelName;

    /**
     * 提交时间
     */
    private LocalDateTime submitTime;

    /**
     * 工单状态
     */
    private TicketStatusEnum status;

    /**
     * 处理结果
     */
    private HiddenDangerResultEnum result;
    /**
     * 是否有处理按钮
     */
    private Boolean isTicketButton;

    /**
     * 是否关闭按钮
     */
    private Boolean isClosedButton;

}

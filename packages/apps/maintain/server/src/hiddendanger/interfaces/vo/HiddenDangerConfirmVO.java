package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lzy
 * @date 2022/4/27
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HiddenDangerConfirmVO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 工单主键
     */
    private Long ticketId;

    /**
     * 工单编号
     */
    private String ticketCode;

    /**
     * 隐患单主键
     */
    private Long hiddenDangerId;

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
     * 隐患类型名称
     */
    private String hiddenDangerTypeName;

    /**
     * 隐患级别主键
     */
    private Long hiddenDangerLevelId;

    /**
     * 隐患级别名称
     */
    private String hiddenDangerLevelName;

    /**
     * 确认结果
     */
    private String confirmResult;

    /**
     * 确认时间
     */
    private LocalDateTime confirmTime;

    /**
     * 排查时限
     */
    private Integer limitTime;

    /**
     * 排查时间类别
     */
    private String limitTimeType;

    /**
     * 备注
     */
    private String remark;

    /**
     * 确认人员主键
     */
    private List<Long> userIds;

    /**
     * 确认人员名称
     */
    private List<String> userNames;

    /**
     * 文件图片集合
     */
    private List<FileVO> files;

    /**
     * 是否有确认按钮
     */
    private Boolean isButton;

}

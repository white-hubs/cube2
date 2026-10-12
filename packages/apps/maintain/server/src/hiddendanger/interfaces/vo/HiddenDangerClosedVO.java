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
 * @date 2022/4/29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HiddenDangerClosedVO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 隐患单主键
     */
    private Long hiddenDangerId;

    /**
     * 解决结果
     */
    private String result;

    /**
     * 关闭人
     */
    private String closedPerson;

    /**
     * 关闭时间
     */
    private LocalDateTime closedTime;

    /**
     * 描述信息
     */
    private String remark;

    /**
     * 文件信息
     */
    private List<FileVO> files;

    /**
     * 用户主键集合
     */
    private List<Long> userIds;

    /**
     * 用户名称集合
     */
    private List<String> userNames;

    /**
     * 是否有关闭按钮
     */
    private Boolean isColsedButton;
}

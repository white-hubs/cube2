package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/3/18 19:24
 * @Description:
 */

@Data
public class InspectionRecordAnalysisVO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 时间
     */
    private LocalDateTime time;

    /**
     * 对象名称
     */
    private String targetName;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 执行人
     */
    private String nickname;

    /**
     * 结果
     */
    private String result;

    /**
     * 异常项id
     */
    private Long errorItemId;

    /**
     * 图片
     */
    private FileVO fileVO;

    /**
     * 异常项
     */
    private String errorItem;
}

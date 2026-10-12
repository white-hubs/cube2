package com.guhecloud.cube.srv.gen.loose.works.dto.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.srv.gen.loose.works.enumeration.ProjectInspectResultEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/14 9:38
 * @Description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectInspectRecordVO {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 记录名称
     */
    private String name;

    /**
     * 巡查人
     */
    private String userName;

    /**
     * 完成时间
     */
    private LocalDateTime completeTime;

    /**
     * 巡查工程名称
     */
    private String projectName;

    /**
     * 巡查结果
     */
    private ProjectInspectResultEnum result;

    /**
     * 巡查结果描述
     */
    private String remarks;

    /**
     * 图片
     */
    private List<FileVO> imageFiles;
}

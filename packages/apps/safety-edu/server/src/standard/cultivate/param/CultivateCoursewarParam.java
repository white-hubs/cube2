package com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.param;

import lombok.Data;

/**
 * @author Appet
 * @Description 培训课件管理
 * @date 2023-04-18 15:08:36
 */
@Data
public class CultivateCoursewarParam {
    private Long cultivateTaskId;
    private String fileName;
    private String fileSize;
    private String fileIds;//逗号分割
    /**
     * 时长
     */
    private Integer duration;
}
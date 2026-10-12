package com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

import java.util.List;

/**
 * @author Appet
 * @Description 培训课件管理
 * @date 2023-04-18 15:08:36
 */
@Data
public class CultivateCoursewarVO {
    private Long cultivateTaskId;
    private Long id;
    private String fileName;
    private String fileSize;
    private String fileIds;
    private List<FileVO> files;
    /**
     * 时长
     */
    private Integer duration;
}
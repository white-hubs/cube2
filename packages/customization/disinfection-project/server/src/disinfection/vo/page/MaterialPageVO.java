package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.page;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MaterialPageVO {
    /**
     * 主键ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    /**
     * 项目ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long projectId;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 资料名称
     */
    private String fileName;

    /**
     * 文件类型
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long fileTypeId;

    /**
     * 文件分类名称
     */
    private String fileTypeName;

    /**
     * 上传的文件ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long fileId;

    /**
     * 上传人ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long uploadUserId;

    /**
     * 上传人名称
     */
    private String uploadUserName;
    /**
     * 上传时间
     */
    private LocalDateTime uploadTime;
    /**
     * 文件
     */
    private FileVO file;
}

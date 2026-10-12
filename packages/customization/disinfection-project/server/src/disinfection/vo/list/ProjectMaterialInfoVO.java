package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.list;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProjectMaterialInfoVO {

    /**
     * Id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    /**
     * 资料名称
     */
    private String fileName;
    /**
     * 文件Id
     */
    private Long fileId;

    /**
     * 文件类型Id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long fileTypeId;
    /**
     * 文件类型名称
     */
    private String fileTypeName;
    /**
     * 上传人Id
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
     * 文件信息
     */
    private FileVO file;
}
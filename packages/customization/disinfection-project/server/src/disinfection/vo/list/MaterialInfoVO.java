package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.list;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MaterialInfoVO {
    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 资料名称
     */
    private String fileName;

    /**
     * 文件类型
     */
    private Long fileTypeId;

    /**
     * 文件分类名称
     */
    private String fileTypeName;

    /**
     * 上传的文件ID
     */
    private Long fileId;

    private FileVO fileVO;

    /**
     * 上传人ID
     */
    private Long uploadUserId;

    /**
     * 上传时间
     */
    private LocalDateTime uploadTime;
}
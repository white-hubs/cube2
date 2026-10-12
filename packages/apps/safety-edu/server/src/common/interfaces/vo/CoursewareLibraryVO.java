package com.guhecloud.cube.srv.gen.safety.education.server.common.interfaces.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

/**
 * 课件库返回参数
 *
 * @author lanchun
 * @date 2023/4/18 11:41
 */
@Data
public class CoursewareLibraryVO {

    private Long id;

    /**
     * 课件名称
     */
    private String name;

    /**
     * 主题主键
     */
    private Long themeId;

    /**
     * 主题名称
     */
    private String themeName;

    /**
     * 附件主键
     */
    private Long fileId;

    /**
     * 课件描述
     */
    private String remark;

    /**
     * 文件对象详细
     */
    private FileVO file;

    /**
     * 时长
     */
    private Integer duration;
}

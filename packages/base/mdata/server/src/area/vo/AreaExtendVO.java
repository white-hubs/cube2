package com.guhecloud.cube.biz.mdata.server.area.vo;

import com.guhecloud.cube.base.file.client.vo.FileVO;
import lombok.Data;

import java.util.List;

/**
 * 区域扩展配置返回参数
 *
 * @author lanchun
 * @date 2024/7/23 20:39
 */
@Data
public class AreaExtendVO {

    /**
     * 区域主键
     */
    private Long id;

    /**
     * 区域介绍
     */
    private String introduce;

    /**
     * 图片附件集合
     */
    private List<FileVO> pictureFiles;

    /**
     * 视频附件集合
     */
    private List<FileVO> videoFiles;
}

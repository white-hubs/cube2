package com.guhecloud.cube.biz.mdata.server.area.param;

import lombok.Data;

import java.util.Set;

@Data
public class AreaExtendParam {

    /**
     * 区域介绍
     */
    private String introduce;

    /**
     * 图片附件id集合
     */
    private Set<String> pictureFiles;

    /**
     * 视频文件id集合
     */
    private Set<String> videoFiles;
}

package com.guhecloud.cube.biz.vmonitor.server.vo;

import lombok.Data;

import java.util.List;

/**
 * 多路视频收藏返回参数
 *
 * @author lanchun
 * @date 2021/12/6 19:51
 */
@Data
public class MultipleVideoCollectVO {

    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 行数
     */
    private Integer row;

    /**
     * 列数
     */
    private Integer col;

    /**
     * 摄像机列表
     */
    private List<MultipleVideoCollectCameraVO> cameraVOS;
}
package com.guhecloud.cube.biz.vmonitor.server.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 多路视频收藏分页返回参数
 *
 * @author lanchun
 * @date 2021/12/6 19:51
 */
@Data
public class MultipleVideoCollectPageVO {

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
     * 创建时间
     */
    private LocalDateTime createTime;
}
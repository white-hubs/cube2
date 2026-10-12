package com.guhecloud.cube.customized.guhecloudwebsite.vo;

import lombok.Data;

/**
 * 友情链接VO
 */
@Data
public class FriendLinkVO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 友链名称
     */
    private String linkName;

    /**
     * 友链地址
     */
    private String linkUrl;

    /**
     * 启用状态：0-禁用，1-启用
     */
    private Integer status;

}

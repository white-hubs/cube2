package com.guhecloud.cube.customized.guhecloudwebsite.param;

import lombok.Data;

/**
 * 友情链接新增/编辑参数
 */
@Data
public class FriendLinkParam {

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

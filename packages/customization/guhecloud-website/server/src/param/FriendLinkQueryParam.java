package com.guhecloud.cube.customized.guhecloudwebsite.param;

import lombok.Data;

/**
 * 友情链接查询参数
 */
@Data
public class FriendLinkQueryParam {

    /**
     * 友链名称
     */
    private String linkName;

    /**
     * 启用状态：0-禁用，1-启用
     */
    private Integer status;

}

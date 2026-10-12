package com.guhecloud.cube.biz.linkage.server.linkage.strategy.messagenofity.messagetarget.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 用户组
 *
 * @author lanchun
 * @date 2022/10/31 16:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessageUserGroupDTO {

    /**
     * 用户组主键
     */
    private Long userGroupId;

    /**
     * 用户组名称
     */
    private String userGroupName;
}

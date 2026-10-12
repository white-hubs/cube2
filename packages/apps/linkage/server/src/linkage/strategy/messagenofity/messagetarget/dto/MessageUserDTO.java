package com.guhecloud.cube.biz.linkage.server.linkage.strategy.messagenofity.messagetarget.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 用户
 *
 * @author lanchun
 * @date 2022/10/31 16:44
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessageUserDTO {

    /**
     * 用户主键
     */
    private Long userId;

    /**
     * 用户名称
     */
    private String userName;
}

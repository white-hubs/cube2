package com.guhecloud.cube.biz.linkage.server.linkage.strategy.messagenofity.messagetarget.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 岗位
 *
 * @author lanchun
 * @date 2022/10/31 16:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessagePostDTO {

    /**
     * 岗位主键
     */
    private Long postId;

    /**
     * 岗位名称
     */
    private Long postName;
}

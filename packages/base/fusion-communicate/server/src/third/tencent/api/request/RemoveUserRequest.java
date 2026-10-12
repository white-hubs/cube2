package com.guhecloud.cube.biz.fusion.communicate.server.third.tencent.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * 从房间移除用户请求参数
 *
 * @author lanchun
 * @date 2025/2/11 14:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemoveUserRequest {

    /**
     * 房间id
     */
    private String roomId;

    /**
     * 移除的用户id集合
     */
    private Set<String> userIds;
}

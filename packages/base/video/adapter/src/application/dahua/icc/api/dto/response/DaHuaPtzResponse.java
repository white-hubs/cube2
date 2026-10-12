package com.guhecloud.cube.srv.gen.adapter.video.server.application.dahua.icc.api.dto.response;

import lombok.Data;

/**
 * 云台控制返回参数
 *
 * @author lanchun
 * @date 2025/5/19 13:50
 */
@Data
public class DaHuaPtzResponse {

    /**
     * 操作结果：0=失败，1=成功，失败时参考LockUser
     */
    private String result;

    /**
     * 锁定用户信息
     */
    @Data
    public static class LockUser {

        /**
         * 控制失败时，持有锁的用户名称
         */
        private String userName;

        /**
         * 控制失败时，持有锁的用户级别
         */
        private String userLevel;

        /**
         * 下级域地址，null或者空字符串表示是本级，有地址时表示下级
         */
        private String lowerDomain;
    }
}

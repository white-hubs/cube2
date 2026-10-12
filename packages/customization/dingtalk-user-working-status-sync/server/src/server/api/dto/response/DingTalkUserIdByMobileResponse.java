package com.guhecloud.cube.customized.dingtalkuserworkingstatussync.server.api.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DingTalkUserIdByMobileResponse {

    /**
     * 钉钉用户id
     */
    private String userid;
}

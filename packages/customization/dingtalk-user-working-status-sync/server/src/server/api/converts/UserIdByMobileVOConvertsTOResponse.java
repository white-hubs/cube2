package com.guhecloud.cube.customized.dingtalkuserworkingstatussync.server.api.converts;

import com.dingtalk.api.response.OapiV2UserGetbymobileResponse;
import com.guhecloud.cube.customized.dingtalkuserworkingstatussync.server.api.dto.response.DingTalkUserIdByMobileResponse;

public final class UserIdByMobileVOConvertsTOResponse {

    private UserIdByMobileVOConvertsTOResponse() {

    }

    public static DingTalkUserIdByMobileResponse userIdByMobileVOConvertsTOResponse(
            OapiV2UserGetbymobileResponse.UserGetByMobileResponse vo
                                                                                   ) {
        return DingTalkUserIdByMobileResponse.builder()
                .userid(vo.getUserid())
                .build();
    }
}

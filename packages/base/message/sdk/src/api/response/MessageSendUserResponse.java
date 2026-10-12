package com.guhecloud.cube.base.message.sdk.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageSendUserResponse {

    private Long userId;
    private String userName;
    private String userPhone;
    private String userEmail;
}

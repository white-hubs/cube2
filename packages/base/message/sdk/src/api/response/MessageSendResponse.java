package com.guhecloud.cube.base.message.sdk.api.response;

import com.guhecloud.cube.base.message.sdk.api.constants.MessageSendStatusCodeConstants;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageSendResponse {

    /**
     * 发送id集合
     */
    public Set<String> sendIds;

    /**
     * code码
     */
    public String code;

    /**
     * 详细信息描述
     */
    public String message;

    /**
     * 发送用户信息
     */
    public Set<MessageSendUserResponse> sendUsers;

    public static MessageSendResponse fail(String errorMsg) {
        return MessageSendResponse.fail(MessageSendStatusCodeConstants.SEND_FAIL, errorMsg);
    }

    public static MessageSendResponse fail() {
        return MessageSendResponse.fail(MessageSendStatusCodeConstants.SEND_FAIL, "发送失败");
    }

    public static MessageSendResponse fail(String statusCode, String errorMsg) {
        return MessageSendResponse
                .builder()
                .code(statusCode)
                .message(errorMsg)
                .build();
    }

    public static MessageSendResponse success() {
        return MessageSendResponse.success(Collections.emptyList());
    }

    public static MessageSendResponse success(List<UserDTO> userDTOS) {
        Set<MessageSendUserResponse> sendUsers = new LinkedHashSet<>(userDTOS.size());
        userDTOS.stream().forEach(userDTO -> {
            sendUsers.add(MessageSendUserResponse
                                  .builder()
                                  .userId(userDTO.getId())
                                  .userName(userDTO.getName())
                                  .userPhone(userDTO.getMobile())
                                  .build());
        });
        return MessageSendResponse
                .builder()
                .code(MessageSendStatusCodeConstants.SEND_SUCCESS)
                .sendUsers(sendUsers)
                .message("发送成功")
                .build();
    }

}

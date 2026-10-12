package com.guhecloud.cube.base.message2.client.dto;

import com.guhecloud.cube.base.message2.client.enumeration.CodeVerifyResultEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CodeResponseDTO {
    private CodeVerifyResultEnum code;
    private String message;

    public static CodeResponseDTO build(String message, CodeVerifyResultEnum code) {
        return CodeResponseDTO.builder().code(code).message(message).build();
    }

    public static CodeResponseDTO failed(String message) {
        return build(message, CodeVerifyResultEnum.FAILED);
    }

    public static CodeResponseDTO failed(String message, CodeVerifyResultEnum code) {
        return build(message, code);
    }

    public static CodeResponseDTO success() {
        return build("成功", CodeVerifyResultEnum.SUCCESS);
    }
}

package com.guhecloud.cube.base.message2.server.handler.pending.tmp.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TmpVO {

    private Boolean succeed;

    /**
     * 模板配置
     */
    private String tmpConfig;

    /**
     * 错误信息
     */
    private String errorMsg;

    public static TmpVO failTmpVO(String errorMsg) {
        return new TmpVO(false, null, errorMsg);
    }
}

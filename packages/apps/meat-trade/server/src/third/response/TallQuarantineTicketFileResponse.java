package com.guhecloud.cube.biz.meattrade.server.third.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 第三方批次检疫证明
 *
 * @author lanchun
 * @date 2025/1/23 11:38
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TallQuarantineTicketFileResponse {

    /**
     * 附件地址
     */
    private String fileUrl;

    /**
     * 附件名称
     */
    private String fileName;
}

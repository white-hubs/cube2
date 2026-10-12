package com.guhecloud.cube.sn.meattrade.plugin.dbc.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 检疫证明获取
 *
 * @author lanchun
 * @date 2025/1/16 18:23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
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

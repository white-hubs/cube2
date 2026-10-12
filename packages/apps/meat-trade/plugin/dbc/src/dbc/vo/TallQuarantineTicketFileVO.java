package com.guhecloud.cube.sn.meattrade.plugin.dbc.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 批次检疫证明
 *
 * @author lanchun
 * @date 2025/1/20 16:44
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TallQuarantineTicketFileVO {

    /**
     * 附件地址
     */
    private String fileUrl;

    /**
     * 附件名称
     */
    private String fileName;
}

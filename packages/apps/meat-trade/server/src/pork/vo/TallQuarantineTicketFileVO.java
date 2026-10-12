package com.guhecloud.cube.biz.meattrade.server.pork.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 批次检疫票文件
 *
 * @author lanchun
 * @date 2025/1/23 14:41
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

package com.guhecloud.cube.sn.meattrade.plugin.dbc.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 批次检疫证明请求参数
 *
 * @author lanchun
 * @date 2025/1/16 18:26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TallQuarantineTicketFileRequest extends AbstractDbcRequest {

    /**
     * 批次id
     */
    private String tallyId;

    @Override
    public String getUrl() {
        return getUrl("/api/v2/query/getRegFiles")
                .append("?tallyId={tallyId}").toString();
    }
}

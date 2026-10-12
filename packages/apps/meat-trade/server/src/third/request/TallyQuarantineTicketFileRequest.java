package com.guhecloud.cube.biz.meattrade.server.third.request;

import com.guhecloud.cube.biz.meattrade.server.third.config.ThirdMeatTradeConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TallyQuarantineTicketFileRequest extends AbstractThirdRequest {

    /**
     * 批次id
     */
    private String tallyId;

    @Override
    public String getUrl() {
        ThirdMeatTradeConfig requestConfig = getRequestConfig();
        return getUrl(requestConfig.getQuarantineCertificateFileApiUrl() + "?tallyId={tallyId}").toString();
    }
}

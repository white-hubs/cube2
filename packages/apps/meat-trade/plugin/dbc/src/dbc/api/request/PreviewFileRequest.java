package com.guhecloud.cube.sn.meattrade.plugin.dbc.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Collections;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PreviewFileRequest extends AbstractDbcRequest {

    /**
     * 文件地址
     */
    private String fileUrl;

    @Override
    public HttpHeaders getCustomHttpHeaders() {
        HttpHeaders customHttpHeaders = super.getCustomHttpHeaders();
        customHttpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_OCTET_STREAM));
        return customHttpHeaders;
    }

    @Override
    public String getUrl() {
        return getUrl("/api/v2/query/previewFile")
                .append("?fileUrl={fileUrl}").toString();
    }
}

package com.guhecloud.cube.biz.fusion.communicate.server.third.xunyi.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.guhecloud.cube.biz.fusion.communicate.server.third.xunyi.constants.XunYiApiDocConstant.FILE_RECORD_BY_SID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CallRecordBySidXunYiRequest extends AbstractXunYiRequest {

    private static final long serialVersionUID = 1L;

    /**
     * CallId(实际测试情况应该是sessionId)
     */
    @JsonProperty("CID")
    private String cid;

    @Override
    public String getUrl() {
        return getUrl(FILE_RECORD_BY_SID + "?CID=" + getCid()).toString();
    }
}

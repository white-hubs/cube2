package com.guhecloud.cube.base.message2.server.handler.handler.tmp.sms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeXinTemplate {

    /**
     * 模板id(用于编辑、删除)
     */
    protected String tmpId;

    /**
     * 模版编号(用于发送)
     */
    protected String tmpCode;
}

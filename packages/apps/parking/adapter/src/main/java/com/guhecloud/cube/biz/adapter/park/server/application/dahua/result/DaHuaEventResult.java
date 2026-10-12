package com.guhecloud.cube.biz.adapter.park.server.application.dahua.result;

import com.alibaba.fastjson2.JSONObject;
import lombok.Data;

/**
 * 大华事件返回对象
 */
@Data
public class DaHuaEventResult {
    private JSONObject data;
    private String resCode;
    private String resMsg;
}

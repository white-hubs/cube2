package com.guhecloud.cube.srv.gen.identity.server.qrcode.factory;

import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.command.QrcodeStyleJsonCommand;

/**
 * @author bq
 * @date 2022/7/21
 * @description 将json样式的字符串格式化成对象
 */
public class QrcodeStyleFactory {

    private final String json;

    public QrcodeStyleFactory(String json) {
        this.json = json;
    }

    public QrcodeStyleJsonCommand getObject() {
        return JSONObject.parseObject(json, QrcodeStyleJsonCommand.class);
    }

}

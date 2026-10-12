package com.guhecloud.cube.base.message2.server.handler.handler.tmp;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.base.enumeration.ManufacturerCodeEnum;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 负责通道厂家与模板处理器的映射
 *
 * @author lanchun
 * @date 2023/3/13 14:45
 */
@Component
public class MsgChannelManufacturerTmpHandlerHolder {

    private Map<String, MsgChannelManufacturerTmpHandler> handlerMap = new HashMap<>();

    public void putHandler(ChannelEnum channelCode, ManufacturerCodeEnum manufacturerCode, MsgChannelManufacturerTmpHandler handler) {
        this.handlerMap.put(channelCode.name() + "_" + manufacturerCode.name(), handler);
    }

    public MsgChannelManufacturerTmpHandler getHandler(ChannelEnum channelCode, ManufacturerCodeEnum manufacturerCode) {
        MsgChannelManufacturerTmpHandler msgChannelManufacturerTmpHandler = this.handlerMap.get(
                channelCode.name() + "_" + manufacturerCode.name());
        if (Objects.isNull(msgChannelManufacturerTmpHandler)) {
            msgChannelManufacturerTmpHandler = ApplicationContextHolder.getBean(DefaultMsgChannelManufacturerTmpHandler.class);
        }
        return msgChannelManufacturerTmpHandler;
    }
}

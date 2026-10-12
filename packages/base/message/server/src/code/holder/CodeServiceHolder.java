package com.guhecloud.cube.base.message2.server.code.holder;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.code.service.CodeService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CodeServiceHolder {
    public Map<ChannelEnum, CodeService> codeServiceMap = new HashMap<>();

    public CodeService getCodeService(ChannelEnum channelCode) {
        return codeServiceMap.get(channelCode);
    }

    public void putCodeServiceMap(ChannelEnum channelCode, CodeService codeService) {
        this.codeServiceMap.put(channelCode, codeService);
    }

}

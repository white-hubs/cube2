package com.guhecloud.cube.base.message2.server.handler.handler.send;

import com.guhecloud.platform.base.context.ApplicationContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class ManufacturerHolder implements ApplicationRunner {

    private Map<String, ManufacturerSender> handlerMap = new HashMap<>();

    public ManufacturerSender getHandler(String manufacturerCode) {
        return this.handlerMap.get(manufacturerCode);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //通道发送器
        ApplicationContextHolder.getBeansOfType(ManufacturerSender.class)
                .forEach((k, v) ->
                                 this.handlerMap.put(v.getChannelCode() + "-" + v.getManufacturerCode(), v)
                        );
    }
}

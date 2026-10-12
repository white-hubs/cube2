package com.guhecloud.cube.base.message2.server.handler.pending;

import com.guhecloud.cube.base.message2.server.handler.config.HandlerThreadPoolConfig;
import com.guhecloud.cube.base.message2.server.handler.util.ChannelUtil;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 存储 每种消息类型 与 TaskPending 的关系
 *
 * @author lanchun
 * @date 2024/9/2 17:20
 */
@Component
public class MessageSendTaskPendingHolder {

    private Map<String, ExecutorService> taskPendingHolder = new HashMap<>(32);

    /**
     * 获取得到所有的通道
     */
    private static List<String> channels = ChannelUtil.getAllChannels();

    /**
     * 给每个渠道初始化一个线程池
     */
    @PostConstruct
    public void init() {

        for (String channel : channels) {
            ThreadPoolExecutor executor = HandlerThreadPoolConfig.getExecutor(channel);
            taskPendingHolder.put(channel, executor);
        }
    }

    /**
     * 得到对应的线程池
     *
     * @param channelCode
     *
     * @return
     */
    public ExecutorService route(String channelCode) {
        return taskPendingHolder.get(channelCode);
    }

}

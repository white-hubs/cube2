package com.guhecloud.cube.base.device.center.core.service;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.device.center.core.dto.DeviceStateMessage;
import com.guhecloud.cube.base.device.center.core.mapper.CenterBizDeviceMapper;
import com.guhecloud.cube.base.device.center.core.mapper.ExtDeviceAttrCmdMapper;
import com.guhecloud.cube.base.device.center.core.mapper.ExtDeviceAttrCmdStepMapper;
import com.guhecloud.cube.base.device.center.core.model.BizDevice;
import com.guhecloud.cube.base.device.center.core.model.ExtDeviceAttrCmd;
import com.guhecloud.cube.base.device.center.core.model.ExtDeviceAttrCmdStep;
import com.guhecloud.cube.base.device.center.metadata.event.CommandExecuteCallbackEvent;
import com.guhecloud.cube.base.device.center.metadata.lang.AttrCtlExecuteResultEnum;
import com.guhecloud.cube.base.device.center.statistics.service.BizDeviceStatisticsService;
import com.guhecloud.cube.iot.north.bridge.consumer.SubscribeContainer;
import com.guhecloud.cube.iot.north.bridge.consumer.util.CallbackUtil;
import com.guhecloud.cube.iot.north.bridge.consumer.vo.CommandResponse;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 设备订阅服务<br/>
 * 向IOT平台订阅设备状态等信息
 */
@Component
@Slf4j
public class CenterIOTSubscribeService {

    /**
     * 设备状态消息队列容量
     */
    private static final int QUEUE_CAPACITY = Short.MAX_VALUE;
    /**
     * 批量处理大小
     */
    private static final int BATCH_SIZE = 500;
    /**
     * 批量处理间隔（毫秒）3秒
     */
    private static final long BATCH_INTERVAL_MS = 3000;

    private final String DEVICE_STATE_KEY = "iot:dev:state:{device-id}";
    /**
     * 设备状态消息队列
     */
    private final BlockingQueue<DeviceStateMessage> deviceStateQueue = new LinkedBlockingQueue<>(QUEUE_CAPACITY);
    /**
     * 虚拟线程执行器
     */
    private ExecutorService virtualThreadExecutor;
    /**
     * 批量处理任务执行器
     */
    private ExecutorService batchConsumerExecutor;
    /**
     * 批量处理任务运行标志
     */
    private volatile boolean running = false;

    @Resource
    private SubscribeContainer subscribeContainer;
    @Resource
    private CenterBizDeviceMapper centerBizDeviceMapper;
    @Resource
    private ExtDeviceAttrCmdStepMapper extDeviceAttrCmdStepMapper;
    @Resource
    private ExtDeviceAttrCmdMapper extDeviceAttrCmdMapper;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private BizDeviceStatisticsService bizDeviceStatisticsService;

    /**
     * 命令执行状态映射
     *
     * @param commandExecuteResultCallbackEnum 命令执行状态
     * @return 命令执行状态
     */
    public static AttrCtlExecuteResultEnum commandExecuteStatusMapping(String commandExecuteResultCallbackEnum) {
        switch (commandExecuteResultCallbackEnum) {
            case "EXPIRED":
                return AttrCtlExecuteResultEnum.EXPIRED;
            case "SUCCESS":
                return AttrCtlExecuteResultEnum.SUCCESS;
            case "FAIL":
                return AttrCtlExecuteResultEnum.FAIL;
            case "INIT":
            case "ACCEPT":
            case "PROCESSING":
                return AttrCtlExecuteResultEnum.ISSUING;
            default:
                throw new IllegalArgumentException("未知的命令执行状态:" + commandExecuteResultCallbackEnum);
        }
    }

    @EventListener
    public void onSystemReadyEvent(ApplicationReadyEvent applicationReadyEvent) {
        log.info("系统准备就绪，订阅准备订阅设备状态");

        // 初始化虚拟线程执行器
        try {
            virtualThreadExecutor = Executors.newVirtualThreadPerTaskExecutor();
            batchConsumerExecutor = Executors.newSingleThreadExecutor(r -> {
                Thread thread = new Thread(r);
                thread.setName("batch-consumer-thread");
                return thread;
            });
        } catch (Exception e) {
            log.error("线程执行器初始化失败，使用固定线程池", e);
        }

        // 启动批量消费任务
        running = true;
        batchConsumerExecutor.submit(this::startBatchConsumer);

        //同步设备状态 - 使用队列异步处理
        subscribeContainer.registerDeviceStatusMsgHandler(msg -> {
            if (log.isDebugEnabled()) {
                log.debug("接收到设备状态消息:" + msg);
            }

            try {
                // 将消息放入队列
                DeviceStateMessage stateMessage = new DeviceStateMessage(
                        Long.parseLong(msg.getDeviceId()),
                        CallbackUtil.communicateIsNormal(msg) ? 1 : 0,
                        LocalDateTime.now()
                );

                if (!deviceStateQueue.offer(stateMessage)) {
                    // 队列满，记录告警
                    log.error("设备状态消息队列已满，消息丢失！当前队列大小：{}，丢弃：{}", deviceStateQueue.size(), stateMessage);
                }
            } catch (Exception e) {
                log.error("处理设备状态消息失败", e);
            }
        });

        //同步设备命令响应
        subscribeContainer.registerDeviceCommandMsgHandler(msg -> {
            if (Objects.isNull(msg)) {
                return;
            }

            log.info("接收到设备命令消息:" + msg);

            CommandResponse commandResponse = msg.getCommandResponse();
            if (Objects.isNull(commandResponse)) {
                return;
            }

            //"controlAttributeCommandSeq:" + batchCmdSeq,
            Integer tenant = (Integer) redisTemplate.opsForHash()
                    .get("controlAttributeCommandSeq:" + commandResponse.getCommandSeq(), "tenant");
            String createBy = (String) redisTemplate.opsForHash()
                    .get("controlAttributeCommandSeq:" + commandResponse.getCommandSeq(), "createBy");

            if (Objects.isNull(tenant)) {
                log.error("未找到租户信息, 丢失命令响应:{}", commandResponse);
                return;
            }

            SessionContextHolder.requestTenant(tenant);
            AttrCtlExecuteResultEnum attrCtlExecuteResultEnum = commandExecuteStatusMapping(commandResponse.getStatusCode());
            if (Objects.isNull(attrCtlExecuteResultEnum)) {
                log.error("未找到命令执行状态, 丢失命令响应:{}", commandResponse);
                return;
            }

            //插入执行记录
            ExtDeviceAttrCmdStep extDeviceAttrCmdStep = ExtDeviceAttrCmdStep.create(Long.valueOf(commandResponse.getCommandSeq()),
                    attrCtlExecuteResultEnum.name());
            extDeviceAttrCmdStepMapper.insert(extDeviceAttrCmdStep);

            //更新命令执行结果
            ExtDeviceAttrCmd extDeviceAttrCmd = extDeviceAttrCmdMapper.selectOne(Wrappers.<ExtDeviceAttrCmd>lambdaQuery()
                    .eq(ExtDeviceAttrCmd::getCommandSeq, commandResponse.getCommandSeq()));
            if (Objects.isNull(extDeviceAttrCmd)) {
                return;
            }

            extDeviceAttrCmd.setExecuteResult(attrCtlExecuteResultEnum.name());
            extDeviceAttrCmdMapper.updateById(extDeviceAttrCmd);

            //对外发送命令执行结果
            CommandExecuteCallbackEvent commandExecuteCallbackEvent = new CommandExecuteCallbackEvent();
            commandExecuteCallbackEvent.setCommandSeq(commandResponse.getCommandSeq());
            commandExecuteCallbackEvent.setExecuteResult(attrCtlExecuteResultEnum);
            commandExecuteCallbackEvent.setDeviceInstanceId(extDeviceAttrCmd.getDeviceId());
            commandExecuteCallbackEvent.setAttributeCode(extDeviceAttrCmd.getAttributeCode());
            RocketMQContextHolder.send(CommandExecuteCallbackEvent.TOPIC, commandExecuteCallbackEvent);
        });
    }

    /**
     * 启动批量消费任务
     */
    private void startBatchConsumer() {
        log.info("设备状态批量消费任务启动");
        List<DeviceStateMessage> batch = new ArrayList<>(BATCH_SIZE);
        long lastProcessTime = System.currentTimeMillis();

        while (running) {
            try {
                // 从队列中获取消息，最多等待批量间隔时间
                DeviceStateMessage message = deviceStateQueue.poll(BATCH_INTERVAL_MS, TimeUnit.MILLISECONDS);

                if (message != null) {
                    batch.add(message);
                }

                long currentTime = System.currentTimeMillis();
                // 判断是否需要处理批量消息 1. 批量大小达到阈值 2. 超过批量间隔时间
                boolean shouldProcess = batch.size() >= BATCH_SIZE
                        || (!batch.isEmpty() && (currentTime - lastProcessTime) >= BATCH_INTERVAL_MS);

                if (shouldProcess) {
                    processBatch(new ArrayList<>(batch));
                    batch.clear();
                    lastProcessTime = currentTime;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.warn("批量消费任务被中断", e);
                break;
            } catch (Exception e) {
                log.error("批量消费任务异常", e);
            }
        }

        // 处理剩余消息
        if (!batch.isEmpty()) {
            processBatch(batch);
        }

        log.info("设备状态批量消费任务停止");
    }

    /**
     * 批量处理设备状态消息
     *
     * @param messages 设备状态消息列表
     */
    private void processBatch(List<DeviceStateMessage> messages) {
        try {
            log.debug("开始批量处理设备状态，数量：{}", messages.size());

            // 异步保存状态日志
            List<BizDevice> devices = new LinkedList<>();
            messages.forEach(msg -> {
                BizDevice device = new BizDevice();
                device.setId(msg.getDeviceId());
                device.setDeviceState(msg.getCommunicateState());
                device.setUpdateTime(msg.getUpdateTime());
                devices.add(device);
            });

            virtualThreadExecutor.submit(() -> {
                centerBizDeviceMapper.batchUpdateDeviceStateNoTenant(devices);
            });

            for (DeviceStateMessage message : messages) {
                try {
                    bizDeviceStatisticsService.saveStateLog(message.getDeviceId(),message.getCommunicateState());
                } catch (Exception e) {
                    log.error("保存设备状态日志失败，deviceId：{}", message.getDeviceId(), e);
                }
            }
        } catch (Exception e) {
            log.error("批量处理设备状态失败", e);
        }
    }

    @PreDestroy
    public void shutdown() {
        running = false;
        if (virtualThreadExecutor != null) {
            virtualThreadExecutor.shutdown();
            try {
                if (!virtualThreadExecutor.awaitTermination(10, TimeUnit.SECONDS)) {
                    virtualThreadExecutor.shutdownNow();
                }
            } catch (InterruptedException e) {
                virtualThreadExecutor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }

        if (Objects.nonNull(batchConsumerExecutor)) {
            batchConsumerExecutor.shutdown();
        }
        log.info("设备订阅服务已关闭");
    }

    /**
     * 分页同步设备在线时间&设备状态
     */
    private void timerToUpdateDevice() {
        try {
            Long countSize = centerBizDeviceMapper.countAllDeviceId();
            if (Objects.isNull(countSize) || countSize == 0) {
                return;
            }

            //分页拉取设备ID
            int pageSize = 1000;
            long totalPage = countSize / pageSize + 1;
            for (int i = 0; i < totalPage; i++) {
                List<Long> deviceIds = centerBizDeviceMapper.selectDeviceIdPage(i * pageSize, pageSize);
                if (Objects.isNull(deviceIds) || deviceIds.isEmpty()) {
                    continue;
                }

                for (Long deviceId : deviceIds) {
                    updateDeviceStateAndOnlineTime(deviceId);

                    //避免消息拥堵
                    TimeUnit.MILLISECONDS.sleep(5);
                }
            }
        } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            log.error("同步设备在线时间失败", e);
        }
    }

    /**
     * 同步设备在线时间&设备状态
     *
     * @param deviceId 设备ID
     */
    private void updateDeviceStateAndOnlineTime(Long deviceId) {
        try {
            //同步设备在线时间
            String redisKey = DEVICE_STATE_KEY.replace("{device-id}", String.valueOf(deviceId));
            String deviceState = String.valueOf(redisTemplate.opsForValue().get(redisKey));
            if (StringUtils.isBlank(deviceState) || deviceState.equalsIgnoreCase("null")) {
                return;
            }

            String lastOnlineTimeStr = JSON.parseObject(deviceState).getString("lastOnlineTime");
            int status = JSON.parseObject(deviceState).getInteger("status");
            int isOnline = status == 1 ? 1 : 0;//非1即离线
            long lastOnlineTime = Long.parseLong(lastOnlineTimeStr);

            if (lastOnlineTime > 1) {
                centerBizDeviceMapper.syncDeviceOnlineTime(deviceId, DateUtils.parse(lastOnlineTime), isOnline);
            } else {
                centerBizDeviceMapper.syncDeviceOnState(deviceId, isOnline);
            }
        } catch (Exception e) {
            log.error("同步设备在线时间失败, deviceId:{}", deviceId, e);
        }
    }

}

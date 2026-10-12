package com.guhecloud.cube.biz.linkage.server.facade.iot.subscribe;

import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.biz.linkage.server.facade.iot.dto.EventAdditionalDTO;
import com.guhecloud.cube.biz.linkage.server.facade.iot.dto.EventAdditionalNotifyDTO;
import com.guhecloud.cube.biz.linkage.server.facade.iot.dto.EventAdditionalPicFileDTO;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.service.IotEventFileConsumeService;
import com.guhecloud.cube.iot.north.bridge.consumer.SubscribeContainer;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IotEventAdditionSubscribeHandler {

    private final SubscribeContainer subscribeContainer;

    private final IotEventFileConsumeService iotEventFileConsumeService;

    @EventListener
    public void subscribeEventAddition(ApplicationReadyEvent applicationReadyEvent) {
        this.subscribeContainer.registerDeviceEventAdditionalMsgHandler((eventAdditional) -> {
            JSONObject additionalObject = JSONObject.parseObject(eventAdditional.getAdditional());
            String additionalWithPicFileDes = additionalObject.getString("additional_with_pic_file_des");
            String additionalWithVideoFileDes = additionalObject.getString("additional_with_video_file_des");
            Set<String> fileStrList = new HashSet<>();
            if (StringUtils.isNotBlank(additionalWithPicFileDes)) {
                fileStrList.add(additionalWithPicFileDes);
            }
            if (StringUtils.isNotBlank(additionalWithVideoFileDes)) {
                fileStrList.add(additionalWithVideoFileDes);
            }
            Set<String> additions = fileStrList.stream().map(key -> {
                JSONObject picFileObject = JSONObject.parseObject(key);
                EventAdditionalNotifyDTO eventAdditionalNotifyDTO = new EventAdditionalNotifyDTO();
                eventAdditionalNotifyDTO.setDeviceId(eventAdditional.getDeviceId());
                eventAdditionalNotifyDTO.setEventInstanceId(eventAdditional.getEventInstanceId());
                eventAdditionalNotifyDTO.setEventCode(eventAdditional.getEventCode());
                eventAdditionalNotifyDTO.setEventTime(eventAdditional.getEventTime());
                eventAdditionalNotifyDTO.setAdditional(new EventAdditionalDTO(picFileObject.toJavaObject(EventAdditionalPicFileDTO.class)));
                return JsonUtils.serialize(eventAdditionalNotifyDTO);
            }).collect(Collectors.toSet());
            //存入消息表(等待消息入库后,开启后台线程持续消费)
            this.iotEventFileConsumeService.save(eventAdditional.getEventInstanceId(), additions);
        });
    }
}

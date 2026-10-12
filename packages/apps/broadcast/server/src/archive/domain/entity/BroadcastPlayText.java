package com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.entity;

import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.application.commandgateway.command.BroadcastPlayVariableParam;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.valueobject.BroadcastTemplate;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.valueobject.DeviceInfo;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class BroadcastPlayText {

    /**
     * 模板信息
     */
    private BroadcastTemplate broadcastTemplate;

    /**
     * 变量参数
     */
    private List<BroadcastPlayVariableParam> broadcastPlayVariableParams;

    /**
     * 广播次数
     */
    private Integer broadcastPlayNum;

    /**
     * 播放内容
     */
    private String playContent;

    /**
     * 播放设备
     */
    private List<DeviceInfo> deviceInfos;

    public BroadcastPlayText() {
    }

    public BroadcastPlayText(BroadcastTemplate broadcastTemplate,
                             List<BroadcastPlayVariableParam> broadcastPlayVariableParams,
                             Integer broadcastPlayNum,
                             List<DeviceInfo> deviceInfos
                            ) {
        this.broadcastTemplate = broadcastTemplate;
        this.broadcastPlayVariableParams = broadcastPlayVariableParams;
        this.broadcastPlayNum = broadcastPlayNum;
        this.deviceInfos = deviceInfos;
        //翻译播放内容
        this.settingPlayContent(broadcastTemplate, broadcastPlayVariableParams);
    }

    public void settingPlayContent(BroadcastTemplate broadcastTemplate, List<BroadcastPlayVariableParam> broadcastPlayVariableParams) {
        if (CollectionUtils.isEmpty(broadcastPlayVariableParams)) {
            this.playContent = broadcastTemplate.getContent();
        } else {
            // 正则表达式匹配以"${"开始，"}"结束的字符串
            Pattern pattern = Pattern.compile("\\$\\{(.*?)\\}");
            Matcher matcher = pattern.matcher(broadcastTemplate.getContent());
            // 使用StringBuilder来构建替换后的字符串
            StringBuilder outputBuilder = new StringBuilder();
            int lastEnd = 0; // 上一个替换结束的位置
            int variableIndex = 0;
            while (matcher.find()) {
                // 将上一个替换结束位置到当前匹配开始位置之间的原文字符串添加到输出中
                outputBuilder.append(broadcastTemplate.getContent(), lastEnd, matcher.start());
                // 获取匹配到的变量名并替换为对应的值
                String replacement = broadcastPlayVariableParams.get(variableIndex).getValue();
                StringBuilder replacementContent = new StringBuilder();
                for (int i = 0; i < replacement.length(); i++) {
                    String content = replacement.charAt(i) + ",";
                    replacementContent.append(content);
                }
                outputBuilder.append(replacementContent);
                // 更新上一个替换结束的位置
                lastEnd = matcher.end();
                variableIndex++;
            }
            outputBuilder.append(broadcastTemplate.getContent(), lastEnd, broadcastTemplate.getContent().length());
            this.playContent = outputBuilder.toString();
        }
    }

    public void removeDeviceInfo(Long deviceId) {
        if (!CollectionUtils.isEmpty(deviceInfos)) {
            //排除掉移除的设备id
            this.deviceInfos = this.deviceInfos.stream().filter(deviceInfo -> !deviceId.equals(deviceInfo.getId()))
                    .collect(Collectors.toList());
        }
    }
}

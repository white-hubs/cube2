package com.guhecloud.cube.biz.adapter.broadcast.server.application.baolun.v6700.facade.request;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Data
@NoArgsConstructor
@SuperBuilder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class PlayTextRequestAbstract extends AbstractBasicRequest {

    /**
     * 待广播文本
     * 编码由 TextCode 字段决定
     * 必须
     */
    private String Content;

    /**
     * 目标 ID 列表
     * 广播目标，分区（或终端）的 ID（或通话编码）
     * 必须
     */
    private Set<Integer> TargetIds;

    /**
     * 目标类型
     * 0-分组 1-终端
     * 必须
     */
    private Integer TargetType;

    /**
     * 时间
     * 本条请求发生的时间点
     * 必须
     */
    private String Time;

    /**
     * 播放次数
     * 必须大于 0
     * 必须
     */
    private Integer Playtime;

    /**
     * 播放间隔
     */
    //private Integer PlayInterval;

    /**
     * 播放优先级
     * 0-1000
     * 必须
     */
    private Integer PlayPrior;

    /**
     * 会话音量
     * 取值范围：1 – 56
     * 不设置该参数则使用终端默认音量。
     */
    private Integer Volume;

    /**
     * 语音引擎
     * 不设置则使用服务器上设置的参数
     * 必须
     */
    private String TTSName;

    /**
     * 待广播文本编码
     * 1- GBK
     * 2- UTF-8
     * 不设置则使用服务器上设置 的参数
     * 必须
     */
    private Integer TextCode;

    /**
     * 待广播文本语速
     * 最小值 -10，最大值 10
     * 不设置则使用服务器上设置的参数
     */
    //private Integer TextSpeed;

    /**
     * 提示音
     * 0- 无提示音
     * 1- 系统预设提示音 1
     * 2- 系统预设提示音 2
     */
    //private Integer PromptTone;
    @Override
    public String getUrl() {
        return getUrl("TextPlay").toString();
    }
}

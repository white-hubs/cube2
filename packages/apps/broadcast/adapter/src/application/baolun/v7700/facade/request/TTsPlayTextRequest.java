package com.guhecloud.cube.biz.adapter.broadcast.server.application.baolun.v7700.facade.request;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * tts文本播放请求参数
 *
 * @author lanchun
 * @date 2024/11/7 11:01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class TTsPlayTextRequest extends AbstractBaoLun7700Request {

    /**
     * 播放参数
     */
    private PlayTextData data;

    /**
     * 公司名取值: BL
     */
    private String company = "BL";

    /**
     * 硬性规定 无需理会 直接空即可
     */
    private String return_message;

    /**
     * 硬性规定 无需理会 默认填0
     */
    private Integer result = 0;

    /**
     * 指令,取值: c2ls_server_tts_mp3_play
     */
    private String actioncode = "c2ls_server_tts_mp3_play";

    /**
     * token
     */
    private String token;

    /**
     * 随机字符串 也可空
     */
    private String sign;

    /**
     * 提示音
     * 0- 无提示音
     * 1- 系统预设提示音 1
     * 2- 系统预设提示音 2
     */
    //private Integer PromptTone;
    @Override
    public String getUrl() {
        return getUrl("/api/v29+/ws/forwarder").toString();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
    public static class PlayTextData {

        /**
         * 终端附加描述 无需理会
         */
        private String EndPointsAdditionalProp;

        /**
         * 终端附id列表
         */
        private String EndPointsList;

        /**
         * TTS引擎名称, 通过接口查询获得
         */
        private String TTSEngineName;

        /**
         * 语速 1-10 (慢<5,5-正常,>5快)
         */
        private Integer TTSSpeed;

        /**
         * 重复次数
         */
        private Integer RepeatTime;

        /**
         * 任务UUID 自行生成
         */
        private String TaskID;

        /**
         * 任务名称 自行生成
         */
        private String TaskName;

        /**
         * 任务优先级1-100,默认0
         */
        private Integer TaskPriority;

        /**
         * 要播放的文本内容
         */
        private String TextContent;

        /**
         * 音量 0-100,默认0
         */
        private Integer Volume;

    }
}

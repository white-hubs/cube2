package com.guhecloud.cube.biz.metric.server.score.suggestion.ai.api.dify.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class ChatQueryRequest extends AbstractRequest {

    /**
     * 允许传入 App 定义的各变量值。 inputs 参数包含了多组键值对（Key/Value pairs），
     * 每组的键对应一个特定变量，每组的值则是该变量的具体值。
     * 如果变量是文件类型，请指定一个包含以下 files 中所述键的对象。 默认 {}
     */
    private Map<String, String> inputs;

    /**
     * 用户输入/提问内容。
     */
    private String query;

    /**
     * streaming 流式模式（推荐）。基于 SSE（Server-Sent Events）实现类似打字机输出方式的流式返回。
     */
    @JsonProperty("response_mode")
    private String responseMode;

    /**
     * （选填）会话 ID，需要基于之前的聊天记录继续对话，必须传之前消息的 conversation_id。
     */
    @JsonProperty("conversation_id")
    private String conversationId;

    /**
     * 用户标识，用于定义终端用户的身份，方便检索、统计。 由开发者定义规则，需保证用户标识在应用内唯一。
     */
    private String user;

    /**
     * 文件列表，适用于传入文件结合文本理解并回答问题，仅当模型支持 Vision 能力时可用。
     */
    private List<File> files;

    @Override
    public String getRequestUrl() {
        return getRequestUrl(getRequestConfig().getRequestUrl()).build();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class File {

        /**
         * document 具体类型包含：'TXT', 'MD', 'MARKDOWN', 'PDF', 'HTML', 'XLSX', 'XLS', 'DOCX', 'CSV', 'EML', 'MSG', 'PPTX', 'PPT', 'XML', 'EPUB'
         * image 具体类型包含：'JPG', 'JPEG', 'PNG', 'GIF', 'WEBP', 'SVG'
         * audio 具体类型包含：'MP3', 'M4A', 'WAV', 'WEBM', 'AMR'
         * video 具体类型包含：'MP4', 'MOV', 'MPEG', 'MPGA'
         * custom 具体类型包含：其他文件类型
         */
        private String type;

        /**
         * remote_url: 图片地址。
         * local_file: 上传文件。
         */
        @JsonProperty("transfer_method")
        private String transferMethod;

        /**
         * 图片地址。（仅当传递方式为 remote_url 时）。
         */
        private String url;
    }
}

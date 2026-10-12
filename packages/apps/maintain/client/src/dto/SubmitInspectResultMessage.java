package com.guhecloud.cube.srv.gen.maintain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wql
 * @describe 巡检记录结果消息
 * @date 2023/9/18 16:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class SubmitInspectResultMessage {
    public static final String TOPIC = "INSPECT_RECORD_RESULT";
    /**
     * 巡检对象id
     */
    private Long objId;
    /**
     * 巡检对象类型
     */
    private String objType;
    /**
     * 巡检任务名称
     */
    private String taskName;
    /**
     * 巡检记录ID
     */
    private Long recordId;
    /**
     * 巡检时间
     */
    private LocalDateTime inspectTime;
    /**
     * 巡检人ID
     */
    private Long userId;
    /**
     * 巡检项信息
     */
    List<ItemInfo> itemInfos;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class ItemInfo {
        /**
         * 标准名
         */
        private String standardName;
        /**
         * 巡检项id
         */
        private Long itemId;
        /**
         * 巡检项名称
         */
        private String itemName;
        /**
         * 项结果名
         */
        private String resultName;
    }
}

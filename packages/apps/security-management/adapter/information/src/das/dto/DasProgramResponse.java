package com.guhecloud.cube.srv.gen.adapter.information.server.das.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class DasProgramResponse {
    private String errorCode;
    private String errorMsg;
    private List<DasProgramPageDTO> data;

    @Data
    public static class DasProgramPageDTO {
        /**
         * 节目的唯一标志
         */
        private String id;
        /**
         * 节目的名字
         */
        private String name;
        /**
         * 节目的类型，n代表普通节目，l代表复合节目，i表示互动节目
         */
        private String state;
        /**
         * 节目的创建时间
         */
        private LocalDateTime createTime;
        /**
         * 节目修改时间
         */
        private LocalDateTime modifyTime;
        /**
         * 创建节目的用户的id
         */
        private String createUserId;
        /**
         * 修改节目的用户的id
         */
        private String modifyUserId;
        /**
         * 节目的标签id
         */
        private String categoryId;
        /**
         * 节目的标签名
         */
        private String categoryName;
    }
}

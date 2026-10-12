package com.guhecloud.cube.biz.fusion.communicate.server.third.xunyi.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class XunYiPageResponse<R> extends XunYiBaseResponseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 分页对象
     */
    private XunYiPageDetail<R> page;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class XunYiPageDetail<T> {

        private static final long serialVersionUID = 1L;

        /**
         * 当前页
         */
        private Integer currentPage;

        /**
         * 总页数
         */
        private Integer totalPages;

        /**
         * 总行数
         */
        private Integer totalRows;

        /**
         * 列表数据
         */
        private List<T> dataList;
    }
}

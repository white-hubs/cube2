package com.guhecloud.cube.srv.gen.library.management.server.vo.result;

import lombok.Data;

/**
 * 编目综合统计
 */
@Data
public class TidyUpStatisticsResult {
    private Long id;
    private String venues; // 馆藏地
    private String tidyBatch; // 编目批次
    private String status; // 文献状态
    private String languages; // 语种
    private String bookType; // 文献类型
    private String contentType; // 中图法分类类型
    private String operator; // 操作员
    private Integer updateNumber;  // 操作次数
    private Integer tidyNumber; // 编目数量

}

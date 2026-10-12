package com.guhecloud.cube.srv.gen.library.management.server.vo.result;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 操作员工作量
 */
@Data
public class TidyUpRecordResult {
    private Long id;
    private String name;
    private LocalDateTime tidyTime;
    private Integer tidyNumber;
    private Integer updateNumber;
    private Float workHour;
    private String bookType;
}

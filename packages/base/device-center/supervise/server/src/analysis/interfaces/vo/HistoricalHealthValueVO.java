package com.guhecloud.cube.srv.gen.device.supervise.server.analysis.interfaces.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author pengjian
 * @date 2022/8/1 19:07
 * @description
 */
@Data
public class HistoricalHealthValueVO {
    private Integer healthValue;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
}

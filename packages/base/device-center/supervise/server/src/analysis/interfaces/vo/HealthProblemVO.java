package com.guhecloud.cube.srv.gen.device.supervise.server.analysis.interfaces.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author pengjian
 * @date 2022/8/9 14:57
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HealthProblemVO {
    private Integer testItemNumber;
    private Integer problemItemNumber;

    private List<String> problems;
    private Integer healthValue;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

}

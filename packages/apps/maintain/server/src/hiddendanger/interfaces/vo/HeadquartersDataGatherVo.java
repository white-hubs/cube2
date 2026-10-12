package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class HeadquartersDataGatherVo {
    private Long id;
    private String name;
    private String hiddenDangerCategory;
    private String hiddenDangerStatus;
    private String remark;
    private String levelName;

    private Integer tenant;
    private Boolean deleted;
    private String createBy;
    private LocalDateTime createTime;
    private String updateBy;
    private LocalDateTime updateTime;

}

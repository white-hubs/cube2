package com.guhecloud.cube.srv.gen.loose.works.dto.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe 小散工程名称下拉列表
 * @date 2023/9/18 9:18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class projectNameListVO {
    /**
     * 小散工程名称
     */
    private String projectName;
    /**
     * 小散工程申请单id
     */
    private Long ApplyId;
}

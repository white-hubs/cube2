package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @author wql
 * @describe 堆叠图纵坐标
 * @date 2023/9/20 9:44
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class PileUpMapVO {
    private String color;
    private String name;
    private String code;
    private List<Integer> value;
}

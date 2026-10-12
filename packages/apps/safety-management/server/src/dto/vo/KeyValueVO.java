package com.guhecloud.cube.srv.gen.safety.management.dto.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wql
 * @describe 键值对对象
 * @date 2024/1/30 17:11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KeyValueVO {
    private String key;
    private String value;
}

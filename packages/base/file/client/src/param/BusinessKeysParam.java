package com.guhecloud.cube.base.file.client.param;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 业务主键信息
 *
 * @author lzy
 * @date 2021/9/30
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BusinessKeysParam {

    /**
     * 业务主键集合
     */
    @NotEmpty(message = "业务主键集合不能为空")
    private List<Long> businessKeys;

}

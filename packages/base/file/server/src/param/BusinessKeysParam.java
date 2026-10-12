package com.guhecloud.cube.base.file.server.param;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/**
 * 业务主键信息
 *
 * @author lzy
 * @date 2021/9/30
 */
@Data
public class BusinessKeysParam {

    /**
     * 业务主键集合
     */
    @NotEmpty(message = "业务主键集合不能为空")
    private List<Long> businessKeys;

}

package com.guhecloud.cube.biz.ouaa.server.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import jakarta.validation.constraints.Size;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/8/13 15:45
 * @Description:
 */

@Data
public class TerminalConfigQueryParam extends Paging {

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字长度不能大于100")
    private String keyword;

}

package com.guhecloud.cube.biz.ouaa.server.param;

import com.guhecloud.platform.base.model.Paging;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 访问端查询参数
 *
 * @author lanchun
 * @date 2021/10/13 19:17
 */
@Data
public class TerminalQueryParam extends Paging {

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字长度不能大于100")
    private String keyword;

    /**
     * 应用id
     */
    @Min(value = 1, message = "产品主键不能小于1")
    private Long productId;

    /**
     * 启用状态
     */
    private Boolean enabled;

    /**
     * 主键集合
     */
    private List<Long> terminalIds;
}

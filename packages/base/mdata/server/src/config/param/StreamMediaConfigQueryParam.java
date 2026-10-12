package com.guhecloud.cube.biz.mdata.server.config.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import jakarta.validation.constraints.Size;

/**
 * 流媒体服务器配置查询参数
 *
 * @author lanchun
 * @date 2021/12/7 15:11
 */
@Data
public class StreamMediaConfigQueryParam extends Paging {

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字不能大于100")
    private String keyword;
}

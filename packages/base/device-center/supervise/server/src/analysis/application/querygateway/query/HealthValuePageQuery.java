package com.guhecloud.cube.srv.gen.device.supervise.server.analysis.application.querygateway.query;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

/**
 * @author pengjian
 * @date 2022/8/1 17:45
 * @description
 */
@Data
public class HealthValuePageQuery extends Paging {
    private String keyword;
    private Long genreId;
    private Long areaId;
}

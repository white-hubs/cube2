package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 查询巡检项选择为异常的巡检记录
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ErrItemTagRecordParam extends Paging {
    private String targetName;
    private Period period;
    private List<String> tagList;
}

package com.guhecloud.cube.srv.gen.library.management.server.vo.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 借阅记录查询入参
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BorrowRecordPageParam extends Paging {
    private static final long serialVersionUID = -6108736603578910788L;
    // 书名模糊搜索
    private String name;
    // 借阅时间段
    private Period period;
    // 状态
    private String status;
    // 热门类型(BorrowHotTypeEnum)
    private String hostType;
    // 热门数量
    private Integer topNumber;
}

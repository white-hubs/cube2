package com.guhecloud.cube.srv.gen.attendance.management.server.operationlog.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

/**
 * @author wql
 * @describe 操作日志记录查询参数
 * @date 2023/6/12 15:06
 */
@Data
public class AttendanceLogQueryParam extends Paging {
    /**
     * 查询时间
     */
    private Period period;
    /**
     * 业务应用
     */
    private String businessType;
    /**
     * 操作人
     */
    private Long userId;
    /**
     * 操作人名称
     */
    private String userName;
    /**
     * 关键字
     */
    private String keyword;
    /**
     * 操作对象
     */
    private String objType;
}

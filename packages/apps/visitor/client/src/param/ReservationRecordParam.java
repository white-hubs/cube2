package com.guhecloud.cube.srv.gen.visitor.management.client.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/12/9 11:52
 * @Description:
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ReservationRecordParam extends Paging {
    private static final long serialVersionUID = 4191988961133974634L;
    private Long id;
    /**
     * // 预约时间段
     */
    private Period period;
    /**
     * // 预约人
     */
    private String name;
    /**
     * // 手机号
     */
    private String phone;
    /**
     * 状态列表
     */
    private List<Integer> statusList;
}

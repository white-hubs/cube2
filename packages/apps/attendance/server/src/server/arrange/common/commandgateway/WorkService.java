package com.guhecloud.cube.srv.gen.attendance.management.server.arrange.common.commandgateway;

import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.interfaces.vo.ArrangeWorkVO;
import com.guhecloud.platform.base.model.Period;

import java.util.List;

/**
 * @author bq
 * @date 2022/11/16
 * @description 排班和值班公共Service
 */
public interface WorkService {

    /**
     * 某月或某时间段内的班次排序
     *
     * @param dateOfClasses 待排序班次
     * @param month         月份
     * @param period        时间段
     */
    List<ArrangeWorkVO.DateOfClasses> sortDateOfClasses(List<ArrangeWorkVO.DateOfClasses> dateOfClasses, String month, Period period);

}

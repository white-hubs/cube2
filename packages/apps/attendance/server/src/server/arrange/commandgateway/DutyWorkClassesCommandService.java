package com.guhecloud.cube.srv.gen.attendance.management.server.arrange.commandgateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.po.DutyWorkClassesPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.domain.classes.Classes;

import java.util.List;

/**
 * @author bq
 * @date 2022/11/28
 * @description
 */
public interface DutyWorkClassesCommandService extends IService<DutyWorkClassesPO> {

    /**
     * 保存某一天的所有班次信息
     *
     * @param classes 班次
     *
     * @result 保存后的ID数组
     */
    List<String> saveClassesByOneDay(List<Classes> classes);

}

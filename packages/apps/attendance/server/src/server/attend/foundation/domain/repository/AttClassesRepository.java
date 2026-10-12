package com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.domain.repository;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttClassesPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.param.AttClassesParam;

public interface AttClassesRepository {

    /**
     * 编辑考勤班次
     *
     * @param param 班次对象
     */
    void updateAttClasses(AttClassesParam param, AttClassesPO oldPo, AttClassesPO newPo);

    /**
     * 新增班次
     *
     * @param classesPO 班次
     */
    void insertAttClasses(AttClassesPO classesPO);

    /**
     * 删除班次
     *
     * @param classesPO 班次信息
     */
    void deleteAttClasses(AttClassesPO classesPO);
}

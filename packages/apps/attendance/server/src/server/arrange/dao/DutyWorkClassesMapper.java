package com.guhecloud.cube.srv.gen.attendance.management.server.arrange.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.po.DutyWorkClassesPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.domain.classes.Classes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author bq
 * @date 2022/11/16
 */
@Mapper
public interface DutyWorkClassesMapper extends BaseMapper<DutyWorkClassesPO> {

    /**
     * 查询员工某天的班次列表
     *
     * @param userId 用户ID
     * @param date   日期
     *
     * @return
     */
    List<Classes> getUserClassesByDay(@Param("userId") Long userId, @Param("date") LocalDateTime date);

    /**
     * 查询未被引用的班次IDS
     *
     * @return
     */
    List<Long> getNoUseWorkClasses();

}

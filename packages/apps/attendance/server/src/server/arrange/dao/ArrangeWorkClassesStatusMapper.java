package com.guhecloud.cube.srv.gen.attendance.management.server.arrange.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.po.ArrangeWorkClassesStatusPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author appet
 * @date 2022/11/30
 */
@Mapper
public interface ArrangeWorkClassesStatusMapper extends BaseMapper<ArrangeWorkClassesStatusPO> {

    void insertOrUpdateClassesStatus(@Param("param") ArrangeWorkClassesStatusPO command);
}

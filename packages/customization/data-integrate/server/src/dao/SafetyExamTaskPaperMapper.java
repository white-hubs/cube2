package com.guhecloud.cube.base.dataintegrate.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.base.dataintegrate.server.entity.SafetyExamTaskPaper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SafetyExamTaskPaperMapper extends BaseMapper<SafetyExamTaskPaper> {

    void insertOrUpdateBatch(List<SafetyExamTaskPaper> dataList);
}

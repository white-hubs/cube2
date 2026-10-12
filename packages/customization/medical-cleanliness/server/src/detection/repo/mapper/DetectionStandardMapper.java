package com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.repo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.entity.Standard;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.vo.StandardTreeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DetectionStandardMapper extends BaseMapper<Standard> {

    /**
     * 查询标准+项目数集合
     *
     * @param propList 属性集合
     *
     * @return 查询标准+项目数集合
     */
    List<StandardTreeVO> getStandardTreeList(@Param("propList") List<String> propList);

}
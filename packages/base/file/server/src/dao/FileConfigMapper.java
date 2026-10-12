package com.guhecloud.cube.base.file.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.base.file.server.entity.FileConfig;
import com.guhecloud.cube.base.file.server.vo.FileConfigVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lzy
 * @date 2022/1/5
 */
@Mapper
public interface FileConfigMapper extends BaseMapper<FileConfig> {
    List<FileConfigVO> getList(@Param("type") String type);
}

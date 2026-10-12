package com.guhecloud.cube.base.file.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.base.file.server.entity.FileExpirationPolicy;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileExpirationPolicyMapper extends BaseMapper<FileExpirationPolicy> {
}

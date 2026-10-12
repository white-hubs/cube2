package com.guhecloud.cube.srv.gen.securitymanagement.server.information.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.entity.PlayRecord;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.param.PlayRecordParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.vo.PlayRecordPageVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PlayRecordMapper extends BaseMapper<PlayRecord> {
    Page<PlayRecordPageVo> getPlayRecordPage(Page page, @Param("param") PlayRecordParam param);
}

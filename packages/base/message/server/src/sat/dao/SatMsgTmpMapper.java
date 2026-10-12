package com.guhecloud.cube.base.message2.server.sat.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.message2.server.sat.entity.SatMsgTmp;
import com.guhecloud.cube.base.message2.server.sat.param.SatMsgTmpQueryParam;
import com.guhecloud.cube.base.message2.server.sat.vo.SatMsgTmpPageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 租户消息模板mapper
 *
 * @author lanchun
 * @date 2023/3/7 11:00
 */
@Mapper
public interface SatMsgTmpMapper extends BaseMapper<SatMsgTmp> {

    /**
     * 模板分页列表
     *
     * @param page
     * @param param
     *
     * @return
     */
    Page<SatMsgTmpPageVO> selectSatMsgTmpPage(Page page, @Param("param") SatMsgTmpQueryParam param);

    /**
     * 模板列表
     *
     * @param param
     *
     * @return
     */
    List<SatMsgTmpPageVO> selectSatMsgTemplates(@Param("param") SatMsgTmpQueryParam param);
}

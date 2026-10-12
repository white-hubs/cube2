package com.guhecloud.cube.srv.gen.official.management.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.official.management.server.dao.dto.CmmFlowStatDTO;
import com.guhecloud.cube.srv.gen.official.management.server.dao.dto.CmmFlowStatDetailDTO;
import com.guhecloud.cube.srv.gen.official.management.server.dao.dto.DocumentReceivedStatDTO;
import com.guhecloud.cube.srv.gen.official.management.server.dao.dto.FocusWorkStatDTO;
import com.guhecloud.cube.srv.gen.official.management.server.dao.dto.FocusWorkUpdateStatDTO;
import com.guhecloud.cube.srv.gen.official.management.server.dao.dto.OfficialSealStatDTO;
import com.guhecloud.cube.srv.gen.official.management.server.dao.dto.OfficialSealStatDetailDTO;
import com.guhecloud.cube.srv.gen.official.management.server.dao.query.FlowCmmQuery;
import com.guhecloud.cube.srv.gen.official.management.server.interfaces.vo.HomePageStatisticsVO;
import com.guhecloud.cube.srv.gen.official.management.server.po.FlowFormDataPO;
import com.guhecloud.platform.base.model.Period;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author appet
 * @date 2023/02/21
 */
@Mapper
public interface FlowFormDataMapper extends BaseMapper<FlowFormDataPO> {

    /**
     * 通用统计
     *
     * @param page
     * @param param
     *
     * @return
     */
    Page<CmmFlowStatDTO> cmmFlowStat(Page page, @Param("param") FlowCmmQuery param);

    List<CmmFlowStatDTO> cmmFlowStat(@Param("param") FlowCmmQuery param);

    /**
     * 通用统计详情
     *
     * @param param
     *
     * @return
     */
    List<CmmFlowStatDetailDTO> cmmFlowStatDetail(@Param("param") FlowCmmQuery param);

    Page<CmmFlowStatDetailDTO> cmmFlowStatDetail(Page page, @Param("param") FlowCmmQuery param);

    /**
     * 用印统计
     *
     * @param page
     * @param param
     *
     * @return
     */
    Page<OfficialSealStatDTO> officialSealStat(Page page, @Param("param") FlowCmmQuery param);

    List<OfficialSealStatDTO> officialSealStat(@Param("param") FlowCmmQuery param);

    /**
     * 用印统计详情
     *
     * @param param
     *
     * @return
     */
    List<OfficialSealStatDetailDTO> officialSealStatDetail(@Param("param") FlowCmmQuery param);

    Page<OfficialSealStatDetailDTO> officialSealStatDetail(Page page, @Param("param") FlowCmmQuery param);

    /**
     * 来问统计
     *
     * @param page
     * @param param
     *
     * @return
     */
    Page<DocumentReceivedStatDTO> documentReceivedStat(Page page, @Param("param") FlowCmmQuery param);

    List<DocumentReceivedStatDTO> documentReceivedStat(@Param("param") FlowCmmQuery param);

    /**
     * 督办事项统计
     *
     * @param page
     * @param param
     *
     * @return
     */
    Page<FocusWorkStatDTO> focusWorkStat(Page page, @Param("param") FlowCmmQuery param);

    List<FocusWorkStatDTO> focusWorkStat(@Param("param") FlowCmmQuery param);

    /**
     * 督办事项变更统计
     *
     * @param page
     * @param param
     *
     * @return
     */
    Page<FocusWorkUpdateStatDTO> focusWorkUpdateStat(Page page, @Param("param") FlowCmmQuery param);

    List<FocusWorkUpdateStatDTO> focusWorkUpdateStat(@Param("param") FlowCmmQuery param);

    /**
     * 行政管理-公用统计接口
     */

    List<HomePageStatisticsVO> administrationStat(@Param("period") Period period, @Param("businessTypeList") List<String> businessTypeList);
}

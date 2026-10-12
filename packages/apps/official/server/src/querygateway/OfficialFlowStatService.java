package com.guhecloud.cube.srv.gen.official.management.server.querygateway;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.flow.client.FlowResult;
import com.guhecloud.cube.srv.gen.official.management.server.dao.FlowFormDataMapper;
import com.guhecloud.cube.srv.gen.official.management.server.dao.FocusWorkCategoryMapper;
import com.guhecloud.cube.srv.gen.official.management.server.dao.FocusWorkMapper;
import com.guhecloud.cube.srv.gen.official.management.server.dao.OfficialSealMapper;
import com.guhecloud.cube.srv.gen.official.management.server.dao.dto.CmmFlowStatDTO;
import com.guhecloud.cube.srv.gen.official.management.server.dao.dto.CmmFlowStatDetailDTO;
import com.guhecloud.cube.srv.gen.official.management.server.dao.dto.DocumentReceivedStatDTO;
import com.guhecloud.cube.srv.gen.official.management.server.dao.dto.FocusWorkStatDTO;
import com.guhecloud.cube.srv.gen.official.management.server.dao.dto.FocusWorkUpdateStatDTO;
import com.guhecloud.cube.srv.gen.official.management.server.dao.dto.OfficialSealStatDTO;
import com.guhecloud.cube.srv.gen.official.management.server.dao.dto.OfficialSealStatDetailDTO;
import com.guhecloud.cube.srv.gen.official.management.server.dao.query.FlowCmmQuery;
import com.guhecloud.cube.srv.gen.official.management.server.enumeration.OfficialApprovalBizEnum;
import com.guhecloud.cube.srv.gen.official.management.server.interfaces.vo.HomePageStatisticsVO;
import com.guhecloud.cube.srv.gen.official.management.server.po.FlowFormDataPO;
import com.guhecloud.cube.srv.gen.official.management.server.po.FocusWorkCategoryPO;
import com.guhecloud.cube.srv.gen.official.management.server.po.FocusWorkPO;
import com.guhecloud.cube.srv.gen.official.management.server.po.OfficialSealPO;
import com.guhecloud.cube.srv.gen.official.management.server.querygateway.query.FlowStatParam;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OfficialFlowStatService extends ServiceImpl<FlowFormDataMapper, FlowFormDataPO> {

    private final FlowFormDataMapper flowFormDataMapper;
    private final OfficialSealMapper officialSealMapper;
    private final FocusWorkCategoryMapper focusWorkCategoryMapper;
    private final FocusWorkMapper focusWorkMapper;

    /**
     * 制度征求统计
     *
     * @param param
     *
     * @return
     */
    public Page<CmmFlowStatDTO> cmmFlowStat(Page page, FlowCmmQuery param) {
        return flowFormDataMapper.cmmFlowStat(page, param);
    }

    public List<CmmFlowStatDTO> cmmFlowStat(FlowCmmQuery param) {
        return flowFormDataMapper.cmmFlowStat(param);
    }

    /**
     * 制度征求统计详情
     *
     * @param param
     *
     * @return
     */
    public List<CmmFlowStatDetailDTO> cmmFlowStatDetail(FlowCmmQuery param) {
        List<CmmFlowStatDetailDTO> datas = flowFormDataMapper.cmmFlowStatDetail(param);
        if (!CollectionUtils.isEmpty(datas)) {
            //翻译 审批结果
            datas.forEach(d -> {
                String status = FlowResult.PASS.name().equals(d.getStatus()) ? "通过"
                                                                             : (FlowResult.REJECT.name().equals(d.getStatus()) ? "拒绝"
                                                                                                                               : "撤销");
                d.setStatus(status);
            });
        }
        return datas;
    }

    public Page<CmmFlowStatDetailDTO> cmmFlowStatDetail(Page page, FlowCmmQuery param) {
        Page<CmmFlowStatDetailDTO> rtnPage = flowFormDataMapper.cmmFlowStatDetail(page, param);
        if (!CollectionUtils.isEmpty(rtnPage.getRecords())) {
            //翻译 审批结果
            rtnPage.getRecords().forEach(d -> {
                String status = FlowResult.PASS.name().equals(d.getStatus()) ? "通过"
                                                                             : (FlowResult.REJECT.name().equals(d.getStatus()) ? "拒绝"
                                                                                                                               : "撤销");
                d.setStatus(status);
            });
        }
        return rtnPage;
    }

    /**
     * 用印统计
     *
     * @param param
     *
     * @return
     */
    public Page<OfficialSealStatDTO> officialSealStat(FlowStatParam param) {
        FlowCmmQuery query = FlowCmmQuery.builder()
                .businessType(OfficialApprovalBizEnum.OFFICIAL_SEAL.name())
                .startTime(param.getPeriod() != null ? param.getPeriod().getBegin() : null)
                .endTime(param.getPeriod() != null ? param.getPeriod().getEnd() : null)
                .build();
        Page<OfficialSealStatDTO> page = flowFormDataMapper.officialSealStat(PagingUtils.convert(param), query);
        if (!CollectionUtils.isEmpty(page.getRecords())) {
            page.getRecords().forEach(d -> {
                OfficialSealPO sealPO = officialSealMapper.selectById(d.getSealId());
                d.setSealName(sealPO != null ? sealPO.getSealName() : "");
            });
        }
        return page;
    }

    public List<OfficialSealStatDTO> officialSealStatForList(FlowStatParam param) {
        FlowCmmQuery query = FlowCmmQuery.builder()
                .businessType(OfficialApprovalBizEnum.OFFICIAL_SEAL.name())
                .startTime(param.getPeriod() != null ? param.getPeriod().getBegin() : null)
                .endTime(param.getPeriod() != null ? param.getPeriod().getEnd() : null)
                .build();
        List<OfficialSealStatDTO> list = flowFormDataMapper.officialSealStat(query);
        if (!CollectionUtils.isEmpty(list)) {
            list.stream().forEach(d -> {
                OfficialSealPO sealPO = officialSealMapper.selectById(d.getSealId());
                d.setSealName(sealPO != null ? sealPO.getSealName() : "");
            });
        }
        return list;
    }

    /**
     * 用印统计详情
     *
     * @param param
     *
     * @return
     */
    public Page<OfficialSealStatDetailDTO> officialSealStatDetail(FlowStatParam param) {
        FlowCmmQuery query = FlowCmmQuery.builder()
                .businessType(OfficialApprovalBizEnum.OFFICIAL_SEAL.name())
                .name(param.getName()).id(param.getId())
                .startTime(param.getPeriod() != null ? param.getPeriod().getBegin() : null)
                .endTime(param.getPeriod() != null ? param.getPeriod().getEnd() : null)
                .build();
        Page<OfficialSealStatDetailDTO> page = flowFormDataMapper.officialSealStatDetail(PagingUtils.convert(param), query);
        if (!CollectionUtils.isEmpty(page.getRecords())) {
            OfficialSealPO seal = officialSealMapper.selectById(param.getId());
            page.getRecords().stream().forEach(d -> d.setSealName(seal != null ? seal.getSealName() : ""));
        }
        return page;
    }

    public List<OfficialSealStatDetailDTO> officialSealStatDetailForList(FlowStatParam param) {
        FlowCmmQuery query = FlowCmmQuery.builder()
                .businessType(OfficialApprovalBizEnum.OFFICIAL_SEAL.name())
                .name(param.getName()).id(param.getId())
                .startTime(param.getPeriod() != null ? param.getPeriod().getBegin() : null)
                .endTime(param.getPeriod() != null ? param.getPeriod().getEnd() : null)
                .build();
        List<OfficialSealStatDetailDTO> datas = flowFormDataMapper.officialSealStatDetail(query);
        if (!CollectionUtils.isEmpty(datas)) {
            OfficialSealPO seal = officialSealMapper.selectById(param.getId());
            datas.forEach(d -> d.setSealName(seal != null ? seal.getSealName() : ""));
        }
        return datas;
    }

    public Page<DocumentReceivedStatDTO> documentReceivedStat(FlowStatParam param) {
        FlowCmmQuery query = FlowCmmQuery.builder()
                .businessType(OfficialApprovalBizEnum.OFFICIAL_DOCUMENT_RECEIVED.name())
                .status(FlowResult.PASS.name())
                .startTime(param.getPeriod() != null ? param.getPeriod().getBegin() : null)
                .endTime(param.getPeriod() != null ? param.getPeriod().getEnd() : null)
                .build();
        Page<DocumentReceivedStatDTO> page = flowFormDataMapper.documentReceivedStat(PagingUtils.convert(param), query);
        if (!CollectionUtils.isEmpty(page.getRecords())) {
            page.getRecords().forEach(d -> {
                var timeLimit = DateUtils.parse(d.getTimeLimit());
                d.setTimeout("否");
                if (d.getApplyDoneTime().isAfter(timeLimit)) {
                    d.setTimeout("是");
                    long millis = DateUtils.between(timeLimit, d.getApplyDoneTime(), ChronoUnit.MILLIS);
                    d.setTimeoutDesc(DateUtils.formatBetween(millis, ChronoUnit.MINUTES));
                }
            });
        }
        return page;
    }

    public List<DocumentReceivedStatDTO> documentReceivedStatForList(FlowStatParam param) {
        FlowCmmQuery query = FlowCmmQuery.builder()
                .businessType(OfficialApprovalBizEnum.OFFICIAL_DOCUMENT_RECEIVED.name())
                .status(FlowResult.PASS.name())
                .startTime(param.getPeriod() != null ? param.getPeriod().getBegin() : null)
                .endTime(param.getPeriod() != null ? param.getPeriod().getEnd() : null)
                .build();
        List<DocumentReceivedStatDTO> datas = flowFormDataMapper.documentReceivedStat(query);
        if (!CollectionUtils.isEmpty(datas)) {
            datas.forEach(d -> {
                var timeLimit = DateUtils.parse(d.getTimeLimit());
                d.setTimeout("否");
                if (d.getApplyDoneTime().isAfter(timeLimit)) {
                    d.setTimeout("是");
                    long millis = DateUtils.between(timeLimit, d.getApplyDoneTime(), ChronoUnit.MILLIS);
                    d.setTimeoutDesc(DateUtils.formatBetween(millis, ChronoUnit.MINUTES));
                }
            });
        }
        return datas;
    }

    /**
     * 督办事项统计
     *
     * @param param
     *
     * @return
     */
    public Page<FocusWorkStatDTO> focusWorkStatPage(FlowStatParam param) {
        FlowCmmQuery query = FlowCmmQuery.builder()
                .businessType(OfficialApprovalBizEnum.OFFICIAL_FOCUS_WORK.name())
                .startTime(param.getPeriod() != null ? param.getPeriod().getBegin() : null)
                .endTime(param.getPeriod() != null ? param.getPeriod().getEnd() : null)
                .build();
        Page<FocusWorkStatDTO> page = flowFormDataMapper.focusWorkStat(PagingUtils.convert(param), query);

        if (!CollectionUtils.isEmpty(page.getRecords())) {
            page.getRecords().stream().forEach(d -> {
                FocusWorkCategoryPO categoryPO = focusWorkCategoryMapper.selectById(d.getWorkCategoryId());
                d.setWorkCategoryName(categoryPO != null ? categoryPO.getName() : "");
            });
        }
        return page;
    }

    public List<FocusWorkStatDTO> focusWorkStatList(FlowStatParam param) {
        FlowCmmQuery query = FlowCmmQuery.builder()
                .businessType(OfficialApprovalBizEnum.OFFICIAL_FOCUS_WORK.name())
                .startTime(param.getPeriod() != null ? param.getPeriod().getBegin() : null)
                .endTime(param.getPeriod() != null ? param.getPeriod().getEnd() : null)
                .build();
        List<FocusWorkStatDTO> datas = flowFormDataMapper.focusWorkStat(query);

        if (!CollectionUtils.isEmpty(datas)) {
            datas.stream().forEach(d -> {
                FocusWorkCategoryPO categoryPO = focusWorkCategoryMapper.selectById(d.getWorkCategoryId());
                d.setWorkCategoryName(categoryPO != null ? categoryPO.getName() : "");
            });
        }
        return datas;
    }

    /**
     * 督办事项变更统计
     *
     * @param param
     *
     * @return
     */
    public Page<FocusWorkUpdateStatDTO> focusWorkUpdateStatPage(FlowStatParam param) {
        FlowCmmQuery query = FlowCmmQuery.builder()
                .businessType(OfficialApprovalBizEnum.OFFICIAL_FOCUS_WORK_UPDATE.name())
                .status(FlowResult.PASS.name())
                .startTime(param.getPeriod() != null ? param.getPeriod().getBegin() : null)
                .endTime(param.getPeriod() != null ? param.getPeriod().getEnd() : null)
                .build();
        Page<FocusWorkUpdateStatDTO> page = flowFormDataMapper.focusWorkUpdateStat(PagingUtils.convert(param), query);

        if (!CollectionUtils.isEmpty(page.getRecords())) {
            page.getRecords().stream().forEach(d -> {
                FocusWorkPO focusWorkPO = this.focusWorkMapper.selectById(d.getFocusWorkId());
                if (focusWorkPO != null) {
                    d.setFocusWorkName(focusWorkPO.getWorkDesc());
                    FocusWorkCategoryPO categoryPO = focusWorkCategoryMapper.selectById(focusWorkPO.getWorkCategoryId());
                    d.setWorkCategoryName(categoryPO != null ? categoryPO.getName() : "");
                }
            });
        }
        return page;
    }

    public List<FocusWorkUpdateStatDTO> focusWorkUpdateStatList(FlowStatParam param) {
        FlowCmmQuery query = FlowCmmQuery.builder()
                .businessType(OfficialApprovalBizEnum.OFFICIAL_FOCUS_WORK_UPDATE.name())
                .status(FlowResult.PASS.name())
                .startTime(param.getPeriod() != null ? param.getPeriod().getBegin() : null)
                .endTime(param.getPeriod() != null ? param.getPeriod().getEnd() : null)
                .build();
        List<FocusWorkUpdateStatDTO> datas = flowFormDataMapper.focusWorkUpdateStat(query);

        if (!CollectionUtils.isEmpty(datas)) {
            datas.stream().forEach(d -> {
                FocusWorkPO focusWorkPO = this.focusWorkMapper.selectById(d.getFocusWorkId());
                if (focusWorkPO != null) {
                    d.setFocusWorkName(focusWorkPO.getWorkDesc());
                    FocusWorkCategoryPO categoryPO = focusWorkCategoryMapper.selectById(focusWorkPO.getWorkCategoryId());
                    d.setWorkCategoryName(categoryPO != null ? categoryPO.getName() : "");
                }
            });
        }
        return datas;
    }

    public List<HomePageStatisticsVO> focusWorkHomePageStatistics(Period period) {
        List<HomePageStatisticsVO> homePageStatisticsVOS = flowFormDataMapper.administrationStat(period, OfficialApprovalBizEnum.getStatEnumList());
        Map<String, HomePageStatisticsVO> integerMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(homePageStatisticsVOS)) {
            integerMap = homePageStatisticsVOS.stream().collect(Collectors.toMap(HomePageStatisticsVO::getBusinessType,
                                                                                 Function.identity(), (k1, k2) -> k1));
        }
        List<HomePageStatisticsVO> result = new ArrayList<>();
        for (String key : OfficialApprovalBizEnum.getStatEnumList()) {
            HomePageStatisticsVO orDefault = integerMap.getOrDefault(key, new HomePageStatisticsVO());
            orDefault.setBusinessType(key);
            orDefault.setBusinessTypeName(OfficialApprovalBizEnum.valueOf(key).getDesc());
            if (Objects.isNull(orDefault.getApplyNum())) {
                orDefault.setApplyNum(0);
            }
            result.add(orDefault);
        }
        return result;
    }
}

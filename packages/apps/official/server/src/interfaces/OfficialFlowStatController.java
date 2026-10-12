package com.guhecloud.cube.srv.gen.official.management.server.interfaces;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.flow.client.FlowResult;
import com.guhecloud.cube.srv.gen.official.management.server.dao.dto.CmmFlowStatDTO;
import com.guhecloud.cube.srv.gen.official.management.server.dao.dto.CmmFlowStatDetailDTO;
import com.guhecloud.cube.srv.gen.official.management.server.dao.dto.DocumentReceivedStatDTO;
import com.guhecloud.cube.srv.gen.official.management.server.dao.dto.FocusWorkStatDTO;
import com.guhecloud.cube.srv.gen.official.management.server.dao.dto.FocusWorkUpdateStatDTO;
import com.guhecloud.cube.srv.gen.official.management.server.dao.dto.OfficialSealStatDTO;
import com.guhecloud.cube.srv.gen.official.management.server.dao.dto.OfficialSealStatDetailDTO;
import com.guhecloud.cube.srv.gen.official.management.server.dao.query.FlowCmmQuery;
import com.guhecloud.cube.srv.gen.official.management.server.enumeration.OfficialApprovalBizEnum;
import com.guhecloud.cube.srv.gen.official.management.server.interfaces.util.ExcelUtils;
import com.guhecloud.cube.srv.gen.official.management.server.interfaces.vo.CmmFlowStatDetailVO;
import com.guhecloud.cube.srv.gen.official.management.server.interfaces.vo.CmmFlowStatVO;
import com.guhecloud.cube.srv.gen.official.management.server.interfaces.vo.DocumentReceivedStatVO;
import com.guhecloud.cube.srv.gen.official.management.server.interfaces.vo.FocusWorkStatVO;
import com.guhecloud.cube.srv.gen.official.management.server.interfaces.vo.FocusWorkUpdateStatVO;
import com.guhecloud.cube.srv.gen.official.management.server.interfaces.vo.HomePageStatisticsVO;
import com.guhecloud.cube.srv.gen.official.management.server.interfaces.vo.OfficialSealStatDetailVO;
import com.guhecloud.cube.srv.gen.official.management.server.interfaces.vo.OfficialSealStatVO;
import com.guhecloud.cube.srv.gen.official.management.server.querygateway.OfficialFlowStatService;
import com.guhecloud.cube.srv.gen.official.management.server.querygateway.query.FlowStatParam;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.permission.annotation.DataPermissionSwitch;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 行政流程统计
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OfficialFlowStatController {
    private final OfficialFlowStatService officialFlowStatService;

    /**
     * 制度征求统计
     *
     * @return
     */
    @GetMapping("/regulation/stat")
    public Pagination<CmmFlowStatVO> regulationStat(FlowStatParam param) {
        FlowCmmQuery query = FlowCmmQuery.builder()
                .businessType(OfficialApprovalBizEnum.OFFICIAL_REGULATION.name())
                .status(FlowResult.PASS.name())
                .startTime(param.getPeriod() != null ? param.getPeriod().getBegin() : null)
                .endTime(param.getPeriod() != null ? param.getPeriod().getEnd() : null)
                .build();
        Page<CmmFlowStatDTO> page = officialFlowStatService.cmmFlowStat(PagingUtils.convert(param), query);
        return PagingUtils.convert(page, CmmFlowStatVO.class);
    }

    @PostMapping("/export/regulation/stat")
    public void regulationStatExport(@RequestBody FlowStatParam param, HttpServletRequest request, HttpServletResponse response) {
        FlowCmmQuery query = FlowCmmQuery.builder()
                .businessType(OfficialApprovalBizEnum.OFFICIAL_REGULATION.name())
                .startTime(param.getPeriod() != null ? param.getPeriod().getBegin() : null)
                .endTime(param.getPeriod() != null ? param.getPeriod().getEnd() : null)
                .build();
        List<CmmFlowStatDTO> datas = officialFlowStatService.cmmFlowStat(query);
        Assert.isNotAcceptable(CollectionUtils.isEmpty(datas), "未查询到数据，无法导出");
        //导出excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("deptName", "所在部门");
        writer.addHeaderAlias("applyNum", "征求次数");

        writer.setOnlyAlias(true);
        //默认配置
        writer.write(datas, true);
        writer.setColumnWidth(-1, 25);

        String fileName = "制度征求统计";
        ExcelUtils.downloadExcel(request, response, fileName, writer);
    }

    /**
     * 制度征求统计详情
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/regulation/stat/{deptId}/detail")
    public Pagination<CmmFlowStatDetailVO> regulationStatDetail(@PathVariable Long deptId, FlowStatParam param) {
        FlowCmmQuery query = FlowCmmQuery.builder()
                .businessType(OfficialApprovalBizEnum.OFFICIAL_REGULATION.name())
                .status(FlowResult.PASS.name())
                .deptId(deptId)
                .startTime(param.getPeriod() != null ? param.getPeriod().getBegin() : null)
                .endTime(param.getPeriod() != null ? param.getPeriod().getEnd() : null)
                .build();
        Page<CmmFlowStatDetailDTO> page = officialFlowStatService.cmmFlowStatDetail(PagingUtils.convert(param), query);
        return PagingUtils.convert(page, CmmFlowStatDetailVO.class);
    }

    /**
     * 制度征求统计详情导出
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/export/regulation/stat/{deptId}/detail")
    public void regulationStatDetailExport(@PathVariable Long deptId,
                                           @RequestBody FlowStatParam param,
                                           HttpServletRequest request,
                                           HttpServletResponse response
                                          ) {
        FlowCmmQuery query = FlowCmmQuery.builder()
                .businessType(OfficialApprovalBizEnum.OFFICIAL_REGULATION.name())
                .deptId(deptId)
                .startTime(param.getPeriod() != null ? param.getPeriod().getBegin() : null)
                .endTime(param.getPeriod() != null ? param.getPeriod().getEnd() : null)
                .build();
        List<CmmFlowStatDetailDTO> datas = officialFlowStatService.cmmFlowStatDetail(query);
        Assert.isNotAcceptable(CollectionUtils.isEmpty(datas), "未查询到数据，无法导出");
        //导出excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("deptName", "所在部门");
        writer.addHeaderAlias("applicantName", "申请人");
        writer.addHeaderAlias("status", "征求结果");
        writer.addHeaderAlias("applyTime", "征求时间");
        writer.addHeaderAlias("applyDoneTime", "处理时间");

        writer.setOnlyAlias(true);
        //默认配置
        writer.write(datas, true);
        writer.setColumnWidth(-1, 25);

        String fileName = "制度征求明细统计";
        ExcelUtils.downloadExcel(request, response, fileName, writer);
    }

    /**
     * 用印统计
     *
     * @return
     */
    @GetMapping("/officialSeal/stat")
    public Pagination<OfficialSealStatVO> officialSealStat(FlowStatParam param) {
        Page<OfficialSealStatDTO> page = officialFlowStatService.officialSealStat(param);
        return PagingUtils.convert(page, OfficialSealStatVO.class);
    }

    @PostMapping("/export/officialSeal/stat")
    public void officialSealStat(@RequestBody FlowStatParam param, HttpServletRequest request, HttpServletResponse response) {
        List<OfficialSealStatDTO> datas = officialFlowStatService.officialSealStatForList(param);
        Assert.isNotAcceptable(CollectionUtils.isEmpty(datas), "未查询到数据，无法导出");
        //导出excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("sealName", "申请印章");
        writer.addHeaderAlias("applicantNum", "申请人数");
        writer.addHeaderAlias("applyNum", "申请次数");

        writer.setOnlyAlias(true);
        //默认配置
        writer.write(datas, true);
        writer.setColumnWidth(-1, 25);

        String fileName = "用印统计";
        ExcelUtils.downloadExcel(request, response, fileName, writer);
    }

    /**
     * 用印统计详情
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/officialSeal/stat/{sealId}/detail")
    public Pagination<OfficialSealStatDetailVO> officialSealStatDetail(@PathVariable Long sealId, FlowStatParam param) {
        param.setId(sealId);
        Page<OfficialSealStatDetailDTO> page = officialFlowStatService.officialSealStatDetail(param);
        return PagingUtils.convert(page, OfficialSealStatDetailVO.class);
    }

    /**
     * 用印统计详情导出
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/export/officialSeal/stat/{sealId}/detail")
    public void officialSealStatDetailExport(@PathVariable Long sealId,
                                             @RequestBody FlowStatParam param,
                                             HttpServletRequest request,
                                             HttpServletResponse response
                                            ) {
        param.setId(sealId);
        List<OfficialSealStatDetailDTO> datas = officialFlowStatService.officialSealStatDetailForList(param);
        Assert.isNotAcceptable(CollectionUtils.isEmpty(datas), "未查询到数据，无法导出");
        //导出excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("sealName", "申请印章");
        writer.addHeaderAlias("applicantName", "申请人");
        writer.addHeaderAlias("applyTime", "申请时间");
        writer.addHeaderAlias("applyDoneTime", "审批时间");

        writer.setOnlyAlias(true);
        //默认配置
        writer.write(datas, true);
        writer.setColumnWidth(-1, 25);

        String fileName = "用印详情统计";
        ExcelUtils.downloadExcel(request, response, fileName, writer);
    }

    /**
     * 议题预报统计
     *
     * @return
     */
    @GetMapping("/topic/stat")
    public Pagination<CmmFlowStatVO> topicStat(FlowStatParam param) {
        FlowCmmQuery query = FlowCmmQuery.builder()
                .businessType(OfficialApprovalBizEnum.OFFICIAL_TOPIC.name())
                .status(FlowResult.PASS.name())
                .startTime(param.getPeriod() != null ? param.getPeriod().getBegin() : null)
                .endTime(param.getPeriod() != null ? param.getPeriod().getEnd() : null)
                .build();
        Page<CmmFlowStatDTO> page = officialFlowStatService.cmmFlowStat(PagingUtils.convert(param), query);
        return PagingUtils.convert(page, CmmFlowStatVO.class);
    }

    /**
     * 导出
     *
     * @param param
     * @param request
     * @param response
     */
    @PostMapping("/export/topic/stat")
    public void topicStatExport(@RequestBody FlowStatParam param, HttpServletRequest request, HttpServletResponse response) {
        FlowCmmQuery query = FlowCmmQuery.builder()
                .businessType(OfficialApprovalBizEnum.OFFICIAL_TOPIC.name())
                .startTime(param.getPeriod() != null ? param.getPeriod().getBegin() : null)
                .endTime(param.getPeriod() != null ? param.getPeriod().getEnd() : null)
                .build();
        List<CmmFlowStatDTO> datas = officialFlowStatService.cmmFlowStat(query);
        Assert.isNotAcceptable(CollectionUtils.isEmpty(datas), "未查询到数据，无法导出");
        //导出excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("deptName", "所在部门");
        writer.addHeaderAlias("applyNum", "议题预报次数");

        writer.setOnlyAlias(true);
        //默认配置
        writer.write(datas, true);
        writer.setColumnWidth(-1, 25);

        String fileName = "议题预报统计";
        ExcelUtils.downloadExcel(request, response, fileName, writer);
    }

    /**
     * 议题预报统计详情
     *
     * @param deptId
     *
     * @return
     */
    @GetMapping("/topic/stat/{deptId}/detail")
    public Pagination<CmmFlowStatDetailVO> topStatDetail(@PathVariable Long deptId, FlowStatParam param) {
        FlowCmmQuery query = FlowCmmQuery.builder()
                .businessType(OfficialApprovalBizEnum.OFFICIAL_TOPIC.name()).deptId(deptId)
                .status(FlowResult.PASS.name())
                .startTime(param.getPeriod() != null ? param.getPeriod().getBegin() : null)
                .endTime(param.getPeriod() != null ? param.getPeriod().getEnd() : null)
                .build();
        Page<CmmFlowStatDetailDTO> page = officialFlowStatService.cmmFlowStatDetail(PagingUtils.convert(param), query);
        return PagingUtils.convert(page, CmmFlowStatDetailVO.class);
    }

    /**
     * 议题预报详情统计
     *
     * @param deptId
     * @param param
     * @param request
     * @param response
     */
    @PostMapping("/export/topic/stat/{deptId}/detail")
    public void topStatDetailExport(@PathVariable Long deptId,
                                    @RequestBody FlowStatParam param,
                                    HttpServletRequest request,
                                    HttpServletResponse response
                                   ) {
        FlowCmmQuery query = FlowCmmQuery.builder()
                .businessType(OfficialApprovalBizEnum.OFFICIAL_TOPIC.name()).deptId(deptId)
                .startTime(param.getPeriod() != null ? param.getPeriod().getBegin() : null)
                .endTime(param.getPeriod() != null ? param.getPeriod().getEnd() : null)
                .build();
        List<CmmFlowStatDetailDTO> datas = officialFlowStatService.cmmFlowStatDetail(query);
        Assert.isNotAcceptable(CollectionUtils.isEmpty(datas), "未查询到数据，无法导出");
        //导出excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("deptName", "所在部门");
        writer.addHeaderAlias("applicantName", "申请人");
        writer.addHeaderAlias("status", "预报结果");
        writer.addHeaderAlias("applyTime", "预报时间");
        writer.addHeaderAlias("applyDoneTime", "处理时间");

        writer.setOnlyAlias(true);
        //默认配置
        writer.write(datas, true);
        writer.setColumnWidth(-1, 25);

        String fileName = "议题预报统计";
        ExcelUtils.downloadExcel(request, response, fileName, writer);
    }

    /**
     * 来文处理统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/documentReceived/stat")
    public Pagination<DocumentReceivedStatVO> documentReceivedStat(FlowStatParam param) {
        Page<DocumentReceivedStatDTO> page = officialFlowStatService.documentReceivedStat(param);
        return PagingUtils.convert(page, DocumentReceivedStatVO.class);
    }

    @PostMapping("/export/documentReceived/stat")
    public void documentReceivedStat(@RequestBody FlowStatParam param,
                                     HttpServletRequest request,
                                     HttpServletResponse response
                                    ) {
        List<DocumentReceivedStatDTO> datas = officialFlowStatService.documentReceivedStatForList(param);
        Assert.isNotAcceptable(CollectionUtils.isEmpty(datas), "未查询到数据，无法导出");
        //导出excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("fromUnit", "来文单位");
        writer.addHeaderAlias("fileCode", "来文编号");
        writer.addHeaderAlias("applicantName", "发起人");
        writer.addHeaderAlias("applyTime", "发起时间");
        writer.addHeaderAlias("applyDoneTime", "处理时间");
        writer.addHeaderAlias("timeout", "是否超时");
        writer.addHeaderAlias("timeoutDesc", "超时时长");

        writer.setOnlyAlias(true);
        //默认配置
        writer.write(datas, true);
        writer.setColumnWidth(-1, 25);

        String fileName = "来文处理统计";
        ExcelUtils.downloadExcel(request, response, fileName, writer);
    }

    /**
     * 部门重点工作统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/keyWork/stat")
    public Pagination<CmmFlowStatVO> keyWorkStat(FlowStatParam param) {
        FlowCmmQuery query = FlowCmmQuery.builder()
                .businessType(OfficialApprovalBizEnum.OFFICIAL_KEY_WORK.name())
                .startTime(param.getPeriod() != null ? param.getPeriod().getBegin() : null)
                .endTime(param.getPeriod() != null ? param.getPeriod().getEnd() : null)
                .build();
        Page<CmmFlowStatDTO> page = officialFlowStatService.cmmFlowStat(PagingUtils.convert(param), query);
        return PagingUtils.convert(page, CmmFlowStatVO.class);
    }

    /**
     * 部门重点工作统计导出
     *
     * @param param
     * @param request
     * @param response
     */
    @PostMapping("/export/keyWork/stat")
    public void keyWorkStatExport(@RequestBody FlowStatParam param,
                                  HttpServletRequest request,
                                  HttpServletResponse response
                                 ) {
        FlowCmmQuery query = FlowCmmQuery.builder()
                .businessType(OfficialApprovalBizEnum.OFFICIAL_KEY_WORK.name())
                .startTime(param.getPeriod() != null ? param.getPeriod().getBegin() : null)
                .endTime(param.getPeriod() != null ? param.getPeriod().getEnd() : null)
                .build();
        List<CmmFlowStatDTO> datas = officialFlowStatService.cmmFlowStat(query);
        Assert.isNotAcceptable(CollectionUtils.isEmpty(datas), "未查询到数据，无法导出");
        //导出excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("deptName", "所在部门");
        writer.addHeaderAlias("applyNum", "发起次数");
        writer.addHeaderAlias("passNum", "审批通过次数");

        writer.setOnlyAlias(true);
        //默认配置
        writer.write(datas, true);
        writer.setColumnWidth(-1, 25);

        String fileName = "部门重点工作统计";
        ExcelUtils.downloadExcel(request, response, fileName, writer);
    }

    /**
     * 督办工作统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/focusWork/stat")
    public Pagination<FocusWorkStatVO> focusWorkStat(FlowStatParam param) {
        Page<FocusWorkStatDTO> page = officialFlowStatService.focusWorkStatPage(param);
        return PagingUtils.convert(page, FocusWorkStatVO.class);
    }

    @PostMapping("/export/focusWork/stat")
    public void focusWorkStatExport(@RequestBody FlowStatParam param,
                                    HttpServletRequest request,
                                    HttpServletResponse response
                                   ) {
        List<FocusWorkStatDTO> datas = officialFlowStatService.focusWorkStatList(param);
        Assert.isNotAcceptable(CollectionUtils.isEmpty(datas), "未查询到数据，无法导出");
        //导出excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("applicantName", "姓名");
        writer.addHeaderAlias("deptName", "部门");
        writer.addHeaderAlias("workCategoryName", "事项类型");
        writer.addHeaderAlias("applyNum", "发起次数");
        writer.addHeaderAlias("passNum", "审批通过次数");

        writer.setOnlyAlias(true);
        //默认配置
        writer.write(datas, true);
        writer.setColumnWidth(-1, 25);

        String fileName = "督办事项统计";
        ExcelUtils.downloadExcel(request, response, fileName, writer);
    }

    /**
     * 督办工作变更统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/focusWork/update/stat")
    public Pagination<FocusWorkUpdateStatVO> focusWorkUpdateStat(FlowStatParam param) {
        Page<FocusWorkUpdateStatDTO> page = officialFlowStatService.focusWorkUpdateStatPage(param);
        return PagingUtils.convert(page, FocusWorkUpdateStatVO.class);
    }

    @PostMapping("/export/focusWork/update/stat")
    public void focusWorkUpdateStat(@RequestBody FlowStatParam param,
                                    HttpServletRequest request,
                                    HttpServletResponse response
                                   ) {
        List<FocusWorkUpdateStatDTO> datas = officialFlowStatService.focusWorkUpdateStatList(param);
        Assert.isNotAcceptable(CollectionUtils.isEmpty(datas), "未查询到数据，无法导出");
        //导出excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("deptName", "部门");
        writer.addHeaderAlias("focusWorkName", "督办事项");
        writer.addHeaderAlias("workCategoryName", "事项类型");
        writer.addHeaderAlias("applyNum", "发起次数");
        writer.addHeaderAlias("stopNum", "中止次数");
        writer.addHeaderAlias("delayNum", "延期次数");

        writer.setOnlyAlias(true);
        //默认配置
        writer.write(datas, true);
        writer.setColumnWidth(-1, 25);

        String fileName = "督办事项变更统计";
        ExcelUtils.downloadExcel(request, response, fileName, writer);
    }

    /**
     * 行政管理-统计接口
     *
     * @param period
     *
     * @return
     */
    @GetMapping("/focusWork/home-page/statistics")
    @DataPermissionSwitch(dataModel = "official:flowFormData")
    public List<HomePageStatisticsVO> focusWorkHomePageStatistics(@RequestParam("period") Period period) {
        return officialFlowStatService.focusWorkHomePageStatistics(period);
    }

}

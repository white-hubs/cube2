package com.guhecloud.cube.deliver.project.merchant.controller.store.stat;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guhecloud.cube.deliver.project.merchant.commons.utils.DateUtils;
import com.guhecloud.cube.deliver.project.merchant.commons.utils.ExcelUtils;
import com.guhecloud.cube.deliver.project.merchant.commons.web.PageRequest;
import com.guhecloud.cube.deliver.project.merchant.dto.store.StallEvaluationExcelDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.store.StallScoreDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.store.StallScoreExcelDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.store.StallScoreQueryDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.store.stat.StallScoreStatOverviewDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.Company;
import com.guhecloud.cube.deliver.project.merchant.entity.Space;
import com.guhecloud.cube.deliver.project.merchant.entity.store.StallEvaluationRecord;
import com.guhecloud.cube.deliver.project.merchant.entity.store.StallEvaluationRecordTag;
import com.guhecloud.cube.deliver.project.merchant.entity.store.StallEvaluationTag;
import com.guhecloud.cube.deliver.project.merchant.entity.store.StallSafetyInsp;
import com.guhecloud.cube.deliver.project.merchant.mapper.CompanyMapper;
import com.guhecloud.cube.deliver.project.merchant.service.SpaceService;
import com.guhecloud.cube.deliver.project.merchant.service.store.*;
import com.guhecloud.cube.deliver.project.merchant.commons.web.PageResponse;
import com.guhecloud.cube.deliver.project.merchant.service.store.*;

import com.guhecloud.platform.base.standard.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.guhecloud.cube.deliver.project.merchant.commons.utils.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 档位分值概览+统计报表 前端控制器
 * </p>
 *
 * @author bq
 * @since 2022-04-06
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StallScoreStatOverviewController {

    private final StallSafetyInspService safetyInspService;
    private final StallSafetyMarkService safetyMarkService;
    private final StallEvaluationRecordService evaluationRecordService;
    private final SpaceService spaceService;
    private final StallEvaluationRecordTagService recordTagService;
    private final StallEvaluationTagService tagService;
    private final CompanyMapper companyMapper;

    /**
     * 概览-年度安全生产值TOP10
     *
     * @param queryDTO
     * @return
     */
    @GetMapping("/stall-score-stat/score/top10")
    public List<StallScoreStatOverviewDTO> getTotalScoreTop10(StallScoreQueryDTO queryDTO) {
        DateUtils.setQueryTime(queryDTO, queryDTO.getTimeRange());
        List<StallScoreStatOverviewDTO> dtos = new ArrayList<>();
        List<Space> spaces = this.querySpaceList(queryDTO);
        List<StallSafetyInsp> scoreRecords = this.getScoreRecord(queryDTO);
        scoreRecords.parallelStream().collect(Collectors.groupingBy(StallSafetyInsp::getStallId)).forEach((stallId, list) -> {
            Space space = spaces.stream().filter(s -> s.getId().equals(stallId)).findAny().orElse(null);
            if (space != null) {
                StallScoreStatOverviewDTO dto = new StallScoreStatOverviewDTO();
                // 计算该档位这段时间的扣减总分值
                int sum = list.stream().mapToInt(StallSafetyInsp::getItemResultMark).sum();
                dto.setId(space.getId());
                dto.setName(space.getName());
                dto.setScore(StallSafetyMarkService.STORE_DEFAULT_MARK - sum);
                dtos.add(dto);
            }
        });
        if (queryDTO.isReverseOrder()) {
            return dtos.stream().sorted(Comparator.comparing(StallScoreStatOverviewDTO::getScore)).limit(10).collect(Collectors.toList());
        } else {
            // 没有扣分的档位时默认给100分
            spaces.forEach(s -> {
                if (dtos.parallelStream().noneMatch(dto -> dto.getId().equals(s.getId()))) {
                    StallScoreStatOverviewDTO dto = new StallScoreStatOverviewDTO();
                    dto.setId(s.getId());
                    dto.setName(s.getName());
                    dto.setScore(StallSafetyMarkService.STORE_DEFAULT_MARK);
                    dtos.add(dto);
                }
            });
            return dtos.stream().sorted(Comparator.comparing(StallScoreStatOverviewDTO::getScore).reversed()).limit(10).collect(Collectors.toList());
        }
    }

    /**
     * 概览-安全生产扣分TOP10-弃用
     *
     * @param queryDTO
     * @return
     */
    @GetMapping("/stall-score-stat/deduct-score/top10")
    public List<StallScoreStatOverviewDTO> getDeductScoreTop10(StallScoreQueryDTO queryDTO) {
        DateUtils.setQueryTime(queryDTO, queryDTO.getTimeRange());
        List<StallScoreStatOverviewDTO> dtos = new ArrayList<>();
        List<Space> spaces = this.querySpaceList(queryDTO);
        List<StallSafetyInsp> scoreRecords = this.getScoreRecord(queryDTO);
        scoreRecords.parallelStream().collect(Collectors.groupingBy(StallSafetyInsp::getStallId)).forEach((stallId, list) -> {
            Space space = spaces.stream().filter(s -> s.getId().equals(stallId)).findAny().orElse(null);
            if (space != null) {
                StallScoreStatOverviewDTO dto = new StallScoreStatOverviewDTO();
                // 计算该档位这段时间的扣减总分值
                int sum = list.stream().mapToInt(StallSafetyInsp::getItemResultMark).sum();
                dto.setId(space.getId());
                dto.setName(space.getName());
                dto.setScore(sum);
                dtos.add(dto);
            }
        });
        return dtos.stream().sorted(Comparator.comparing(StallScoreStatOverviewDTO::getScore).reversed()).limit(10).collect(Collectors.toList());
    }

    /**
     * 概览-评价的五项TOP10
     *
     * @param queryDTO
     * @return
     */
    @GetMapping("/stall-score-stat/evaluation/top10")
    public StallScoreStatOverviewDTO getEvaluationTop10(StallScoreQueryDTO queryDTO) {
        StallScoreStatOverviewDTO dto = new StallScoreStatOverviewDTO();
        // 诚信经营top10
        List<StallScoreStatOverviewDTO> manageTop10 = new ArrayList<>();
        // 商品品质top10
        List<StallScoreStatOverviewDTO> qualityTop10 = new ArrayList<>();
        // 诚实守信top10
        List<StallScoreStatOverviewDTO> honestyTop10 = new ArrayList<>();
        // 服务态度top10
        List<StallScoreStatOverviewDTO> attitudeTop10 = new ArrayList<>();
        // 环境卫生top10
        List<StallScoreStatOverviewDTO> envTop10 = new ArrayList<>();
        DateUtils.setQueryTime(queryDTO, queryDTO.getTimeRange());
        List<Space> spaces = this.querySpaceList(queryDTO);
        List<StallEvaluationRecord> evaluationRecords = this.getEvaluationRecord(queryDTO);
        evaluationRecords.parallelStream().collect(Collectors.groupingBy(StallEvaluationRecord::getStallId)).forEach((stallId, list) -> {
            Space space = spaces.stream().filter(s -> s.getId().equals(stallId)).findAny().orElse(null);
            if (space != null) {
                int times = list.size();
                manageTop10.add(this.countEvaluation(this.rounding(list.stream().mapToDouble(StallEvaluationRecord::getHonestyScore).sum()), times, space));
                qualityTop10.add(this.countEvaluation(list.stream().mapToInt(StallEvaluationRecord::getZScore).sum(), times, space));
                honestyTop10.add(this.countEvaluation(list.stream().mapToInt(StallEvaluationRecord::getSScore).sum(), times, space));
                attitudeTop10.add(this.countEvaluation(list.stream().mapToInt(StallEvaluationRecord::getTScore).sum(), times, space));
                envTop10.add(this.countEvaluation(list.stream().mapToInt(StallEvaluationRecord::getWScore).sum(), times, space));
            }
        });
        // 截取前10名
        if (queryDTO.isReverseOrder()) {
            dto.setManageTop10(manageTop10.stream().sorted(Comparator.comparing(StallScoreStatOverviewDTO::getScore)).limit(10).collect(Collectors.toList()));
            dto.setQualityTop10(qualityTop10.stream().sorted(Comparator.comparing(StallScoreStatOverviewDTO::getScore)).limit(10).collect(Collectors.toList()));
            dto.setHonestyTop10(honestyTop10.stream().sorted(Comparator.comparing(StallScoreStatOverviewDTO::getScore)).limit(10).collect(Collectors.toList()));
            dto.setAttitudeTop10(attitudeTop10.stream().sorted(Comparator.comparing(StallScoreStatOverviewDTO::getScore)).limit(10).collect(Collectors.toList()));
            dto.setEnvTop10(envTop10.stream().sorted(Comparator.comparing(StallScoreStatOverviewDTO::getScore)).limit(10).collect(Collectors.toList()));
        } else {
            dto.setManageTop10(manageTop10.stream().sorted(Comparator.comparing(StallScoreStatOverviewDTO::getScore).reversed()).limit(10).collect(Collectors.toList()));
            dto.setQualityTop10(qualityTop10.stream().sorted(Comparator.comparing(StallScoreStatOverviewDTO::getScore).reversed()).limit(10).collect(Collectors.toList()));
            dto.setHonestyTop10(honestyTop10.stream().sorted(Comparator.comparing(StallScoreStatOverviewDTO::getScore).reversed()).limit(10).collect(Collectors.toList()));
            dto.setAttitudeTop10(attitudeTop10.stream().sorted(Comparator.comparing(StallScoreStatOverviewDTO::getScore).reversed()).limit(10).collect(Collectors.toList()));
            dto.setEnvTop10(envTop10.stream().sorted(Comparator.comparing(StallScoreStatOverviewDTO::getScore).reversed()).limit(10).collect(Collectors.toList()));
        }
        return dto;
    }

    /**
     * 概览-商户诚信经营占比 分析
     *
     * @param queryDTO
     * @return
     */
    @GetMapping("/stall-score-stat/honesty/radio")
    public List<StallScoreStatOverviewDTO> getHonestyRadio(StallScoreQueryDTO queryDTO) {
        List<StallScoreStatOverviewDTO> result = new ArrayList<>();
        // 商品质量
        List<Integer> qualityScore = new ArrayList<>();
        // 诚实守信
        List<Integer> honestyScore = new ArrayList<>();
        // 服务态度
        List<Integer> attitudeScore = new ArrayList<>();
        // 环境卫生
        List<Integer> envScore = new ArrayList<>();
        DateUtils.setQueryTime(queryDTO, queryDTO.getTimeRange());
        List<StallEvaluationRecord> evaluationRecords = this.getEvaluationRecord(queryDTO);
        evaluationRecords.forEach(record -> {
            qualityScore.add(record.getZScore());
            honestyScore.add(record.getSScore());
            attitudeScore.add(record.getTScore());
            envScore.add(record.getWScore());
        });
        result.add(this.countHonestyRadio(StallEvaluationRecordService.EVALUATION_TYPE_Z, qualityScore));
        result.add(this.countHonestyRadio(StallEvaluationRecordService.EVALUATION_TYPE_S, honestyScore));
        result.add(this.countHonestyRadio(StallEvaluationRecordService.EVALUATION_TYPE_T, attitudeScore));
        result.add(this.countHonestyRadio(StallEvaluationRecordService.EVALUATION_TYPE_W, envScore));
        return result;
    }

    /**
     * 商户详情-评价标签分析
     *
     * @param queryDTO
     * @return
     */
    @GetMapping("/stall-score-stat/stall/tag-analysis")
    public List<StallScoreStatOverviewDTO> analyseTags(StallScoreQueryDTO queryDTO) {
        List<StallScoreStatOverviewDTO> dtos = new ArrayList<>();
        List<StallEvaluationRecord> evaluationRecords = this.getEvaluationRecord(queryDTO);
        String type = queryDTO.getType();
        if (StallEvaluationRecordService.EVALUATION_TYPE_S.equals(type)) {
            evaluationRecords.stream().collect(Collectors.groupingBy(StallEvaluationRecord::getSScore)).forEach((zscore, list) -> {
                dtos.add(this.analyseTagOfRecord("1", zscore, list));
            });
        } else if (StallEvaluationRecordService.EVALUATION_TYPE_T.equals(type)) {
            evaluationRecords.stream().collect(Collectors.groupingBy(StallEvaluationRecord::getTScore)).forEach((zscore, list) -> {
                dtos.add(this.analyseTagOfRecord("2", zscore, list));
            });
        } else if (StallEvaluationRecordService.EVALUATION_TYPE_W.equals(type)) {
            evaluationRecords.stream().collect(Collectors.groupingBy(StallEvaluationRecord::getWScore)).forEach((zscore, list) -> {
                dtos.add(this.analyseTagOfRecord("3", zscore, list));
            });
        } else {
            evaluationRecords.stream().collect(Collectors.groupingBy(StallEvaluationRecord::getZScore)).forEach((zscore, list) -> {
                dtos.add(this.analyseTagOfRecord("4", zscore, list));
            });
        }
        return dtos;
    }

    /**
     * 概览-商户评价标签top10
     *
     * @param queryDTO
     * @return
     */
    @GetMapping("/stall-score-stat/tag/top10")
    public List<StallScoreStatOverviewDTO> getTagRadio(StallScoreQueryDTO queryDTO) {
        List<StallScoreStatOverviewDTO> result = new ArrayList<>();
        DateUtils.setQueryTime(queryDTO, queryDTO.getTimeRange());
        List<StallEvaluationRecordTag> evaluationRecordTags = this.getEvaluationRecordTags(queryDTO);
        List<StallScoreStatOverviewDTO> dtos = new ArrayList<>();
        evaluationRecordTags.forEach(record -> {
            StallEvaluationTag tag = tagService.getById(record.getTagId());
            if (tag == null) return;
            StallScoreStatOverviewDTO dto = new StallScoreStatOverviewDTO();
            dto.setName(tag.getTag());
            dto.setType(tag.getEvaluation());
            dtos.add(dto);
        });
        dtos.stream().collect(Collectors.groupingBy(StallScoreStatOverviewDTO::getType)).forEach((type, list) -> {
            StallScoreStatOverviewDTO dto = new StallScoreStatOverviewDTO();
            dto.setType(type);
            List<StallScoreStatOverviewDTO> datas = new ArrayList<>();
            list.stream().collect(Collectors.groupingBy(StallScoreStatOverviewDTO::getName)).forEach((name, tags) -> {
                StallScoreStatOverviewDTO data = new StallScoreStatOverviewDTO();
                data.setName(name);
                data.setCount(tags.size());
                datas.add(data);
            });
            dto.setManageTop10(datas.stream().sorted(Comparator.comparing(StallScoreStatOverviewDTO::getCount).reversed()).limit(10).collect(Collectors.toList()));
            result.add(dto);
        });
        return result;
    }

    /**
     * 统计报表-安全生产值
     *
     * @param queryDTO
     * @param pageRequest
     * @return
     */
    @GetMapping("/stall-score-stat/score/page")
    public PageResponse<StallScoreDTO> getScorePage(StallScoreQueryDTO queryDTO, PageRequest pageRequest) {
        // 安全生产分值，不传年份，默认查询当年的
        if (queryDTO.getYear() == null) {
            queryDTO.setYear(Integer.parseInt(DateUtils.getDate(LocalDateTime.now(), "yyyy")));
        }
        List<StallScoreDTO> stallScoreAndEvaluation = safetyMarkService.getStallScoreAndEvaluation(queryDTO);
        stallScoreAndEvaluation.sort(Comparator.comparing(StallScoreDTO::getScore, Comparator.reverseOrder()));
        return PageResponse.doPage(stallScoreAndEvaluation, pageRequest);
    }

    /**
     * 统计报表-安全生产值导出
     *
     * @param queryDTO
     * @return
     */
    @GetMapping("/stall-score-stat/score/export")
    public void exportScoreData(StallScoreQueryDTO queryDTO, HttpServletRequest request, HttpServletResponse response) {
        // 安全生产分值，不传年份，默认查询当年的
        if (queryDTO.getYear() == null) {
            queryDTO.setYear(LocalDateTime.now().getYear());
        }
        List<StallScoreDTO> stallScoreAndEvaluation = safetyMarkService.getStallScoreAndEvaluation(queryDTO);
        stallScoreAndEvaluation.sort(Comparator.comparing(StallScoreDTO::getScore, Comparator.reverseOrder()));
        List<StallScoreExcelDTO> dtos = new StallScoreExcelDTO(stallScoreAndEvaluation).list();
        ExcelUtils<StallScoreExcelDTO> utils = new ExcelUtils<>(StallScoreExcelDTO.class);
        utils.createWorkbook();
        try {
            XSSFSheet sheet = utils.createSheet("安全生产值", dtos);
        } catch (Exception e) {
            log.error("",e);
            throw new ServiceException("导出数据失败", e);
        }
        utils.downLoadExcel("安全生产值信息", request, response);
    }

    /**
     * 统计报表-诚信经营统计
     *
     * @param queryDTO
     * @return
     */
    @GetMapping("/stall-score-stat/honesty/page")
    public PageResponse<StallScoreDTO> getHonestyPage(StallScoreQueryDTO queryDTO, PageRequest pageRequest) {
        return PageResponse.doPage(this.getHonestyScoreList(queryDTO), pageRequest);
    }

    /**
     * 统计报表-诚信经营导出
     *
     * @param queryDTO
     * @return
     */
    @GetMapping("/stall-score-stat/honesty/export")
    public void export(StallScoreQueryDTO queryDTO, HttpServletRequest request, HttpServletResponse response) {
        List<StallScoreDTO> honestyScoreList = this.getHonestyScoreList(queryDTO);
        List<StallEvaluationExcelDTO> dtos = new StallEvaluationExcelDTO(honestyScoreList).list();
        ExcelUtils<StallEvaluationExcelDTO> utils = new ExcelUtils<>(StallEvaluationExcelDTO.class);
        utils.createWorkbook();
        try {
            XSSFSheet sheet = utils.createSheet("诚信经营值", dtos);
        } catch (Exception e) {
            log.error("",e);
            throw new ServiceException("导出数据失败", e);
        }
        utils.downLoadExcel("诚信经营信息", request, response);
    }

    // 计算评价记录维度的标签
    private StallScoreStatOverviewDTO analyseTagOfRecord(String eva, Integer score, List<StallEvaluationRecord> dtos) {
        StallScoreStatOverviewDTO dto = new StallScoreStatOverviewDTO();
        dto.setType(String.valueOf(score));
        dto.setCount(dtos.size());
        List<Long> recordIds = dtos.stream().map(StallEvaluationRecord::getId).collect(Collectors.toList());
        if (recordIds.size() > 0) {
            // 查询这些评价关联的所有标签
            List<StallEvaluationRecordTag> recordTags = recordTagService.lambdaQuery().in(StallEvaluationRecordTag::getRecordId, recordIds).eq(StallEvaluationRecordTag::getEvaluation, eva).list();
            // 筛选前五的标签
            List<StallScoreStatOverviewDTO> tempTags = new ArrayList<>();
            recordTags.stream().collect(Collectors.groupingBy(StallEvaluationRecordTag::getTag)).forEach((tag, tags) -> {
                StallScoreStatOverviewDTO tempTag = new StallScoreStatOverviewDTO();
                tempTag.setName(tag);
                tempTag.setCount(tags.size());
                tempTags.add(tempTag);
            });
            List<String> tags = tempTags.stream().sorted(Comparator.comparing(StallScoreStatOverviewDTO::getCount).reversed()).limit(5).map(StallScoreStatOverviewDTO::getName).collect(Collectors.toList());
            dto.setTags(tags);
        }
        return dto;
    }

    // 诚信经营值列表
    private List<StallScoreDTO> getHonestyScoreList(StallScoreQueryDTO queryDTO) {
        DateUtils.setQueryTime(queryDTO, DateUtils.TIMERANGE_CUSTOM);
        List<StallScoreDTO> dtos = new ArrayList<>();
        List<Space> spaces = this.querySpaceList(queryDTO);
        List<StallEvaluationRecord> records = this.getEvaluationRecord(queryDTO);
        records.parallelStream().collect(Collectors.groupingBy(StallEvaluationRecord::getStallId)).forEach((stallId, list) -> {
            Space space = spaces.stream().filter(s -> s.getId().equals(stallId)).findAny().orElse(null);
            if (space != null) {
                int size = list.size();
                // 诚信经营平均得分
                float honesty = BigDecimal.valueOf(list.stream().mapToDouble(StallEvaluationRecord::getHonestyScore).sum() / size).setScale(1, RoundingMode.HALF_UP).floatValue();
                // 四项维度平均得分
                float zScore = BigDecimal.valueOf(list.stream().mapToDouble(StallEvaluationRecord::getZScore).sum() / size).setScale(1, RoundingMode.HALF_UP).floatValue();
                float sScore = BigDecimal.valueOf(list.stream().mapToDouble(StallEvaluationRecord::getSScore).sum() / size).setScale(1, RoundingMode.HALF_UP).floatValue();
                float tScore = BigDecimal.valueOf(list.stream().mapToDouble(StallEvaluationRecord::getTScore).sum() / size).setScale(1, RoundingMode.HALF_UP).floatValue();
                float wScore = BigDecimal.valueOf(list.stream().mapToDouble(StallEvaluationRecord::getWScore).sum() / size).setScale(1, RoundingMode.HALF_UP).floatValue();
                StallScoreDTO dto = new StallScoreDTO();
                BeanUtils.copyProperties(space, dto);
                dto.setHonestyScore(honesty);
                dto.setZScoreAvg(zScore);
                dto.setSScoreAvg(sScore);
                dto.setTScoreAvg(tScore);
                dto.setWScoreAvg(wScore);
                Company company= companyMapper.selectOne(new LambdaQueryWrapper<Company>()
                        .eq(Company::getId,dto.getCompanyId()));
                dto.setOwner(ObjectUtil.isNotEmpty(company)?company.getOwner():"");
                dtos.add(dto);
            }
        });
        dtos.sort(Comparator.comparing(StallScoreDTO::getHonestyScore, Comparator.reverseOrder()));
        // 根据诚信经营值筛选
        if (queryDTO.getUnderStar() != null) {
            return dtos.stream().filter(d -> this.rounding(d.getHonestyScore()) <= queryDTO.getUnderStar()).collect(Collectors.toList());
        } else {
            return dtos;
        }
    }

    // 计算诚信经营占比
    private StallScoreStatOverviewDTO countHonestyRadio(String type, List<Integer> score) {
        StallScoreStatOverviewDTO dto = new StallScoreStatOverviewDTO();
        dto.setName(type);
        dto.setCount(score.size());
        List<StallScoreStatOverviewDTO> dtos = new ArrayList<>();
        score.parallelStream().collect(Collectors.groupingBy(s -> s)).forEach((key, list) -> {
            StallScoreStatOverviewDTO dto1 = new StallScoreStatOverviewDTO();
            dto1.setName(key.toString());
            dto1.setCount(list.size());
            dto1.setRadio(this.rounding((list.size() / (double) score.size()) * 100 ));
            dtos.add(dto1);
        });
        dtos.sort(Comparator.comparing(StallScoreStatOverviewDTO::getName));
        dto.setManageTop10(dtos);
        return dto;
    }

    private StallScoreStatOverviewDTO countEvaluation(int totalScore, int times, Space space) {
        StallScoreStatOverviewDTO dto = new StallScoreStatOverviewDTO();
        dto.setId(space.getId());
        dto.setName(space.getName());
        dto.setScore(this.getRate(totalScore, times));
        return dto;
    }

    private int getRate(int num, int total) {
        return this.rounding(num / (double) total);
    }

    // 四舍五入取整数
    private int rounding(double number) {
        return (BigDecimal.valueOf(number).setScale(0, RoundingMode.HALF_UP).intValue());
    }

    // 获取评价标签记录
    private List<StallEvaluationRecordTag> getEvaluationRecordTags(StallScoreQueryDTO queryDTO) {
        return recordTagService.lambdaQuery()
                .eq(StallEvaluationRecordTag::getExamineStatus, StallEvaluationRecordService.EXAMINE_STATUS_PASS)
                .between(null != queryDTO.getBeginTime() && null != queryDTO.getEndTime(), StallEvaluationRecordTag::getTime, queryDTO.getBeginTime(), queryDTO.getEndTime()).list();
    }

    // 获取档位巡检扣分值记录
    private List<StallSafetyInsp> getScoreRecord(StallScoreQueryDTO queryDTO) {
        return safetyInspService.lambdaQuery().between(null != queryDTO.getBeginTime() && null != queryDTO.getEndTime(), StallSafetyInsp::getCreateTime, queryDTO.getBeginTime(), queryDTO.getEndTime()).list();
    }

    // 获取档位评价记录
    private List<StallEvaluationRecord> getEvaluationRecord(StallScoreQueryDTO queryDTO) {
        return evaluationRecordService.lambdaQuery()
                .eq(StallEvaluationRecord::getProjectId, queryDTO.getProjectId())
                .eq(StallEvaluationRecord::getExamineStatus, StallEvaluationRecordService.EXAMINE_STATUS_PASS)
                .eq(StringUtils.isNotEmpty(queryDTO.getStallId()), StallEvaluationRecord::getStallId, queryDTO.getStallId())
                .between(null != queryDTO.getBeginTime() && null != queryDTO.getEndTime(), StallEvaluationRecord::getCreateTime, queryDTO.getBeginTime(), queryDTO.getEndTime()).list();
    }

    private List<Space> querySpaceList(StallScoreQueryDTO queryDTO) {
        return spaceService.lambdaQuery().eq(Space::getProjectId, queryDTO.getProjectId())
                .and(StringUtils.isNotEmpty(queryDTO.getSearch()), q -> q
                        .like(Space::getName, queryDTO.getSearch())
                        .or()
                        .like(Space::getCompanyName, queryDTO.getSearch())
                )
                .ne(Space::getCompanyId, "")
                .eq(Space::getAttribute, SpaceService.ATTR_BOOTH)
                .list();
    }

}


package com.guhecloud.cube.deliver.project.merchant.controller.store;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.deliver.project.merchant.commons.core.LoginUser;
import com.guhecloud.cube.deliver.project.merchant.commons.web.PageRequest;
import com.guhecloud.cube.deliver.project.merchant.commons.web.PageResponse;
import com.guhecloud.cube.deliver.project.merchant.dto.store.StallEvaluationRecordDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.store.StallEvaluationRecordQueryDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.Space;
import com.guhecloud.cube.deliver.project.merchant.entity.store.StallEvaluationRecord;
import com.guhecloud.cube.deliver.project.merchant.service.SpaceService;
import com.guhecloud.cube.deliver.project.merchant.service.store.StallEvaluationRecordService;

import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 档位评价记录表 前端控制器
 * </p>
 *
 * @author benhuabao
 * @since 2021-12-22
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StallEvaluationRecordController {

    private final String ALL = "all";
    private final String HAS_FILE = "hasFile";
    private final String IS_GOOD = "isGood";
    private final String IS_BAD = "isBad";

    private final StallEvaluationRecordService service;
    private final FileClient fileClient;
    private final FileRefClient fileRefClient;
    private final SpaceService spaceService;

    /**
     * 根据 日期 + 档位名称 + projectId  ===》 评论列表
     */
    @GetMapping("/stall-evaluation-records/store/page")
    public PageResponse<StallEvaluationRecordDTO> getRecordsByStore(StallEvaluationRecordQueryDTO queryDTO, PageRequest pageRequest) {
        String condition = queryDTO.getCondition();
        Page<StallEvaluationRecord> page = service.lambdaQuery()
                .like(ObjectUtil.isNotEmpty(queryDTO.getSearch()), StallEvaluationRecord::getNickname, queryDTO.getSearch())
                .eq(StallEvaluationRecord::getProjectId, queryDTO.getProjectId())
                .eq(StringUtils.isNotEmpty(queryDTO.getStallId()), StallEvaluationRecord::getStallId, queryDTO.getStallId())
                .and(queryDTO.getBeginTime() != null && queryDTO.getEndTime() != null,
                     q -> q.between(StallEvaluationRecord::getCreateTime,
                                    DateUtils.beginOfDay(queryDTO.getBeginTime()), DateUtils.endOfDay(queryDTO.getEndTime())))
                .and(StringUtils.isNotEmpty(condition) && !ALL.equals(condition), q -> q
                        .between(IS_GOOD.equals(condition), StallEvaluationRecord::getScore, 3.5, 5)
                        .between(IS_BAD.equals(condition), StallEvaluationRecord::getScore, 0, 2.49)
                        .eq(HAS_FILE.equals(condition), StallEvaluationRecord::getHasFile, true)
                    )
                .and(queryDTO.getHandled() != null, q -> q
                        .ne(queryDTO.getHandled(), StallEvaluationRecord::getExamineStatus, StallEvaluationRecordService.EXAMINE_STATUS_NO)
                        .eq(!queryDTO.getHandled(), StallEvaluationRecord::getExamineStatus, StallEvaluationRecordService.EXAMINE_STATUS_NO)
                    )
                .eq(null != queryDTO.getExamineStatus(), StallEvaluationRecord::getExamineStatus, queryDTO.getExamineStatus())
                .between(null != queryDTO.getUnderStar(), StallEvaluationRecord::getHonestyScore, 0, queryDTO.getUnderStar())
                .orderByDesc(StallEvaluationRecord::getCreateTime)
                .page(pageRequest.page());
        List<StallEvaluationRecordDTO> dtos = new StallEvaluationRecordDTO(page.getRecords()).list();
        dtos.parallelStream().forEach(this::extendFile);
        return new PageResponse<>(dtos, page.getTotal(), page.getPages());
    }

    /**
     * 查询筛选条件的统计
     */
    @GetMapping("/stall-evaluation-records/query/stat")
    public List<Map<String, Object>> getQueryConStat(StallEvaluationRecordQueryDTO queryDTO) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<StallEvaluationRecord> records = service.lambdaQuery().eq(StallEvaluationRecord::getProjectId, queryDTO.getProjectId())
                .eq(StallEvaluationRecord::getExamineStatus, StallEvaluationRecordService.EXAMINE_STATUS_PASS)
                .eq(StallEvaluationRecord::getStallId, queryDTO.getStallId())
                .list();
        // 全部
        Map<String, Object> total = new HashMap<>();
        total.put("name", ALL);
        total.put("value", records.size());
        result.add(total);
        // 有图
        int hasFile = (int) records.parallelStream().filter(StallEvaluationRecord::getHasFile).count();
        Map<String, Object> file = new HashMap<>();
        file.put("name", HAS_FILE);
        file.put("value", hasFile);
        result.add(file);
        // 好评
        int isGood = (int) records.parallelStream().filter(r -> r.getScore() >= 3.5 && r.getScore() <= 5).count();
        Map<String, Object> good = new HashMap<>();
        good.put("name", IS_GOOD);
        good.put("value", isGood);
        result.add(good);
        // 差评
        int isBad = (int) records.parallelStream().filter(r -> r.getScore() > 0 && r.getScore() < 2.5).count();
        Map<String, Object> bad = new HashMap<>();
        bad.put("name", IS_BAD);
        bad.put("value", isBad);
        result.add(bad);
        return result;
    }

    /**
     * 根据ID查看评论详情（包含图片，文件）
     */
    @GetMapping("/stall-evaluation-records/{id}")
    public StallEvaluationRecordDTO getEvaluationDetail(@PathVariable("id") String id) {
        StallEvaluationRecord record = service.lambdaQuery()
                .eq(StallEvaluationRecord::getId, id).one();
        StallEvaluationRecordDTO dto = new StallEvaluationRecordDTO(record);
        //id关联File的business_key
        this.getMarkFile(dto);
        this.extendStallName(dto);
        return dto;
    }

    /**
     * 根据userid --> 我的评价
     */
    @GetMapping("/stall-evaluation-records/user")
    public PageResponse<StallEvaluationRecordDTO> getEvaluationByUser(String projectId, LoginUser loginUser, PageRequest pageRequest) {
        loginUser.setId(SecurityContextHolder.getPrincipal().getId());
        Page<StallEvaluationRecord> page = service.lambdaQuery()
                .eq(StallEvaluationRecord::getProjectId, projectId)
                .eq(StallEvaluationRecord::getUserId, loginUser.getId())
                .orderByDesc(StallEvaluationRecord::getCreateTime)
                .page(pageRequest.page());
        List<StallEvaluationRecordDTO> dtos = new StallEvaluationRecordDTO(page.getRecords()).list();
        //然后要获取照片
        dtos.parallelStream().forEach(this::getMarkFile);
        return new PageResponse<>(dtos, page.getTotal(), page.getPages());
    }

    /**
     * 新增评价（图片,文件）
     */
    @PostMapping("/stall-evaluation-records")
    public Long save(@Valid @RequestBody StallEvaluationRecordDTO dto, LoginUser loginUser) {
        loginUser.setId(SecurityContextHolder.getPrincipal().getId());
        return service.save(dto, loginUser.getId());
    }

    /**
     * 审核评论
     */
    @PostMapping("/stall-evaluation-records/examine")
    public void examine(@RequestBody StallEvaluationRecordDTO dto) {
        service.examine(dto);
    }

    /**
     * 根据ID删除评论记录
     */
    @DeleteMapping("/stall-evaluation-records/{id}")
    public void remove(@PathVariable("id") String id) {
        service.remove(id);
    }

    /**
     * 获取评论DTO关联的文件
     */
    private void getMarkFile(StallEvaluationRecordDTO dto) {
        List<FileVO> fileDTOS = null;
        try {
            fileDTOS = fileClient.getByBusinessKey(dto.getId());
        } catch (Exception e) {
            log.error("获取文件异常", e);
        }
        assert fileDTOS != null;
        dto.setFileIds(fileDTOS.stream().map(FileVO::getId).collect(Collectors.toList()));
        dto.setFileDTOS(fileDTOS);
    }

    private void extendFile(StallEvaluationRecordDTO dto) {
        List<FileVO> fileDTOS = fileClient.getByBusinessKey(dto.getId());
        assert fileDTOS != null;
        dto.setFileIds(fileDTOS.parallelStream().map(FileVO::getId).collect(Collectors.toList()));
        dto.setFileDTOS(fileDTOS);
    }

    private void extendStallName(StallEvaluationRecordDTO dto) {
        Space space = spaceService.getById(dto.getStallId());
        dto.setStallName(space == null ? "" : space.getName());
    }

}


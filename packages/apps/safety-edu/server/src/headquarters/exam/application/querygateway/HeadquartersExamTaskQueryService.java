package com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.application.querygateway;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.application.HeadquartersExamTaskMarketService;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.application.HeadquartersExamTaskPaperService;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.application.HeadquartersExamTaskService;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.application.querygateway.query.HeadquartersExamTaskQuery;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.domain.po.HeadquartersExamTaskMarketPO;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.domain.po.HeadquartersExamTaskPO;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.infrastructure.repository.mapper.HeadquartersExamTaskMapper;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.interfaces.vo.HeadquartersExamTaskMarketVO;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.interfaces.vo.HeadquartersExamTaskVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo.ExamPaperVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HeadquartersExamTaskQueryService {

    private final HeadquartersExamTaskMapper headquartersExamTaskMapper;

    private final HeadquartersExamTaskService headquartersExamTaskService;

    private final HeadquartersExamTaskMarketService headquartersExamTaskMarketService;

    private final HeadquartersExamTaskPaperService headquartersExamTaskPaperService;

    /**
     * 任务分页列表
     *
     * @param examTaskQuery
     *
     * @return
     */
    public Pagination<HeadquartersExamTaskVO> getExamTaskPage(HeadquartersExamTaskQuery examTaskQuery) {
        Page<HeadquartersExamTaskVO> page = this.headquartersExamTaskMapper.selectExamTaskPage(PagingUtils.convert(examTaskQuery), examTaskQuery);
        page.getRecords().stream().forEach(examTaskVO ->
                                                   this.extend(examTaskVO)
                                          );
        return PagingUtils.convert(page);
    }

    /**
     * 任务详情
     *
     * @param id
     *
     * @return
     */
    public HeadquartersExamTaskVO getExamTask(Long id) {
        HeadquartersExamTaskPO examTaskPO = this.headquartersExamTaskService.getById(id);
        Assert.isNotFound(examTaskPO, "任务不存在");
        HeadquartersExamTaskVO examTaskVO = ObjectUtils.copying(examTaskPO, HeadquartersExamTaskVO.class);
        this.extend(examTaskVO);
        //考试市场信息
        List<HeadquartersExamTaskMarketPO> examTaskMarketPOS = this.headquartersExamTaskMarketService.list(Wrappers
                                                                                                                   .<HeadquartersExamTaskMarketPO>lambdaQuery()
                                                                                                                   .eq(HeadquartersExamTaskMarketPO::getHeadquartersExamTaskId, examTaskPO.getId()));
        List<HeadquartersExamTaskMarketVO> examTaskMarketVOS = ObjectUtils.copying(examTaskMarketPOS, HeadquartersExamTaskMarketVO.class);
        examTaskVO.setExamTaskMarkets(examTaskMarketVOS);
        return examTaskVO;
    }

    private void extend(List<HeadquartersExamTaskVO> examTaskVOS) {
        for (HeadquartersExamTaskVO examTaskVO : examTaskVOS) {
            this.extend(examTaskVO);
        }
    }

    private void extend(HeadquartersExamTaskVO examTaskVO) {
        examTaskVO.setExamTaskTypeName(examTaskVO.getExamTaskType().getDescription());
        examTaskVO.setExamTaskStatusName(examTaskVO.getExamTaskStatus().getDescription());
        //试卷信息
        List<ExamPaperVO> examTaskPaperVOS = this.headquartersExamTaskPaperService.getExamTaskPaperVOS(examTaskVO.getId());
        if (!CollectionUtils.isEmpty(examTaskPaperVOS)) {
            examTaskVO.setExamPapers(examTaskPaperVOS);
        }
    }
}

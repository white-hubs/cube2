package com.guhecloud.cube.deliver.project.merchant.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guhecloud.cube.deliver.project.merchant.commons.utils.BizCodeUtils;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerQuesTempDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerQuesTempQueryDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerQuestionDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerQuesTemp;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerQuestion;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerQuestionnaire;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerRelationship;
import com.guhecloud.cube.deliver.project.merchant.enums.ProjectMerchantAreaEnum;
import com.guhecloud.cube.deliver.project.merchant.enums.WholesalerEnum;
import com.guhecloud.cube.deliver.project.merchant.mapper.WholesalerQuesTempMapper;
import com.guhecloud.cube.deliver.project.merchant.service.WholesalerQuesTempService;
import com.guhecloud.cube.deliver.project.merchant.service.WholesalerQuestionService;
import com.guhecloud.cube.deliver.project.merchant.service.WholesalerQuestionnaireService;
import com.guhecloud.cube.deliver.project.merchant.service.WholesalerRelationshipService;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wangliang
 * @since 2022-01-11
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WholesalerQuesTempServiceImpl extends ServiceImpl<WholesalerQuesTempMapper, WholesalerQuesTemp> implements WholesalerQuesTempService {

    private final WholesalerQuestionService wqService;
    private final WholesalerQuesTempMapper mapper;
    private final WholesalerQuestionnaireService wqnService;
    private final WholesalerRelationshipService wrService;

    @Override
    public List<WholesalerQuesTempDTO> getTempDtoList(WholesalerQuesTempQueryDTO queryDto) {
        List<WholesalerQuesTemp> tempList = this.lambdaQuery()
                .eq(WholesalerQuesTemp::getProjectId, queryDto.getProjectId())
                .eq(StrUtil.isNotBlank(queryDto.getType()), WholesalerQuesTemp::getType, queryDto.getType())
                //                .like(StrUtil.isNotBlank(queryDto.getName()),WholesalerQuesTemp::getName,queryDto.getName())
                //                .like(StrUtil.isNotBlank(queryDto.getCode()),WholesalerQuesTemp::getCode,queryDto.getCode())
                //                .like(StrUtil.isNotBlank(queryDto.getCreateBy()),WholesalerQuesTemp::getCreateBy,queryDto.getCreateBy())
                .and(StrUtil.isNotBlank(queryDto.getSearch()), q -> q
                        .like(WholesalerQuesTemp::getName, queryDto.getSearch())
                        .or()
                        .like(WholesalerQuesTemp::getCode, queryDto.getSearch())
                        .or()
                        .like(WholesalerQuesTemp::getCreateBy, queryDto.getSearch())
                    )
                .between(queryDto.getBeginTime() != null && queryDto.getEndTime() != null,
                         WholesalerQuesTemp::getCreateTime, queryDto.getBeginTime(), queryDto.getEndTime())
                .orderByDesc(WholesalerQuesTemp::getCreateTime)
                .list();
        return new WholesalerQuesTempDTO(tempList).list();
    }

    @Override
    public WholesalerQuesTempDTO getTempDto(String id) {
        //获取模板
        WholesalerQuesTemp quesTemp = this.lambdaQuery().eq(WholesalerQuesTemp::getId, id).one();
        WholesalerQuesTempDTO dto = new WholesalerQuesTempDTO(quesTemp);
        dto.setTypeName(ProjectMerchantAreaEnum.getCodeByName(dto.getType()));
        //获取问题列表
        List<WholesalerQuestion> questionList = wqService.lambdaQuery()
                .eq(WholesalerQuestion::getQuestionnaireTempId, dto.getId())
                .list();
        //问题列表放进去
        dto.setQuestionDtos(new WholesalerQuestionDTO(questionList).list());
        return dto;
    }

    @Override
    public String saveOrUpdate(WholesalerQuesTempDTO dto) {
        WholesalerQuesTemp temp = dto.convert();
        long count = this.lambdaQuery().eq(WholesalerQuesTemp::getId, dto.getId()).count();
        //如果传入了正确的ID，就是修改，否则就是增加
        if (StrUtil.isNotBlank(dto.getId()) && count > 0) {
            mapper.updateById(temp);
            //删除关联的题
            List<WholesalerQuestion> questionList = wqService.lambdaQuery()
                    .eq(WholesalerQuestion::getQuestionnaireTempId, dto.getId())
                    .list();
            questionList.forEach(question -> wqService.removeById(question.getId()));
            // 修改调查问卷的引用名称
            wqnService.lambdaUpdate().eq(WholesalerQuestionnaire::getTempId, dto.getId())
                    .set(WholesalerQuestionnaire::getTempType, dto.getType()).set(WholesalerQuestionnaire::getTempName, dto.getName())
                    .update();
        } else {
            temp.setCode(BizCodeUtils.generate("WJ"));
            temp.setCreateBy(SecurityContextHolder.getPrincipal().getName());
            temp.setCreateTime(LocalDateTime.now());
            mapper.insert(temp);
        }
        // 保存题目
        dto.getQuestionDtos().forEach(questionDto -> {
            if (StringUtils.isEmpty(questionDto.getName())) {
                throw new ServiceException("第" + (questionDto.getNumber() + 1) + "题的题目不能为空！");
            }
            if (!WholesalerEnum.QUESTION_TYPE_Q_A.getCode().equals(questionDto.getType())) {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    List<String> options = mapper.readValue(questionDto.getOptions(), List.class);
                    if (options.size() < 1) {
                        throw new ServiceException("第" + (questionDto.getNumber() + 1) + "题的选项不能为空！");
                    }
                } catch (JsonProcessingException e) {

                }
            }
            questionDto.setQuestionnaireTempId(temp.getId());
            questionDto.setCreateBy(SecurityContextHolder.getPrincipal().getName());
            questionDto.setCreateTime(LocalDateTime.now());
            wqService.save(questionDto.convert());
        });
        return temp.getId();
    }

    @Override
    public void remove(String id) {
        //删除模板的时候，如果有对应的问卷，问卷下有任务，就不让删除。没有任务就可以删
        List<WholesalerQuestionnaire> questionnaireList = wqnService.lambdaQuery().eq(WholesalerQuestionnaire::getTempId, id).list();
        if (!questionnaireList.isEmpty()) {
            questionnaireList.forEach(questionnaire -> {
                //如果有一个模板下头有任务，那么就不能删除
                long count = wrService.lambdaQuery()
                        .eq(WholesalerRelationship::getType, WholesalerQuestionnaireService.QUES_RELATIONSHIP)
                        .eq(WholesalerRelationship::getBusinessId, questionnaire.getId())
                        .count();
                if (count > 0) {
                    throw new ServiceException("该模板生成的问卷调查已经创建了任务，不能删除");
                }
            });
        }
        //删除模板，
        mapper.deleteById(id);
        //删除模板对应的试题
        List<WholesalerQuestion> questionList = wqService.lambdaQuery().eq(WholesalerQuestion::getQuestionnaireTempId, id).list();
        questionList.forEach(question -> wqService.removeById(question.getId()));
        //删除模板对应的问卷
        if (!questionnaireList.isEmpty()) {
            questionnaireList.forEach(questionnaire -> wqnService.removeById(questionnaire.getId()));
        }
    }
}

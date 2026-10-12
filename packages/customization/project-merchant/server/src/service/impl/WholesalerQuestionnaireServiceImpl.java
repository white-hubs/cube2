package com.guhecloud.cube.deliver.project.merchant.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guhecloud.cube.deliver.project.merchant.commons.core.LoginUser;
import com.guhecloud.cube.deliver.project.merchant.commons.utils.BizCodeUtils;
import com.guhecloud.cube.deliver.project.merchant.commons.utils.DateUtils;
import com.guhecloud.cube.deliver.project.merchant.commons.utils.ExcelUtils;
import com.guhecloud.cube.deliver.project.merchant.dto.*;
import com.guhecloud.cube.deliver.project.merchant.entity.CompanyUser;
import com.guhecloud.cube.deliver.project.merchant.entity.Space;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.*;
import com.guhecloud.cube.deliver.project.merchant.enums.WholesalerEnum;
import com.guhecloud.cube.deliver.project.merchant.job.CheckTaskJob;
import com.guhecloud.cube.deliver.project.merchant.mapper.WholesalerQuesTempMapper;
import com.guhecloud.cube.deliver.project.merchant.mapper.WholesalerQuestionnaireMapper;
import com.guhecloud.cube.deliver.project.merchant.service.*;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import com.guhecloud.platform.base.util.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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
public class WholesalerQuestionnaireServiceImpl extends ServiceImpl<WholesalerQuestionnaireMapper, WholesalerQuestionnaire> implements WholesalerQuestionnaireService {

    private final WholesalerQuestionnaireMapper mapper;
    private final WholesalerRelationshipService wrService;
    private final SpaceService spaceService;
    private final WholesalerQuesTempMapper qtMapper;
    private final WholesalerQuestionService wqService;
    private final WholesalerResponseService responseService;
    private final CompanyUserService cuService;
    private final MerchantAreaService merchantAreaService;
    private final CheckTaskJob xxlTaskJob;
    private final TaskExecutor taskExecutor;
    //    private final SmsApi smsApi;
    //    private final SiteMsgApi siteMsgApi;
    //    private final WeChatMsgApi weChatMsgApi;
    //    private final SysUserApi userApi;

    @Override
    public List<WholesalerQuestionnaireAppDTO> getQuestionnaireByStallId(String stallId) {
        List<WholesalerQuestionnaireAppDTO> dtos = new ArrayList<>();
        List<WholesalerRelationship> relationshipList = wrService.lambdaQuery()
                .eq(WholesalerRelationship::getType, QUES_RELATIONSHIP)
                .eq(WholesalerRelationship::getStallId, stallId).list();
        relationshipList.forEach(relationship -> {
            WholesalerQuestionnaireAppDTO dto = new WholesalerQuestionnaireAppDTO();
            WholesalerQuestionnaire questionnaire = mapper.selectById(relationship.getBusinessId());
            if (questionnaire != null && WholesalerEnum.QUES_STATUS_VALID.getCode().equals(questionnaire.getStatus())) {
                dto.setId(questionnaire.getId());
                dto.setName(questionnaire.getName());
                dto.setTempType(questionnaire.getTempType());
                dto.setCreateTime(questionnaire.getCreateTime());
                dto.setDone(relationship.getStatus());
                dtos.add(dto);
            }
        });
        dtos.sort(Comparator.comparing(WholesalerQuestionnaireAppDTO::getDone)
                          .thenComparing(WholesalerQuestionnaireAppDTO::getCreateTime, Comparator.reverseOrder()));
        return dtos;
    }

    @Override
    public List<WholesalerQuestionnaireDTO> getQuestionnaires(WholesalerQuestionnaireQueryDTO queryDTO) {
        List<WholesalerQuestionnaire> questionnaireList = this.lambdaQuery()
                .eq(WholesalerQuestionnaire::getProjectId, queryDTO.getProjectId())
                .eq(StrUtil.isNotBlank(queryDTO.getType()), WholesalerQuestionnaire::getTempType, queryDTO.getType())
                .and(StrUtil.isNotBlank(queryDTO.getSearch()), q -> q
                        .like(WholesalerQuestionnaire::getName, queryDTO.getSearch())
                        .or()
                        .like(WholesalerQuestionnaire::getCreateBy, queryDTO.getSearch())
                        .or()
                        .like(WholesalerQuestionnaire::getCode, queryDTO.getSearch())
                    )
                .eq(StringUtils.isNotEmpty(queryDTO.getStatus()), WholesalerQuestionnaire::getStatus, queryDTO.getStatus())
                .between(queryDTO.getBeginTime() != null && queryDTO.getEndTime() != null,
                         WholesalerQuestionnaire::getCreateTime, queryDTO.getBeginTime(), queryDTO.getEndTime())
                .list();
        List<WholesalerQuestionnaireDTO> dtos = new WholesalerQuestionnaireDTO(questionnaireList).list();
        dtos.forEach(dto -> {
            List<WholesalerRelationship> relationships = wrService.lambdaQuery()
                    .eq(WholesalerRelationship::getType, QUES_RELATIONSHIP)
                    .eq(WholesalerRelationship::getBusinessId, dto.getId())
                    .list();
            //应填
            int count = relationships.size();
            int signedCount = (int) relationships.stream().filter(relationship -> relationship.getStatus() == 1).count();
            int notSignedCount = count - signedCount;
            dto.setCount(count);
            dto.setSignedCount(signedCount);
            dto.setNotSignedCount(notSignedCount);
            dto.setProcessingStatus(notSignedCount == 0 ? 1 : 0);
        });
        if (queryDTO.getProcessingStatus() != null) {
            dtos = dtos.stream().filter(dto -> queryDTO.getProcessingStatus().equals(dto.getProcessingStatus()))
                    .collect(Collectors.toList());
        }
        return dtos;
    }

    @Override
    public WholesalerQuestionnaireDTO getQuestionnaireById(String id) {
        WholesalerQuestionnaire questionnaire = this.lambdaQuery().eq(WholesalerQuestionnaire::getId, id).one();
        WholesalerQuestionnaireDTO dto = new WholesalerQuestionnaireDTO(questionnaire);
        int count = 0;
        int signedCount = 0;
        //找关联的档位
        List<WholeSpaceDTO> spaceDtos = new ArrayList<>();
        //遍历，在中间表找到对应的档位ID集合，然后找到对应的所有spacedto
        for (WholesalerRelationship wholesalerRelationship : wrService.lambdaQuery().eq(WholesalerRelationship::getType, QUES_RELATIONSHIP)
                .eq(WholesalerRelationship::getBusinessId, dto.getId())
                .list()) {
            String stallId = wholesalerRelationship.getStallId();
            Space space = spaceService.lambdaQuery().eq(Space::getId, stallId).one();
            if (space != null) {
                if (wholesalerRelationship.getStatus() != null && wholesalerRelationship.getStatus() == 1) {
                    signedCount += 1;
                }
                count++;
                spaceDtos.add(this.buildWhoSpaceDto(wholesalerRelationship, space));
            }
        }
        dto.setCount(count);
        dto.setSignedCount(signedCount);
        dto.setNotSignedCount(count - signedCount);
        dto.setStallDtos(spaceDtos);
        //有任意一个没有做题的话，该状态就是未完成0,
        Integer processingStatus = spaceDtos.stream().anyMatch(spaceDto -> spaceDto.getSignedWho() == null) ? 0 : 1;
        dto.setProcessingStatus(processingStatus);
        dto.setStallIds(spaceDtos.stream().map(WholeSpaceDTO::getId).collect(Collectors.toList()));
        return dto;
    }

    @Override
    public List<WholeSpaceDTO> getStallList(String id, WholesalerQuestionnaireQueryDTO queryDTO) {
        List<WholeSpaceDTO> spaceDtos = new ArrayList<>();
        //遍历，在中间表找到对应的档位ID集合，然后找到对应的所有spacedto
        List<WholesalerRelationship> relationships = wrService.lambdaQuery().eq(WholesalerRelationship::getType, QUES_RELATIONSHIP)
                .eq(WholesalerRelationship::getBusinessId, id)
                .eq(null != queryDTO.getWrite(), WholesalerRelationship::getStatus, queryDTO.getWrite())
                .list();
        for (WholesalerRelationship wholesalerRelationship : relationships) {
            String stallId = wholesalerRelationship.getStallId();
            Space space = spaceService.lambdaQuery().eq(Space::getId, stallId)
                    .and(StringUtils.isNotEmpty(queryDTO.getSearch()), q -> q
                            .like(Space::getName, queryDTO.getSearch())
                            .or()
                            .like(Space::getCompanyName, queryDTO.getSearch())
                        )
                    .one();
            if (space != null) {
                spaceDtos.add(this.buildWhoSpaceDto(wholesalerRelationship, space));
            }
        }
        // 排序，null值放最后
        spaceDtos.sort(Comparator.comparing(WholeSpaceDTO::getStatus)
                               .thenComparing(WholeSpaceDTO::getSignedTime, Comparator.nullsFirst(LocalDateTime::compareTo)).reversed());
        return spaceDtos;
    }

    private WholeSpaceDTO buildWhoSpaceDto(WholesalerRelationship wholesalerRelationship, Space space) {
        WholeSpaceDTO wholeSpaceDto = new WholeSpaceDTO(space);
        wholeSpaceDto.setSignedTime(wholesalerRelationship.getSigendTime());
        wholeSpaceDto.setSignedWho(wholesalerRelationship.getSignedWho());
        wholeSpaceDto.setOwner(wholesalerRelationship.getOwner());
        wholeSpaceDto.setStatus(wholesalerRelationship.getStatus());
        wholeSpaceDto.setRentStatus(space.getCompanyId() == null ? 0 : 1);
        wholeSpaceDto.setArea(merchantAreaService.parseNames(Arrays.asList(space.getAreaId1(), space.getAreaId2(), space.getAreaId3())));
        return wholeSpaceDto;
    }

    /**
     * @param id      问卷ID
     * @param stallId 档位ID
     *
     * @return 很多的DTO集合
     */
    @Override
    public WholeStallQuesDTO getStallQuestionnaire(String id, String stallId) {
        WholeStallQuesDTO stallQuesDto = new WholeStallQuesDTO();
        if (spaceService.lambdaQuery().eq(Space::getId, stallId).count() == 0) {
            throw new ServiceException("该档位不存在");
        }
        stallQuesDto.setStallId(stallId);
        //获取问卷对象，页面头部内容要写问卷名称，描述等
        WholesalerQuestionnaire questionnaire = this.lambdaQuery().eq(WholesalerQuestionnaire::getId, id).one();
        if (questionnaire == null) {
            throw new ServiceException("该问卷不存在");
        }
        stallQuesDto.setQuestionnaire(questionnaire);
        //获取到问卷模板DTO
        WholesalerQuesTemp quesTemp = qtMapper.selectById(questionnaire.getTempId());
        WholesalerQuesTempDTO quesTempDto = new WholesalerQuesTempDTO(quesTemp);

        //根据问卷模板ID获取到问卷下面的题目
        //然后根据题目ID和档位ID找到答过的答案
        List<WholesalerQuestionDTO> questionDTOList = new ArrayList<>();
        for (WholesalerQuestion question : wqService.lambdaQuery()
                .eq(WholesalerQuestion::getQuestionnaireTempId, quesTemp.getId())
                .list()) {

            WholesalerResponse response = responseService.lambdaQuery().eq(WholesalerResponse::getStallId, stallId)
                    .eq(WholesalerResponse::getQuestionnaireId, questionnaire.getId())
                    .eq(WholesalerResponse::getQuestionId, question.getId())
                    .one();
            WholesalerQuestionDTO questionDTO = new WholesalerQuestionDTO(question);
            questionDTO.setAnswer(response == null ? null : response.getAnswer());
            questionDTO.setResponseId(response == null ? null : response.getId());
            questionDTOList.add(questionDTO);
        }
        quesTempDto.setQuestionDtos(questionDTOList);
        //        stallQuesDto.setTemplateDTO(quesTempDto);
        stallQuesDto.setQuestionDtos(questionDTOList);
        return stallQuesDto;
    }

    @Override
    public String saveOrUpdate(WholesalerQuestionnaireDTO dto) {
        this.checkValidStatus(dto);
        dto.setCreateBy(SecurityContextHolder.getPrincipal().getName());
        dto.setCreateTime(LocalDateTime.now());
        WholesalerQuestionnaire questionnaire = dto.convert();
        long count = this.lambdaQuery().eq(WholesalerQuestionnaire::getId, dto.getId()).count();
        if (StrUtil.isNotBlank(dto.getId()) && count == 1) {
            //update
            mapper.updateById(questionnaire);
            //删除掉之前的和档位的对应关系
            wrService.removeBatch(dto.getId(), QUES_RELATIONSHIP);
        } else {
            //insert
            questionnaire.setCode(BizCodeUtils.generate("WJ"));
            //            if (dto.getBeginTimeIsNow()){
            //                dto.setBeginTime(LocalDateTime.now());
            //            }
            mapper.insert(questionnaire);
            dto.setId(questionnaire.getId());
            //            this.firstSendNotice(dto.getStallIds(), dto.getProjectId(), questionnaire);
        }
        String questionnaireId = count == 1 ? dto.getId() : questionnaire.getId();
        //添加关系到中间表
        wrService.saveBatch(dto.getStallIds(), questionnaireId, QUES_RELATIONSHIP);
        this.createXxlJob(dto);
        return questionnaireId;
    }

    @Override
    public void responseQuestionnaire(WholeStallQuesDTO dto, LoginUser user) {
        //获取问卷信息
        WholesalerQuestionnaire questionnaire = dto.getQuestionnaire();
        boolean flag = this.checkQuestIsWrite(questionnaire.getId(), dto.getStallId());
        if (!flag) {
            return;
        }
        //获取模板信息
        //        List<WholesalerQuestionDTO> questionDTOList = new WholesalerQuestionDTO(wqService.lambdaQuery().eq(WholesalerQuestion::getQuestionnaireTempId, questionnaire.getTempId()).list()).list();
        //获取题目信息的List
        List<WholesalerQuestionDTO> questionDTOList = dto.getQuestionDtos();
        questionDTOList.forEach(questionDTO -> {
            WholesalerResponse response = new WholesalerResponse();
            response.setAnswer(questionDTO.getAnswer());
            response.setQuestionnaireId(questionnaire.getId());
            response.setQuestionId(questionDTO.getId());
            response.setTempId(questionnaire.getTempId());
            response.setStallId(dto.getStallId());
            response.setId(questionDTO.getResponseId());
            responseService.saveOrUpdateResponse(response);
        });
        //        UserInfoDTO infoDTO = userApi.getById(user.getId());
        //做完题以后，将档位-问卷 关系表中，对应的那条记录进行更新
        wrService.lambdaUpdate().eq(WholesalerRelationship::getStallId, dto.getStallId())
                .eq(WholesalerRelationship::getType, QUES_RELATIONSHIP)
                .eq(WholesalerRelationship::getBusinessId, questionnaire.getId())
                //                .set(WholesalerRelationship::getSignedWho, infoDTO == null ? "--" : infoDTO.getNickname())
                .set(WholesalerRelationship::getSigendTime, LocalDateTime.now())
                .set(WholesalerRelationship::getOwner, this.parseUserIsOwner(dto.getStallId(), user.getId()))
                .set(WholesalerRelationship::getStatus, 1)
                .update();
    }

    @Override
    public void removeById(String id) {
        //首先获取到问卷内容
        WholesalerQuestionnaire questionnaire = this.lambdaQuery().eq(WholesalerQuestionnaire::getId, id).one();
        if (questionnaire == null) {
            return;
        }
        List<WholesalerRelationship> relationshipList = wrService.lambdaQuery()
                .eq(WholesalerRelationship::getBusinessId, questionnaire.getId())
                .eq(WholesalerRelationship::getType, QUES_RELATIONSHIP)
                .list();
        //该协议时间是否有效(当前时间在有效时间范围内)
        //        boolean valid = questionnaire.getBeginTime().compareTo(LocalDateTime.now()) < 0
        //                && questionnaire.getEndTime().compareTo(LocalDateTime.now()) > 0;
        //        if (valid && relationshipList.stream().anyMatch(relationship -> relationship.getStatus() == 0)) {
        //            throw new ServiceException("该问卷下还有任务没有完成，不能删除该问卷");
        //        }
        //删除回答内容
        List<WholesalerResponse> responseList = responseService.lambdaQuery()
                .eq(WholesalerResponse::getQuestionnaireId, questionnaire.getId())
                .list();
        responseList.forEach(response -> responseService.removeById(response.getId()));
        //删除和档位的关联关系
        if (!relationshipList.isEmpty()) {
            relationshipList.forEach(relationship -> wrService.removeById(relationship.getId()));
        }
        //删除问卷
        mapper.deleteById(id);
        xxlTaskJob.removeTask(questionnaire.getXxlTaskIds());
    }

    @Override
    public void sendNotice(String projectId, String id) {
        //首先根据ID找到协议，
        WholesalerQuestionnaire questionnaire = this.lambdaQuery().eq(WholesalerQuestionnaire::getId, id).one();
        List<String> userIds = new ArrayList<>();
        //根据协议找到对应的没有签约的档位
        //在根据这些档位找到对应的user
        for (WholesalerRelationship wholesalerRelationship : wrService.lambdaQuery().eq(WholesalerRelationship::getType, QUES_RELATIONSHIP)
                .eq(WholesalerRelationship::getBusinessId, questionnaire.getId())
                .eq(WholesalerRelationship::getStatus, 0)
                .list()) {
            try {
                CompanyUser companyUser = cuService.lambdaQuery().eq(CompanyUser::getStallId, wholesalerRelationship.getStallId())
                        .eq(CompanyUser::getProjectId, projectId)
                        .eq(CompanyUser::getOwner, true).one();
                if (companyUser != null) {
                    userIds.add(companyUser.getUserId());
                }
            } catch (Exception e) {
                log.error("调查问卷催办失败，档位ID：" + wholesalerRelationship.getStallId());
            }

        }
        sendUrgeNotice(questionnaire, projectId, userIds);
    }

    @Override
    public WholeStallQuesDTO analysis(String id) {
        WholeStallQuesDTO stallQuesDto = new WholeStallQuesDTO();

        //获取问卷对象，页面头部内容要写问卷名称，描述等
        WholesalerQuestionnaire questionnaire = this.getById(id);
        if (questionnaire == null) {
            throw new ServiceException("该问卷不存在或已被删除");
        }
        stallQuesDto.setQuestionnaire(questionnaire);
        //获取到问卷模板DTO
        WholesalerQuesTemp quesTemp = qtMapper.selectById(questionnaire.getTempId());

        //根据问卷模板ID获取到问卷下面的题目
        //然后根据题目ID和档位ID找到答过的答案
        List<WholesalerQuestionDTO> questionDTOList = new ArrayList<>();
        for (WholesalerQuestion question : wqService.lambdaQuery()
                .eq(WholesalerQuestion::getQuestionnaireTempId, quesTemp.getId())
                .list()) {

            List<WholesalerResponse> responses = responseService.lambdaQuery()
                    .eq(WholesalerResponse::getQuestionnaireId, questionnaire.getId())
                    .eq(WholesalerResponse::getQuestionId, question.getId())
                    .list();
            // 填写的档位数
            int stallCount = (int) responses.parallelStream().filter(re -> StringUtils.isNotEmpty(re.getAnswer())).count();
            WholesalerQuestionDTO questionDTO = new WholesalerQuestionDTO(question);
            // 如果是选择题，则统计每个答案的答题数
            if (WholesalerEnum.QUESTION_TYPE_SELECT.getCode().equals(question.getType()) ||
                WholesalerEnum.QUESTION_TYPE_MULT_SELECT.getCode().equals(question.getType())) {
                questionDTO.setAnswerAnalysisDTOS(this.analysisOptions(responses, stallCount));
            }
            questionDTO.setStallCount(stallCount);
            questionDTOList.add(questionDTO);
        }
        stallQuesDto.setQuestionDtos(questionDTOList);
        return stallQuesDto;
    }

    @Override
    public void downloadQuestionnaire(String questionnaireId, HttpServletResponse response) {
        try {
            String file = "问卷调查填写情况" + ".xlsx";
            String codedFileName;
            codedFileName = java.net.URLEncoder.encode(file, "UTF-8");
            // 设置response参数，可以打开下载页面
            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + codedFileName);
            OutputStream out = new BufferedOutputStream(response.getOutputStream());
            List<List<String>> datas = new ArrayList<>();
            // 表头
            List<String> headers = new ArrayList<>();
            headers.add("序号");
            headers.add("档位名称");
            headers.add("填写时间");
            // 查询标题和数据
            this.getQuestionnaireByDownload(questionnaireId, headers, datas);
            ExcelUtils utils = new ExcelUtils();
            XSSFWorkbook workbook = new XSSFWorkbook();
            utils.exportExcel(workbook, 0, "问卷调查数据", headers, datas, out);
            workbook.write(out);
            out.close();
        } catch (Exception e) {
            log.error("下载问卷调查填写详情失败", e);
            throw new ServiceException("下载失败！");
        }
    }

    private void getQuestionnaireByDownload(String questionnaireId, List<String> headers,
                                            List<List<String>> datas
                                           ) throws JsonProcessingException {
        //获取问卷对象，页面头部内容要写问卷名称，描述等
        WholesalerQuestionnaire questionnaire = this.getById(questionnaireId);
        //获取到问卷模板DTO
        WholesalerQuesTemp quesTemp = qtMapper.selectById(questionnaire.getTempId());
        // 查询该问卷调查已经填写的档位
        Map<String, List<String>> map = new HashMap<>();
        List<WholesalerRelationship> relationships = wrService.lambdaQuery().eq(WholesalerRelationship::getType, QUES_RELATIONSHIP)
                .eq(WholesalerRelationship::getBusinessId, questionnaireId).eq(WholesalerRelationship::getStatus, 1).list();
        // 序号
        AtomicInteger num = new AtomicInteger(1);
        // 给每个档位设置默认的前三列数据；序号、档位名称、填写时间
        relationships.forEach(relationship -> {
            List<String> stallInfo = new ArrayList<>();
            Space space = spaceService.getById(relationship.getStallId());
            if (space != null) {
                stallInfo.add(String.valueOf(num.get()));
                stallInfo.add(space.getName());
                stallInfo.add(DateUtils.getDateTime(relationship.getSigendTime()));
                map.put(relationship.getStallId(), stallInfo);
                num.getAndIncrement();
            }
        });
        //根据问卷模板ID获取到问卷下面的题目
        //然后根据题目ID和档位ID找到答过的答案
        for (WholesalerQuestion question : wqService.lambdaQuery()
                .eq(WholesalerQuestion::getQuestionnaireTempId, quesTemp.getId())
                .list()) {
            // 把题目依次设置成列名
            headers.add(question.getNumber() + 1 + "、" + question.getName());
            List<WholesalerResponse> responses = responseService.lambdaQuery()
                    .eq(WholesalerResponse::getQuestionnaireId, questionnaire.getId())
                    .eq(WholesalerResponse::getQuestionId, question.getId())
                    .list();
            Map<String, String> optionAndValue = new HashMap<>();
            boolean select = false;
            // 如果是选择题，则初始化选项和值
            if (WholesalerEnum.QUESTION_TYPE_SELECT.getCode().equals(question.getType()) ||
                WholesalerEnum.QUESTION_TYPE_MULT_SELECT.getCode().equals(question.getType())) {
                optionAndValue.putAll(this.getQuestionOptionValue(question.getOptions()));
                select = true;
            }
            boolean finalSelect = select;
            // 循环该题的下的用户答案
            responses.forEach(response -> {
                String stallId = response.getStallId();
                if (map.containsKey(stallId)) {
                    // 得到档位的excel数据，追加每道题的该档位的答案
                    List<String> stallExcelDatas = map.get(stallId);
                    if (finalSelect) {
                        // 选择题，把选项转成具体的值
                        List<String> values = new ArrayList<>();
                        List<String> userAnswers = this.getUserAnswerToList(response.getAnswer());
                        if (userAnswers.size() < 1) {
                            // 未答
                            stallExcelDatas.add("");
                        } else {
                            // 把用户选项转换成值；如：A、什么是快乐星球；存储的时候只存了一个A
                            userAnswers.forEach(ua -> {
                                values.add(ua + "、" + optionAndValue.get(ua));
                            });
                            stallExcelDatas.add(String.join(";", values));
                        }
                    } else {
                        // 非选择题直接填写答案，去掉前端格式化数据的双引号
                        stallExcelDatas.add(
                                response.getAnswer().length() > 0 ? response.getAnswer().substring(1, response.getAnswer().length() - 1)
                                                                  : "");
                    }
                }
            });
        }
        // 构造返回数据的datas
        map.forEach((stallId, stallQuests) -> {
            datas.add(stallQuests);
        });
        // 根据序号排序
        datas.sort(Comparator.comparing(da -> Integer.parseInt(da.get(0))));
    }

    // 获取题目选项和值；如：A：xxxx
    private Map<String, String> getQuestionOptionValue(String options) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, String>> optionList = mapper.readValue(options, List.class);
        Map<String, String> map = new HashMap<>();
        optionList.forEach(option -> {
            map.put(option.get("label"), option.get("value"));
        });
        return map;
    }

    // 分析答题结果的每个选项人数占比
    private List<WholesalerQuestionAnswerAnalysisDTO> analysisOptions(List<WholesalerResponse> responses, int stallCount) {
        List<String> answers = new ArrayList<>();
        responses.forEach(response -> {
            String answer = response.getAnswer();
            answers.addAll(this.getUserAnswerToList(answer));
        });
        List<WholesalerQuestionAnswerAnalysisDTO> analysisDTOS = new ArrayList<>();
        answers.stream().collect(Collectors.groupingBy(answer -> answer)).forEach((answer, list) -> {
            WholesalerQuestionAnswerAnalysisDTO dto = new WholesalerQuestionAnswerAnalysisDTO();
            dto.setOption(answer);
            dto.setCount(list.size());
            dto.setRatio(this.getRate(list.size(), stallCount));
            analysisDTOS.add(dto);
        });
        return analysisDTOS;
    }

    // 把用户答案的字符串转成数组
    private List<String> getUserAnswerToList(String answer) {
        List<String> answers = new ArrayList<>();
        if (StringUtils.isEmpty(answer)) {
            return answers;
        }
        if (answer.contains("[")) {
            // 多选答案
            ObjectMapper mapper = new ObjectMapper();
            try {
                answers.addAll(mapper.readValue(answer, List.class));
            } catch (Exception e) {

            }
        } else {
            // 单选答案
            answers.add(answer.substring(1, 2));
        }
        return answers;
    }

    // 判断用户是否是档主
    private boolean parseUserIsOwner(String stallId, Long userId) {
        try {
            CompanyUser companyUser = cuService.lambdaQuery().eq(CompanyUser::getStallId, stallId).eq(CompanyUser::getUserId, userId).one();
            return companyUser.getOwner();
        } catch (Exception e) {
            log.error("", e);
        }
        return true;
    }

    // 判断问卷是否已经填写（同一个档位下每个人都会收到任务，如果一人已经提交了，其他人便不可再次提交更改）
    private synchronized boolean checkQuestIsWrite(String questionnaireId, String stallId) {
        WholesalerQuestionnaire questionnaire = this.lambdaQuery().eq(WholesalerQuestionnaire::getId, questionnaireId)
                .eq(WholesalerQuestionnaire::getStatus, WholesalerEnum.QUES_STATUS_VALID.getCode()).one();
        if (questionnaire == null) {
            return false;
        }
        WholesalerRelationship one = wrService.lambdaQuery().eq(WholesalerRelationship::getStallId, stallId)
                .eq(WholesalerRelationship::getType, QUES_RELATIONSHIP)
                .eq(WholesalerRelationship::getBusinessId, questionnaireId)
                .eq(WholesalerRelationship::getStatus, true)
                .one();
        if (one != null) {
            return false;
        }
        return true;
    }

    private int getRate(int increment, int total) {
        double result = (double) increment / total * 100;
        return new BigDecimal(result).setScale(0, RoundingMode.HALF_UP).intValue();
    }

    private void sendUrgeNotice(WholesalerQuestionnaire questionnaire, String projectId, List<String> userIds) {
        try {
            //            smsApi.send(projectId, QUES_NOTICE, questionnaire.getId(), new ArrayList<>(userIds),
            //                    JSmsTemplateEnum.T_PROTOCOL.getSmsTempId(), questionnaire.getName(), DateUtils.getDate(questionnaire.getBeginTime(), "yyyy-MM-dd"), DateUtils.getDate(questionnaire.getEndTime(), "yyyy-MM-dd"));
            //            weChatMsgApi.send(projectId, QUES_NOTICE, new ArrayList<>(userIds), QUES_CONTENT);
            //            siteMsgApi.send(projectId, QUES_NOTICE, questionnaire.getId(), userIds, QUES_TITLE, QUES_CONTENT, 7 * 24);
        } catch (Exception e) {
            log.error("催办问卷调查信息发送失败，任务ID：" + questionnaire.getId(), e);
        }
    }

    //    改状态
    private void createXxlJob(WholesalerQuestionnaireDTO dto) {
        // 生成定时任务，准时更新任务的生效和失效状态
        if (!WholesalerEnum.QUES_STATUS_NOT_VALID.getCode().equals(dto.getStatus())) {
            String xxlIds = xxlTaskJob.createTask(WholesalerEnum.BUSINESS_TYPE_QUESTIONNAIRE.getCode(), dto.getId(),
                                                  WholesalerEnum.QUES_STATUS_VALID.getCode().equals(dto.getStatus()) ? null
                                                                                                                     : dto.getBeginTime(), dto.getEndTime());
            WholesalerQuestionnaire questionnaire = dto.convert();
            questionnaire.setXxlTaskIds(xxlIds);
            questionnaire.setCode(BizCodeUtils.generate("WJ"));
            this.updateById(questionnaire);
        }
    }

    private void checkValidStatus(WholesalerQuestionnaireDTO dto) {
        // 判断状态
        if (dto.getEndTime().isBefore(LocalDateTime.now())) {
            dto.setStatus(WholesalerEnum.QUES_STATUS_NOT_VALID.getCode());
            dto.setBeginTime(LocalDateTime.now());
        } else if (dto.isBeginTimeIsNow() || dto.getBeginTime().isBefore(LocalDateTime.now())) {
            if (dto.isBeginTimeIsNow()) {
                dto.setBeginTime(LocalDateTime.now());
            }
            dto.setStatus(WholesalerEnum.QUES_STATUS_VALID.getCode());
        } else {
            dto.setStatus(WholesalerEnum.QUES_STATUS_NOT_CREATE.getCode());
        }
        dto.setBeginTime(dto.getBeginTime() == null ? LocalDateTime.now() : dto.getBeginTime());
    }

    private void firstSendNotice(List<String> stallIds, String projectId, WholesalerQuestionnaire questionnaire) {
        if (stallIds == null || stallIds.size() < 1) {
            return;
        }
        WholesalerQuestionnaireServiceImpl _this = this;
        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                //                DataSourceContextHolder.setDataSourceKey(DataSources.PROJECT.getValue());
                // 查到档位的档主信息并发送通知
                List<String> userIds = new ArrayList<>();
                stallIds.forEach(stallId -> {
                    String stallOwner = _this.getStallOwner(projectId, stallId);
                    if (StringUtils.isNotEmpty(stallOwner)) {
                        userIds.add(stallOwner);
                    }
                });
                _this.sendUrgeNotice(questionnaire, projectId, userIds);
            }
        });
    }

    // 查询档位的档主
    private String getStallOwner(String projectId, String stallId) {
        CompanyUser companyUser = cuService.lambdaQuery().eq(CompanyUser::getStallId, stallId)
                .eq(CompanyUser::getProjectId, projectId)
                .eq(CompanyUser::getOwner, true).one();
        return companyUser == null ? "" : companyUser.getUserId();
    }

}

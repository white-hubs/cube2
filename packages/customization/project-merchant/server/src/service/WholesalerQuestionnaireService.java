package com.guhecloud.cube.deliver.project.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.deliver.project.merchant.commons.core.LoginUser;
import com.guhecloud.cube.deliver.project.merchant.dto.*;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerQuestionnaire;

import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wangliang
 * @since 2022-01-11
 */
public interface WholesalerQuestionnaireService extends IService<WholesalerQuestionnaire> {

    /**
     * 多对多中间关系表中问卷调查的type
     */
    String QUES_RELATIONSHIP = "questionnaire-relationship";
    String QUES_TITLE ="问卷调查待填写";
    String QUES_NOTICE = "questionnaireNotice";
    String QUES_CONTENT ="您有新的问卷调查，请尽快在手机上进行填写";

    /**
     * 获取档位下的问卷调查列表
     * @param stallId 档位ID
     * @return 返回问卷列表集合
     */
    List <WholesalerQuestionnaireAppDTO> getQuestionnaireByStallId(String stallId);

    /**
     * PC根据查询条件返回问卷记录
     * @param queryDTO 查询条件
     * @return 返回问卷调查列表
     */
    List<WholesalerQuestionnaireDTO> getQuestionnaires(WholesalerQuestionnaireQueryDTO queryDTO);


    /**
     * 根据ID返回问卷调查详情
     * @param id 问卷调查的ID
     * @return 返回问卷调查详情DTO
     */
    WholesalerQuestionnaireDTO getQuestionnaireById(String id);

    /**
     * 查询问卷调查下的档位列表
     * @param id 问卷ID
     * @param queryDTO 查询参数
     * @return
     */
    List<WholeSpaceDTO> getStallList(String id, WholesalerQuestionnaireQueryDTO queryDTO);

    /**
     *  根据档位ID和问卷ID，获取到某档位对于具体问卷的回答DTO
     * @param id 问卷ID
     * @param stallId 档位ID
     * @return 答题内容
     */
    WholeStallQuesDTO getStallQuestionnaire(String id, String stallId);
    /**
     * 新增问卷调查DTO
     * @param dto 问卷调查DTO
     * @return 返回新增记录的ID
     */
    String saveOrUpdate(WholesalerQuestionnaireDTO dto);

    /**
     * 传入答题模板DTO
     * 实际上WholesalerQuesTempDTO中的WholesalerQuestionDTO中多增加了一个答案字段，存放用户的答案
     * （数据库没有该字段）
     * @param dto 答题模板
     * @param user 登录用户
     */
    void responseQuestionnaire(WholeStallQuesDTO dto, LoginUser user);

    /**
     * 根据ID删除问卷调查
     * 如果下面有未完成的问卷调查，那么是不允许删除的
     * @param id 问卷ID
     */
    void removeById(String id);


    void sendNotice(String projectId, String id);

    WholeStallQuesDTO analysis(String id);

    /**
     * 下载问卷调查的填写详情
     * @param questionnaireId 问卷ID
     * @param response
     */
    void downloadQuestionnaire(String questionnaireId, HttpServletResponse response);

}

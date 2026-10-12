package com.guhecloud.cube.deliver.project.merchant.controller;

import com.guhecloud.cube.deliver.project.merchant.commons.core.LoginUser;
import com.guhecloud.cube.deliver.project.merchant.commons.web.PageRequest;
import com.guhecloud.cube.deliver.project.merchant.commons.web.PageResponse;
import com.guhecloud.cube.deliver.project.merchant.dto.WholeSpaceDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.WholeStallQuesDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerQuestionnaireAppDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerQuestionnaireDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerQuestionnaireQueryDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerQuestionnaire;
import com.guhecloud.cube.deliver.project.merchant.service.WholesalerQuestionnaireService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wangliang
 * @since 2022-01-11
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WholesalerQuestionnaireController {

    private final WholesalerQuestionnaireService service;

    @GetMapping("/wholesaler-questionnaire/notice/{id}")
    public void sendNotice(String projectId, @PathVariable("id") String id) {
        service.sendNotice(projectId, id);
    }

    @GetMapping("/wholesaler-questionnaire/notices")
    public void sendNotices(String projectId) {
        List<WholesalerQuestionnaire> list = service.lambdaQuery()
                .eq(WholesalerQuestionnaire::getProjectId, projectId).list();
        list.forEach(questionnaire -> service.sendNotice(projectId, questionnaire.getId()));
    }

    @GetMapping("/wholesaler-questionnaire/{stallId}/page")
    public PageResponse<WholesalerQuestionnaireAppDTO> getQuestionnaireByStallId(@PathVariable("stallId") String stallId,
                                                                                 PageRequest pageRequest
                                                                                ) {
        return PageResponse.doPage(service.getQuestionnaireByStallId(stallId), pageRequest);
    }

    @GetMapping("/wholesaler-questionnaire/page")
    public PageResponse<WholesalerQuestionnaireDTO> getQuestionnaires(WholesalerQuestionnaireQueryDTO queryDTO, PageRequest pageRequest) {
        List<WholesalerQuestionnaireDTO> questionnaires = service.getQuestionnaires(queryDTO);
        //        questionnaires.sort(Comparator.comparing(WholesalerQuestionnaireDTO::getCreateTime, Comparator.reverseOrder()));
        return PageResponse.doPage(questionnaires, pageRequest);
    }

    @GetMapping("/wholesaler-questionnaire/{id}")
    public WholesalerQuestionnaireDTO getQuestionnaireById(@PathVariable("id") String id) {
        return service.getQuestionnaireById(id);
    }

    @GetMapping("/wholesaler-questionnaire/stall/page")
    public PageResponse<WholeSpaceDTO> getStallPage(
            @RequestParam("id") String id,
            WholesalerQuestionnaireQueryDTO queryDTO,
            PageRequest pageRequest
                                                   ) {
        return PageResponse.doPage(service.getStallList(id, queryDTO), pageRequest);
    }

    @GetMapping("/wholesaler-questionnaire/{id}/{stallId}")
    public WholeStallQuesDTO getStallQuestionnaire(@PathVariable("id") String id, @PathVariable("stallId") String stallId) {
        return service.getStallQuestionnaire(id, stallId);
    }

    @GetMapping("/wholesaler-questionnaire/analysis")
    public WholeStallQuesDTO analysis(String id) {
        return service.analysis(id);
    }

    @GetMapping("/wholesaler-questionnaire/detail-download")
    public void downloadTemplate(
            @RequestParam(value = "questionnaireId") String questionnaireId,
            HttpServletResponse response
                                ) {
        service.downloadQuestionnaire(questionnaireId, response);
    }

    @PostMapping("/wholesaler-questionnaire")
    public String saveOrUpdate(@Valid @RequestBody WholesalerQuestionnaireDTO dto) {
        return service.saveOrUpdate(dto);
    }

    @PostMapping("/wholesaler-questionnaire/relationship")
    public void responseQuestionnaire(@Valid @RequestBody WholeStallQuesDTO dto, LoginUser user) {
        service.responseQuestionnaire(dto, user);
    }

    @DeleteMapping("/wholesaler-questionnaire/{id}")
    public void removeById(@PathVariable("id") String id) {
        service.removeById(id);
    }

}


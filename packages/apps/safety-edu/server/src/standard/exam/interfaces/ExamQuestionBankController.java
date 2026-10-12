package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.safety.education.server.common.application.ThemeService;
import com.guhecloud.cube.srv.gen.safety.education.server.common.domain.po.ThemePO;
import com.guhecloud.cube.srv.gen.safety.education.server.common.interfaces.vo.BatchOptResultVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.ExamQuestionBankService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.command.ChangeExamQuestionBankCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.commandgateway.command.IncreaseExamQuestionBankCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.application.querygateway.query.ExamQuestionBankQuery;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po.ExamQuestionBankPO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo.ExamQuestionBankImportVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo.ExamQuestionBankVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.contant.ImportConstant.EXAM_QUESTION_BANK_TEMPLATE;
import static com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.contant.ImportConstant.IMPORT_ERROR_LIST;

/**
 * 考题库管理接口
 *
 * @author lanchun
 * @date 2023/4/18 15:42
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExamQuestionBankController {
    private final ExamQuestionBankService examQuestionBankService;

    private final ThemeService themeService;

    /**
     * 考题新增
     *
     * @param increaseExamQuestionBankCommand
     *
     * @return
     */
    @PostMapping("/exam-question-bank")
    public Long save(@Valid @RequestBody IncreaseExamQuestionBankCommand increaseExamQuestionBankCommand) {
        ExamQuestionBankPO examQuestionBankPO = ObjectUtils.copying(increaseExamQuestionBankCommand, ExamQuestionBankPO.class);
        this.examQuestionBankService.save(examQuestionBankPO);
        return examQuestionBankPO.getId();
    }

    /**
     * 考题编辑
     *
     * @param id
     * @param changeExamQuestionBankCommand
     */
    @PutMapping("/exam-question-bank/{id}")
    public void update(@PathVariable("id") Long id, @Valid @RequestBody ChangeExamQuestionBankCommand changeExamQuestionBankCommand) {
        ExamQuestionBankPO examQuestionBankPO = this.examQuestionBankService.getById(id);
        Assert.isNotFound(examQuestionBankPO, "考题不存在");
        ObjectUtils.copying(changeExamQuestionBankCommand, examQuestionBankPO);
        this.examQuestionBankService.updateById(examQuestionBankPO);
    }

    /**
     * 考题详情
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/exam-question-bank/{id}")
    public ExamQuestionBankVO getExamQuestionBank(@PathVariable("id") Long id) {
        ExamQuestionBankPO examQuestionBankPO = this.examQuestionBankService.getById(id);
        Assert.isNotFound(examQuestionBankPO, "考题不存在");
        ExamQuestionBankVO examQuestionBankVO = ObjectUtils.copying(examQuestionBankPO, ExamQuestionBankVO.class);
        examQuestionBankVO.setQuestionTypeName(examQuestionBankVO.getQuestionType().getDescription());
        ThemePO themePO = this.themeService.getById(examQuestionBankPO.getThemeId());
        if (Objects.nonNull(themePO)) {
            examQuestionBankVO.setThemeName(themePO.getName());
        }
        return examQuestionBankVO;
    }

    /**
     * 考题分页列表
     *
     * @param examQuestionBankQuery
     *
     * @return
     */
    @GetMapping("/exam-question-bank/page")
    public Pagination<ExamQuestionBankVO> getExamQuestionBankPage(@Valid ExamQuestionBankQuery examQuestionBankQuery) {
        Page<ExamQuestionBankVO> page = this.examQuestionBankService.getExamQuestionBankPage(examQuestionBankQuery);
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }
        page.getRecords().forEach(examQuestionBankVO ->
                                          examQuestionBankVO.setQuestionTypeName(examQuestionBankVO.getQuestionType().getDescription())
                                 );
        return PagingUtils.convert(page);
    }

    /**
     * 考题删除
     *
     * @param id
     */
    @DeleteMapping("/exam-question-bank/{id}")
    public void removeExamQuestionBank(@PathVariable("id") Long id) {
        DatabaseContextHolder.softDelete(ExamQuestionBankPO.class, id);
    }

    /**
     * 考题导入模板下载
     */
    @GetMapping("/exam-question-bank/template")
    public void downloadExamQuestionBankTemplate(@RequestParam(value = "sheetName", required = false) String sheetName,
                                                 HttpServletResponse response
                                                ) {
        examQuestionBankService.writeTemplate(new ArrayList<>(), EXAM_QUESTION_BANK_TEMPLATE, response);
    }

    /**
     * 考题导入
     */
    @PostMapping("/exam-question-bank/import")
    public BatchOptResultVO importExamQuestionBank(@RequestParam("file") MultipartFile file) {
        return examQuestionBankService.importExamQuestionBank(file);
    }

    /**
     * 下载导入错误数据
     */
    @PostMapping("/exam-question-bank/error/export")
    public void exportImportErrorData(@RequestBody List<ExamQuestionBankImportVO> importErrorList, HttpServletResponse response) {
        examQuestionBankService.writeTemplate(importErrorList, IMPORT_ERROR_LIST, response);
    }
}

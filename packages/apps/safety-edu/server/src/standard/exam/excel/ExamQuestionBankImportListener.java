package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo.ExamQuestionBankImportVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo.QuestionOptionVO;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/11/26 20:11
 * @Description:
 */
@Slf4j
public class ExamQuestionBankImportListener implements ReadListener<Map<Integer, String>> {

    public static final ThreadLocal<List<ExamQuestionBankImportVO>> IMPORT_DATA_CACHE =
            ThreadLocal.withInitial(ArrayList::new);
    private final Map<Integer, String> headCache = new HashMap<>();
    private boolean first = true;

    @Override
    public void invoke(Map<Integer, String> data, AnalysisContext context) {
        if (first) {
            headCache.putAll(data);
            first = false;
            return;
        }
        ExamQuestionBankImportVO importVO = new ExamQuestionBankImportVO();
        List<QuestionOptionVO> options = new ArrayList<>();

        String rightAnswer = null;
        Map<Integer, String> optionMap = new HashMap<>();
        Field[] fields = importVO.getClass().getDeclaredFields();
        for (Map.Entry<Integer, String> entry : data.entrySet()) {
            String title = headCache.get(entry.getKey());

            boolean deafult = false;
            for (Field field : fields) {
                ExcelProperty annotation = field.getAnnotation(ExcelProperty.class);
                if (annotation != null && annotation.value()[1].equals(title)) {
                    field.setAccessible(true);
                    ReflectionUtils.setField(field, importVO, entry.getValue());
                    deafult = true;
                    //正确答案
                    if (ExamQuestionBankImportVO.RIGHT_ANSWER.equals(field.getName())) {
                        rightAnswer = entry.getValue();
                    }
                    //默认选项
                    if (ExamQuestionBankImportVO.OPTION_A.equals(field.getName())) {
                        optionMap.put(entry.getKey(), entry.getValue());
                    }
                    if (ExamQuestionBankImportVO.OPTION_B.equals(field.getName())) {
                        optionMap.put(entry.getKey(), entry.getValue());
                    }
                    break;
                }
            }
            //新增的选项
            if (StringUtils.isNotBlank(entry.getValue()) && !deafult) {
                optionMap.put(entry.getKey(), entry.getValue());
            }
        }
        //选项
        int i = 0;
        for (Map.Entry<Integer, String> entry : optionMap.entrySet()) {
            String title = headCache.get(entry.getKey());
            String optionId = getOptionId(title);
            QuestionOptionVO questionOptionVO = new QuestionOptionVO();
            questionOptionVO.setSort(i);
            questionOptionVO.setOptionID(optionId);
            questionOptionVO.setOptionTitle(entry.getValue());
            if (StringUtils.isNotBlank(rightAnswer) && StringUtils.containsAny(rightAnswer, optionId)) {
                questionOptionVO.setIsRight(true);
            }
            options.add(questionOptionVO);
            i++;
        }
        importVO.setOptions(options);
        IMPORT_DATA_CACHE.get().add(importVO);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
    }

    private String getOptionId(String optionTitle) {
        StringBuilder optionId = new StringBuilder();
        for (int i = 0; i < optionTitle.length(); i++) {
            if (Character.isUpperCase(optionTitle.charAt(i))) {
                optionId.append(optionTitle.charAt(i));
            }
        }
        return optionId.toString();
    }
}

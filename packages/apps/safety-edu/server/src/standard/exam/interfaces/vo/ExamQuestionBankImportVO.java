package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import lombok.Data;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/11/26 10:36
 * @Description: 导入考题数据模型
 */

@Data
@ContentRowHeight(10)
@ColumnWidth(25)
public class ExamQuestionBankImportVO {

    public static final String RIGHT_ANSWER = "rightAnswer";
    public static final String OPTION_A = "optionA";
    public static final String OPTION_B = "optionB";
    /**
     * 名称
     */
    @ExcelProperty(value = { "${tip}", "*题目" }, index = 0)
    private String name;
    /**
     * 分数
     */
    @ExcelProperty(value = { "${tip}", "*题目分数" }, index = 1)
    private String scoreStr;
    /**
     * 题目主题
     */
    @ExcelProperty(value = { "${tip}", "*题目主题" }, index = 2)
    private String themeName;

    /**
     * 题目类型
     */
    @ExcelProperty(value = { "${tip}", "*题目类型" }, index = 3)
    private String questionTypeDesc;
    /**
     * 正确答案
     */
    @ExcelProperty(value = { "${tip}", "*正确答案" }, index = 4)
    private String rightAnswer;
    /**
     * 答案解析
     */
    @ExcelProperty(value = { "${tip}", "*答案解析" }, index = 5)
    private String answerAnalysis;
    /**
     * 选项
     */
    @ExcelProperty(value = { "${tip}", "A选项" }, index = 6)
    private String optionA;

    @ExcelProperty(value = { "${tip}", "B选项" }, index = 7)
    private String optionB;

    /**
     * 选项
     */
    @ExcelIgnore
    private List<QuestionOptionVO> options;

    /**
     * 校验结果
     */
    @ExcelIgnore
    private boolean validFail = false;
}

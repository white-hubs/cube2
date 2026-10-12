package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.contant;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/11/26 17:08
 * @Description:
 */
public class ImportConstant {

    public final static String EXAM_QUESTION_BANK_TEMPLATE = "考题导入模板";

    public final static String IMPORT_ERROR_LIST = "导入错误清单";

    /**
     * 导入模板提示
     */
    public static final String TIP_CONTENT = "温馨提示" + "\n" +
                                             "1、Excel中题目、题目分数、题目主题、选项、正确答案都不可改变顺序或删除重新添加，否则会上传不成功" +
                                             "\n" +
                                             "2、模板默认提供A、B、两个选项，如需增加选项，请手动添加, 多选题答案使用英文逗号拼接,例:A,B,C";
}

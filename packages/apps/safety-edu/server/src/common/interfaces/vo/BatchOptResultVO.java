package com.guhecloud.cube.srv.gen.safety.education.server.common.interfaces.vo;

import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo.ExamQuestionBankImportVO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/11/26 17:34
 * @Description: 导入返回统一结果
 */

@Data
@Builder
public class BatchOptResultVO {
    /**
     * 成功数
     */
    private long success;

    /**
     * 失败数
     */
    private long fail;

    /**
     * 错误信息
     */
    private List<String> errorMsg;

    /**
     * 错误数据
     */
    private List<ExamQuestionBankImportVO> importErrorList;
}

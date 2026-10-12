package com.guhecloud.cube.base.flow.context.interfaces.vo;

import lombok.Data;
import vo.FormBizFieldMapVO;

import java.util.List;

/**
 * @author bq
 * @date 2022/09/08
 * @description 表单定义详情
 */
@Data
public class FormFlowDetailExtendVO extends FormFlowDetailVO {
    List<FormBizFieldMapVO> formBizFieldMapList;
}

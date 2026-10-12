package com.guhecloud.cube.base.flow.context.commandgateway;

import com.guhecloud.cube.base.flow.context.commandgateway.command.FormFlowCommand;
import com.guhecloud.cube.base.flow.context.interfaces.vo.FormFieldVO;
import com.guhecloud.cube.base.flow.context.interfaces.vo.FormFlowDetailVO;
import com.guhecloud.cube.base.flow.context.interfaces.vo.FormFlowGroupCountVO;
import com.guhecloud.cube.base.flow.context.interfaces.vo.FormFlowGroupCountTreeVO;
import com.guhecloud.cube.base.flow.context.querygateway.query.FormFlowGroupQueryParam;
import com.guhecloud.cube.base.flow.context.querygateway.query.FormFlowQueryParam;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/9/4 18:26
 * @Description:
 */
public interface CustomFormFlowService {
    FormFlowDetailVO get(Long id);

    List<FormFlowDetailVO> getList(FormFlowQueryParam param);

    Pagination<FormFlowDetailVO> getPage(FormFlowQueryParam param);

    Long saveOrUpdate(FormFlowCommand formFlowCommand);

    Boolean enable(Long id, Boolean enable);

    Boolean delete(Long id);

    List<FormFlowGroupCountVO> getFormFlowGroupCount(FormFlowGroupQueryParam param);

    List<FormFlowGroupCountVO> getFormFlowGroupCountPermission(FormFlowGroupQueryParam param);

    List<FormFlowGroupCountTreeVO> getFormFlowGroupTreeCount(FormFlowGroupQueryParam param);

    List<FormFlowGroupCountTreeVO> getFormFlowGroupTreeCountPermission(FormFlowGroupQueryParam param);

    List<FormFieldVO> getBusinessType(String application);

}

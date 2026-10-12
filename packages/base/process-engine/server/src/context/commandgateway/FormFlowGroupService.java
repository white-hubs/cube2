package com.guhecloud.cube.base.flow.context.commandgateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.flow.context.commandgateway.command.FormFlowGroupCommand;
import com.guhecloud.cube.base.flow.context.interfaces.vo.FormFlowGroupTreeVO;
import com.guhecloud.cube.base.flow.context.po.FormFlowGroupPO;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/9/4 10:14
 * @Description:
 */
public interface FormFlowGroupService extends IService<FormFlowGroupPO> {

    /**
     * 保存或更新表单流程分组
     *
     * @param command
     *
     * @return
     */
    Boolean saveOrUpdate(FormFlowGroupCommand command);

    Boolean deletedById(Long id);

    /**
     * 查询分组树
     *
     * @param keyword 关键字
     *
     * @return 分组树
     */
    List<FormFlowGroupTreeVO> getTree(String keyword, String application);

    /**
     * 查询可关联表单模板的末级分组
     *
     * @param keyword 关键字
     *
     * @return 末级分组
     */
    List<FormFlowGroupPO> listLeafGroups(String keyword, String application);

    /**
     * 校验表单模板关联分组
     *
     * @param formGroupId 分组ID
     */
    void assertLeafGroupForFormFlow(Long formGroupId);

    /**
     * 解析分组筛选对应的末级分组ID
     *
     * @param formGroupId 分组ID
     *
     * @return 末级分组ID
     */
    List<Long> listLeafGroupIds(Long formGroupId);
}

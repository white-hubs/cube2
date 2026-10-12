package com.guhecloud.cube.base.flow.context.commandgateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.flow.context.enumeration.PermissionType;
import com.guhecloud.cube.base.flow.context.interfaces.vo.FormPermissionVO;
import com.guhecloud.cube.base.flow.context.po.FormPermissionPO;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/9/4 15:00
 * @Description:
 */
public interface FormPermissionService extends IService<FormPermissionPO> {

    /**
     * 新增或更新表单权限
     *
     * @param id
     * @param permissionType
     * @param permissionIds
     */
    void saveOrUpdate(Long id, PermissionType permissionType, List<Long> permissionIds);

    /**
     * 根据表单流程id获取表单权限
     *
     * @param id
     *
     * @return
     */
    FormPermissionVO getPermissionByFormFlowId(Long id);

    List<Long> getCurrentUserPermissionList();

}

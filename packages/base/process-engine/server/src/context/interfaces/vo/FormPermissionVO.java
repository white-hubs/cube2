package com.guhecloud.cube.base.flow.context.interfaces.vo;

import com.guhecloud.cube.base.flow.context.enumeration.PermissionType;
import lombok.Data;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/9/8 14:33
 * @Description:
 */
@Data
public class FormPermissionVO {

    private PermissionType permissionType;

    private List<FormFieldVO> permissionList;
}

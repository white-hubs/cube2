package com.guhecloud.cube.base.bracing.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wql
 * @describe 通知对象
 * @date 2023/9/1 17:33
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssemblyNoticeObjDTO {
    /**
     * 通知对象类型
     */
    private String objType;
    /**
     * 通知对象id
     */
    private String objId;
    /**
     * 通知对象信息
     */
    private String objInfo;
}

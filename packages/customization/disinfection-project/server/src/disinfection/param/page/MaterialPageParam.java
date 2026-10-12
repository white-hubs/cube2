package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.page;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MaterialPageParam extends Paging {
    /*8
     * 项目ID
     */
    private Long projectId;

    /**
     * 文件类型ID
     */
    private Long fileTypeId;
    /**
     * 关键字
     */
    private String keyword;
}

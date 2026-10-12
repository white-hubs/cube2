package com.guhecloud.subsystem.requester.sdk.vo.information;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 节目单详情
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProgramVo {

    /**
     * 节目id
     */
    private String id;
    /**
     * 节目名称
     */
    private String name;
    /**
     * 分类名称
     */
    private String categoryName;
    /**
     * 素材列表
     */
    private List<MaterialVo> materials;
}

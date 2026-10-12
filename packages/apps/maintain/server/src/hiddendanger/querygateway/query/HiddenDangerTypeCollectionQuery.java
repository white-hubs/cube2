package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.querygateway.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author lzy
 * @date 2022/4/26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class HiddenDangerTypeCollectionQuery {

    /**
     * 隐患分类主键
     */
    private Long hiddenDangerCategoryId;
    /**
     * 隐患类型主键
     */
    private Long hiddenDangerTypeId;

}

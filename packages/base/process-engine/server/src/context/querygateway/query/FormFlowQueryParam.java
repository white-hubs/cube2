package com.guhecloud.cube.base.flow.context.querygateway.query;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class FormFlowQueryParam extends Paging {

    private String keyword;

    private String type;

    private String status;

    private String application;

    private Boolean enable;

    private Long fromGroupId;
    // 逗号分割的类型
    private String types;

    public List<String> getTypeList() {
        if (StringUtils.isBlank(types)) {
            return Collections.emptyList();
        }
        return StringUtils.split(types, StringUtils.DEFAULT_DELIMITER_REGEX);
    }

}

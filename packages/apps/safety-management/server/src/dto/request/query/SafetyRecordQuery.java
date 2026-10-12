package com.guhecloud.cube.srv.gen.safety.management.dto.request.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class SafetyRecordQuery extends BaseQuery {
    private Long recordId;
    private Boolean errorTagBool;
    private List<String> errorTagList;
}

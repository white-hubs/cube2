package com.guhecloud.cube.srv.gen.safety.management.dto.request.query.loosework;

import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.BaseQuery;
import com.guhecloud.cube.srv.gen.safety.management.enumeration.ProjectReformStausEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/3/19 16:26
 * @Description:
 */

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class ProjectReformQuery extends BaseQuery {

    private ProjectReformStausEnum status;
}

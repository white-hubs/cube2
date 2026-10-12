package com.guhecloud.cube.srv.gen.loose.works.enumeration;

import com.guhecloud.cube.srv.gen.loose.works.dto.ProjectApplyDTO;
import com.guhecloud.cube.srv.gen.loose.works.dto.ProjectReformDTO;
import com.guhecloud.cube.srv.gen.loose.works.dto.ProjectSignContractDTO;
import lombok.Getter;

/**
 * @author wql
 * @describe 小散工程流程表单枚举
 * @date 2023/9/18 15:41
 */
@Getter
public enum ProjectApprovalBizEnum {
    PROJECT_APPLY("工程请示", ProjectApplyDTO.class),
    PROJECT_SIGN_CONTRACT("工程签约", ProjectSignContractDTO.class),
    PROJECT_REFORM("工程整改", ProjectReformDTO.class),
    ;

    final String desc;
    final Class clazz;

    ProjectApprovalBizEnum(String desc, Class clazz) {
        this.desc = desc;
        this.clazz = clazz;
    }
}

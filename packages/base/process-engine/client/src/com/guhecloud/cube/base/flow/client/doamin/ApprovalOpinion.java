package com.guhecloud.cube.base.flow.client.doamin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.guhecloud.cube.base.flow.client.doamin.enumeration.ApprovalTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.Set;

/**
 * 审批意见
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalOpinion {
    /**
     * 文本意见
     */
    private String text;
    /**
     * 附件
     */
    private Set<Long> attachmentIds;

    /**
     * 手写签名
     */
    private String sign;
    private String approvalType = ApprovalTypeEnum.ARTIFICIAL.name();

    public boolean isSign() {
        return StringUtils.isNotEmpty(this.sign);
    }

    @JsonIgnore
    public boolean isOpinion() {
        return StringUtils.isNotEmpty(this.text) || !CollectionUtils.isEmpty(this.attachmentIds);
    }
}

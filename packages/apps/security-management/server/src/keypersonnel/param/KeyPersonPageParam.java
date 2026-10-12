package com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.param;

import com.guhecloud.platform.base.model.Paging;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class KeyPersonPageParam extends Paging {
    /**
     * 查询关键字，匹配姓名或手机号。
     */
    @Size(max = 100, message = "关键字不能大于100")
    private String keyword;

    /**
     * 风险等级：HIGH 高风险，MEDIUM 中风险，LOW 低风险。
     */
    private String riskLevel;

    /**
     * 人员状态：ACTIVE 在管，INACTIVE 撤管。为空时默认查询 ACTIVE。
     */
    private String status;

    /**
     * mdata 区域序列号，传入后查询该区域及其下级区域。
     */
    private String areaSequence;
}

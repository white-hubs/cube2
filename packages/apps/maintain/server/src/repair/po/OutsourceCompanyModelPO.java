package com.guhecloud.cube.srv.gen.maintain.server.repair.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author pengjian
 * @date 2022/5/19 11:32
 * @description
 */
@Data
@NoArgsConstructor
@TableName("outsource_company_model")
public class OutsourceCompanyModelPO {
    private Long id;
    private Long outsourceCompanyId;
    private Long outsourceModelId;

    public OutsourceCompanyModelPO(Long outsourceCompanyId, Long outsourceModelId) {
        this.outsourceCompanyId = outsourceCompanyId;
        this.outsourceModelId = outsourceModelId;
    }
}

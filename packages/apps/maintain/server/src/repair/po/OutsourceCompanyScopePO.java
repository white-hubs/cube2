package com.guhecloud.cube.srv.gen.maintain.server.repair.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author pengjian
 * @date 2022/5/19 11:33
 * @description 委外范围与公司联表
 * 注意：目前委外范围等同于设备类型
 */
@Data
@NoArgsConstructor
@TableName("outsource_company_scope")
public class OutsourceCompanyScopePO {
    private Long id;
    private String deviceGenreId;
    private Long outsourceCompanyId;

    public OutsourceCompanyScopePO(String deviceGenreId, Long outsourceCompanyId) {
        this.deviceGenreId = deviceGenreId;
        this.outsourceCompanyId = outsourceCompanyId;
    }
}

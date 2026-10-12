package com.guhecloud.cube.srv.gen.product.market.server.common.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

/**
 * @author Appet
 * @Description 农产品来货量
 * @date 2023-05-10 15:08:36
 */
@Data
public class IncomingQuantityQueryParam extends Paging {

    //来货日期
    private String dateNo;
    //客户ID
    private String custId;
    //客户名称
    private String custName;
    //客户电话
    private String custPhone;
    //来货品类ID
    private String goodsCategoryId;
    //来货量
    //单位
    private String unit;

}
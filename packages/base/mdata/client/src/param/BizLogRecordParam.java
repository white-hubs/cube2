package com.guhecloud.cube.biz.mdata.client.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

@Data
public class BizLogRecordParam extends Paging {
    private String account;
    private String userName;
    private Boolean fail;//false 成功，true 失败；失败的信息也再action中
    private String type;//定义为模块名称或（应用名称）
    private String subType;//业务功能

}

package com.guhecloud.platform.dcm.support;

import com.guhecloud.platform.dcm.entity.RequestedConfigItem;

import java.util.List;

/**
 * 差异应用接口
 */
public interface IRciApplier {

    /**
     * 应用一组仅以Add/Updata/Remove形态命令标识的差异.
     *
     * @param rcis 差异项目列表.
     *
     * @return 返回被应用的差异项.
     */
    List<RequestedConfigItem> apply(List<RequestedConfigItem> rcis);
}

package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.querygateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.po.HiddenDangerType;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.querygateway.query.HiddenDangerTypeCollectionQuery;

import java.util.List;

/**
 * @author lzy
 * @date 2022/4/26
 */
public interface HiddenDangerTypeQueryService extends IService<HiddenDangerType> {

    /**
     * 获取隐患类别列表
     *
     * @return 隐患类别列表信息
     */
    List<HiddenDangerType> listHiddenDangerType(HiddenDangerTypeCollectionQuery query);

}

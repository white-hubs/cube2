package com.guhecloud.cube.srv.gen.park.management.server.querygateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo.TypeDataVO;
import com.guhecloud.cube.srv.gen.park.management.server.po.TypeDataPO;

import java.util.List;

public interface TypeDataQueryService extends IService<TypeDataPO> {

    List<TypeDataVO> getListByTag(String tag, String keyword);
}

package com.guhecloud.cube.srv.gen.stall.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.stall.server.entity.GenStall;
import com.guhecloud.cube.srv.gen.stall.server.param.GenStallCreateParam;
import com.guhecloud.cube.srv.gen.stall.server.param.GenStallQueryParam;
import com.guhecloud.cube.srv.gen.stall.server.param.GenStallUpdateParam;

import java.util.List;

public interface GenStallService extends IService<GenStall> {

    /**
     * 档位新增
     *
     * @param param 档位新增参数
     */
    void save(GenStallCreateParam param);

    /**
     * 导表档位新增
     *
     * @param param 档位新增参数
     */
    void excelSave(List<GenStallCreateParam> param);

    /**
     * 档位更新
     *
     * @param param 档位更新参数
     */
    void update(GenStallUpdateParam param);

    /**
     * 档位分页查询
     *
     * @param queryParam 档位查询参数
     *
     * @return 档位分页集合
     */
    Page<GenStall> page(GenStallQueryParam queryParam);

    /**
     * 档位删除
     *
     * @param id 档位ID
     */
    void genRemove(Long id);

    GenStall getByName(String name);

    /**
     * 根据档位编号获取档位信息
     *
     * @param code 档位编号
     *
     * @return 档位信息
     */
    GenStall getByCode(String code);

    List<GenStall> list(GenStallQueryParam queryParam);

    List<GenStall> listByArea(String area);

}

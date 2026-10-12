package com.guhecloud.cube.biz.mdata.server.stall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.mdata.server.stall.entity.StallCategory;
import com.guhecloud.cube.biz.mdata.server.stall.param.StallCategoryCreateBatchParam;
import com.guhecloud.cube.biz.mdata.server.stall.param.StallCategoryCreateParam;
import com.guhecloud.cube.biz.mdata.server.stall.param.StallCategoryQueryParam;
import com.guhecloud.cube.biz.mdata.server.stall.param.StallCategoryUpdateParam;

import java.util.List;

public interface StallCategoryService extends IService<StallCategory> {

    /**
     * 档位分类新增
     *
     * @param param 档位分类新增参数
     */
    void save(StallCategoryCreateParam param);

    /**
     * 档位分类更新
     *
     * @param param 档位分类更新参数
     */
    void update(StallCategoryUpdateParam param);

    /**
     * 档位分类分页查询
     *
     * @param queryParam 档位分类查询参数
     *
     * @return 档位分类分页集合
     */
    Page<StallCategory> page(StallCategoryQueryParam queryParam);

    /**
     * 档位分类列表查询
     *
     * @param queryParam 查询参数
     *
     * @return 档位类型集合
     */
    List<StallCategory> list(StallCategoryQueryParam queryParam);

    /**
     * 档位类型ID删除
     *
     * @param id 档位类型主键
     */
    void remove(Long id);

    /**
     * 根据档位类型名称获取档位类型信息
     *
     * @param name 档位类型名称
     *
     * @return 档位类型信息
     */
    StallCategory getByName(String name);

    /**
     * 档位类型主键查询
     *
     * @param ids 档位类型主键集合
     *
     * @return 档位类型信息
     */
    List<StallCategory> getByIds(List<Long> ids);

    /**
     * 大白菜挡位类型批量新增
     * 如果存在相同名称挡位跳过
     * 注意：不会新增文件信息
     *
     * @param param 挡位集合入参
     */
    void saveBatchOnlyEntity(StallCategoryCreateBatchParam param);
}

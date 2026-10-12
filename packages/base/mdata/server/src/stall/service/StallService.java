package com.guhecloud.cube.biz.mdata.server.stall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.mdata.server.excel.ExcelColumnSelectionProvider;
import com.guhecloud.cube.biz.mdata.server.scene.param.StallSceneConfigModifyParam;
import com.guhecloud.cube.biz.mdata.server.stall.dto.DbcStallDTO;
import com.guhecloud.cube.biz.mdata.server.stall.dto.StallIdTypeDTO;
import com.guhecloud.cube.biz.mdata.server.stall.dto.StallStatDTO;
import com.guhecloud.cube.biz.mdata.server.stall.entity.Stall;
import com.guhecloud.cube.biz.mdata.server.stall.param.ExcelTemplate;
import com.guhecloud.cube.biz.mdata.server.stall.param.StallCreateBathParam;
import com.guhecloud.cube.biz.mdata.server.stall.param.StallCreateParam;
import com.guhecloud.cube.biz.mdata.server.stall.param.StallQueryParam;
import com.guhecloud.cube.biz.mdata.server.stall.param.StallUpdateParam;
import com.guhecloud.cube.biz.mdata.server.stat.param.AreaStatQueryParam;
import com.guhecloud.cube.biz.mdata.server.supplier.entity.Supplier;
import com.guhecloud.platform.base.model.Paging;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface StallService extends IService<Stall> {

    /**
     * 档位新增
     *
     * @param param 档位新增参数
     */
    void save(StallCreateParam param);

    /**
     * 导表档位
     *
     * @param param 档位新增参数
     */
    List<String> excelSave(List<ExcelTemplate> param, Map<String, ExcelColumnSelectionProvider> providerMap);

    /**
     * 档位更新
     *
     * @param param 档位更新参数
     */
    void update(StallUpdateParam param);

    /**
     * 档位分页查询
     *
     * @param queryParam 档位查询参数
     *
     * @return 档位分页集合
     */
    Page<Stall> page(StallQueryParam queryParam);

    /**
     * 档位删除
     *
     * @param id 档位ID
     */
    void remove(Long id);

    void saveSupplierStall(Collection<Long> stallIds, Long supplierId);

    void removeSupplierStall(Long supplierId);

    /**
     * 根据档位类型主键查询档位
     *
     * @param id 档位类型
     *
     * @return 档位信息
     */
    List<Stall> getByCategoryId(Long id);

    List<Stall> getByIds(Collection<Long> ids);

    /**
     * 根据档位名称获取档位信息
     *
     * @param name 档位名称
     *
     * @return 档位信息
     */
    Stall getByName(String name);

    /**
     * 根据档位编号获取档位信息
     *
     * @param code 档位编号
     *
     * @return 档位信息
     */
    Stall getByCode(String code);

    /**
     * 根据模型编号获取档位信息
     *
     * @param code
     *
     * @return
     */
    Stall getByVisualizationCode(String code);

    /**
     * 获取档位信息集合
     *
     * @param ids 档位集合主键
     *
     * @return 档位信息
     */
    List<Stall> listStalls(Collection<Long> ids);

    /**
     * 获取该区域下的所有档位
     *
     * @param areaId 区域主键
     *
     * @return 档位信息
     */
    Page<Stall> listAreaStalls(Long areaId, Paging paging);

    List<Stall> list(StallQueryParam queryParam);

    /**
     * 获取区域下的所有档位
     *
     * @param areas 区域序列号
     *
     * @return 档位信息
     */
    List<Stall> listByAreas(Collection<String> areas);

    /**
     * 获取区域下的所有档位分页信息
     *
     * @param queryParam 查询参数
     *
     * @return 档位信息
     */
    Page<Stall> pageByAreas(AreaStatQueryParam queryParam);

    /**
     * 统计一级区域下的档位数量
     *
     * @return
     */
    List<StallStatDTO> getStallStatByArea();

    /**
     * 修改bim绑定状态
     *
     * @param id      档位主键
     * @param bimBind 是否绑定
     */
    void updateBindBim(Long id, Boolean bimBind);

    /**
     * 获取我的档位信息
     *
     * @return 档位信息
     */
    List<Stall> getMineStalls(Supplier supplier);

    /**
     * 批量更新挡位信息(没有就新增，有就根据id更新)
     *
     * @param param 挡位
     */
    void saveOrUpdateBatch(StallCreateBathParam param);

    /**
     * 获取挡位完整的po集合
     *
     * @return 挡位集合
     */
    List<DbcStallDTO> getAllStallEntity();

    /**
     * 根据绑定
     *
     * @param param
     *
     * @return
     */
    Set<Long> getStallsBySceneConfig(StallSceneConfigModifyParam param);

    /**
     * 档位上传文件状态同步
     */
    void stallUploadFileStateSync();

    Long getStallMasterCount();

    /**
     * 获取档位ID列表
     *
     * @param queryParam 查询参数
     *
     * @return 档位ID列表
     */
    List<StallIdTypeDTO> getStallIdTypes(StallQueryParam queryParam);

    /**
     * 根据区域id列表批量获取档位数统计
     *
     * @param areaIds 区域id列表
     * @return 区域id与档位数的映射，key为区域id，value为档位数
     */
    Map<Long, Long> countByAreaIds(List<Long> areaIds);

}

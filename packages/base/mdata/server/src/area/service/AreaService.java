package com.guhecloud.cube.biz.mdata.server.area.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.mdata.client.dto.AreaIdTypeDTO;
import com.guhecloud.cube.biz.mdata.server.area.dto.AreaImportDTO;
import com.guhecloud.cube.biz.mdata.server.area.dto.AreaSortEditDTO;
import com.guhecloud.cube.biz.mdata.server.area.dto.SpaceAreaImportDTO;
import com.guhecloud.cube.biz.mdata.server.area.entity.Area;
import com.guhecloud.cube.biz.mdata.server.area.param.AreaCreateParam;
import com.guhecloud.cube.biz.mdata.server.area.param.AreaQueryParam;
import com.guhecloud.cube.biz.mdata.server.area.param.AreaUpdateParam;
import com.guhecloud.cube.biz.mdata.server.area.vo.AreaListVO;
import com.guhecloud.cube.biz.mdata.server.area.vo.AreaStatisticsVo;
import com.guhecloud.cube.biz.mdata.server.area.vo.AreaVO;
import com.guhecloud.cube.biz.mdata.server.area.vo.EnergyAreaVo;

import java.util.List;
import java.util.Map;

public interface AreaService extends IService<Area> {

    /**
     * 新增区域
     *
     * @param param 区域新增参数
     */
    void save(AreaCreateParam param);

    /**
     * 新增区域+扩展配置
     *
     * @param param
     */
    void saveAreaExtent(AreaCreateParam param);

    /**
     * 更新区域
     *
     * @param param 区域更新参数
     */
    void update(AreaUpdateParam param);

    /**
     * 更新区域+扩展配置
     *
     * @param param
     */
    void updateAreaExtent(AreaUpdateParam param);

    /**
     * 区域列表
     *
     * @param queryParam 区域查询参数
     *
     * @return 区域列表集合
     */
    List<Area> getList(AreaQueryParam queryParam);

    /**
     * 区域列表
     *
     * @param level 区域层级
     *
     * @return
     */
    List<Area> getListByLevel(Integer level);

    /**
     * 区域分页查询
     *
     * @param queryParam 查询参数
     *
     * @return 区域信息
     */
    Page<Area> page(AreaQueryParam queryParam);

    List<Area> getSequence(List<String> sequences);

    List<Area> getListBySequences(String sequence);

    List<EnergyAreaVo> innerList();

    List<Area> getByIds(List<Long> ids);

    /**
     * 区域删除
     *
     * @param id 区域ID
     */
    void remove(Long id);

    /**
     * 根据区域编号获取区域
     *
     * @param code 区域编号
     *
     * @return 区域信息
     */
    Area getByCode(String code);

    /**
     * 根据区域名称获取区域
     *
     * @param name 区域名称
     *
     * @return 区域信息
     */
    Area getByName(String name);

    /**
     * 根据区域名称获取区域
     *
     * @param name
     * @param parentId 父级区域主键
     *
     * @return
     */
    Area getByName(String name, Long parentId);

    /**
     * 根据父类主键获取区域
     *
     * @param parentId 区域父类主键
     *
     * @return 区域信息
     */
    List<Area> getByParentId(Long parentId);

    /**
     * 根据父类主键集合获取区域
     *
     * @param parentIds
     *
     * @return
     */
    List<Area> getByParentIds(List<Long> parentIds);

    String getAreaNameById(Long id, Integer tenant);

    Area getAreaNameBySequence(String sequence);

    String getAreaNamesById(Long id);

    List<AreaStatisticsVo> getAreaStatistics(Long id);

    List<Area> getAreaByName(String areaName);

    String getAndAppendAreaName(List<Long> areaIds, Map<Long, List<Area>> areaMap);

    List<AreaVO> getAllAreaFullName();

    /**
     * 区域数据导入
     *
     * @param datas 导入文件
     */
    List<String> areaDataImport(List<AreaImportDTO> datas);

    /**
     * 区域数据导出
     *
     * @param queryParam 查询参数
     */
    List<AreaImportDTO> areaDataExport(AreaQueryParam queryParam);

    /**
     * 区域拖动排序批量保存
     *
     * @param areaSortEditDTOList
     */
    void areaSortEdit(List<AreaSortEditDTO> areaSortEditDTOList);

    // 获取导出的空间数据
    List<SpaceAreaImportDTO> getSpaceAreaDataExport(AreaQueryParam queryParam);

    /**
     * 获取区域ID列表
     *
     * @param queryParam 查询参数
     *
     * @return 区域ID列表
     */
    List<AreaIdTypeDTO> getAreaIdTypes(AreaQueryParam queryParam);

    /**
     * 获取区域树(list项为当前传入区域)
     *
     * @param areaIds 区域ID
     *
     * @return 区域树
     */
    List<AreaListVO> getAreaTreesWithChildren(List<Long> areaIds);
}

package com.guhecloud.cube.biz.mdata.server.area.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import com.guhecloud.cube.biz.mdata.server.area.converters.AreaConverterForAreaListVO;
import com.guhecloud.cube.biz.mdata.client.dto.AreaIdTypeDTO;
import com.guhecloud.cube.biz.mdata.server.area.dto.AreaListDTO;
import com.guhecloud.cube.biz.mdata.server.area.dto.AreaSortEditDTO;
import com.guhecloud.cube.biz.mdata.server.area.entity.Area;
import com.guhecloud.cube.biz.mdata.server.area.entity.AreaExtend;
import com.guhecloud.cube.biz.mdata.server.area.entity.AreaFeature;
import com.guhecloud.cube.biz.mdata.server.area.entity.AreaStatus;
import com.guhecloud.cube.biz.mdata.server.area.entity.AreaTag;
import com.guhecloud.cube.biz.mdata.server.area.param.AreaCreateParam;
import com.guhecloud.cube.biz.mdata.server.area.param.AreaLinkTagParam;
import com.guhecloud.cube.biz.mdata.server.area.param.AreaQueryParam;
import com.guhecloud.cube.biz.mdata.server.area.param.AreaUpdateParam;
import com.guhecloud.cube.biz.mdata.server.area.rocketmq.message.AreaChangeMessage;
import com.guhecloud.cube.biz.mdata.server.area.service.AreaBatchOptService;
import com.guhecloud.cube.biz.mdata.server.area.service.AreaExtendService;
import com.guhecloud.cube.biz.mdata.server.area.service.AreaFeatureService;
import com.guhecloud.cube.biz.mdata.server.area.service.AreaService;
import com.guhecloud.cube.biz.mdata.server.area.service.AreaStatusService;
import com.guhecloud.cube.biz.mdata.server.area.service.AreaTagService;
import com.guhecloud.cube.biz.mdata.server.area.vo.AreaExtendVO;
import com.guhecloud.cube.biz.mdata.server.area.vo.AreaListVO;
import com.guhecloud.cube.biz.mdata.server.area.vo.AreaStallStatVO;
import com.guhecloud.cube.biz.mdata.server.area.vo.AreaStallVO;
import com.guhecloud.cube.biz.mdata.server.area.vo.AreaStatisticsVo;
import com.guhecloud.cube.biz.mdata.server.area.vo.AreaVO;
import com.guhecloud.cube.biz.mdata.server.area.vo.EnergyAreaVo;
import com.guhecloud.cube.biz.mdata.server.stall.entity.Stall;
import com.guhecloud.cube.biz.mdata.server.stall.service.StallService;
import com.guhecloud.cube.biz.mdata.server.stall.vo.BatchOptResult;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.param.UserQueryParam;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.base.util.TreeUtils;
import com.guhecloud.platform.database.util.EntityUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 区域管理接口
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AreaController {
    private final static String EXCEL_TEMPLATE_NAME = "区域导入模板";
    private final AreaService areaService;
    private final AreaExtendService areaExtendService;
    private final StallService stallService;
    private final UserClient userClient;
    private final FileClient fileClient;
    private final AreaBatchOptService areaBatchOptService;
    private final AreaStatusService areaStatusService;
    private final AreaTagService areaTagService;
    private final AreaFeatureService areaFeatureService;

    /**
     * 获取当前id的所有子集
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/area/child/ids")
    public Set<Long> getChildrenIds(@RequestParam Long id) {
        Area area = this.areaService.getAreaNameBySequence(id + "");
        List<Area> list = new ArrayList<>();
        if (area != null && StringUtils.isNotBlank(area.getSequence())) {
            list = this.areaService.getListBySequences(area.getSequence());
        }
        return list.stream().map(Area::getId).collect(Collectors.toSet());
    }

    /**
     * 新增区域
     *
     * @param param 区域新增参数
     */
    @PostMapping("/area")
    public void save(@Valid @RequestBody AreaCreateParam param) {
        this.areaService.save(param);
    }

    /**
     * 新增区域
     *
     * @param param 区域新增参数
     */
    @PostMapping("/area/extend")
    public void saveAreaExtend(@Valid @RequestBody AreaCreateParam param) {
        this.areaService.saveAreaExtent(param);
    }

    /**
     * 更新区域
     *
     * @param param 区域更新参数
     */
    @PutMapping("/area")
    public void update(@Valid @RequestBody AreaUpdateParam param) {
        this.areaService.update(param);
    }

    /**
     * 更新区域
     *
     * @param param 区域更新参数
     */
    @PutMapping("/area/extend")
    public void updateAreaExtend(@Valid @RequestBody AreaUpdateParam param) {
        this.areaService.updateAreaExtent(param);
        RocketMQContextHolder.send(AreaChangeMessage.TOPIC, new AreaChangeMessage(param.getId()));
    }

    /**
     * 区域列表
     *
     * @param queryParam 区域查询参数
     *
     * @return 区域列表集合
     */
    @GetMapping("/area/list")
    public List<AreaListVO> list(@Valid AreaQueryParam queryParam) {
        List<Area> areas = areaService.getList(queryParam);
        if (CollectionUtils.isEmpty(areas)) {
            return Collections.emptyList();
        }
        // 获取上级sequence
        List<String> sequences = EntityUtils.getAllSequences(areas);
        List<Area> list = this.areaService.getSequence(sequences);
        //
        List<AreaListVO> copying = ObjectUtils.copying(list, AreaListVO.class);
        List<Long> managerUserIds = new ArrayList<>();
        copying.stream().filter(areaListVO -> !StringUtils.isEmpty(areaListVO.getAreaManagerId()))
                .forEach(areaListVO -> {
                    String[] split = areaListVO.getAreaManagerId().split("-");
                    managerUserIds.addAll(Arrays.asList(split).stream().map(userId -> Long.valueOf(userId)).collect(Collectors.toList()));
                });
        if (!CollectionUtils.isEmpty(managerUserIds)) {
            UserQueryParam userQueryParam = new UserQueryParam();
            userQueryParam.setIds(managerUserIds);
            userQueryParam.setPageNumber(1);
            userQueryParam.setPageSize(managerUserIds.size());
            Pagination<UserDTO> userDTOPagination = this.userClient.postListUsers(userQueryParam);
            if (!CollectionUtils.isEmpty(userDTOPagination.getRecords())) {
                copying.forEach(s -> {
                    if (!StringUtils.isEmpty(s.getAreaManagerId())) {
                        List<String> strings = Arrays.asList(s.getAreaManagerId().split("-"));
                        List<Long> ids = strings.stream().map(Long::valueOf).collect(Collectors.toList());
                        List<UserDTO> userDTOS = userDTOPagination.getRecords().stream().filter(userDTO -> ids.contains(userDTO.getId()))
                                .collect(Collectors.toList());
                        List<String> names = userDTOS.stream().map(UserDTO::getName).collect(Collectors.toList());
                        s.setAreaManagerName(names);
                        s.setAreaManagerIds(ids);
                    }
                });
            }
        }
        // 获取下级sequence
        List<AreaListVO> areaList = TreeUtils.assembleTrees(copying);
        return areaList;
    }

    @PostMapping("/area/tree")
    public List<AreaListVO> areas(@RequestBody @Valid AreaQueryParam queryParam) {
        return list(queryParam);
    }

    /**
     * 获取区域ID列表
     *
     * @param queryParam 查询参数
     *
     * @return 区域ID列表
     */
    @PostMapping("/area/idTypes")
    public List<AreaIdTypeDTO> getAreaIdTypes(@RequestBody @Valid AreaQueryParam queryParam) {
        return areaService.getAreaIdTypes(queryParam);
    }

    /**
     * @Description 内部区域树
     */
    @GetMapping("/area/inner/list")
    public List<EnergyAreaVo> innerList() {
        List<EnergyAreaVo> list = this.areaService.innerList();
        return list;
    }

    /**
     * @Description 安全管理区域树
     * @Author lrj
     */
    @GetMapping("/area/security/list")
    public List<AreaListVO> securityAreaList(AreaQueryParam queryParam) {
        List<Area> areas = areaService.getList(queryParam);
        if (CollectionUtils.isEmpty(areas)) {
            return Collections.emptyList();
        }
        // 获取上级sequence
        List<String> sequences = EntityUtils.getAllSequences(areas);
        List<Area> list = this.areaService.getSequence(sequences);
        List<AreaListVO> copying = ObjectUtils.copying(list, AreaListVO.class);
        return copying;
    }

    /**
     * 区域列表(内部调用)
     *
     * @param queryParam 区域查询参数
     *
     * @return 区域列表集合
     */
    @GetMapping("/area-inner/list")
    public List<AreaListDTO> listInnerArea(@Valid AreaQueryParam queryParam) {
        List<Area> areas = areaService.getList(queryParam);
        return ObjectUtils.copying(areas, AreaListDTO.class);
    }

    @PostMapping("/area-inner/list/post")
    public List<AreaDTO> listInnerAreaPost(@RequestBody AreaQueryParam queryParam) {
        List<Area> areas = areaService.getList(queryParam);
        return ObjectUtils.copying(areas, AreaDTO.class);
    }

    /**
     * 区域导航列表(三维可视化使用)
     *
     * @return
     */
    @GetMapping("/area/bim/list")
    public List<AreaListVO> listBimAreas() {
        List<Area> areas = areaService.list();
        if (CollectionUtils.isEmpty(areas)) {
            return Collections.emptyList();
        }
        // 获取下级sequence
        List<AreaListVO> areaListVOS = AreaConverterForAreaListVO.toAreaListVOS(areas);
        return TreeUtils.assembleTrees(areaListVOS);
    }

    /**
     * 父级区域IDS列表
     *
     * @param id 区域查询参数
     *
     * @return 区域列表集合
     */
    @GetMapping("/area/{id}/parents")
    public List<Long> getParentIds(@PathVariable Long id) {
        List<Long> parentIds = new ArrayList<>();
        Area area = this.areaService.getById(id);
        if (area == null) {
            return parentIds;
        }
        String sequence = area.getSequence();
        if (!StringUtils.isEmpty(sequence)) {
            String[] tempParentIds = sequence.split("-");
            parentIds = Arrays.asList(tempParentIds).stream()
                    .map(pid -> Long.valueOf(pid)).collect(Collectors.toList());
        }
        return parentIds;
    }

    /**
     * 通过ID 查询区域
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/area/{id}")
    public AreaListVO getAreaById(@PathVariable Long id) {
        Area area = this.areaService.getById(id);
        if (Objects.isNull(area)) {
            return null;
        }
        AreaListVO areaListVO = ObjectUtils.copying(area, AreaListVO.class);
        AreaExtend areaExtend = this.areaExtendService.getById(id);
        if (Objects.isNull(areaExtend)) {
            return areaListVO;
        }
        AreaExtendVO areaExtendVO = new AreaExtendVO();
        areaExtendVO.setId(areaExtend.getId());
        areaExtendVO.setIntroduce(areaExtend.getIntroduce());
        if (com.guhecloud.platform.base.util.StringUtils.isNotBlank(areaExtend.getPictureFileIds())) {
            List<Long> fileIds = Arrays.asList(areaExtend.getPictureFileIds().split(",")).stream().map(Long::valueOf)
                    .collect(Collectors.toList());
            areaExtendVO.setPictureFiles(this.fileClient.getByIds(fileIds));
        }
        if (com.guhecloud.platform.base.util.StringUtils.isNotBlank(areaExtend.getVideoFileIds())) {
            List<Long> fileIds = Arrays.asList(areaExtend.getVideoFileIds().split(",")).stream().map(Long::valueOf)
                    .collect(Collectors.toList());
            areaExtendVO.setVideoFiles(this.fileClient.getByIds(fileIds));
        }
        areaListVO.setAreaExtend(areaExtendVO);
        return areaListVO;
    }

    /**
     * 通过ID 查询区域
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/hiddendanger-area/{id}")
    public AreaDTO getHiddenDangerAreaById(@PathVariable Long id) {
        Area area = this.areaService.getById(id);
        return ObjectUtils.copying(area, AreaDTO.class);
    }

    /**
     * 区域下档位的租赁统计
     *
     * @param queryParam 查询参数
     *
     * @return 统计信息
     */
    @GetMapping("/area-stat/stall/page")
    public Pagination<AreaStallStatVO> pageAreaStalls(@Valid AreaQueryParam queryParam) {
        Page<Area> page = this.areaService.page(queryParam);
        List<AreaStallStatVO> statVOS = ObjectUtils.copying(page.getRecords(), AreaStallStatVO.class);
        List<Stall> stalls = this.stallService.list();
        statVOS.forEach(areaStallStatVO -> this.calculateLeaseData(areaStallStatVO, stalls));
        return PagingUtils.convert(page, statVOS);
    }

    /**
     * @param id
     *
     * @return
     */
    @GetMapping("/area-stat/{id}")
    public AreaStallStatVO getAreaStall(@NotNull(message = "区域主键不能为空") @Min(value = 1, message = "区域主键不能小于1")
                                        @PathVariable("id") Long id, Paging paging
                                       ) {
        if (Objects.isNull(paging.getPageNumber())) {
            paging = new Paging();
        }
        Area area = this.areaService.getById(id);
        Assert.isNotFound(area, "不存在区域信息");
        AreaStallStatVO stat = ObjectUtils.copying(area, AreaStallStatVO.class);
        // 获取该区域下的所有档位
        Page<Stall> stalls = this.stallService.listAreaStalls(area.getId(), paging);
        // 统计信息
        this.calculateLeaseData(stat, this.stallService.list());
        stat.setNotRentable(stat.getQuantity() - stat.getRentable());
        stat.setStalls(stalls);
        return stat;
    }

    /**
     * 区域列表树（计算每个区域下的档位数量）
     *
     * @param queryParam 查询参数
     *
     * @return 区域列表集合
     */
    @GetMapping("/area/stall/list")
    public List<AreaStallVO> listAreaStalls(@Valid AreaQueryParam queryParam) {
        List<Area> areaList = this.areaService.getList(queryParam);
        if (CollectionUtils.isEmpty(areaList)) {
            return Collections.emptyList();
        }
        List<AreaStallVO> areas = ObjectUtils.copying(areaList, AreaStallVO.class);
        List<Stall> stalls = this.stallService.list();
        List<AreaStallVO> areaStallVOS = areas.stream().peek(area -> area.setQuantity((int) stalls.stream().filter(stall -> {
            if (Objects.nonNull(stall.getArea())) {
                List<Long> areaIds = Arrays.stream(stall.getArea().split(",")).map(Long::parseLong).collect(Collectors.toList());
                return areaIds.contains(area.getId());
            }
            return false;
        }).count())).collect(Collectors.toList());
        return TreeUtils.assembleTrees(areaStallVOS);
    }

    /**
     * 区域删除
     *
     * @param id 区域ID
     */
    @DeleteMapping("/area/{id}")
    public void remove(@NotNull(message = "主键不能为空") @Min(value = 1, message = "主键不能小于1")
                       @PathVariable("id") Long id
                      ) {
        this.areaService.remove(id);
    }

    /**
     * 区域列表
     *
     * @return
     */
    @GetMapping("/areas-inner")
    public List<AreaVO> listAreas() {
        List<Area> list = this.areaService.list();
        return ObjectUtils.copying(list, AreaVO.class);
    }

    /**
     * 通过区域IDS 查询区域列表
     *
     * @param ids
     *
     * @return
     */
    @GetMapping("/area/ids")
    List<AreaVO> getByIds(@RequestParam("ids") Collection<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        List<Area> list = this.areaService.list(Wrappers.<Area>lambdaQuery().in(Area::getId, ids));
        return ObjectUtils.copying(list, AreaVO.class);
    }

    private void calculateLeaseData(AreaStallStatVO area, List<Stall> stalls) {
        area.setQuantity((int) stalls.stream().filter(stall -> Arrays.stream(stall.getArea().split(","))
                .collect(Collectors.toList()).contains(String.valueOf(area.getId()))).count());
        area.setRentable((int) stalls.stream().filter(stall -> Boolean.TRUE.equals(stall.getEnabled())
                                                               && Arrays.stream(stall.getArea().split(","))
                                                                       .collect(Collectors.toList()).contains(String.valueOf(area.getId())))
                .count());
        area.setLeased((int) stalls.stream().filter(stall -> Boolean.TRUE.equals(stall.getLeased())
                                                             && Boolean.TRUE.equals(stall.getEnabled())
                                                             && Arrays.stream(stall.getArea().split(","))
                                                                     .collect(Collectors.toList()).contains(String.valueOf(area.getId())))
                .count());
        area.setUnleased((int) stalls.stream().filter(stall -> Boolean.FALSE.equals(stall.getLeased())
                                                               && Boolean.TRUE.equals(stall.getEnabled())
                                                               && Arrays.stream(stall.getArea().split(","))
                                                                       .collect(Collectors.toList()).contains(String.valueOf(area.getId())))
                .count());
        area.setVacantRate(area.getRentable() == 0 ? 0 : Double.valueOf(area.getUnleased()) / Double.valueOf(area.getRentable()));
    }

    @GetMapping("/area/{level}/level")
    List<AreaVO> getAreaByLevelOne(@PathVariable("level") Integer level) {
        List<Area> list = this.areaService.list(Wrappers.<Area>lambdaQuery().eq(Area::getLevel, level));
        return ObjectUtils.copying(list, AreaVO.class);
    }

    /**
     * @Description 区域过滤
     * @Author lrj
     */
    @GetMapping("/area/level/filter")
    List<AreaVO> getFilterAreaByLevelOne(@RequestParam("level") Integer level) {
        List<Area> list = this.areaService.list(Wrappers.<Area>lambdaQuery().eq(Area::getLevel, level));
        return ObjectUtils.copying(list, AreaVO.class);
    }

    /**
     * 获取区域名称
     *
     * @return
     */
    @GetMapping("getAreaNameById")
    public String getAreaNameById(@RequestParam("id") Long id, @RequestParam("tenant") Integer tenant) {
        return areaService.getAreaNameById(id, tenant);
    }

    /**
     * @Description 区域拼接
     * @Author lrj
     */
    @GetMapping("/getAreaNamesById")
    public String getAreaNamesById(@RequestParam("id") Long id) {
        return areaService.getAreaNamesById(id);
    }

    /**
     * @Description 统计数据区域
     * @Author lrj
     */
    @GetMapping("/area-statistics")
    public List<AreaStatisticsVo> getAreaStatistics(@RequestParam("id") Long id) {
        return areaService.getAreaStatistics(id);
    }

    /**
     * 根据名称查询区域
     *
     * @param areaName
     *
     * @return
     */
    @GetMapping("/area/by-name")
    public List<AreaDTO> getAreaByName(@RequestParam("areaName") String areaName) {
        List<Area> areas = this.areaService.getAreaByName(areaName);
        if (CollectionUtils.isEmpty(areas)) {
            return Collections.emptyList();
        }
        return ObjectUtils.copying(areas, AreaDTO.class);
    }

    /**
     * 根据区域树序列号查询区域。
     *
     * @param sequence 区域树序列号
     *
     * @return 区域信息；不存在时返回空。
     */
    @GetMapping("/area/by-sequence")
    public AreaDTO getAreaBySequence(@RequestParam("sequence") String sequence) {
        if (StringUtils.isBlank(sequence)) {
            return null;
        }
        List<Area> areas = this.areaService.getSequence(Collections.singletonList(sequence.trim()));
        if (CollectionUtils.isEmpty(areas)) {
            return null;
        }
        Assert.isParameterError(areas.size() > 1, "区域序列号不唯一");
        return ObjectUtils.copying(areas.get(0), AreaDTO.class);
    }

    /**
     * @description: 查询所有区域，名称使用全称如：广东省-广州市-天河区
     * @author: lijie
     * @date: 2024/8/16 10:04
     * @param:
     * @return: java.util.List<com.guhecloud.cube.biz.mdata.server.area.vo.AreaVO>
     **/
    @GetMapping("/getAllAreaFullName")
    public List<AreaVO> getAllAreaFullName() {
        return this.areaService.getAllAreaFullName();
    }

    /**
     * 区域导入模板下载
     *
     * @param response HttpServletResponse
     */
    @GetMapping("/area/import/template")
    public void getImportAreaTemplate(HttpServletResponse response) {
        areaBatchOptService.writeTemplate(new ArrayList<>(), EXCEL_TEMPLATE_NAME, response);
    }

    /**
     * 根据模板导入区域
     *
     * @param file 导入文件
     */
    @PostMapping("/area/import")
    public BatchOptResult areDataImport(@RequestParam("file") MultipartFile file) {

        return areaBatchOptService.areaBatchImportFromTemplate(file);
    }

    /**
     * 区域导出
     */
    @GetMapping("/area/export")
    public void areaDataExport(@Valid AreaQueryParam queryParam, HttpServletResponse response) {
        areaBatchOptService.exportAreaData(queryParam, response);
    }

    /**
     * 区域批量排序编辑
     */
    @PostMapping("/area/sort/edit")
    public void areaSortEdit(@RequestBody List<AreaSortEditDTO> areaSortEditDTOList) {
        areaService.areaSortEdit(areaSortEditDTOList);
    }

    /**
     * 空间分页列表
     *
     * @param queryParam 条件
     *
     * @return 分页
     */
    @GetMapping("/area-space/list")
    public List<AreaListVO> spaceList(@Valid AreaQueryParam queryParam) {
        List<Area> areas = areaService.getList(queryParam);
        if (CollectionUtils.isEmpty(areas)) {
            return Collections.emptyList();
        }
        // 获取上级sequence
        List<String> sequences = EntityUtils.getAllSequences(areas);
        List<Area> list = this.areaService.getSequence(sequences);
        //
        List<AreaListVO> copying = ObjectUtils.copying(list, AreaListVO.class);
        // 扩展空间信息
        this.extSpecs(copying, false);
        // 获取下级sequence
        return TreeUtils.assembleTrees(copying);
    }

    /**
     * 区域关联标签
     *
     * @param param 入参
     *
     * @return 是否成功
     */
    @PostMapping("/area/link-tag")
    public Boolean areaLinkTag(@RequestBody AreaLinkTagParam param) {
        Assert.isParameterError(param == null || param.getAreaId() == null || StringUtils.isEmpty(param.getTagIds()), "入参不能为空！");
        Assert.isNotFound(areaService.getById(param.getAreaId()));
        // 校验标签id是否为数字
        Assert.isParameterError(!this.checkOnlyNumber(param.getTagIds(), ","), "标签id异常");
        areaService.update(Wrappers.<Area>lambdaUpdate()
                                   .set(Area::getTagIds, param.getTagIds())
                                   .eq(Area::getId, param.getAreaId())
                          );
        return true;
    }

    /**
     * 下载空间模板
     *
     * @param response HttpServletResponse
     */
    @GetMapping("/area-space/import/template")
    public void getImportSpaceAreaTemplate(HttpServletResponse response) {
        areaBatchOptService.spaceWriteTemplate(new ArrayList<>(), "模板", response);
    }

    /**
     * 空间模板导入
     *
     * @param file 导入文件
     */
    @PostMapping("/area-space/import")
    public BatchOptResult spaceAreaDataImport(@RequestParam("file") MultipartFile file) {
        return areaBatchOptService.areaSpaceBatchImportFromTemplate(file);
    }

    /**
     * 空间区域导出
     */
    @GetMapping("/area/space-export")
    public void areaSpaceDataExport(@Valid AreaQueryParam queryParam, HttpServletResponse response) {
        areaBatchOptService.exportSpaceAreaData(queryParam, response);
    }

    /**
     * 校验是否纯数字
     *
     * @param str    字符串
     * @param symbol 符号(如果不为空就通过符号分割)
     *
     * @return ture表示纯数字
     */
    private Boolean checkOnlyNumber(String str, String symbol) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        if (!StringUtils.isEmpty(symbol)) {
            String[] strings = str.split(symbol);
            for (String string : strings) {
                if (!checkOnlyNumber(string)) {
                    return false;
                }
            }
            return true;
        }
        return checkOnlyNumber(str);
    }

    private Boolean checkOnlyNumber(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param areaList
     */
    private void extSpecs(List<AreaListVO> areaList, Boolean ext) {
        if (CollectionUtils.isEmpty(areaList)) {
            return;
        }
        Map<Long, String> areaPictureIdMap = new HashMap<>();
        Map<Long, String> areaVideoIdMap = new HashMap<>();
        List<Long> areaIdList = new ArrayList<>();
        List<Long> statusList = new ArrayList<>();
        List<Long> tagList = new ArrayList<>();
        List<Long> featureList = new ArrayList<>();
        areaList.forEach(a -> {
            areaIdList.add(a.getId());
            if (a.getStatusId() != null) {
                statusList.add(a.getStatusId());
            }
            if (!StringUtils.isEmpty(a.getTagIds())) {
                Arrays.asList(a.getTagIds().split(",")).forEach(tagId -> tagList.add(Long.parseLong(tagId)));
            }
            if (!StringUtils.isEmpty(a.getFeatureIds())) {
                Arrays.asList(a.getFeatureIds().split(",")).forEach(featureId -> featureList.add(Long.parseLong(featureId)));
            }
        });
        Map<Long, FileVO> fileVOMap = new HashMap<>();
        Map<Long, String> areaIntroduceMap = new HashMap<>();
        Map<Long, String> statusMap = new HashMap<>();
        Map<Long, String> tagMap = new HashMap<>();
        Map<Long, String> featureMap = new HashMap<>();

        // 处理扩展
        if (Boolean.TRUE.equals(ext)) {
            List<AreaExtend> areaExtendList = areaExtendService.listByIds(areaIdList);
            if (!CollectionUtils.isEmpty(areaExtendList)) {
                List<Long> fileIdList = new ArrayList<>();
                areaExtendList.forEach(a -> {
                    areaIntroduceMap.put(a.getId(), a.getIntroduce());
                    String pictureFileIds = a.getPictureFileIds();
                    if (!StringUtils.isEmpty(pictureFileIds)) {
                        areaPictureIdMap.put(a.getId(), a.getPictureFileIds());
                    }
                    String videoFileIds = a.getVideoFileIds();
                    if (!StringUtils.isEmpty(videoFileIds)) {
                        areaVideoIdMap.put(a.getId(), a.getVideoFileIds());
                    }
                    areaIdList.add(a.getId());
                });
                List<FileVO> fileVOList = fileClient.getByIds(fileIdList);
                if (!CollectionUtils.isEmpty(fileVOList)) {
                    fileVOList.forEach(fileVO -> fileVOMap.put(fileVO.getId(), fileVO));
                }
            }
        }

        if (!CollectionUtils.isEmpty(statusList)) {
            List<AreaStatus> areaStatusList = areaStatusService.listByIds(statusList);
            if (!CollectionUtils.isEmpty(areaStatusList)) {
                areaStatusList.forEach(a -> statusMap.put(a.getId(), a.getName()));
            }
        }
        if (!CollectionUtils.isEmpty(tagList)) {
            List<AreaTag> areaTagList = areaTagService.listByIds(tagList);
            if (!CollectionUtils.isEmpty(areaTagList)) {
                areaTagList.forEach(a -> tagMap.put(a.getId(), a.getName()));
            }
        }
        if (!CollectionUtils.isEmpty(featureList)) {
            List<AreaFeature> areaFeatureList = areaFeatureService.listByIds(featureList);
            if (!CollectionUtils.isEmpty(areaFeatureList)) {
                areaFeatureList.forEach(a -> featureMap.put(a.getId(), a.getName()));
            }
        }

        areaList.forEach(area -> {
            /* 扩充扩展信息 */
            if (Boolean.TRUE.equals(ext)) {
                AreaExtendVO extendVO = new AreaExtendVO();
                String pictureIds = areaPictureIdMap.get(area.getId());
                if (!StringUtils.isEmpty(pictureIds)) {
                    List<FileVO> pictureFileList = new ArrayList<>();
                    for (String string : pictureIds.split(",")) {
                        pictureFileList.add(fileVOMap.get(Long.parseLong(string)));
                    }
                    extendVO.setPictureFiles(pictureFileList);
                }
                String videoIds = areaVideoIdMap.get(area.getId());
                if (!StringUtils.isEmpty(videoIds)) {
                    List<FileVO> videoFileList = new ArrayList<>();
                    for (String string : videoIds.split(",")) {
                        videoFileList.add(fileVOMap.get(Long.parseLong(string)));
                    }
                    extendVO.setVideoFiles(videoFileList);
                }
                String introduce = areaIntroduceMap.get(area.getId());
                if (!StringUtils.isEmpty(introduce)) {
                    extendVO.setIntroduce(introduce);
                }
                area.setAreaExtend(extendVO);
            }

            /* 扩充状态 */
            if (area.getStatusId() != null) {
                area.setStatusName(statusMap.get(area.getStatusId()));
            }
            /* 扩充标签 */
            if (!StringUtils.isEmpty(area.getTagIds())) {
                String[] tagIds = area.getTagIds().split(",");
                StringBuilder sb = new StringBuilder();
                for (String tagId : tagIds) {
                    sb.append(tagMap.get(Long.parseLong(tagId))).append(",");
                }
                if (sb.length() > 0) {
                    sb.deleteCharAt(sb.length() - 1);
                }
                area.setTagName(sb.toString());
            }
            /* 扩充特征 */
            if (!StringUtils.isEmpty(area.getFeatureIds())) {
                String[] featureIds = area.getFeatureIds().split(",");
                StringBuilder sb = new StringBuilder();
                for (String featureId : featureIds) {
                    sb.append(featureMap.get(Long.parseLong(featureId))).append(",");
                }
                if (sb.length() > 0) {
                    sb.deleteCharAt(sb.length() - 1);
                }
                area.setFeatureName(sb.toString());
            }
            /* 处理面积 */
            if (area.getBuiltUpArea() != null) {
                area.setBuiltUpArea(area.getBuiltUpArea().setScale(2, RoundingMode.HALF_UP));
            }

        });
    }
}

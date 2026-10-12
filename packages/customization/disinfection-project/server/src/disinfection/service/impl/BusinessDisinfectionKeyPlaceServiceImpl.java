package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.dto.excel.KeyPlaceExcelDTO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity.DisinfectionDataType;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity.DisinfectionKeyPlace;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity.DisinfectionProject;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DinsinfectionFileInduceSceneCodeEnum;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DisinfectionDataTypeEnum;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.helper.AddressResolvingHelper;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.helper.DisinfectionFileHelper;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.page.KeyPlacePageParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.query.KeyPlacesQueryParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.upsert.KeyPlaceUpsertParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.BusinessDisinfectionKeyPlaceService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionDataTypeService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionKeyPlaceService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionProjectService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.list.KeyPlaceInfoVO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.page.KeyPlacePageVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.base.utils.poi.CascadeSelectDownloader;
import com.guhecloud.platform.cloud.util.ExcelUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BusinessDisinfectionKeyPlaceServiceImpl implements BusinessDisinfectionKeyPlaceService {

    private final DisinfectionKeyPlaceService keyPlaceService;
    private final DisinfectionProjectService projectService;
    private final DisinfectionDataTypeService disinfectionDataTypeService;
    private final DisinfectionFileHelper disinfectionFileHelper;
    private final ExcelUtils excelUtils;
    private final DisinfectionFileHelper fileHelper;
    private final AreaClient areaClient;

    @Override
    public List<KeyPlaceInfoVO> listAll(KeyPlacesQueryParam queryParam) {
        LambdaQueryWrapper<DisinfectionKeyPlace> queryWrapper = buildQueryWrapper(queryParam);
        List<DisinfectionKeyPlace> keyPlaces = keyPlaceService.list(queryWrapper);
        if (CollectionUtils.isEmpty(keyPlaces)) {
            return new ArrayList<>();
        }
        Set<Long> fileIdList = new HashSet<>();
        Set<Long> keyPlaceTypeIds = new HashSet<>();
        keyPlaces.forEach(place -> {
            if (Objects.nonNull(place.getPlaceTypeId())) {
                keyPlaceTypeIds.add(place.getPlaceTypeId());
            }
            if (StringUtils.isNotBlank(place.getImgs())) {
                for (String s : place.getImgs().split(",")) {
                    fileIdList.add(Long.valueOf(s));
                }
            }
        });
        Map<Long, FileVO> fileVOMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(fileIdList)) {
            List<FileVO> fileVOList = fileHelper.getFiles(new ArrayList<>(fileIdList));
            Optional.ofNullable(fileVOList).orElse(Collections.emptyList()).forEach(item -> fileVOMap.put(item.getId(), item));
        }
        Map<Long, FileVO> keyPlaceIdFileMap = disinfectionDataTypeService.getFileVOMapByKeyPlaceIds(new ArrayList<>(keyPlaceTypeIds));
        List<Long> projectIds = ListUtils.convertItem(keyPlaces, DisinfectionKeyPlace::getProjectId);
        Map<Long, String> projectNamesMap = projectService.getProjectNamesMap(projectIds);
        return keyPlaces.stream().map(t -> {
            KeyPlaceInfoVO keyPlaceInfoVO = ObjectUtils.copying(t, KeyPlaceInfoVO.class);
            keyPlaceInfoVO.setPlaceTypeIcon(keyPlaceIdFileMap.get(t.getPlaceTypeId()));
            keyPlaceInfoVO.setProjectName(projectNamesMap.getOrDefault(t.getProjectId(), ""));
            if (StringUtils.isNotBlank(t.getImgs())) {
                List<FileVO> fileVOS = new ArrayList<>();
                for (String s : t.getImgs().split(",")) {
                    fileVOS.add(fileVOMap.get(Long.valueOf(s)));
                }
                keyPlaceInfoVO.setFiles(fileVOS);
            }
            return keyPlaceInfoVO;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean insertKeyPlace(KeyPlaceUpsertParam param) {
        DisinfectionKeyPlace disinfectionKeyPlace = ObjectUtils.copying(param, DisinfectionKeyPlace.class);
        //设置场所分类名称
        String typeName = disinfectionDataTypeService.getTypeName(disinfectionKeyPlace.getPlaceTypeId());
        disinfectionKeyPlace.setPlaceTypeName(typeName);
        disinfectionKeyPlace.setDistrict(AddressResolvingHelper.getAddressDistrict(param.getPlaceAddress()));
        boolean result = keyPlaceService.save(disinfectionKeyPlace);
        induceFile(disinfectionKeyPlace);
        return result;
    }

    @Override
    @Transactional
    public boolean updateKeyPlace(KeyPlaceUpsertParam param, long id) {
        //查询修改的数据
        DisinfectionKeyPlace disinfectionKeyPlace = keyPlaceService.getById(id);
        Assert.parameterIsNull(disinfectionKeyPlace, "修改的数据不存在");
        ObjectUtils.copying(param, disinfectionKeyPlace);
        //设置场所分类名称
        String typeName = disinfectionDataTypeService.getTypeName(disinfectionKeyPlace.getPlaceTypeId());
        disinfectionKeyPlace.setPlaceTypeName(typeName);
        disinfectionKeyPlace.setDistrict(AddressResolvingHelper.getAddressDistrict(param.getPlaceAddress()));
        boolean result = keyPlaceService.updateById(disinfectionKeyPlace);
        //持久化文件
        induceFile(disinfectionKeyPlace);
        return result;
    }

    @Override
    public boolean deleteKeyPlace(String ids) {
        if (StringUtils.isBlank(ids)) {
            return true;
        }
        List<Long> idList = Arrays.stream(ids.split(",")).map(Long::valueOf).collect(Collectors.toList());
        return keyPlaceService.removeByIds(idList);
    }

    @Override
    public KeyPlaceInfoVO detail(Long id) {
        DisinfectionKeyPlace keyPlace = keyPlaceService.getById(id);
        Assert.parameterIsNull(keyPlace, "该数据不存在");
        KeyPlaceInfoVO infoVO = ObjectUtils.copying(keyPlace, KeyPlaceInfoVO.class);
        //设置项目名称
        DisinfectionProject project = projectService.getById(keyPlace.getProjectId());
        infoVO.setProjectName(Objects.isNull(project) ? "" : project.getProjectName());
        //设置项目上传的图片信息信息
        setKeyPlaceFileInfo(infoVO, keyPlace.getImgs());
        return infoVO;
    }

    @Override
    public Pagination<KeyPlacePageVO> pageKeyPlaces(KeyPlacePageParam pageParam) {
        Page<DisinfectionKeyPlace> keyPlacePage = keyPlaceService.lambdaQuery()
                .eq(Objects.nonNull(pageParam.getPlaceTypeId()), DisinfectionKeyPlace::getPlaceTypeId, pageParam.getPlaceTypeId())
                .eq(Objects.nonNull(pageParam.getProjectId()), DisinfectionKeyPlace::getProjectId, pageParam.getProjectId())
                .and(Objects.nonNull(pageParam.getKeyword()), q -> {
                    q.like(DisinfectionKeyPlace::getPlaceName, pageParam.getKeyword())
                            .or()
                            .like(DisinfectionKeyPlace::getPlaceDetail, pageParam.getKeyword());
                })
                .page(PagingUtils.convert(pageParam));
        Pagination<KeyPlacePageVO> voPagination = PagingUtils.convert(keyPlacePage, KeyPlacePageVO.class);

        //查询文件信息
        List<KeyPlacePageVO> records = voPagination.getRecords();
        List<Long> fileIds = ListUtils
                .convertItem(records, KeyPlacePageVO::getImgs)
                .stream()
                .filter(StringUtils::isNotBlank)
                .map(imgs -> {
                    return Arrays.asList(imgs.split(","));
                })
                .flatMap(List::stream)
                .map(Long::valueOf)
                .collect(Collectors.toList());
        List<FileVO> fileVOList = disinfectionFileHelper.getFiles(fileIds);
        Map<Long, FileVO> fileVOMap = MapUtils.listToMap(fileVOList, FileVO::getId, fileVO -> fileVO);
        records.forEach(record -> {
            String imgs = record.getImgs();
            if (StringUtils.isBlank(imgs)) {
                record.setFiles(Collections.emptyList());
                return;
            }
            List<FileVO> files = Arrays.stream(imgs.split(","))
                    .map(Long::valueOf)
                    .map(fileVOMap::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            record.setFiles(files);
            //设置项目名称
            record.setProjectName(projectService.getProjectName(record.getProjectId()));

        });

        return voPagination;
    }

    @Override
    public List<KeyPlacePageVO> listKeyPlacesListAll(KeyPlacePageParam pageParam) {
        List<DisinfectionKeyPlace> disinfectionKeyPlaces = keyPlaceService.lambdaQuery()
                .eq(Objects.nonNull(pageParam.getPlaceTypeId()), DisinfectionKeyPlace::getPlaceTypeId, pageParam.getPlaceTypeId())
                .eq(Objects.nonNull(pageParam.getProjectId()), DisinfectionKeyPlace::getProjectId, pageParam.getProjectId())
                .and(Objects.nonNull(pageParam.getKeyword()), q -> {
                    q.like(DisinfectionKeyPlace::getPlaceName, pageParam.getKeyword())
                            .or()
                            .like(DisinfectionKeyPlace::getPlaceDetail, pageParam.getKeyword());
                }).list();
        return ObjectUtils.copying(disinfectionKeyPlaces, KeyPlacePageVO.class);
    }

    @Override
    public void downloadExcelTemplate(HttpServletRequest request, HttpServletResponse response) {
        //获取所有的项目名称
        var projectNames = projectService.listAllProjectNames();
        //获取所有的场所分类名称
        var placeTypeNames = disinfectionDataTypeService.listPlaceTypeNames();

        var headerList = this.haeaderList();
        var mapProvince = this.getMotherlandDistrictLevelMap(1);
        var mapCity = this.getMotherlandDistrictLevelMap(2);
        var startRow = 1;
        new CascadeSelectDownloader("重点场所信息模板")
                .setContentFirstRow(startRow)
                .setHeader(headerList)
                .cascadeDateList(mapProvince, true)
                .cascadeSelectParent(3)
                .cascadeSelectChild(4)
                .cascadeDateList(mapCity, true)
                .cascadeSelectChild(5)
                .selectSetDateList(placeTypeNames, true)
                .selectBoxSet(startRow, 1, CascadeSelectDownloader.DEFAULT_WRITE_LAST_ROW, 1)
                .selectSetDateList(projectNames, true)
                .selectBoxSet(startRow, 2, CascadeSelectDownloader.DEFAULT_WRITE_LAST_ROW, 2)
                .download("重点场所导入模板", request, response);
    }

    private List<String> haeaderList() {
        List<String> headerList = new ArrayList<>();
        headerList.add("*场所名称");
        headerList.add("*场所分类");
        headerList.add("*所属项目名称");
        headerList.add("*省");
        headerList.add("市");
        headerList.add("区");
        headerList.add("重点场所详细位置");
        headerList.add("经度");
        headerList.add("纬度");
        headerList.add("场所地址");
        headerList.add("备注");
        return headerList;
    }

    /**
     * 获取归家区域数据源
     * 按等级获取1表示省，2表示市
     * map的key表示获取的等级，values是该等级的子集
     *
     * @param level 等级
     *
     * @return 区域map
     */
    private Map<String, List<String>> getMotherlandDistrictLevelMap(Integer level) {
        return areaClient.getMotherlandDistrictLevelMap(level);
    }

    @Override
    public boolean importExcel(MultipartFile file) {
        Assert.parameterIsNull(file, "请选择要导入的文件！");
        //转为需要的数据
        List<KeyPlaceExcelDTO> dataList;
        try {
            dataList = excelUtils.readExcelFile(file, KeyPlaceExcelDTO.class);
        } catch (IOException e) {
            throw new ServiceException("导入数据读取错误！");
        }
        Assert.isParameterError(ListUtils.isEmpty(dataList), "导入的数据为空！");
        //查询需要的场所类型以及项目是否存在，如果存在缺少的项目、场所类型则返回错误
        List<String> projectNames = ListUtils.convertItem(dataList, KeyPlaceExcelDTO::getProjectName);
        //根据名称获取项目信息
        List<DisinfectionProject> projects = projectService.listInNames(projectNames);
        Assert.isParameterError(ListUtils.isEmpty(projects), "项目不存在,导入失败！");
        Map<String, DisinfectionProject> projectMap = MapUtils.listToMap(projects, DisinfectionProject::getProjectName, project -> project);
        //根据名称获取场所类型信息
        List<String> placeTypeNames = ListUtils.convertItem(dataList, KeyPlaceExcelDTO::getPlaceTypeName);
        List<DisinfectionDataType> placeTypes = disinfectionDataTypeService.listInNames(placeTypeNames, DisinfectionDataTypeEnum.PLACE);
        Assert.isParameterError(ListUtils.isEmpty(placeTypes), "场所类型不存在,导入失败！");
        Map<String, DisinfectionDataType> placeTypeMap = MapUtils.listToMap(placeTypes, DisinfectionDataType::getDataName, placeType -> placeType);
        List<DisinfectionKeyPlace> keyPlaceList = dataList.stream().map(data -> {
            DisinfectionKeyPlace disinfectionKeyPlace = ObjectUtils.copying(data, DisinfectionKeyPlace.class);
            Assert.isParameterError(StringUtils.isEmpty(data.getPlaceName()), "场所名称为空,导入失败！");
            //设置项目id
            Assert.isParameterError(StringUtils.isEmpty(data.getProjectName()), "项目名称为空,导入失败！");
            DisinfectionProject disinfectionProject = projectMap.get(data.getProjectName());
            Assert.parameterIsNull(disinfectionProject, "项目:%s,不存在,导入失败！", data.getProjectName());
            disinfectionKeyPlace.setProjectId(disinfectionProject.getId());
            //设置场所类型id
            Assert.isParameterError(StringUtils.isEmpty(data.getPlaceTypeName()), "场所类型为空,导入失败！");
            DisinfectionDataType placeType = placeTypeMap.get(data.getPlaceTypeName());
            Assert.parameterIsNull(placeType, "场所类型:%s,不存在,导入失败！", data.getPlaceTypeName());
            disinfectionKeyPlace.setPlaceTypeId(placeType.getId());
            disinfectionKeyPlace.setDistrict(AddressResolvingHelper.getAddressDistrict(data.getPlaceAddress()));
            return disinfectionKeyPlace;

        }).collect(Collectors.toList());
        return keyPlaceService.saveBatch(keyPlaceList);
    }

    private void induceFile(DisinfectionKeyPlace disinfectionKeyPlace) {
        String imgs = disinfectionKeyPlace.getImgs();
        if (StringUtils.isNotBlank(imgs)) {
            List<Long> fileIds = Arrays.stream(imgs.split(","))
                    .map(Long::valueOf)

                    .collect(Collectors.toList());
            disinfectionFileHelper.induceFiles(fileIds, DinsinfectionFileInduceSceneCodeEnum.KEY_PLACE);
        }
    }

    private void setKeyPlaceFileInfo(KeyPlaceInfoVO infoVO, String imgs) {
        if (StringUtils.isBlank(imgs)) {
            infoVO.setFiles(Collections.emptyList());
            return;
        }
        String[] imgFileIds = imgs.split(",");
        List<Long> fileIds = Arrays
                .stream(imgFileIds)
                .filter(StringUtils::isNotBlank)
                .map(Long::parseLong)
                .collect(Collectors.toList());
        try {
            List<FileVO> fileVOList = disinfectionFileHelper.getFiles(fileIds);
            infoVO.setFiles(fileVOList);
        } catch (Exception e) {
            log.error("获取文件失败:{}", e.getMessage());
            infoVO.setFiles(Collections.emptyList());
        }

    }

    private LambdaQueryWrapper<DisinfectionKeyPlace> buildQueryWrapper(KeyPlacesQueryParam queryParam) {
        LambdaQueryWrapper<DisinfectionKeyPlace> queryChainWrapper = new LambdaQueryWrapper<DisinfectionKeyPlace>()
                .eq(Objects.nonNull(queryParam.getProjectId()), DisinfectionKeyPlace::getProjectId, queryParam.getProjectId());

        queryChainWrapper.and(StringUtils.isNotBlank(queryParam.getKeyword()), q -> {
            q.like(DisinfectionKeyPlace::getPlaceName, queryParam.getKeyword()).or()
                    .like(DisinfectionKeyPlace::getPlaceDetail, queryParam.getKeyword());
        });
        queryChainWrapper.eq(Objects.nonNull(queryParam.getDataTypeId()), DisinfectionKeyPlace::getPlaceTypeId, queryParam.getDataTypeId());
        queryChainWrapper.eq(StringUtils.isNotBlank(queryParam.getArea()), DisinfectionKeyPlace::getDistrict, queryParam.getArea());
        return queryChainWrapper;
    }

}

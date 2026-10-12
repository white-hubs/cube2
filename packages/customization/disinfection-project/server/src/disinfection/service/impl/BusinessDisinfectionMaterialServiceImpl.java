package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity.DisinfectionMaterial;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DinsinfectionFileInduceSceneCodeEnum;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.helper.DisinfectionFileHelper;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.helper.DisinfectionUserHelper;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.insert.MaterialInsertParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.page.MaterialPageParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.BusinessDisinfectionMaterialService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionDataTypeService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionMaterialService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionProjectService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.list.MaterialInfoVO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.list.ProjectMaterialInfoVO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.page.MaterialPageVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BusinessDisinfectionMaterialServiceImpl implements BusinessDisinfectionMaterialService {

    private final DisinfectionMaterialService materialService;
    private final DisinfectionUserHelper disinfectionUserHelper;
    private final DisinfectionDataTypeService disinfectionDataTypeService;
    private final DisinfectionFileHelper disinfectionFileHelper;
    private final DisinfectionProjectService disinfectionProjectService;
    private final UserClient userClient;

    @Override
    public List<ProjectMaterialInfoVO> listAll(long projectId, String keyword) {
        LambdaQueryWrapper<DisinfectionMaterial> queryWrapper = buildQueryWrapper(projectId, keyword);
        List<DisinfectionMaterial> materialList = materialService.list(queryWrapper);
        //查询文件信息
        List<FileVO> fileVOList = disinfectionFileHelper.getFiles(ListUtils.convertItem(materialList, DisinfectionMaterial::getFileId));
        Map<Long, FileVO> fileVOMap = MapUtils.listToMap(fileVOList, FileVO::getId, fileVO -> fileVO);
        List<ProjectMaterialInfoVO> projectMaterialInfoVOList = ObjectUtils.copying(materialList, ProjectMaterialInfoVO.class);
        //获取需要的文件类型名称
        List<Long> fileTypeIds = ListUtils.convertItem(projectMaterialInfoVOList, ProjectMaterialInfoVO::getFileTypeId);
        Map<Long, String> fileTypeNamesMap = disinfectionDataTypeService.getNamesMap(fileTypeIds);
        Set<Long> userIds = materialList.stream().filter(material -> Objects.nonNull(material.getFileId()))
                .map(DisinfectionMaterial::getUploadUserId)
                .collect(Collectors.toSet());
        Map<Long, String> userMap;
        if (CollectionUtils.isNotEmpty(userIds)) {
            userMap = Optional.ofNullable(userClient.listUserByIds(userIds))
                    .orElse(new ArrayList<>())
                    .stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getName));
        } else {
            userMap = new HashMap<>();
        }
        projectMaterialInfoVOList.forEach(materialVO -> {
            //设置文件信息
            materialVO.setFile(fileVOMap.get(materialVO.getFileId()));
            materialVO.setUploadUserName(userMap.get(materialVO.getUploadUserId()));
            //设置文件类型名称
            materialVO.setFileTypeName(fileTypeNamesMap.getOrDefault(materialVO.getFileTypeId(), "-"));
        });

        return projectMaterialInfoVOList;
    }

    @Transactional
    @Override
    public boolean insertMaterial(MaterialInsertParam param) {

        //保存文件上传信息
        DisinfectionMaterial material = ObjectUtils.copying(param, DisinfectionMaterial.class);
        //设置上传时间
        material.setUploadTime(LocalDateTime.now());
        //设置上传人
        material.setUploadUserId(SecurityContextHolder.getPrincipal().getId());
        //设置类型名称
        material.setFileTypeName(disinfectionDataTypeService.getTypeName(material.getFileTypeId()));
        //保存资料信息
        boolean saveMaterial = materialService.save(material);
        Assert.isUnknownError(!saveMaterial, "资料信息保存失败！");
        boolean induce = disinfectionFileHelper.induceFile(DinsinfectionFileInduceSceneCodeEnum.MATERIAL, material.getFileId());
        Assert.isUnknownError(!induce, "资料上传失败！");
        return true;
    }

    @Override
    public boolean deleteMaterial(String ids) {
        if (StringUtils.isBlank(ids)) {
            return true;
        }
        List<Long> idList = Arrays.stream(ids.split(",")).map(Long::valueOf).collect(Collectors.toList());
        return materialService.removeByIds(idList);
    }

    @Override
    public List<MaterialInfoVO> listMaterials(long fileTypeId, Long projectId, String keyword) {
        List<DisinfectionMaterial> materialList = materialService.lambdaQuery()
                .eq(DisinfectionMaterial::getFileTypeId, fileTypeId)
                .eq(Objects.nonNull(projectId), DisinfectionMaterial::getProjectId, projectId)
                .like(StringUtils.isNotBlank(keyword), DisinfectionMaterial::getFileName, keyword)
                .list();
        List<MaterialInfoVO> voList = ObjectUtils.copying(materialList, MaterialInfoVO.class);
        List<FileVO> fileVOList = disinfectionFileHelper.getFiles(ListUtils.convertItem(voList, MaterialInfoVO::getFileId));
        Map<Long, FileVO> fileVOMap = MapUtils.listToMap(fileVOList, FileVO::getId, vo -> vo);
        voList.forEach(vo -> vo.setFileVO(fileVOMap.get(vo.getFileId())));
        return voList;
    }

    @Override
    public Pagination<MaterialPageVO> pageMaterials(MaterialPageParam param) {
        Page<DisinfectionMaterial> materialPage = materialService.lambdaQuery()
                .eq(Objects.nonNull(param.getProjectId()), DisinfectionMaterial::getProjectId, param.getProjectId())
                .eq(Objects.nonNull(param.getFileTypeId()), DisinfectionMaterial::getFileTypeId, param.getFileTypeId())
                .like(StringUtils.isNotBlank(param.getKeyword()), DisinfectionMaterial::getFileName, param.getKeyword())
                .page(PagingUtils.convert(param));
        Pagination<MaterialPageVO> voPagination = PagingUtils.convert(materialPage, MaterialPageVO.class);
        List<FileVO> fileVOList = disinfectionFileHelper.getFiles(ListUtils.convertItem(voPagination.getRecords(), MaterialPageVO::getFileId));
        Map<Long, FileVO> fileVOMap = MapUtils.listToMap(fileVOList, FileVO::getId, fileVO -> fileVO);
        //获取需要的项目名称
        List<Long> projectIds = voPagination.getRecords().stream().map(MaterialPageVO::getProjectId).filter(Objects::nonNull)
                .collect(Collectors.toList());
        Map<Long, String> projectNamesMap = disinfectionProjectService.getProjectNamesMap(projectIds);

        //获取需要的类型名称
        List<Long> fileTypeIds = voPagination.getRecords().stream().map(MaterialPageVO::getFileTypeId).filter(Objects::nonNull)
                .collect(Collectors.toList());
        Map<Long, String> fileTypeNamesMap = disinfectionDataTypeService.getNamesMap(fileTypeIds);
        voPagination.getRecords().forEach(vo -> {
            //设置文件信息
            Long fileId = vo.getFileId();
            FileVO fileVO = fileVOMap.getOrDefault(fileId, null);
            vo.setFile(fileVO);
            //设置上传用户信息
            vo.setUploadUserName(disinfectionUserHelper.getUserName(vo.getUploadUserId()));
            //设置文件类型名称
            vo.setFileTypeName(fileTypeNamesMap.getOrDefault(vo.getFileTypeId(), ""));
            //设置项目名称
            vo.setProjectName(projectNamesMap.getOrDefault(vo.getProjectId(), ""));

        });
        return voPagination;
    }

    private LambdaQueryWrapper<DisinfectionMaterial> buildQueryWrapper(Long projectId, String keyword) {
        LambdaQueryWrapper<DisinfectionMaterial> queryChainWrapper = new LambdaQueryWrapper<DisinfectionMaterial>()
                .eq(Objects.nonNull(projectId), DisinfectionMaterial::getProjectId, projectId);
        if (StringUtils.isNotBlank(keyword)) {
            queryChainWrapper.and(queryWrapper -> {
                //查询满足条件的用户
                queryWrapper
                        .like(DisinfectionMaterial::getFileName, keyword);
                List<UserDTO> userDTOS = disinfectionUserHelper.listUsers(keyword);
                //如果满足条件的用户信息不为空
                if (ListUtils.nonEmpty(userDTOS)) {
                    queryWrapper
                            .or()
                            .in(DisinfectionMaterial::getUploadUserId, ListUtils.convertItem(userDTOS, UserDTO::getId));
                }

            });
        }
        return queryChainWrapper;
    }

}

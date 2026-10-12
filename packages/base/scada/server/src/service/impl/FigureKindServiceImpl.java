package com.guhecloud.kaihe.scada.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.kaihe.scada.server.dto.FigureKindDTO;
import com.guhecloud.kaihe.scada.server.entity.Figure;
import com.guhecloud.kaihe.scada.server.entity.FigureKind;
import com.guhecloud.kaihe.scada.server.infrastructure.exception.ServiceDataException;
import com.guhecloud.kaihe.scada.server.infrastructure.exception.ServiceException;
import com.guhecloud.kaihe.scada.server.mapper.FigureKindMapper;
import com.guhecloud.kaihe.scada.server.service.FigureKindService;
import com.guhecloud.kaihe.scada.server.service.FigureService;
import com.guhecloud.kaihe.scada.server.service.FileService;
import com.guhecloud.kaihe.scada.server.service.ProjectService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FigureKindServiceImpl extends ServiceImpl<FigureKindMapper, FigureKind> implements FigureKindService {

    private final FigureKindMapper figureKindMapper;
    private final FigureService figureService;
    private final FileService fileService;
    private final ProjectService projectService;

    @Override
    public void save(FigureKindDTO figureKindDTO) {
        //        figureKindDTO.setProjectId(SessionContextHolder.getRequester().getTenant());
        if (figureKindMapper.selectOne(Wrappers.<FigureKind>lambdaQuery().eq(FigureKind::getKindName, figureKindDTO.getKindName())
                                               .eq(FigureKind::getProjectId, figureKindDTO.getProjectId())) != null) {
            Map<String, String> message = new HashMap<>();
            message.put("AreaName", "名称已经存在");
            throw new ServiceDataException(message);
        }
        if (figureKindMapper.selectOne(Wrappers.<FigureKind>lambdaQuery().eq(FigureKind::getFolderName, figureKindDTO.getFolderName())
                                               .eq(FigureKind::getProjectId, figureKindDTO.getProjectId())) != null) {
            Map<String, String> message = new HashMap<>();
            message.put("AreaName", "文件夹已经存在");
            throw new ServiceDataException(message);
        }
        FigureKind figureKind = figureKindDTO.convert();
        figureKind.setCreateTime(new Date());
        figureKindMapper.insert(figureKind);
        try {
            // 在创建第一个目录时,初始化该项目下其他三个文件夹
            Files.createDirectories(Paths.get(
                    fileService.getRootDir() + "/" + projectService.getProjectBasePath(figureKindDTO.getProjectId()) + "/" +
                    FileService.FIGURES_FOLDER + figureKind.getFolderName()));
            Files.createDirectories(Paths.get(
                    fileService.getRootDir() + "/" + projectService.getProjectBasePath(figureKindDTO.getProjectId()) + "/" +
                    FileService.DIAGRAMS_FOLDER));
            Files.createDirectories(Paths.get(
                    fileService.getRootDir() + "/" + projectService.getProjectBasePath(figureKindDTO.getProjectId()) + "/" +
                    FileService.IMPORT_FOLDER));
            Files.createDirectories(Paths.get(
                    fileService.getRootDir() + "/" + projectService.getProjectBasePath(figureKindDTO.getProjectId()) + "/" +
                    FileService.TEMP_DIAGRAMS_FOLDER));
        } catch (IOException e) {
            throw new ServiceException("", e);
        }
    }

    @Override
    public void update(FigureKindDTO figureKindDTO) {
        figureKindDTO.setProjectId(SessionContextHolder.getRequester().getTenant());
        FigureKind saved = figureKindMapper.selectById(figureKindDTO.getID());
        if (saved == null) {
            throw new ServiceException("ID不存在");
        }
        FigureKind savedKindName = figureKindMapper.selectOne(Wrappers.<FigureKind>lambdaQuery()
                                                                      .eq(FigureKind::getKindName, figureKindDTO.getKindName())
                                                                      .eq(FigureKind::getProjectId, figureKindDTO.getProjectId()));
        if (savedKindName != null && !Objects.equals(savedKindName.getID(), figureKindDTO.getID())) {
            Map<String, String> message = new HashMap<>();
            message.put("AreaName", "名称已经存在");
            throw new ServiceDataException(message);
        }
        FigureKind savedFolderName = figureKindMapper.selectOne(Wrappers.<FigureKind>lambdaQuery()
                                                                        .eq(FigureKind::getFolderName, figureKindDTO.getFolderName())
                                                                        .eq(FigureKind::getProjectId, figureKindDTO.getProjectId()));
        if (savedFolderName != null && !Objects.equals(savedFolderName.getID(), figureKindDTO.getID())) {
            Map<String, String> message = new HashMap<>();
            message.put("AreaName", "文件夹已经存在");
            throw new ServiceDataException(message);
        }
        if (!saved.getFolderName().isEmpty() && !figureKindDTO.getFolderName().isEmpty()) {
            File oldFile = Paths
                    .get(fileService.getRootDir() + "/" + projectService.getProjectBasePath(figureKindDTO.getProjectId()) + "/" +
                         FileService.FIGURES_FOLDER + saved.getFolderName()).toFile();
            File file = Paths.get(fileService.getRootDir() + "/" + projectService.getProjectBasePath(figureKindDTO.getProjectId()) + "/" +
                                  FileService.FIGURES_FOLDER + figureKindDTO.getFolderName()).toFile();
            oldFile.renameTo(file);
        }
        figureKindMapper.updateById(figureKindDTO.convert());
    }

    @Override
    public void remove(Integer id, String folder, Integer projectId) {
        if (figureKindMapper.selectOne(Wrappers.<FigureKind>lambdaQuery().eq(FigureKind::getID, id)
                                               .eq(FigureKind::getProjectId, projectId)) == null) {
            throw new ServiceException("ID不存在或者不属于该项目");
        }
        if (figureService.list(Wrappers.<Figure>lambdaQuery().eq(Figure::getFigureKindID, id)).size() > 0) {
            throw new ServiceException("该目录下存在图元不能删除,请先删除图元");
        }
        List<FigureKindDTO> figureKindDTOS = new ArrayList<>();
        figureKindDTOS.add(new FigureKindDTO().setID(id).setFolderName(folder));
        List<FigureKindDTO> childFigureKindDTOS = this.getChildFigureKinds(figureKindDTOS, id);
        for (FigureKindDTO childFigureKind : childFigureKindDTOS) {
            figureKindMapper.deleteById(childFigureKind.getID());
            figureService.remove(Wrappers.<Figure>lambdaQuery().eq(Figure::getFigureKindID, childFigureKind.getID()));
            if (Paths.get(projectService.getProjectBasePath(projectId), FileService.FIGURES_FOLDER, childFigureKind.getFolderName())
                    .toFile().exists()) {
                fileService.removeDir(projectService.getProjectBasePath(projectId), FileService.FIGURES_FOLDER, childFigureKind.getFolderName());
            }
        }
    }

    private List<FigureKindDTO> getChildFigureKinds(List<FigureKindDTO> figureKindDTOS, Integer id) {
        List<FigureKindDTO> childFigureKindDTOS = new FigureKindDTO(figureKindMapper.selectList(Wrappers.<FigureKind>lambdaQuery()
                                                                                                        .eq(FigureKind::getParentID, id))).list();
        if (childFigureKindDTOS.size() > 0) {
            for (FigureKindDTO figureKindDTO : childFigureKindDTOS) {
                if (figureService.list(Wrappers.<Figure>lambdaQuery().eq(Figure::getFigureKindID, figureKindDTO.getID())).size() > 0) {
                    throw new ServiceException("该目录下的子目录存在图元，不能删除");
                } else {
                    figureKindDTOS.add(figureKindDTO);
                    getChildFigureKinds(figureKindDTOS, figureKindDTO.getID());
                }
            }
        }
        return figureKindDTOS;
    }

}
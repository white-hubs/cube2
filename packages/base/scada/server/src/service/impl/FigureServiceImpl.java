package com.guhecloud.kaihe.scada.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.kaihe.scada.server.entity.Figure;
import com.guhecloud.kaihe.scada.server.entity.FigureKind;
import com.guhecloud.kaihe.scada.server.infrastructure.exception.ServiceException;
import com.guhecloud.kaihe.scada.server.mapper.FigureKindMapper;
import com.guhecloud.kaihe.scada.server.mapper.FigureMapper;
import com.guhecloud.kaihe.scada.server.service.FigureService;
import com.guhecloud.kaihe.scada.server.service.FileService;
import com.guhecloud.kaihe.scada.server.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FigureServiceImpl extends ServiceImpl<FigureMapper, Figure> implements FigureService {

    private final FigureMapper figureMapper;
    private final FigureKindMapper figureKindMapper;
    private final FileService fileService;
    private final ProjectService projectService;

    @Override
    public int save(String fileId, Integer figureKindId, String folder, MultipartFile fileData, Integer projectId) {
        String fileName = fileData.getOriginalFilename();
        String fileExt = StringUtils.getFilenameExtension(fileName);
//        if (figureMapper.selectOne(Wrappers.<Figure>lambdaQuery().eq(Figure::getFigureKindID, figureKindId).eq(Figure::getFigureName, fileName)) != null) {
//            throw new ServiceException("文件已经存在");
//        }
        int fileType = 0;
        if (fileExt.equals("jpg") || fileExt.equals("jpeg") || fileExt.equals("png") || fileExt.equals("gif") || fileExt.equals("bmp")) {
            fileType = 2;
        } else if (fileExt.equals("svg")) {
            fileType = 1;
        }
        if (fileType < 0 || fileType > 2) {
            throw new ServiceException("文件类型错误");
        }
        try {
            fileService.saveFile(fileName, fileData.getBytes(), projectService.getProjectBasePath(projectId), FileService.FIGURES_FOLDER, folder);
        } catch (IOException e) {
            throw new ServiceException("", e);
        }
        Figure figure = new Figure();
        figure.setFigureKindID(figureKindId);
        figure.setFigureName(fileName);
        figure.setFigureFunctionID(fileType);
        figure.setFileName(fileName);
        figure.setCreateTime(new Date());
        figure.setProjectId(projectId);
        figure.setPublic(false);
        figureMapper.insert(figure);
        Integer id = figure.getID();
        return id;
    }

    @Override
    public void rename(Integer id, String name) {
        Figure figure = figureMapper.selectById(id);
        figure.setFigureName(name);
        figureMapper.updateById(figure);
    }

    @Override
    public void remove(Integer figureId, String fileName) {
        Figure figure = figureMapper.selectOne(Wrappers.<Figure>lambdaQuery().eq(Figure::getID, figureId));
        FigureKind figureKind = figureKindMapper.selectOne(Wrappers.<FigureKind>lambdaQuery().eq(FigureKind::getID, figure.getFigureKindID()));
        figureMapper.deleteById(figureId);
        fileService.removeFile(figure.getFileName(), projectService.getProjectBasePath(figureKind.getProjectId()), FileService.FIGURES_FOLDER, figureKind.getFolderName());
    }

}

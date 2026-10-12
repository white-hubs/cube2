package com.guhecloud.kaihe.scada.server.controller;

import com.guhecloud.kaihe.scada.server.entity.Figure;
import com.guhecloud.kaihe.scada.server.entity.FigureKind;
import com.guhecloud.kaihe.scada.server.infrastructure.util.ServletUtils;
import com.guhecloud.kaihe.scada.server.service.FigureKindService;
import com.guhecloud.kaihe.scada.server.service.FigureService;
import com.guhecloud.kaihe.scada.server.service.FileService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 获取图元
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DownController {

    private final FileService fileService;
    private final FigureService figureService;
    private final FigureKindService figureKindService;

    @GetMapping("/figures/{figureId}/svg")
    public void downFigure(@PathVariable("figureId") Integer figureId, HttpServletResponse response) {
        try {
            Figure figure = figureService.lambdaQuery().eq(Figure::getID, figureId).one();
            String fileName = figure.getFileName();
            String folderName = figureKindService.lambdaQuery().eq(FigureKind::getID, figure.getFigureKindID()).one().getFolderName();
            byte[] file = fileService.getFile(fileName, FileService.FIGURES_FOLDER, folderName);
            ServletUtils.respondFileInfo(fileName, file.length, response);
            ServletUtils.respondBytes(file, response);
        } catch (Exception e) {
            log.error("", e);
        }
    }

    @GetMapping("/diagrams/{diagramId}/png")
    public void downDiagram(@PathVariable("diagramId") Integer diagramId, HttpServletResponse response) {
        try {
            String fileName = diagramId + ".png";
            byte[] file = fileService.getFile(fileName, FileService.DIAGRAMS_FOLDER);
            ServletUtils.respondFileInfo(fileName, file.length, response);
            ServletUtils.respondBytes(file, response);
        } catch (Exception e) {
            log.error("", e);
        }
    }

}

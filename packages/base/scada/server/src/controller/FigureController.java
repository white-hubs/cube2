package com.guhecloud.kaihe.scada.server.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.kaihe.scada.server.dto.FigureDTO;
import com.guhecloud.kaihe.scada.server.dto.FigurePageDTO;
import com.guhecloud.kaihe.scada.server.entity.Figure;
import com.guhecloud.kaihe.scada.server.entity.FigureFunction;
import com.guhecloud.kaihe.scada.server.entity.FigureKind;
import com.guhecloud.kaihe.scada.server.infrastructure.web.response.Result;
import com.guhecloud.kaihe.scada.server.infrastructure.web.response.UnifiedResponseBody;
import com.guhecloud.kaihe.scada.server.mapper.FigureFunctionMapper;
import com.guhecloud.kaihe.scada.server.mapper.FigureKindMapper;
import com.guhecloud.kaihe.scada.server.service.FigureService;
import com.guhecloud.kaihe.scada.server.service.FileService;
import com.guhecloud.kaihe.scada.server.service.ProjectService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 图元
 */
@RequestMapping("/Home")
@RestController
@UnifiedResponseBody
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FigureController {

    @Value("${figure.visitUrlPath}")
    private String visitUrlPath;

    @Value("${spring.application.name}")
    private String springApplicationName;

    private final FigureService figureService;
    private final FigureKindMapper figureKindMapper;
    private final FigureFunctionMapper figureFunctionMapper;

    @PostMapping("/GetFigures")
    public List<FigureDTO> getById(
            @RequestParam(name = "id", defaultValue = "") Integer id,
            HttpServletRequest request
                                  ) {
        //        String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        List<FigureDTO> figureDTOS = new FigureDTO(figureService.list(Wrappers.<Figure>lambdaQuery()
                                                                              .eq(Figure::getFigureKindID, id))).list();
        extend(figureDTOS, "");
        return figureDTOS;
    }

    @PostMapping("/GetFiguresByPage")
    public Result getPage(
            @RequestParam(name = "start", defaultValue = "0") Integer start,
            @RequestParam(name = "length", defaultValue = "8") Integer length,
            @RequestParam(name = "figureKind", defaultValue = "0") Integer figureKindId,
            @RequestParam(name = "projectId", defaultValue = ProjectService.DEFAULT_PROJECT_ID) Integer projectId,
            HttpServletRequest request
                         ) {
        //        String visitUrlPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        //        projectId = SessionContextHolder.getRequester().getTenant();
        Page<Figure> page = figureService.lambdaQuery()
                .eq(figureKindId > 0, Figure::getFigureKindID, figureKindId)
                .eq(Figure::getProjectId, projectId)
                .page(new Page<>(getPageNum(start, length), length));
        List<FigureDTO> figureDTOS = new FigureDTO(page.getRecords()).list();
        List<FigurePageDTO> figurePageDTOS = figureDTOS.stream().map(figureDTO -> {
            FigureKind figureKind = figureKindMapper.selectById(figureDTO.getFigureKindID());
            FigurePageDTO figurePageDTO = new FigurePageDTO()
                    .setID(figureDTO.getID())
                    .setName(figureDTO.getFigureName())
                    .setType(figureFunctionMapper.selectOne(Wrappers.<FigureFunction>lambdaQuery()
                                                                    .eq(FigureFunction::getID, figureDTO.getFigureFunctionID()))
                                     .getFunctionName())
                    .setFolderName(figureKind.getFolderName())
                    .setFileName(visitUrlPath /*+ springApplicationName*/ + FileService.FIGURES_FOLDER + figureDTO.getID() + "/svg")
                    .setLastUpdate(new Date());
            return figurePageDTO;
        }).collect(Collectors.toList());
        Result result = Result.builder().success(true).data(figurePageDTOS).build();
        result.prop("count", page.getTotal());
        result.setCount(page.getTotal());
        return result;
    }

    @PostMapping("/UploadImage")
    public Result save(
            @RequestParam(name = "file_id", defaultValue = "") String fileId,
            @RequestParam(name = "folderId", defaultValue = "") Integer figureKindId,
            @RequestParam(name = "folder", defaultValue = "") String folder,
            @RequestParam(name = "file_data", required = false) MultipartFile fileData,
            @RequestParam(name = "projectId", defaultValue = ProjectService.DEFAULT_PROJECT_ID) Integer projectId
                      ) {
        //        projectId = SessionContextHolder.getRequester().getTenant();
        int save = figureService.save(fileId, figureKindId, folder, fileData, projectId);
        return Result.builder().success(true).data(save).build();
    }

    @PostMapping("/RenameFigure")
    public void rename(
            @RequestParam("ID") Integer id,
            @RequestParam("Name") String name
                      ) {
        figureService.rename(id, name);
    }

    @PostMapping("/DeleteFigure")
    public void delete(
            @RequestParam(name = "figureId", defaultValue = "") Integer figureId,
            @RequestParam(name = "fileName", defaultValue = "") String fileName
                      ) {
        figureService.remove(figureId, fileName);
    }

    private void extend(List<FigureDTO> figureDTOS, String url) {
        for (FigureDTO figureDTO : figureDTOS) {
            if (figureDTO.getFigureKindID() != null) {
                figureDTO.setFolderName(figureKindMapper.selectOne(Wrappers.<FigureKind>lambdaQuery()
                                                                           .eq(FigureKind::getID, figureDTO.getFigureKindID()))
                                                .getFolderName());
                figureDTO.setFileName(url + FileService.FIGURES_FOLDER + figureDTO.getID() + "/svg");
            }
            if (figureDTO.getFigureFunctionID() != null) {
                figureDTO.setFunctionName(figureFunctionMapper.selectOne(Wrappers.<FigureFunction>lambdaQuery()
                                                                                 .eq(FigureFunction::getID, figureDTO.getFigureFunctionID()))
                                                  .getFunctionName());
            }
        }
    }

    private int getPageNum(int start, int length) {
        int pageNum = (start / length) + 1;
        return pageNum;
    }

}

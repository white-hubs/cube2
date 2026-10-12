package com.guhecloud.kaihe.scada.server.controller;

import com.guhecloud.kaihe.scada.server.dto.FigureKindDTO;
import com.guhecloud.kaihe.scada.server.dto.FigureKindTreeDTO;
import com.guhecloud.kaihe.scada.server.entity.FigureKind;
import com.guhecloud.kaihe.scada.server.infrastructure.web.response.UnifiedResponseBody;
import com.guhecloud.kaihe.scada.server.service.FigureKindService;
import com.guhecloud.kaihe.scada.server.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 图元类型
 */
@RequestMapping("/Home")
@RestController
@UnifiedResponseBody
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FigureKindController {

    private final FigureKindService figureKindService;

    @PostMapping("/GetFigureKind")
    public List<FigureKindDTO> getAll(
            @RequestParam(name = "projectId", defaultValue = ProjectService.DEFAULT_PROJECT_ID) Integer projectId
                                     ) {
        //        projectId = SessionContextHolder.getRequester().getTenant();
        //        return Result.builder().success(true).data(new FigureKindDTO(figureKindService.lambdaQuery().eq(FigureKind::getProjectId, projectId).list()).list()).build();
        return new FigureKindDTO(figureKindService.lambdaQuery().eq(FigureKind::getProjectId, projectId).list()).list();
    }

    @PostMapping("/GetFigureKindTree")
    public List<FigureKindTreeDTO> getTree(
            @RequestParam(name = "projectId", defaultValue = ProjectService.DEFAULT_PROJECT_ID) Integer projectId
                                          ) {
        //        projectId = SessionContextHolder.getRequester().getTenant();
        List<FigureKind> rootFigureKinds = figureKindService.lambdaQuery()
                .eq(FigureKind::getParentID, -1)
                .eq(FigureKind::getProjectId, projectId)
                .list();
        return getFigureKindTreeDTOS(rootFigureKinds);
        // TODO 查询本项目的所有目录以及其他项目可见的部分，以项目结构显示，需前端配合
    }

    @PostMapping("/FigureKindDetail/{id}")
    public void saveOrUpdate(
            @Valid FigureKindDTO figureKindDTO,
            @PathVariable("id") Integer id
                            ) {
        if (id == -1) {
            figureKindService.save(figureKindDTO);
        } else {
            figureKindService.update(figureKindDTO);
        }
    }

    @PostMapping("/DeleteFigureKind")
    public void remove(
            @RequestParam(name = "id", defaultValue = "") Integer id,
            @RequestParam(name = "folder", defaultValue = "") String folder,
            @RequestParam(name = "projectId", defaultValue = ProjectService.DEFAULT_PROJECT_ID) Integer projectId
                      ) {
        //        projectId = SessionContextHolder.getRequester().getTenant();
        figureKindService.remove(id, folder, projectId);
    }

    private List<FigureKindTreeDTO> getChildFigureDTOS(FigureKindDTO rootFigureKindDTO) {
        List<FigureKind> childFigureKinds = figureKindService.lambdaQuery()
                .eq(FigureKind::getParentID, rootFigureKindDTO.getID())
                .list();
        return getFigureKindTreeDTOS(childFigureKinds);
    }

    private List<FigureKindTreeDTO> getFigureKindTreeDTOS(List<FigureKind> rootFigureKinds) {
        List<FigureKindDTO> rootFigureKindDTOS = new FigureKindDTO(rootFigureKinds).list();
        List<FigureKindTreeDTO> figureKindTreeDTOS = rootFigureKindDTOS.stream().map(rootFigureKindDTO -> {
            FigureKindTreeDTO figureKindTreeDTO = new FigureKindTreeDTO()
                    .setText(rootFigureKindDTO.getKindName())
                    .setHref("#" + rootFigureKindDTO.getID())
                    .setKey(rootFigureKindDTO.getID())
                    .setPkey(rootFigureKindDTO.getParentID())
                    .setFolder(rootFigureKindDTO.getFolderName())
                    .setNodes(this.getChildFigureDTOS(rootFigureKindDTO));
            return figureKindTreeDTO;
        }).collect(Collectors.toList());
        return figureKindTreeDTOS;
    }

}

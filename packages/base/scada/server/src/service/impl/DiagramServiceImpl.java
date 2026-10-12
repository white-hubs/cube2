package com.guhecloud.kaihe.scada.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.kaihe.scada.server.dto.DiagramDTO;
import com.guhecloud.kaihe.scada.server.dto.DiagramDataDTO;
import com.guhecloud.kaihe.scada.server.dto.DiagramUpdateDTO;
import com.guhecloud.kaihe.scada.server.entity.Diagram;
import com.guhecloud.kaihe.scada.server.entity.DiagramData;
import com.guhecloud.kaihe.scada.server.infrastructure.exception.ServiceException;
import com.guhecloud.kaihe.scada.server.mapper.DiagramMapper;
import com.guhecloud.kaihe.scada.server.service.DiagramDataService;
import com.guhecloud.kaihe.scada.server.service.DiagramService;
import com.guhecloud.kaihe.scada.server.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DiagramServiceImpl extends ServiceImpl<DiagramMapper, Diagram> implements DiagramService {

    private final DiagramMapper diagramMapper;
    private final DiagramDataService diagramDataService;
    private final FileService fileService;

    @Override
    public int save(DiagramDTO diagramDTO) {
        // 1 save Diagram
        Diagram diagram = diagramDTO.convert();
        diagramMapper.insert(diagram);
        int diagramId = diagram.getID();
        // 2 create Dia file
        DiagramDataDTO dia = new DiagramDataDTO();
        dia.setDiagramID(diagramId);
        dia.setTYPE(DiagramDataService.TYPE_DMO);
        dia.setFileName(diagramId + ".dmo");
        dia.setFileSize(diagramDTO.getDiagram().getBytes().length);
        dia.setLastUpdate(diagram.getLastUpdate());
        dia.setProjectId(diagramDTO.getProjectId());
        diagramDataService.save(dia);
        fileService.saveFile(diagramId + ".dmo", diagramDTO.getDiagram().getBytes(), FileService.DIAGRAMS_FOLDER);
        // 3 create PNG file
        byte[] pngBytes = parsePng(diagramDTO.getPng());
        DiagramDataDTO png = new DiagramDataDTO();
        png.setDiagramID(diagramId);
        png.setTYPE(DiagramDataService.TYPE_PNG);
        png.setFileName(diagramId + ".png");
        png.setFileSize(pngBytes.length);
        png.setLastUpdate(diagram.getLastUpdate());
        png.setProjectId(diagramDTO.getProjectId());
        diagramDataService.save(png);
        fileService.saveFile(diagramId + ".png", pngBytes, FileService.DIAGRAMS_FOLDER);
        // 4 create CSV file
        DiagramDataDTO csv = new DiagramDataDTO();
        csv.setDiagramID(diagramId);
        csv.setTYPE(DiagramDataService.TYPE_CSV);
        csv.setFileName(diagramId + ".csv");
        csv.setFileSize(diagramDTO.getLinkMap().getBytes().length);
        csv.setLastUpdate(diagram.getLastUpdate());
        csv.setProjectId(diagramDTO.getProjectId());
        diagramDataService.save(csv);
        fileService.saveFile(diagramId + ".csv", diagramDTO.getLinkMap().getBytes(), FileService.DIAGRAMS_FOLDER);
        //
        return diagram.getID();
    }

    @Override
    public void update(DiagramUpdateDTO diagramUpdateDTO) {
        // @formatter:off
        Integer diagramId = diagramUpdateDTO.getDiagramId();
        Date now = new Date();
        // 1 update Diagram
        Diagram diagram = diagramMapper.selectById(diagramUpdateDTO.getDiagramId());
        diagram.setSize(diagramUpdateDTO.getDiagram().getBytes().length);
        diagram.setLastUpdate(now);
        diagram.setProjectId(diagramUpdateDTO.getProjectId());
        diagramMapper.updateById(diagram);
        // 2 - update the Dia file
        diagramDataService.lambdaUpdate()
                .eq(DiagramData::getDiagramID, diagramId)
                .eq(DiagramData::getTYPE, DiagramDataService.TYPE_DMO)
                .set(DiagramData::getFileSize, diagramUpdateDTO.getDiagram().getBytes().length)
                .set(DiagramData::getLastUpdate, now)
                .set(DiagramData::getProjectId, diagramUpdateDTO.getProjectId())
                .update();
        fileService.saveFile(diagramId + ".dmo", diagramUpdateDTO.getDiagram().getBytes(), FileService.DIAGRAMS_FOLDER);
        // 3 - update the PNG file
        byte[] pngBytes = parsePng(diagramUpdateDTO.getPng());
        diagramDataService.lambdaUpdate()
                .eq(DiagramData::getDiagramID, diagramId)
                .eq(DiagramData::getTYPE, DiagramDataService.TYPE_PNG)
                .set(DiagramData::getFileSize, pngBytes.length)
                .set(DiagramData::getLastUpdate, now)
                .set(DiagramData::getProjectId, diagramUpdateDTO.getProjectId())
                .update();
        fileService.saveFile(diagramId + ".png", pngBytes, FileService.DIAGRAMS_FOLDER);
        // 4 - update the CSV file
        diagramDataService.lambdaUpdate()
                .eq(DiagramData::getDiagramID, diagramId)
                .eq(DiagramData::getTYPE, DiagramDataService.TYPE_CSV)
                .set(DiagramData::getFileSize, diagramUpdateDTO.getLinkMap().getBytes().length)
                .set(DiagramData::getLastUpdate, now)
                .set(DiagramData::getProjectId, diagramUpdateDTO.getProjectId())
                .update();
        fileService.saveFile(diagramId + ".csv", diagramUpdateDTO.getLinkMap().getBytes(), FileService.DIAGRAMS_FOLDER);
        // @formatter:on
    }

    @Override
    public void remove(Integer id) {
        try {
            diagramMapper.deleteById(id);
            diagramDataService.remove(Wrappers.<DiagramData>lambdaQuery().eq(DiagramData::getDiagramID, id));
            fileService.removeFile(id + ".dmo", FileService.DIAGRAMS_FOLDER);
            fileService.removeFile(id + ".png", FileService.DIAGRAMS_FOLDER);
            fileService.removeFile(id + ".svg", FileService.DIAGRAMS_FOLDER);
            fileService.removeFile(id + ".csv", FileService.DIAGRAMS_FOLDER);
        } catch (Exception e) {
            throw new ServiceException("", e);
        }
    }

    private byte[] parsePng(String png) {
        String pngBase64 = png.substring(png.indexOf(",") + 1);
        return Base64.getDecoder().decode(pngBase64);
    }

}

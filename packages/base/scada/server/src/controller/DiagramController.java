package com.guhecloud.kaihe.scada.server.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guhecloud.kaihe.scada.server.dto.DiagramDTO;
import com.guhecloud.kaihe.scada.server.dto.DiagramLoadDTO;
import com.guhecloud.kaihe.scada.server.dto.DiagramQueryDTO;
import com.guhecloud.kaihe.scada.server.dto.DiagramUpdateDTO;
import com.guhecloud.kaihe.scada.server.entity.Diagram;
import com.guhecloud.kaihe.scada.server.entity.DiagramData;
import com.guhecloud.kaihe.scada.server.infrastructure.exception.ServiceException;
import com.guhecloud.kaihe.scada.server.infrastructure.util.ServletUtils;
import com.guhecloud.kaihe.scada.server.infrastructure.web.response.Result;
import com.guhecloud.kaihe.scada.server.infrastructure.web.response.UnifiedResponseBody;
import com.guhecloud.kaihe.scada.server.mapper.DiagramDataMapper;
import com.guhecloud.kaihe.scada.server.service.DiagramDataService;
import com.guhecloud.kaihe.scada.server.service.DiagramService;
import com.guhecloud.kaihe.scada.server.service.FileService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 组态页面
 */
@Slf4j
@RequestMapping("/Home")
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DiagramController {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSSS");

    private static final String ALLOWED_IMAGES_REG = ".+(.jpg|.jpeg|.png|.gif|.bmp|.JPG|.JPEG|.PNG|.GIF|.BMP)$";

    private static final String RESULT_FIRST_SAVE = "firstSave";
    private static final String RESULT_SAVED = "saved";
    private static final String RESULT_NOT_SAVED = "diagramdata not saved";

    private final ObjectMapper objectMapper;
    private final DiagramService diagramService;
    private final DiagramDataMapper diagramDataMapper;
    private final FileService fileService;

    @Value("${figure.visitUrlPath}")
    private String visitUrlPath;

    @Value("${spring.application.name}")
    private String springApplicationName;

    @PostMapping("/ImportDiagramExe")
    @UnifiedResponseBody
    public Result importDiagram(@RequestParam("fileInfo") MultipartFile fileInfo) {
        try {
            String newFileName = "tmp" + sdf.format(new Date()) + ".dmo";
            fileService.saveFile(newFileName, fileInfo.getBytes(), FileService.TEMP_DIAGRAMS_FOLDER);
            // result
            Result result = Result.builder().success(true).build();
            result.prop("diagramId", newFileName);
            return result;
        } catch (Exception e) {
            log.error("", e);
            return Result.builder().success(false).message("导入图形失败").build();
        }
    }

    @PostMapping("/RenameDiagram")
    @UnifiedResponseBody
    public Result RenameDiagram(@RequestParam("ID") Integer id,
                                @RequestParam("Name") String name
                               ) {
        try {
            boolean update = diagramService.lambdaUpdate().eq(Diagram::getID, id).set(Diagram::getTitle, name).update();
            // result
            Result result = Result.builder().success(update).build();
            return result;
        } catch (Exception e) {
            log.error("", e);
            return Result.builder().success(false).message("重命名失败").build();
        }
    }

    @PostMapping("/FirstSaveExe")
    @UnifiedResponseBody
    public Result saveDiagram(

            @RequestParam(name = "action", defaultValue = "") String action,
            @RequestParam(name = "category", defaultValue = "") Integer category,
            @RequestParam(name = "title", defaultValue = "") String title,
            @RequestParam(name = "description", defaultValue = "") String description,
            @RequestParam(name = "public", defaultValue = "true") Boolean pub,
            @RequestParam(name = "diagram", defaultValue = "") String diagram,
            @RequestParam(name = "png", defaultValue = "") String png,
            @RequestParam(name = "linkMap", defaultValue = "") String linkMap,
            @RequestParam(name = "svg", defaultValue = "") String svg,
            @RequestParam(name = "projectId", defaultValue = "") Integer projectId
                             ) {
        //        projectId = SessionContextHolder.getRequester().getTenant();
        try {
            Date now = new Date();
            DiagramDTO diagramDTO = new DiagramDTO();
            diagramDTO.setTitle(title);
            diagramDTO.setDescription(description);
            diagramDTO.setPublic(pub);
            diagramDTO.setCreatedDate(now);
            diagramDTO.setLastUpdate(now);
            diagramDTO.setSize(diagram.getBytes().length);
            diagramDTO.setCategory(category);
            diagramDTO.setDiagram(diagram);
            diagramDTO.setPng(png);
            diagramDTO.setLinkMap(linkMap);
            diagramDTO.setSvg(svg);
            diagramDTO.setProjectId(projectId);
            int diagramId = diagramService.save(diagramDTO);
            // result
            Result result = Result.builder().success(true).build();
            result.prop("diagramId", diagramId);
            return result;
        } catch (Exception e) {
            log.error("", e);
            return Result.builder().success(false).message(e.getMessage()).build();
        }
    }

    @PostMapping("/SaveDiagram")
    public String updateDiagram(HttpServletRequest request) {
        try {
            DiagramUpdateDTO diagramUpdateDTO = objectMapper.readValue(IOUtils.toByteArray(request.getInputStream()), DiagramUpdateDTO.class);
            if (diagramUpdateDTO.getDiagramId() == null) {
                return RESULT_FIRST_SAVE;
            }
            diagramService.update(diagramUpdateDTO);
            return RESULT_SAVED;
        } catch (Exception e) {
            log.error("", e);
            return RESULT_NOT_SAVED;
        }
    }

    @PostMapping("/GetAllDiagram")
    @UnifiedResponseBody
    public Result getPage(
            @RequestParam(name = "start", defaultValue = "0") Integer start,
            @RequestParam(name = "length", defaultValue = "8") Integer length,
            @RequestParam(name = "category", defaultValue = "0") Integer category,
            @RequestParam(name = "projectId", defaultValue = "0") Integer projectId,
            HttpServletRequest request
                         ) {
        //        String visitUrlPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        //        projectId = SessionContextHolder.getRequester().getTenant();
        Page<Diagram> page = diagramService.lambdaQuery()
                .eq(category > 0, Diagram::getCategory, category)
                //                .eq(Diagram::getProjectId,projectId)
                .page(new Page<>(getPageNum(start, length), length));

        List<DiagramDTO> diagramDTOS = new DiagramDTO(page.getRecords()).list();
        List<DiagramQueryDTO> diagramQueryDTOS = diagramDTOS.stream().map(diagramDTO -> {
            DiagramData diagramData = diagramDataMapper.selectOne(Wrappers.<DiagramData>lambdaQuery()
                                                                          .eq(DiagramData::getDiagramID, diagramDTO.getID())
                                                                          .eq(DiagramData::getTYPE, DiagramDataService.TYPE_PNG));
            DiagramQueryDTO diagramQueryDTO = new DiagramQueryDTO()
                    .setID(diagramDTO.getID())
                    .setTitle(diagramDTO.getTitle())
                    .setDescription(diagramDTO.getDescription())
                    .setFileName(visitUrlPath /*+springApplicationName*/ + FileService.DIAGRAMS_FOLDER + diagramDTO.getID() + "/png")
                    .setLastUpdate(diagramData.getLastUpdate());
            return diagramQueryDTO;
        }).collect(Collectors.toList());
        Result result = Result.builder().success(true).data(diagramQueryDTOS).build();
        result.prop("count", page.getTotal());
        result.setCount(page.getTotal());
        return result;
    }

    @PostMapping("/Load")
    public String load(HttpServletRequest request) {
        try {
            DiagramLoadDTO diagramLoadDTO = objectMapper.readValue(IOUtils.toByteArray(request.getInputStream()), DiagramLoadDTO.class);
            return new String(fileService.getFile(diagramLoadDTO.getDiagramId() + ".dmo", FileService.DIAGRAMS_FOLDER));
        } catch (Exception e) {
            log.error("", e);
            return "";
        }
    }

    @PostMapping("/GetUploadedImageFileNames")
    @UnifiedResponseBody
    public Result getUploadedImageFileNames() {
        List<String> list = new ArrayList<>();
        try {
            list = Files.list(Paths.get(fileService.getRootDir() + "/" + FileService.IMPORT_FOLDER))
                    .filter(p -> isAllowedImageFile(p.toFile().getName()))
                    .map(p -> p.toFile().getName())
                    .collect(Collectors.toList());
            return Result.builder().success(true).data(list).build();

        } catch (Exception e) {
            log.error("", e);
            return Result.builder().success(false).data(list).message("读取上传图片失败!").build();
        }
    }

    @PostMapping("/insertImage")
    public Result insertImage(
            @RequestParam(name = "image-group", defaultValue = "") String imageGroup,
            @RequestParam(name = "imageFile", required = false) MultipartFile imageFile,
            @RequestParam(name = "reuseImageFile", defaultValue = "") String reuseImageFile,
            @RequestParam(name = "imageURL", defaultValue = "") String imageURL
                             ) {
        String fileName = "";
        try {
            if ("URL".equals(imageGroup)) {
                URL url = new URL(imageURL);
                fileName = FilenameUtils.getName(url.getPath());
                if (!isAllowedImageFile(fileName)) {
                    throw new ServiceException("不支持的文件类型");
                }
                Path target = Paths.get(fileService.getRootDir() + "/" + FileService.IMPORT_FOLDER, fileName);
                Files.copy(new URL(imageURL).openStream(), target, StandardCopyOption.REPLACE_EXISTING);
            } else if ("Upload".equals(imageGroup)) {
                fileName = imageFile.getOriginalFilename();
                if (!isAllowedImageFile(fileName)) {
                    throw new ServiceException("不支持的文件类型");
                }
                fileService.saveFile(fileName, imageFile.getBytes(), FileService.IMPORT_FOLDER);
            } else if ("Reuse".equals(imageGroup)) {
                fileName = reuseImageFile;
            } else {
                throw new ServiceException("无法处理的类型");
            }
            return Result.builder().success(true).data(fileName).build();
        } catch (Exception e) {
            log.error("", e);
            return Result.builder().success(false).message("上载图像时出错。检查所选文件。").build();
        }
    }

    @GetMapping("/DownLoad")
    public void downLoad(@RequestParam("diagramId") Integer diagramId, HttpServletResponse response) {
        try {
            byte[] file = fileService.getFile(diagramId.toString() + ".dmo", FileService.DIAGRAMS_FOLDER);
            ServletUtils.respondFileInfo(diagramId.toString() + ".dmo", file.length, response);
            ServletUtils.respondBytes(file, response);
        } catch (Exception e) {
            log.error("", e);
        }
    }

    @PostMapping("/Delete")
    @UnifiedResponseBody
    public Result remove(Integer diagramId) {
        diagramService.remove(diagramId);
        return Result.builder().success(true).build();
    }

    private boolean isAllowedImageFile(String fileName) {
        return Pattern.compile(ALLOWED_IMAGES_REG).matcher(fileName).matches();
    }

    private int getPageNum(int start, int length) {
        int pageNum = (start / length) + 1;
        return pageNum;
    }

}

package com.guhecloud.cube.deliver.project.merchant.controller.store;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.BaseQueryDTO;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.BatchDTO;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.ErrorExcelDTO;
import com.guhecloud.cube.deliver.project.merchant.commons.utils.ExcelUtils;
import com.guhecloud.cube.deliver.project.merchant.commons.web.PageRequest;
import com.guhecloud.cube.deliver.project.merchant.commons.web.PageResponse;
import com.guhecloud.cube.deliver.project.merchant.dto.SensitiveWordDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.SensitiveWordExcelDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.SensitiveWord;
import com.guhecloud.cube.deliver.project.merchant.service.SensitiveWordService;
import com.guhecloud.cube.deliver.project.merchant.utils.SensitiveWordUtil;

import com.guhecloud.platform.base.standard.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 敏感词库 前端控制器
 * </p>
 *
 * @author bq
 * @since 2022-06-14
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SensitiveWordController {

    private final SensitiveWordService service;

    @GetMapping("/sensitive-word/test")
    public String test(@RequestParam("name") String name) {
        return SensitiveWordUtil.replaceSensitiveWord(name);
    }

    /**
     * 下载模板
     * @param request
     * @param response
     */
    @GetMapping("/sensitive-word/template-download")
    public void downloadTemplate(HttpServletRequest request, HttpServletResponse response) {
        // 构造区域数据
        ExcelUtils<SensitiveWordExcelDTO> utils = new ExcelUtils<>(SensitiveWordExcelDTO.class);
        utils.createWorkbook();
        XSSFSheet sheet = utils.createSheetTemplate("敏感词库");
        utils.downLoadExcel("敏感词导入模板", request, response);
    }

    /**
     * 批量导入
     * @param file
     * @return
     */
    @PostMapping("/sensitive-word/data-import")
    public List<ErrorExcelDTO> importData(@RequestParam("file") MultipartFile file) {
        ExcelUtils<SensitiveWordExcelDTO> utils = new ExcelUtils<>(SensitiveWordExcelDTO.class);
        List<SensitiveWordExcelDTO> dtos;
        try {
            dtos = utils.getDataByExcel(file.getInputStream(), "敏感词导入模板");
        } catch (Exception e) {
            if (ExcelUtils.CHECKDATA_ERROR.equals(e.getMessage())) {
                List<ExcelUtils.ExcelMsg> excelMsgs = utils.getExcelMsgs();
                if (excelMsgs != null) {
                    List<ErrorExcelDTO> collect = excelMsgs.stream().map(excelMsg -> new ErrorExcelDTO(excelMsg.getRowId(), excelMsg.getColName(), excelMsg.getMsg())).collect(Collectors.toList());
                    return collect;
                }
            }
            log.error("导入敏感词文件解析异常！", e);
            throw new ServiceException("文件解析异常！");
        }
        if (dtos == null) throw new ServiceException("excel没有数据");
        try {
            return service.importData(dtos);
        } catch (Exception e) {
            throw new ServiceException("文件解析异常！");
        }
    }

    /**
     * 分页查询
     * @param queryDTO
     */
    @GetMapping("/sensitive-word/page")
    public PageResponse<SensitiveWord> getPage(BaseQueryDTO queryDTO, PageRequest pageRequest) {
        Page<SensitiveWord> page = service.lambdaQuery()
                .like(StringUtils.isNotEmpty(queryDTO.getSearch()), SensitiveWord::getName, queryDTO.getSearch())
                .orderByAsc(SensitiveWord::getOrder)
                .page(pageRequest.page());
        return new PageResponse<>(page.getRecords(), page.getTotal(), page.getPages());
    }

    /**
     * 新增和修改
     * @param wordDTO
     */
    @PostMapping("/sensitive-word")
    public void saveOrUpdate(@RequestBody @Valid SensitiveWordDTO wordDTO) {
        service.saveOrUpdate(wordDTO);
    }

    /**
     * 删除
     * @param id
     */
    @DeleteMapping("/sensitive-word/{id}")
    public void remove(@PathVariable("id") String id) {
        service.removeById(id);
        service.updateSensitiveWordInit();
    }

    /**
     * 批量删除
     * @param batchDTO
     */
    @DeleteMapping("/sensitive-word/batch")
    public void removeBatch(@RequestBody BatchDTO batchDTO) {
        service.removeBatch(batchDTO);
    }

}


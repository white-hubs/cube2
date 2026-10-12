package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.controller;

import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.page.KeyPlacePageParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.query.KeyPlacesQueryParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.upsert.KeyPlaceUpsertParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.BusinessDisinfectionKeyPlaceService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.list.KeyPlaceInfoVO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.page.KeyPlacePageVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 消杀重点场所接口
 */
@RestController
@RequestMapping("/disinfection/key-places")
@RequiredArgsConstructor
@Slf4j
public class BusinessDisinfectionKeyPlaceController {
    private final BusinessDisinfectionKeyPlaceService businessKeyPlaceService;

    /**
     * 根据条件获取全部列表
     */
    @GetMapping("/list-all")
    public List<KeyPlaceInfoVO> listAll(KeyPlacesQueryParam queryParam) {
        return businessKeyPlaceService.listAll(queryParam);
    }

    /**
     * 新增项目重点场所
     */
    @PostMapping
    public Boolean insertKeyPlace(@Valid @RequestBody KeyPlaceUpsertParam param) {
        return businessKeyPlaceService.insertKeyPlace(param);
    }

    /**
     * 修改项目重点场所
     */
    @PutMapping("{id}")
    public Boolean updateKeyPlace(@Valid @RequestBody KeyPlaceUpsertParam param, @PathVariable Long id) {
        return businessKeyPlaceService.updateKeyPlace(param, id);

    }

    /**
     * 删除项目重点场所
     */
    @DeleteMapping
    public Boolean deleteKeyPlace(@RequestParam String ids) {
        return businessKeyPlaceService.deleteKeyPlace(ids);
    }

    /**
     * 分页项目重点场所
     */
    @GetMapping
    public Pagination<KeyPlacePageVO> listKeyPlaces(KeyPlacePageParam pageParam) {
        return businessKeyPlaceService.pageKeyPlaces(pageParam);
    }

    /**
     * 详情
     */
    @GetMapping("{id}")
    public KeyPlaceInfoVO detail(@PathVariable Long id) {
        return businessKeyPlaceService.detail(id);
    }

    /**
     * 模板下载
     */
    @GetMapping("/download/excel-template")
    public void downloadExcelTemplate(HttpServletRequest request, HttpServletResponse response) {
        businessKeyPlaceService.downloadExcelTemplate(request, response);
    }

    /**
     * 导入
     */
    @PostMapping("/import/excel")
    public Boolean importExcel(@RequestParam MultipartFile file) {
        return businessKeyPlaceService.importExcel(file);
    }

}

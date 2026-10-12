package com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.controller;

import com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.param.SurgeryItemParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.param.SurgeryItemQueryParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.service.SurgeryItemService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.vo.ImportResult;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.vo.ItemDevicePageVO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.vo.SurgeryItemVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 手术室物品
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SurgeryItemController {
    private final SurgeryItemService surgeryItemService;

    /**
     * 分页列表
     *
     * @param param 条件
     *
     * @return 分页列表
     */
    @GetMapping("/resources/item/page")
    public Pagination<SurgeryItemVO> getSurgeryItemPage(SurgeryItemQueryParam param) {
        return surgeryItemService.getSurgeryItemPage(param);
    }

    /**
     * 新增物品
     *
     * @param param 物品入参
     *
     * @return 主键
     */
    @PostMapping("/resources/item/add")
    public Long addSurgeryItem(@Valid @RequestBody SurgeryItemParam param) {
        Assert.isParameterError(param.getDeviceId() == null, "手术室不能为空！");
        return surgeryItemService.saveOrUpdateSurgeryItem(param);
    }

    /**
     * 新增/编辑物品
     *
     * @param param 物品入参
     *
     * @return 主键
     */
    @PutMapping("/resources/item/edit")
    public Long editSurgeryItem(@Valid @RequestBody SurgeryItemParam param) {
        Assert.isParameterError(param.getId() == null, "主键不能为空！");
        Assert.isParameterError(param.getDeviceId() == null, "手术室不能为空！");
        return surgeryItemService.saveOrUpdateSurgeryItem(param);
    }

    /**
     * 移除
     *
     * @param id 主键
     */
    @DeleteMapping("/resources/item/remove")
    public void removeSurgeryItem(Long id) {
        surgeryItemService.removeSurgeryItem(id);
    }

    /**
     * 查询物品详情分页列表
     *
     * @param param 条件
     *
     * @return 详情的分页列表
     */
    @GetMapping("/resources/item/detailPage")
    public Pagination<ItemDevicePageVO> getSurgeryItemDetailPage(SurgeryItemQueryParam param) {
        return surgeryItemService.getSurgeryItemDetailPage(param);
    }

    /**
     * 下载物品模板
     *
     * @param request  请求
     * @param response 响应
     */
    @GetMapping("/resources/item/template")
    void downloadTemplate(HttpServletRequest request, HttpServletResponse response) {
        surgeryItemService.downloadTemplate(request, response);
    }

    /**
     * 导入物品
     *
     * @param file 文件
     */
    @PostMapping("/resources/item/import")
    public ImportResult importTemplateMachines(@RequestParam("file") MultipartFile file) {
        return surgeryItemService.importTemplateItem(file);
    }

    /**
     * 下载物品详情
     *
     * @param param    条件
     * @param request  请求
     * @param response 响应
     */
    @GetMapping("/resources/item/downloadItemDetail")
    public void downloadItemDetail(SurgeryItemQueryParam param, HttpServletRequest request, HttpServletResponse response) {
        surgeryItemService.downloadItemDetail(param, request, response);
    }
}

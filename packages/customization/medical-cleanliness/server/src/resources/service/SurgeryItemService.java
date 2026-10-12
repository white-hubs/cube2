package com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.entity.SurgeryItem;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.param.SurgeryItemParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.param.SurgeryItemQueryParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.vo.ImportResult;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.vo.ItemDevicePageVO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.vo.SurgeryItemVO;
import com.guhecloud.platform.base.model.Pagination;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * （手术室）物品
 */
public interface SurgeryItemService extends IService<SurgeryItem> {
    /**
     * 分页列表
     *
     * @param param 条件
     *
     * @return 分页列表
     */
    Pagination<SurgeryItemVO> getSurgeryItemPage(SurgeryItemQueryParam param);

    /**
     * 新增/编辑物品
     *
     * @param param 物品入参
     *
     * @return 主键
     */
    Long saveOrUpdateSurgeryItem(SurgeryItemParam param);

    /**
     * 移除
     *
     * @param id 主键
     */
    void removeSurgeryItem(Long id);

    /**
     * 查询物品详情分页列表
     *
     * @param param 条件
     *
     * @return 详情的分页列表
     */
    Pagination<ItemDevicePageVO> getSurgeryItemDetailPage(SurgeryItemQueryParam param);

    /**
     * 下载物品模板
     *
     * @param request  请求
     * @param response 响应
     */
    void downloadTemplate(HttpServletRequest request, HttpServletResponse response);

    /**
     * 导入物品
     *
     * @param file 文件
     */
    ImportResult importTemplateItem(MultipartFile file);

    /**
     * 下载物品详情
     *
     * @param param    条件
     * @param request  请求
     * @param response 响应
     */
    void downloadItemDetail(SurgeryItemQueryParam param, HttpServletRequest request, HttpServletResponse response);
}

package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service;

import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.page.KeyPlacePageParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.query.KeyPlacesQueryParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.upsert.KeyPlaceUpsertParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.list.KeyPlaceInfoVO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.page.KeyPlacePageVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BusinessDisinfectionKeyPlaceService {

    /**
     * 获取项目下所有的重点区域
     */
    List<KeyPlaceInfoVO> listAll(KeyPlacesQueryParam queryParam);

    /**
     * 新增重点区域
     */
    boolean insertKeyPlace(@Valid KeyPlaceUpsertParam param);

    /**
     * 修改重点区域
     */
    boolean updateKeyPlace(@Valid KeyPlaceUpsertParam param, long id);

    /**
     * 删除重点区域
     */
    boolean deleteKeyPlace(String ids);

    /**
     * 获取重点区域详情
     */
    KeyPlaceInfoVO detail(Long id);

    /**
     * 分页
     */
    Pagination<KeyPlacePageVO> pageKeyPlaces(KeyPlacePageParam pageParam);

    /**
     * 全量查询
     *
     * @param pageParam
     *
     * @return
     */
    List<KeyPlacePageVO> listKeyPlacesListAll(KeyPlacePageParam pageParam);

    /**
     * 下载模板
     */
    void downloadExcelTemplate(HttpServletRequest request, HttpServletResponse response);

    /**
     * 导入
     */
    boolean importExcel(MultipartFile file);
}

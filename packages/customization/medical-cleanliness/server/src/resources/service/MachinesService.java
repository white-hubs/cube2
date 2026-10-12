package com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.entity.Machines;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.param.MachinesParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.param.MachinesQueryParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.vo.ImportResult;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.vo.MachinesRoomVO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.vo.MachinesVO;
import com.guhecloud.platform.base.model.Pagination;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 机组
 */
public interface MachinesService extends IService<Machines> {
    /**
     * 机组分页列表
     *
     * @param param 条件
     *
     * @return 分页列表
     */
    Pagination<MachinesVO> getMachinesPage(MachinesQueryParam param);

    /**
     * 新增或修改机组
     *
     * @param param 机组
     *
     * @return 主键
     */
    Long saveOrUpdateMachines(MachinesParam param);

    /**
     * 机组详情
     *
     * @param param 条件
     *
     * @return 详情
     */
    MachinesVO getMachinesDetail(MachinesParam param);

    /**
     * 移除
     *
     * @param id 主键
     */
    void removeMachines(Long id);

    /**
     * 机组手术室
     *
     * @param param 条件 机组id
     *
     * @return 手术室列表(机组下的)
     */
    Pagination<MachinesRoomVO> getMachinesRoomPage(MachinesParam param);

    /**
     * 编辑机组手术室
     *
     * @param param 入参
     */
    void saveOrUpdateMachinesRooms(MachinesParam param);

    /**
     * 移除机组手术室
     *
     * @param param 条件
     */
    void removeMachinesRooms(MachinesParam param);

    /**
     * 下载机组模板
     *
     * @param request  请求
     * @param response 响应
     */
    void downloadTemplate(HttpServletRequest request, HttpServletResponse response);

    ImportResult importTemplateMachines(MultipartFile file);
}

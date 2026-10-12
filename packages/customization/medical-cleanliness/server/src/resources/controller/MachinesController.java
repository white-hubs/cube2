package com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.controller;

import com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.param.MachinesParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.param.MachinesQueryParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.service.MachinesService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.vo.ImportResult;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.vo.MachinesRoomVO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.vo.MachinesVO;
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
 * 机组
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MachinesController {
    private final MachinesService machinesService;

    /**
     * 机组分页列表
     *
     * @param param 条件
     *
     * @return 分页列表
     */
    @GetMapping("/resources/machines/page")
    public Pagination<MachinesVO> getMachinesPage(@Valid MachinesQueryParam param) {
        return machinesService.getMachinesPage(param);
    }

    /**
     * 新增机组
     *
     * @param param 机组
     *
     * @return 主键
     */
    @PostMapping("/resources/machines/add")
    public Long addMachines(@Valid @RequestBody MachinesParam param) {
        return machinesService.saveOrUpdateMachines(param);
    }

    /**
     * 编辑机组
     *
     * @param param 机组
     *
     * @return 主键
     */
    @PutMapping("/resources/machines/edit")
    public Long editMachines(@Valid @RequestBody MachinesParam param) {
        Assert.isParameterError(param.getId() == null, "主键不能为空");
        return machinesService.saveOrUpdateMachines(param);
    }

    /**
     * 机组详情
     *
     * @param param 条件
     *
     * @return 详情
     */
    @GetMapping("/resources/machines/detail")
    public MachinesVO getMachinesDetail(MachinesParam param) {
        return machinesService.getMachinesDetail(param);
    }

    /**
     * 移除
     *
     * @param id 主键
     */
    @DeleteMapping("/resources/machines/remove")
    public void removeMachines(Long id) {
        machinesService.removeMachines(id);
    }

    /**
     * 机组手术室分页列表
     *
     * @param param 条件 机组id
     *
     * @return 手术室列表(机组下的)
     */
    @GetMapping("/resources/machines/room/page")
    public Pagination<MachinesRoomVO> getMachinesRoomPage(MachinesParam param) {
        return machinesService.getMachinesRoomPage(param);
    }

    @PutMapping("/resources/machines/room/edit")
    public void editMachinesRooms(@RequestBody MachinesParam param) {
        machinesService.saveOrUpdateMachinesRooms(param);
    }

    /**
     * 移除机组手术室
     *
     * @param param 条件
     */
    @DeleteMapping("/resources/machines/room/remove")
    public void removeMachinesRooms(MachinesParam param) {
        machinesService.removeMachinesRooms(param);
    }

    /**
     * 下载机组模板
     *
     * @param request  请求
     * @param response 响应
     */
    @GetMapping("/resources/machines/template")
    public void downloadTemplate(HttpServletRequest request, HttpServletResponse response) {
        machinesService.downloadTemplate(request, response);
    }

    /**
     * 导入机组
     *
     * @param file 文件
     */
    @PostMapping("/resources/machines/import")
    public ImportResult importTemplateMachines(@RequestParam("file") MultipartFile file) {
        return machinesService.importTemplateMachines(file);
    }
}

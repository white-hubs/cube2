package com.guhecloud.cube.biz.dispatch.server.controller;

import com.guhecloud.cube.biz.dispatch.server.param.DispatchGroupParam;
import com.guhecloud.cube.biz.dispatch.server.param.DispatchGroupQueryParam;
import com.guhecloud.cube.biz.dispatch.server.service.DispatchGroupService;
import com.guhecloud.cube.biz.dispatch.server.vo.DispatchGroupTreeVO;
import com.guhecloud.cube.biz.dispatch.server.vo.DispatchGroupVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.TreeUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Appet
 * @Description 调度设备-分组
 * @date 2022-07-26 10:08:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DispatchGroupController {
    private final DispatchGroupService dispatchDeviceGroupService;

    /**
     * 功能描述： 调度设备-分组添加
     */
    @PostMapping(value = "/dispatch-device-group")
    public String add(@Valid @RequestBody DispatchGroupParam param) {
        return dispatchDeviceGroupService.add(param);
    }

    /**
     * 通过 code 删除创建的组，包括第三方调度台的组
     *
     * @param code
     */
    @DeleteMapping(value = "/dispatch-device-group/code/{code}")
    public void deleteByCode(@PathVariable String code) {
        dispatchDeviceGroupService.deleteByCode(code);
    }

    /**
     * 人员离开组
     *
     * @param code
     */
    @PutMapping(value = "/dispatch-device-group/{code}/remove/member")
    public void removeMembersFromGroup(@PathVariable String code, @RequestParam List<String> deviceCodes) {
        dispatchDeviceGroupService.removeMembersFromGroup(code, deviceCodes);
    }

    /**
     * 人员加入组
     *
     * @param code
     */
    @PostMapping(value = "/dispatch-device-group/{code}/add/member")
    public void addMembersToGroup(@PathVariable String code, @RequestParam List<String> deviceCodes) {
        dispatchDeviceGroupService.addMembersToGroup(code, deviceCodes);
    }

    /**
     * 对比数据：现在没有的就删除；现在新增的就添加
     *
     * @param code
     */
    @PostMapping(value = "/dispatch-device-group/{code}/add-and-delete/member")
    public void addAndDeleteMembersToGroup(@PathVariable String code, @RequestParam List<String> deviceCodes) {
        dispatchDeviceGroupService.addAndDeleteMembersToGroup(code, deviceCodes);
    }

    /**
     * 功能描述： 调度设备-分组修改
     */
    @PutMapping(value = "/dispatch-device-group/{id}")
    public void update(@PathVariable String id, @Valid @RequestBody DispatchGroupParam param) {
        dispatchDeviceGroupService.update(id, param);
    }

    /**
     * 功能描述： 调度设备-分组删除
     */
    @DeleteMapping(value = "/dispatch-device-group/{id}")
    public void delete(@PathVariable String id) {
        dispatchDeviceGroupService.delete(id);
    }

    /**
     * 功能描述： 调度设备-分组批量删除
     */
    @DeleteMapping(value = "/dispatch-device-group/batchdelete")
    public void deleteBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") List<String> ids) {
        dispatchDeviceGroupService.batchDelete(ids);
    }

    /**
     * 功能描述： 调度设备-分组分页
     */
    @GetMapping(value = "/dispatch-device-group/page")
    public Pagination<DispatchGroupVO> getPage(DispatchGroupQueryParam param) {
        return dispatchDeviceGroupService.getPage(param);
    }

    /**
     * 功能描述：调度组列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping(value = "/dispatch-device-group/list")
    public List<DispatchGroupVO> getList(DispatchGroupQueryParam param) {
        return dispatchDeviceGroupService.getList(param);
    }

    /**
     * 功能描述：调度组-树
     * 只有组信息 没有设备信息
     *
     * @return
     */
    @GetMapping(value = "/dispatch-device-group/tree")
    public List<DispatchGroupTreeVO> getTree(DispatchGroupQueryParam param) {
        List<DispatchGroupVO> list = dispatchDeviceGroupService.getList(param);
        List<DispatchGroupTreeVO> datas = ObjectUtils.copying(list, DispatchGroupTreeVO.class);
        return TreeUtils.assembleTrees(datas);
    }

    /**
     * 功能描述：调度组-树
     * 组信息 带设备信息
     *
     * @return
     */
    @GetMapping(value = "/dispatch-device-group/tree/with-device")
    public List<DispatchGroupTreeVO> getTreeWithDevice(DispatchGroupQueryParam param) {
        param.setTenant(SessionContextHolder.getRequester().getTenant());
        List<DispatchGroupTreeVO> list = dispatchDeviceGroupService.getListWithDevice(param);
        return TreeUtils.assembleTrees(list);
    }

    /**
     * 功能描述：调度组-树
     * 组信息 带设备信息以及设备状态
     *
     * @return
     */
    @GetMapping(value = "/dispatch-device-group/tree/with-device-status")
    public List<DispatchGroupTreeVO> getTreeWithDeviceDetail(DispatchGroupQueryParam param) {
        param.setTenant(SessionContextHolder.getRequester().getTenant());
        List<DispatchGroupTreeVO> list = dispatchDeviceGroupService.getListWithDeviceDetail(param);
        List<DispatchGroupTreeVO> dispatchGroupTreeVOS = TreeUtils.assembleTrees(list);
        countLevelNodes(dispatchGroupTreeVOS, 0);
        return dispatchGroupTreeVOS;
    }

    /**
     * 递归统计分组下的人员数量
     *
     * @param treeList
     * @param preCount
     *
     * @return
     */
    private long countLevelNodes(List<DispatchGroupTreeVO> treeList, long preCount) {
        long curLevelCount = 0;
        for (DispatchGroupTreeVO tree : treeList) {
            if (!CollectionUtils.isEmpty(tree.getChildren()) && "group".equals(tree.getCategory())) {
                curLevelCount = tree.getChildren().stream().filter(t -> "device".equals(t.getCategory())).count();
                tree.setCount(countLevelNodes(tree.getChildren(), curLevelCount));
                preCount = preCount + curLevelCount;
            }
        }
        return preCount;
    }

    /**
     * 功能描述： 调度设备-分组详情
     */
    @GetMapping(value = "/dispatch-device-group/{id}")
    public DispatchGroupVO getById(@PathVariable String id) {
        return dispatchDeviceGroupService.getById(id);
    }

    /**
     * 同步第三方调度台的设备分组数据
     *
     * @param code
     *
     * @return
     */
    @PostMapping(value = "/dispatch-device-group/third/{code}/group")
    public void getGroupInThirdPlatform(@PathVariable String code) {
        dispatchDeviceGroupService.obtainThirdPlatformDevice(code);
    }

}
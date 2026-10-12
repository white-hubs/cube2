package com.guhecloud.cube.biz.dispatch.server.service;

import com.guhecloud.cube.biz.dispatch.server.model.DispatchCallQueryModel;
import com.guhecloud.cube.biz.dispatch.server.model.DispatchDeviceModel;
import com.guhecloud.cube.biz.dispatch.server.model.DispatchFileQueryModel;
import com.guhecloud.cube.biz.dispatch.server.model.DispatchGPSQueryModel;
import com.guhecloud.cube.biz.dispatch.server.model.DispatchGroupModel;
import com.guhecloud.cube.biz.dispatch.server.vo.DispatchCallRecordVO;
import com.guhecloud.cube.biz.dispatch.server.vo.DispatchCommonTypeVO;
import com.guhecloud.cube.biz.dispatch.server.vo.DispatchDeviceStatusVO;
import com.guhecloud.cube.biz.dispatch.server.vo.DispatchFileVO;
import com.guhecloud.cube.biz.dispatch.server.vo.DispatchGPSVO;
import com.guhecloud.cube.biz.dispatch.server.vo.DispatchNodeVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.Collections;
import java.util.List;

/**
 * 描述：调度服务
 *
 * @author Appet
 * @date 2022-07-26 10:08:36
 */
public interface DispatchService {
    //登陆 返回token
    String dispatchLogin(String userName, String password);

    //添加组
    DispatchGroupModel addGroup(DispatchGroupModel param);

    /**
     * 这是一个接口的默认实现；
     * 例如：普天的调度台，目前不需要修改组，就不用实现这个接口
     *
     * @return
     */
    default boolean updateGroup(DispatchGroupModel param) {
        return true;
    }

    //删除组
    boolean delGroup(String code);

    //添加设备
    DispatchDeviceModel addDevice(DispatchDeviceModel param);

    //修改设备
    boolean updateDevice(DispatchDeviceModel param);

    //删除设备
    boolean delDevice(String code);

    //组下添加设备
    boolean addDeviceToGroup(String groupCode, List<String> codes);

    //组下删除设备
    boolean delDeviceFromGroup(String groupCode, List<String> codes);

    //呼叫分页
    Pagination<DispatchCallRecordVO> callPage(DispatchCallQueryModel param);

    //历史轨迹
    List<DispatchGPSVO> gpsHistory(DispatchGPSQueryModel param);

    //历史轨迹分页
    Pagination<DispatchGPSVO> gpsHistoryPage(DispatchGPSQueryModel param);

    //通讯文件
    default List<DispatchFileVO> communicationFile(DispatchFileQueryModel param) {
        return Collections.emptyList();
    }

    default Pagination<DispatchFileVO> communicationFilePage(DispatchFileQueryModel param) {
        return Pagination.empty();
    }

    //获取设备当前状态:查询多个设备的状态
    List<DispatchDeviceStatusVO> deviceStatus(List<String> codes);

    //获取节点下的子对象 包括：节点、组、终端（用户）
    List<DispatchNodeVO> allSubNode(String code);

    //获取节点
    default List<DispatchNodeVO> allNode(String code) {
        return Collections.emptyList();
    }

    //获取组
    List<DispatchNodeVO> allGroup(String code);

    //获取终端（用户）
    List<DispatchNodeVO> allDevice(String code);

    //获取终端类型
    String getDeviceTypeName(String type);

    //获取组类型
    String getGroupTypeName(String type);

    //获取设备类型
    List<DispatchCommonTypeVO> getAllDeviceType();

    //获取组类型
    List<DispatchCommonTypeVO> getAllGroupType();

    default List<DispatchCommonTypeVO> getCallType() {
        return Collections.emptyList();
    }
}
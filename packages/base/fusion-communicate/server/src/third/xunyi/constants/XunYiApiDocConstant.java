package com.guhecloud.cube.biz.fusion.communicate.server.third.xunyi.constants;

public final class XunYiApiDocConstant {

    private XunYiApiDocConstant() {

    }

    public static final String ADD_GROUP_URI = "/group/add";//添加组
    public static final String UPDATE_GROUP_URI = "/group/modify";
    public static final String DEL_GROUP_URI = "/api/userManage/doDelGroup";//删除组
    public static final String ADD_DEVICE_URI = "/employee/add";//添加设备
    public static final String UPDATE_DEVICE_URI = "/employee/modify";//修改设备
    public static final String DEL_DEVICE_URI = "/doDelUserByEID";//删除设备
    //查询通话记录
    public static final String CALL_PAGE_URI = "/GetCallRecordByConditions";
    public static final String FILE_RECORD_LIST_URI = "/selectCallRecordByConditions";
    public static final String FILE_VIDEO_LIST_URI = "/selectCallRecordByConditions2";
    public static final String FILE_RECORD_BY_SID = "/GetCallRecordByCID";
    public static final String GPS_LIST_URI = "/api/gisDetail/page";
    public static final String DEVICE_STATUS_URI = "/GetAllOnlineUser2";
    public static final String ALL_GROUP_URI = "/GetGroupPage";//获取所有组
    public static final String ALL_DEVICE_URI = "/GetEmployeePage";//获取所有设备或用户
    public static final String ALL_DEVICE_IN_GROUP_URI = "/getGroupMember";//获取所有设备或用户
    public static final String DEVICE_JOIN_GROUP_URI = "/api/userManage/doAddGroupMember";
    public static final String DEVICE_LEAVE_GROUP_URI = "/api/userManage/batchRemoveGroupMembers";
    public static final String BATCH_SAVE_EMPLOYEE_INFO_URL = "/api/employee/batchSaveEmployeeInfo";//批量保存用户信息
}

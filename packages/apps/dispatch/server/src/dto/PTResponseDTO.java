package com.guhecloud.cube.biz.dispatch.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 普天调度台的 相关返回数据
 */
@Data
public class PTResponseDTO {
    private Integer code;
    private String msg;

    /**
     * 登陆 返回模型
     */
    @Data
    public static class LoginResponse extends PTResponseDTO {
        public DispatchLogin data;
    }

    @Data
    public static class DispatchLogin {
        //编号
        private String number;
        //名称
        private String name;
        //token
        private String token;
        //节点编号
        private String nodeNumber;
    }

    /**
     * 呼叫记录分页模型
     */
    @Data
    public static class CallPageResponse extends PTResponseDTO {
        public CallPage data;
    }

    @Data
    public static class CallPage {
        private long pages;//总页数
        private long size;//页大小
        private long total;//数据总数
        private long current;//当前页
        private List<CallRecord> records;//数据
    }

    @Data
    public static class CallRecord {
        private String callerNumber;//主叫号码
        private String callerName;//主叫名称
        private String calleeNumber;//被叫号码
        private String calleeName;//被叫名称
        private String callType;//呼叫类型
        private String callTypeName;//呼叫类型名称
        private String groupNumber;//组编号
        private String groupName;//组名称
        private String startTime;//开始时间
        private String endTime;//结束时间
        private String acceptTime;//接听时间
        private String callTime;//拨号时间
        private String callResult;//拨号结果
    }

    /**
     * 通讯文件 查询返回数据模型
     */
    @Data
    public static class FileListResponse extends PTResponseDTO {
        public List<FileRecord> data;
    }

    @Data
    public static class FileRecord {
        private String name;//上传文件的设备名称
        private String dataTime;//上传时间
        private String dataType;//数据类型
        private String dataSize;//数据大小
        private String dataLength;//数据长度
        private String dataFilename;//文件名称
        private String url;//文件路径
    }

    /**
     * GPS 数据模型
     */
    @Data
    public static class GPSResponse {
        private Integer errorCode;//错误代码
        private String errorDesc;//错误描述
        private String resultContent;//gps数据：json数组字符串
    }

    @Data
    public static class GPSData {
        private String dn;//设备编号
        private long clearTime;
        private long starttime;
        private long endtime;
        private Integer direction;//方向
        private double longiTude;//经度
        private double latiTude;//纬度
        private float height;//高度
        private float rout;//里程
        private float totalRout;//总里程
        private float speed;//速度
        private Integer status;//状态

    }

    /**
     * 当前设备状态模型
     */
    @Data
    public static class DeviceStatusResponse extends PTResponseDTO {
        public List<DeviceStatus> data;
    }

    @Data
    public static class DeviceStatus {
        private String number;//设备编号
        private double longitude;//经度
        private double latitude;//维度
        private Integer direction;//方向
        private float speed;//速度
        private String time;//时间
        private boolean onlineStatus;//在线状态
        private boolean videoStatus;//视频状态
    }

    /**
     * 节点数据模型：包括：节点、组、用户（终端）
     */
    @Data
    public static class NodeResponse extends PTResponseDTO {
        private List<Node> data;
    }

    @Data
    public static class Node {
        private String number;
        private String name;
        private String type;
        private String subType;
        private List<String> parentList;
        private List<String> childList;
    }

    /**
     * 调度台配置
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Config {
        //账号
        private String account;
        //密码
        private String password;
        //平台地址
        private String platformAddr;
        //根节点
        private String rootNode;
    }

}

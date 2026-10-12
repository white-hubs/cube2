package com.guhecloud.cube.biz.dispatch.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 讯一调度台的 相关返回数据
 */
@Data
public class XYResponseDTO {
    private Integer state;
    private Integer code;
    private String describe;
    private String msg;

    /**
     * 登陆 返回模型
     */
    @Data
    public static class LoginResponse extends XYResponseDTO {
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
    public static class CallPageResponse extends XYResponseDTO {
        private Long totalCount;
        private Long pages;
        private List<CallRecord> data;
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
        private String id;
        private String cid;//呼叫ID
        private String caller;//主叫号码
        private String callerName;//主叫名称
        private String called;//被叫号码
        private String calledName;//被叫名称
        private String createTime;//呼叫开始时间
        private String releaseTime;//呼叫结束时间
        private Long timeLength;//呼叫时长
        //1或12-单呼  2-组呼  3-通播  4-广播  5-会议  6-强插  7-强拆  8-监听	9-对讲10-转接  13-临时对讲  14-监控  15-视频会议  16-紧急呼叫  17-视	频转发19-视频回传
        private String callType;//呼叫类型
        private String callResult;//拨号结果(可选，1-已接	2-未接)
        private String releaseReassion;//通话结束原因
        private List<callMember> callMembers;//会议成员列表(非会议类型值为null)

    }

    @Data
    public static class callMember {
        private String number;
        private String name;
        private String createTime;
        private String releaseTime;
        private Long timeLength;
        private String callResult;
    }

    /**
     * 通讯文件 查询返回数据模型
     */
    @Data
    public static class FileListResponse extends XYResponseDTO {
        public FileData data;
    }

    @Data
    public static class FileData {
        private Long totalCount;
        private Long pages;
        public List<FileRecord> data;
    }

    @Data
    public static class FileRecord {
        private String id;    //自增标识
        private String cid;    //呼叫ID
        private String caller;//	：主叫号码
        private String called;    //：被叫号码
        private String createTime;//	：开始录音时间
        private String releaseTime;    //：录音结束时间
        private Long timeLength;    //：录音时长
        private String callType;    //  	：呼叫类型
        private String callResult;   //  	：呼叫结果
        private String releaseReassion;    //：通话结束原因
        private String callerName;        //：主叫名称
        private String calledName;     //：被叫名称
        private String callerDnsPrefix;    //：主叫域名前缀
        private String calledDnsPrefix;//	：被叫域名前缀
        private String groupCid;     // 	：总呼叫ID
        private String frontCid1;      //	：前一个落点呼叫ID
        private String frontCid2;      //	：前一个落点呼叫ID
        private String remark;       //	：备注
        private String recordName;    //：录音文件名称
        private String subPath;    //：录音文件存放子路径
        private String readTimes;    //：查听次数
        private String recordType;    // 	：呼叫类型
        private String members;        //	：会议成员
        private String web_PATH;        //	：Web访问地址
        private String fileType;        //：文件类型：0-音频文件，1-视频文件
    }

    /**
     * GPS 数据模型
     */
    @Data
    public static class GPSResponse extends XYResponseDTO {
        private GPSPageData data;
    }

    @Data
    public static class GPSPageData {
        private Long totalPages;
        private Long totalRows;
        private List<GPSData> dataList;
    }

    @Data
    public static class GPSData {
        private String id;
        private String employeeId;//设备编号
        private double longitude;//经度
        private double latitude;//纬度
        private String positionTime;
        private String address;
        private String direction;//方向
        private float speed;//速度

    }

    /**
     * 当前设备状态模型
     */
    @Data
    public static class DeviceStatusResponse {
        private List<DeviceStatus> Onlines;
    }

    @Data
    public static class DeviceStatus {
        private String employeeID;//设备编号
        private String employeeName;//设备名称
        private String type;//类型
        private String longitude;//经度
        private String latitude;//维度
        private String direct;//方向
        private String speed;//速度
        private String time;//时间
    }

    /**
     * 节点数据模型：包括：节点、组、用户（终端）
     */
    @Data
    public static class NodeResponse {
        private String rootGroupNum;
        private List<Node> list;
    }

    @Data
    public static class DeviceResponse {
        private DevicePage page;
    }

    @Data
    public static class DeviceInGroupResponse {
        private DeviceInGroupPage page;
    }

    @Data
    public static class DevicePage {
        private List<Device> dataList;
    }

    @Data
    public static class DeviceInGroupPage {
        private List<DeviceInGroup> dataList;
    }

    @Data
    public static class DeviceCommon {
        private String groupNum;
        private String password;
        private Integer employeeLevel;
        private String name;
        private String type;
        private String remark;
        private Integer isRecord;
        private Integer isVideo;
    }

    @Data
    public static class Device extends DeviceCommon {
        private String employeeId;
    }

    @Data
    public static class DeviceInGroup extends DeviceCommon {
        private String employeeID;
    }

    @Data
    public static class Node {
        private String parentGroupNum;
        private String groupNum;
        private String groupName;
        private String type;
        private String subType;
        private Integer seq;
        private Integer iSRecord;
        private Integer isVideo;
        private Integer groupLevel;
        private Integer intercomIdleTimeOut;
        private Integer intercomPowerIdleTimeOut;
        private Integer intercomPowerTimeCount;
        private Integer queueMaxNum;
        private Integer intercomHeartBeatSpace;
        private Integer intercomStateSpace;
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
        //文件访问地址
        private String fileUrl;
    }

}

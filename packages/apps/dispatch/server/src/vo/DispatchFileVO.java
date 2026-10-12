package com.guhecloud.cube.biz.dispatch.server.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author Appet
 * @Description 通话记录分页
 * @date 2022-07-26 10:08:36
 */
@Data
@Builder
public class DispatchFileVO {
    private String name;//上传文件的设备名称或设备持有人姓名
    private String time;//上传时间
    private String type;//数据类型 视频 还是 语音
    private String size;//数据大小
    private String length;//数据长度
    private String suffix;//文件后缀
    private String url;//文件路径
    private String uploadUserName;//上传文件的用户名或设备名
    //主叫
    private String caller;
    private String callerName;
    //被叫
    private String callee;
    private String calleeName;
    //呼叫类型
    private String callType;
    //时长 秒
    private Long fileTimeLength;
    //创建时间
    private String fileCreateTime;
    private String fileEndTime;
    //其他信息 留着业务方自行存储，简单字符 也可以是json字符串
    private String other;
}
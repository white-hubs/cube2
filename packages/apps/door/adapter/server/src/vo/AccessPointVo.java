package com.guhecloud.cube.srv.gen.adapter.access.control.server.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessPointVo {
    //资源唯一编码
    private String indexCode;
    //资源类型
    private String resourceType;
    //资源名称
    private String name;
    //门禁点编号
    private String doorNo;
    //通道号
    private String channelNo;
    //门序号
    private Integer doorSerial;
    //所属区域
    private String regionIndexCode;
    //区域名称
    private String regionName;
    //创建时间
    private String createTime;
    //通道类型
    private String channelType;
}

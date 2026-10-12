package com.guhecloud.cube.srv.gen.conferencemanage.server.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import java.util.List;

@Data
public class AppointmentParam extends Paging {
    //关键字
    private String keyword;
    //状态
    private String status;
    //日期
    private String date;
    //过期时间
    private List<String> passTimes;
    //会议室id
    private Long conferenceId;

    //人员过滤参数
    private String userId;
    private List<String> organizationIdList;
    private List<String> roleIdList;
}

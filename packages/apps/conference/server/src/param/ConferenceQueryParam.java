package com.guhecloud.cube.srv.gen.conferencemanage.server.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

@Data
public class ConferenceQueryParam extends Paging {
    //会议室名称
    private String name;

    //区域名称
    private String areaName;
}

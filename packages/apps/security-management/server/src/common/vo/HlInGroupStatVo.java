package com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 回路组下的回路统计
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HlInGroupStatVo {
    //组名称
    private String groupName;
    //总回路数
    private Integer total;
    //组下回路总数
    private Integer totalOnGroup;

    private List<CommonStat> detailStat;

    public HlInGroupStatVo(String groupName, List<CommonStat> detailStat) {
        this.groupName = groupName;
        this.detailStat = detailStat;
    }

    public HlInGroupStatVo(Integer totalOnGroup, List<CommonStat> detailStat) {
        this.totalOnGroup = totalOnGroup;
        this.detailStat = detailStat;
    }
}

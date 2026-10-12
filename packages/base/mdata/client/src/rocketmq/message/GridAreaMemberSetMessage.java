package com.guhecloud.cube.biz.mdata.client.rocketmq.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GridAreaMemberSetMessage {
    public static final String TOPIC = "grid-area-member-set";

    private Integer tenant;

    private String dailyType;

    private Long gridAreaId;

    private Set<Long> memberIds;
}

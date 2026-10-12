package com.guhecloud.cube.deliver.project.merchant.dto.store.stat;

import lombok.EqualsAndHashCode;

import java.util.List;

@lombok.Data
@EqualsAndHashCode(callSuper = false)
public class StallScoreStatOverviewDTO {

    private String id;

    private String name;

    private String type;

    private int score;

    private int count;

    private int radio;

    private List<StallScoreStatOverviewDTO> manageTop10;

    private List<StallScoreStatOverviewDTO> qualityTop10;

    private List<StallScoreStatOverviewDTO> honestyTop10;

    private List<StallScoreStatOverviewDTO> attitudeTop10;

    private List<StallScoreStatOverviewDTO> envTop10;

    private List<String> tags;

}

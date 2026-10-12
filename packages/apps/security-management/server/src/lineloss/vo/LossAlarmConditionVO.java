package com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.vo;

import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.entity.LineLossConnection;
import lombok.Data;

import java.util.List;

@Data
public class LossAlarmConditionVO {
    private String condition;
    private List<Long> lineIdList;
    private List<LineLossConnection> connectionList;
}

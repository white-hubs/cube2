package com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.vo;

import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.entity.LineLossConnection;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.entity.LineLossNode;
import lombok.Data;

import java.util.List;

@Data
public class ElectricitySupplyChangeVO {
    private List<Long> nodeInserIdList;
    private List<Long> nodeDeleteIdList;
    private List<LineLossNode> nodeInsertList;

    private List<Long> connectionInserIdList;
    private List<Long> connectionDeleteIdList;
    private List<LineLossConnection> connectionInsertList;
    private List<LineLossConnection> connectionUpdateList;
}

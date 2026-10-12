package com.guhecloud.cube.biz.mdata.server.stall.param;

import com.guhecloud.cube.biz.mdata.server.stall.dto.DbcStallDTO;
import lombok.Data;

import java.util.List;

/**
 * 挡位批量新增
 */
@Data
public class StallCreateBathParam {
    List<DbcStallDTO> stallList;
}

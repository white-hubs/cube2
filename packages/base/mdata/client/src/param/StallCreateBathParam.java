package com.guhecloud.cube.biz.mdata.client.param;

import com.guhecloud.cube.biz.mdata.client.dto.DbcStallDTO;
import lombok.Data;

import java.util.List;

/**
 * 挡位批量新增
 */
@Data
public class StallCreateBathParam {
    List<DbcStallDTO> stallList;
}

package com.guhecloud.cube.srv.gen.cabbage.server.service;

import com.guhecloud.cube.srv.gen.cabbage.server.vo.dto.DbcGoodsCategoryDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface DbcGoodsCategoryService {
    void dbcGoodsCategoryPull(Integer marketId, Integer tenant, LocalDateTime newTime, String username,
                              List<DbcGoodsCategoryDTO> categoryDTOList
                             );
}

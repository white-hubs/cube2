package com.guhecloud.cube.biz.meattrade.server.pork.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkArrangeDetailLog;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PorkArrangeDetailLogMapper extends BaseMapper<PorkArrangeDetailLog> {

    List<PorkArrangeDetailLog> selectNonTransferFiles(@Param("querySize") Integer querySize,
                                                      @Param("transferNumber") Integer transferNumber
                                                     );

    int updateTransferFile(@Param("id") Long id, @Param("transferStatus") Boolean transferStatus,
                           @Param("transferNumber") Integer transferNumber,
                           @Param("addNumber") Integer addNumber,
                           @Param("transferIds") String transferIds
                          );
}

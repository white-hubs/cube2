package com.guhecloud.cube.srv.gen.visitor.management.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.visitor.management.server.po.BlacklistPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 黑名单
 */
@Mapper
public interface BlacklistMapper extends BaseMapper<BlacklistPO> {

    /**
     * 获取新的的三方code
     * 最大的code + 1
     *
     * @return 新的三方code
     */
    @Select("select ifnull(max(`third_user_code`),0) + 1 as `maxCode` from blacklist")
    Integer getThirdUserCode();
}

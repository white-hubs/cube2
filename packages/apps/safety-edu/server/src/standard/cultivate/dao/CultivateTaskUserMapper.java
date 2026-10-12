package com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.entity.CultivateTaskUser;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.vo.CultivateTaskWithStatVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * 描述：培训人员 Repository接口
 *
 * @author Appet
 * @date 2023-04-18 15:08:36
 */
@Repository
public interface CultivateTaskUserMapper extends BaseMapper<CultivateTaskUser> {

    @Select("select \n" +
            "count(id) as trainNum,\n" +
            "sum(IF(user_sign_status = 'SIGNED',1,0)) AS signedNum,\n" +
            "sum(IF(user_sign_status = 'UNSIGNED',1,0)) AS unsignedNum,\n" +
            "sum(IF(belated = 1,1,0)) AS belatedNum\n" +
            "from cultivate_task_user t\n" +
            "where t.cultivate_task_id = #{id}"
    )
    CultivateTaskWithStatVO getTrainStat(@Param("id") Long id);

}
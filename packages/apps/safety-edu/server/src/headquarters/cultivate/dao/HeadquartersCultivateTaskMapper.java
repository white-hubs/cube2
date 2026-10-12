package com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.entity.HeadquartersCultivateTask;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.vo.CultivateUserSignRecordExportVO;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.vo.CultivateUserSignRecordVO;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.param.CultivateTaskUserRecordQueryParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 培训任务 Repository接口
 *
 * @author lanchun
 * @date 2024/4/10 14:16
 */
@Repository
public interface HeadquartersCultivateTaskMapper extends BaseMapper<HeadquartersCultivateTask> {

    Page<CultivateUserSignRecordVO> getUserSignRecordPage(Page page, @Param("param") CultivateTaskUserRecordQueryParam param);

    List<CultivateUserSignRecordExportVO> getUserSignRecordExport(@Param("param") CultivateTaskUserRecordQueryParam param);
}
package com.guhecloud.cube.srv.gen.maintain.server.electron.controller.assembly;//package com.guhecloud.cube.srv.gen.maintain.electron.server.controller.assembly;

import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.PatrolLocationVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 巡更点配置公共接口
 *
 * @author lanchun
 * @date 2025/1/3 14:51
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PublicPatrolLocationController {

    private final PatrolLocationController patrolLocationController;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     *
     * @return nfc信息
     */
    @GetMapping("/public/patrol/location/{id}")
    public PatrolLocationVO queryById(@PathVariable("id") Long id) {
        return patrolLocationController.queryById(id);
    }
}

package com.guhecloud.cube.srv.gen.official.management.server.interfaces;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.official.management.server.commandgateway.OfficialSealCommandService;
import com.guhecloud.cube.srv.gen.official.management.server.commandgateway.command.OfficialSealCommand;
import com.guhecloud.cube.srv.gen.official.management.server.interfaces.vo.OfficialSealVO;
import com.guhecloud.cube.srv.gen.official.management.server.po.OfficialSealPO;
import com.guhecloud.cube.srv.gen.official.management.server.querygateway.OfficialSealQueryService;
import com.guhecloud.cube.srv.gen.official.management.server.querygateway.query.OfficialSealParam;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vo.FormFieldVO;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 印章统计
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OfficialSealController {
    private final OfficialSealQueryService sealQueryService;
    private final OfficialSealCommandService sealCommandService;

    /**
     * 新增印章
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/officialSeal/add")
    public Long saveOfficialSeal(@Valid @RequestBody OfficialSealCommand param) {
        return sealCommandService.addOfficialSeal(param);
    }

    /**
     * 修改印章
     *
     * @param id
     * @param param
     */
    @PutMapping("/officialSeal/{id}/update")
    public void updateOfficialSeal(@PathVariable Long id, @RequestBody OfficialSealCommand param) {
        sealCommandService.updateOfficialSeal(id, param);
    }

    /**
     * 删除印章
     *
     * @param id
     */
    @DeleteMapping("/officialSeal/{id}")
    public void deleteOfficialSeal(@PathVariable Long id) {
        sealCommandService.deleteOfficialSeal(id);
    }

    /**
     * 印章分页查询
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/officialSeal/page")
    public Pagination<OfficialSealVO> pageSeal(OfficialSealParam param) {
        Page<OfficialSealPO> page = sealQueryService.pageOfficialSeal(param);
        return PagingUtils.convert(page, OfficialSealVO.class);
    }

    @GetMapping("/officialSeal/widget/list")
    public List<FormFieldVO> listSealForWeight(OfficialSealParam param) {
        List<OfficialSealPO> list = sealQueryService.listOfficialSeal(param);
        if (!CollectionUtils.isEmpty(list)) {
            return list.stream().map(d -> FormFieldVO.builder().id(d.getId().toString()).name(d.getSealName()).build())
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}

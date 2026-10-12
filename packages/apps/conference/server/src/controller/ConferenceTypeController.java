package com.guhecloud.cube.srv.gen.conferencemanage.server.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.conferencemanage.server.entity.ConferenceType;
import com.guhecloud.cube.srv.gen.conferencemanage.server.param.ConferenceBaseQueryParam;
import com.guhecloud.cube.srv.gen.conferencemanage.server.service.ConferenceTypeService;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.ConferenceBaseEntityVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
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

import java.util.List;
import java.util.Objects;

/**
 * 会议类型
 */

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ConferenceTypeController {
    private final ConferenceTypeService conferenceTypeService;

    /**
     * 新增
     *
     * @param param
     */
    @PostMapping("/add-conference-type")
    public void add(@RequestBody ConferenceBaseQueryParam param) {
        ConferenceType one = conferenceTypeService.getOne(Wrappers.<ConferenceType>lambdaQuery()
                                                                  .eq(ConferenceType::getName, param.getName()));
        Assert.isConflict(Objects.nonNull(one), "该类型已添加");
        ConferenceType copying = ObjectUtils.copying(param, ConferenceType.class);
        conferenceTypeService.save(copying);
    }

    /**
     * 修改
     *
     * @param id
     * @param param
     */
    @PutMapping("/update-conference-type/{id}")
    public void add(@PathVariable Long id, @RequestBody ConferenceBaseQueryParam param) {

        ConferenceType byId = conferenceTypeService.getById(id);
        ObjectUtils.copying(param, byId);
        conferenceTypeService.updateById(byId);
    }

    /**
     * 删除
     *
     * @param id
     */
    @DeleteMapping("/delete-conference-type/{id}")
    public void add(@PathVariable Long id) {
        DatabaseContextHolder.softDelete(conferenceTypeService, id);
    }

    /**
     * 查询分页
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/conference-type-page")
    public Pagination<ConferenceBaseEntityVO> getConferenceTypeVosPage(ConferenceBaseQueryParam param) {
        Page<ConferenceType> page = conferenceTypeService.page(PagingUtils.convert(param), Wrappers.<ConferenceType>lambdaQuery()
                .like(StringUtils.isNotEmpty(
                              param.getKeyword()),
                      ConferenceType::getName,
                      param.getKeyword()));
        return PagingUtils.convert(page, ConferenceBaseEntityVO.class);
    }

    /**
     * 查询列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/conference-type-list")
    public List<ConferenceBaseEntityVO> getConferenceTypeVos(ConferenceBaseQueryParam param) {
        List<ConferenceType> list = conferenceTypeService.list(Wrappers.<ConferenceType>lambdaQuery()
                                                                       .eq(ConferenceType::getStatus, true)
                                                                       .like(StringUtils.isNotEmpty(param.getKeyword()),
                                                                             ConferenceType::getName, param.getKeyword()));
        return ObjectUtils.copying(list, ConferenceBaseEntityVO.class);
    }

}

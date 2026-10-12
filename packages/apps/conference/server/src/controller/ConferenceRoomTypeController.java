package com.guhecloud.cube.srv.gen.conferencemanage.server.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.conferencemanage.server.entity.ConferenceRoomType;
import com.guhecloud.cube.srv.gen.conferencemanage.server.param.ConferenceBaseParam;
import com.guhecloud.cube.srv.gen.conferencemanage.server.param.ConferenceBaseQueryParam;
import com.guhecloud.cube.srv.gen.conferencemanage.server.service.ConferenceRoomTypeService;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.ConferenceBaseEntityVO;
import com.guhecloud.platform.base.model.Pagination;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 会议室类型
 *
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/4/7 10:18
 */

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/conference-room/type")
public class ConferenceRoomTypeController {
    private final ConferenceRoomTypeService conferenceRoomTypeService;

    /**
     * 新增或更新
     *
     * @param param
     */
    @PostMapping("/save-or-update")
    public Boolean saveOrUpdate(@RequestBody ConferenceBaseParam param) {
        return conferenceRoomTypeService.saveOrUpdate(param);
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    public Boolean add(@PathVariable Long id) {
        return DatabaseContextHolder.softDelete(conferenceRoomTypeService, id);
    }

    /**
     * 会议室类型-分页
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/conference-type-page")
    public Pagination<ConferenceBaseEntityVO> getConferenceTypeVosPage(ConferenceBaseQueryParam param) {
        Page<ConferenceRoomType> page = conferenceRoomTypeService.page(PagingUtils.convert(param), Wrappers
                .<ConferenceRoomType>lambdaQuery()
                .like(StringUtils.isNotEmpty(
                              param.getKeyword()),
                      ConferenceRoomType::getName,
                      param.getKeyword()));
        return PagingUtils.convert(page, ConferenceBaseEntityVO.class);
    }

    /**
     * 会议室类型-列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/conference-type-list")
    public List<ConferenceBaseEntityVO> getConferenceTypeVos(ConferenceBaseQueryParam param) {
        List<ConferenceRoomType> list = conferenceRoomTypeService.list(Wrappers.<ConferenceRoomType>lambdaQuery()
                                                                               .like(StringUtils.isNotEmpty(param.getKeyword()),
                                                                                     ConferenceRoomType::getName, param.getKeyword())
                                                                               .eq(ConferenceRoomType::getStatus, true)
                                                                      );
        return ObjectUtils.copying(list, ConferenceBaseEntityVO.class);
    }

}

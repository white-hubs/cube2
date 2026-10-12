package com.guhecloud.cube.srv.gen.content.management.server.technicalnotice.interfaces;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.content.management.server.noticeconfig.interfaces.vo.NoticeConfigVO;
import com.guhecloud.cube.srv.gen.content.management.server.noticeconfig.po.NoticeConfig;
import com.guhecloud.cube.srv.gen.content.management.server.noticeconfig.querygateway.NoticeConfigQueryService;
import com.guhecloud.cube.srv.gen.content.management.server.technicalnotice.commandgateway.TechnicalNoticeCommandService;
import com.guhecloud.cube.srv.gen.content.management.server.technicalnotice.commandgateway.command.SubmitTechnicalNoticeCommand;
import com.guhecloud.cube.srv.gen.content.management.server.technicalnotice.interfaces.vo.TechnicalNoticeVO;
import com.guhecloud.cube.srv.gen.content.management.server.technicalnotice.po.TechnicalNotice;
import com.guhecloud.cube.srv.gen.content.management.server.technicalnotice.querygateway.TechnicalNoticeQueryService;
import com.guhecloud.cube.srv.gen.content.management.server.technicalnotice.querygateway.query.TechnicalNoticeCollectionQuery;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lzy
 * @date 2022/5/20
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TechnicalNoticeController {

    private final TechnicalNoticeCommandService technicalNoticeCommandService;
    private final TechnicalNoticeQueryService technicalNoticeQueryService;
    private final NoticeConfigQueryService noticeConfigQueryService;

    /**
     * 技术通报新增
     *
     * @param command 新增参数
     */
    @PostMapping("/technical-notice")
    public void save(@Valid @RequestBody SubmitTechnicalNoticeCommand command) {
        this.technicalNoticeCommandService.save(command);
    }

    /**
     * 技术通报删除
     *
     * @param id 主键
     */
    @DeleteMapping("/technical-notice/{id}")
    public void remove(@PathVariable("id") Long id) {
        this.technicalNoticeCommandService.remove(id);
    }

    /**
     * 技术通报详情
     *
     * @param id 主键
     *
     * @return 技术通报信息
     */
    @GetMapping("/technical-notice/{id}")
    public TechnicalNoticeVO getById(@PathVariable("id") Long id) {
        // 获取信息
        TechnicalNotice technicalNotice = this.technicalNoticeQueryService.getById(id);
        Assert.isNotFound(technicalNotice, "不存在技术通报信息!");

        // 获取通知人员
        List<NoticeConfig> noticeConfigs = this.noticeConfigQueryService.listByBusinessKey(id);

        // 构建信息
        TechnicalNoticeVO record = ObjectUtils.copying(technicalNotice, TechnicalNoticeVO.class);
        record.setNoticeConfigs(this.buildNoticeConfigInfo(noticeConfigs));
        return record;
    }

    /**
     * 技术通报分页
     *
     * @param query 查询参数
     *
     * @return 技术通报信息
     */
    @GetMapping("/technical-notice/page")
    public Pagination<TechnicalNoticeVO> pageTechnicalNotice(TechnicalNoticeCollectionQuery query) {
        // 查询
        Page<TechnicalNotice> page = this.technicalNoticeQueryService.pageTechnicalNotice(query);
        return PagingUtils.convert(page, TechnicalNoticeVO.class);
    }

    private List<NoticeConfigVO> buildNoticeConfigInfo(List<NoticeConfig> noticeConfigs) {
        return noticeConfigs.stream().map(noticeConfig -> {
            NoticeConfigVO noticeConfigVO = ObjectUtils.copying(noticeConfig, NoticeConfigVO.class);
            noticeConfigVO.setUserIds(StringUtils.isEmpty(noticeConfig.getUser()) ? Collections.emptyList() :
                                      Arrays.stream(noticeConfig.getUser().split("-"))
                                              .map(t -> NoticeConfigVO.InfoDTO.builder().id(Long.parseLong(t)).build())
                                              .collect(Collectors.toList()));
            noticeConfigVO.setDeptIds(StringUtils.isEmpty(noticeConfig.getDepartment()) ? Collections.emptyList() :
                                      Arrays.stream(noticeConfig.getDepartment().split("-"))
                                              .map(t -> NoticeConfigVO.InfoDTO.builder().id(Long.parseLong(t)).build())
                                              .collect(Collectors.toList()));
            noticeConfigVO.setPostIds(StringUtils.isEmpty(noticeConfig.getPost()) ? Collections.emptyList() :
                                      Arrays.stream(noticeConfig.getPost().split("-"))
                                              .map(t -> NoticeConfigVO.InfoDTO.builder().id(Long.parseLong(t)).build())
                                              .collect(Collectors.toList()));
            return noticeConfigVO;
        }).collect(Collectors.toList());
    }
}

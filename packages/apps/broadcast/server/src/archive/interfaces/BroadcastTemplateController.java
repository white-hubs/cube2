package com.guhecloud.cube.biz.subsystem.broadcast.server.archive.interfaces;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.application.BroadcastTemplateService;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.po.BroadcastTemplatePO;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.interfaces.vo.BroadcastTemplatePageVO;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.interfaces.vo.BroadcastTemplateVO;
import com.guhecloud.cube.biz.subsystem.broadcast.server.param.BroadcastTemplateModifyParam;
import com.guhecloud.cube.biz.subsystem.broadcast.server.param.BroadcastTemplateQueryPageParam;
import com.guhecloud.cube.biz.subsystem.broadcast.server.param.BroadcastTemplateQueryParam;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
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
 * 广播模板相关接口
 *
 * @author lanchun
 * @date 2022/12/12 14:53
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BroadcastTemplateController {

    private final BroadcastTemplateService broadcastTemplateService;

    private final UserClient userClient;

    /**
     * 广播模板分页列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/broadcast-template/page")
    public Pagination<BroadcastTemplatePageVO> getPage(@Valid BroadcastTemplateQueryPageParam param) {
        Page<BroadcastTemplatePO> pageVOPage = this.broadcastTemplateService.page(PagingUtils.convert(param),
                                                                                  Wrappers.<BroadcastTemplatePO>lambdaQuery()
                                                                                          .like(StringUtils.isNotEmpty(param.getKeyword()), BroadcastTemplatePO::getName, param.getKeyword())
                                                                                          .orderByDesc(BroadcastTemplatePO::getCreateTime));
        List<BroadcastTemplatePageVO> broadcastTemplatePageVOS = ObjectUtils.copying(pageVOPage.getRecords(), BroadcastTemplatePageVO.class);
        if (CollectionUtils.isEmpty(broadcastTemplatePageVOS)) {
            return Pagination.empty();
        }
        broadcastTemplatePageVOS.forEach(broadcastTemplatePageVO -> {
            if (!broadcastTemplatePageVO.getDefaulted()) {
                UserDTO userDTO = this.userClient.getUserByUserName(broadcastTemplatePageVO.getUpdateBy());
                if (Objects.nonNull(userDTO)) {
                    broadcastTemplatePageVO.setUpdateByName(userDTO.getName());
                }
            }
        });
        return PagingUtils.convert(pageVOPage, broadcastTemplatePageVOS);
    }

    /**
     * 广播模板列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/broadcast-templates")
    public List<BroadcastTemplateVO> getBroadcastTemplates(@Valid BroadcastTemplateQueryParam param) {
        List<BroadcastTemplatePO> broadcastTemplatePOS = this.broadcastTemplateService.list(Wrappers.<BroadcastTemplatePO>lambdaQuery()
                                                                                                    .like(StringUtils.isNotEmpty(param.getKeyword()), BroadcastTemplatePO::getName, param.getKeyword()));
        return ObjectUtils.copying(broadcastTemplatePOS, BroadcastTemplateVO.class);
    }

    /**
     * 广播模板新增
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/broadcast-template")
    public Long save(@RequestBody @Valid BroadcastTemplateModifyParam param) {
        long count = this.broadcastTemplateService.count();
        if (count >= 20) {
            throw new ServiceException("模板数量不能大于20个");
        }
        BroadcastTemplatePO broadcastTemplatePO = ObjectUtils.copying(param, BroadcastTemplatePO.class);
        broadcastTemplatePO.setDefaulted(false);
        this.broadcastTemplateService.save(broadcastTemplatePO);
        return broadcastTemplatePO.getId();
    }

    /**
     * 广播模板修改
     *
     * @param id
     * @param param
     */
    @PutMapping("/broadcast-template/{id}")
    public void update(@PathVariable("id") @Min(value = 1, message = "主键不能小于1") Long id,
                       @RequestBody @Valid BroadcastTemplateModifyParam param
                      ) {
        BroadcastTemplatePO broadcastTemplatePO = this.broadcastTemplateService.getById(id);
        Assert.isNotFound(broadcastTemplatePO, "模板信息不存在或已被删除");
        if (broadcastTemplatePO.getDefaulted()) {
            //内置模板只修改内容
            broadcastTemplatePO.setContent(param.getContent());
        } else {
            ObjectUtils.copying(param, broadcastTemplatePO);
        }
        this.broadcastTemplateService.updateById(broadcastTemplatePO);
    }

    /**
     * 广播模板删除
     *
     * @param id
     */
    @DeleteMapping("/broadcast-template/{id}")
    public void delete(@PathVariable("id") @Min(value = 1, message = "主键不能小于1") Long id) {
        BroadcastTemplatePO broadcastTemplatePO = this.broadcastTemplateService.getById(id);
        if (Objects.isNull(broadcastTemplatePO)) {
            return;
        }
        Assert.isNotAcceptable(broadcastTemplatePO.getDefaulted(), "内置模板不允许删除");
        DatabaseContextHolder.softDelete(this.broadcastTemplateService, id);
    }
}

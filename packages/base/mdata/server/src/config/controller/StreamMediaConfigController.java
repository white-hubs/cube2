package com.guhecloud.cube.biz.mdata.server.config.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.mdata.server.config.entity.StreamMediaConfig;
import com.guhecloud.cube.biz.mdata.server.config.param.StreamMediaConfigModifyParam;
import com.guhecloud.cube.biz.mdata.server.config.param.StreamMediaConfigQueryParam;
import com.guhecloud.cube.biz.mdata.server.config.param.StreamMediaConfigUpdateParam;
import com.guhecloud.cube.biz.mdata.server.config.service.StreamMediaConfigService;
import com.guhecloud.cube.biz.mdata.server.config.vo.StreamMediaConfigVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
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

import java.util.Collections;
import java.util.List;

/**
 * 流媒体服务器配置接口
 *
 * @author lanchun
 * @date 2021/12/7 15:17
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StreamMediaConfigController {

    private final StreamMediaConfigService streamMediaConfigService;

    /**
     * 流媒体服务器配置新增
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/stream-media-config")
    public Long save(@Valid @RequestBody StreamMediaConfigModifyParam param) {
        return this.streamMediaConfigService.save(param);
    }

    /**
     * 流媒体服务器配置修改
     *
     * @param id
     * @param param
     */
    @PutMapping("/stream-media-config/{id}")
    public void update(@PathVariable("id") @Min(value = 1, message = "配置主键不能小于1") Long id,
                       @Valid @RequestBody StreamMediaConfigUpdateParam param
                      ) {
        this.streamMediaConfigService.update(id, param);
    }

    /**
     * 流媒体服务器配置分页列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/stream-media-configs")
    public Pagination<StreamMediaConfigVO> listStreamMediaConfigs(@Valid StreamMediaConfigQueryParam param) {
        Page<StreamMediaConfig> page = this.streamMediaConfigService.listStreamMediaConfigs(param);
        List<StreamMediaConfigVO> pageVOS = ObjectUtils.copying(page.getRecords(), StreamMediaConfigVO.class);
        if (CollectionUtils.isEmpty(pageVOS)) {
            return PagingUtils.convert(page, Collections.emptyList());
        }
        return PagingUtils.convert(page, pageVOS);
    }

    /**
     * 流媒体服务器配置详情
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/stream-media-config/{id}")
    public StreamMediaConfigVO getStreamMediaConfig(@PathVariable("id") @Min(value = 1, message = "配置主键不能小于1") Long id) {
        StreamMediaConfig streamMediaConfig = this.streamMediaConfigService.getById(id);
        Assert.isNotFound(streamMediaConfig, "流媒体配置不存在");
        return ObjectUtils.copying(streamMediaConfig, StreamMediaConfigVO.class);
    }

    @GetMapping("/stream-media-config")
    public StreamMediaConfigVO getStreamMediaConfig() {
        StreamMediaConfig streamMediaConfig = this.streamMediaConfigService.getOne();
        return ObjectUtils.copying(streamMediaConfig, StreamMediaConfigVO.class);
    }

    /**
     * 流媒体服务器配置删除
     *
     * @param id
     */
    @DeleteMapping("/stream-media-config/{id}")
    public void remove(@PathVariable("id") @Min(value = 1, message = "配置主键不能小于1") Long id) {
        this.streamMediaConfigService.remove(id);
    }
}

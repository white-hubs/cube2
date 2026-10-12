package com.guhecloud.cube.deliver.project.merchant.controller;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.deliver.project.merchant.commons.core.LoginUser;
import com.guhecloud.cube.deliver.project.merchant.commons.web.PageRequest;
import com.guhecloud.cube.deliver.project.merchant.commons.web.PageResponse;
import com.guhecloud.cube.deliver.project.merchant.dto.AppealDescriptionDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.AppealDescriptionQueryDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.AppealDescription;
import com.guhecloud.cube.deliver.project.merchant.service.AppealDescriptionService;

import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.List;

/**
 * <p>
 * 诉求描述表 前端控制器
 * </p>
 *
 * @author lzy
 * @since 2021-04-28
 */
@CacheConfig(cacheNames = "Cache::AppealDesc")
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AppealDescriptionController {
    private final AppealDescriptionService appealDescriptionService;

    @GetMapping("/appeal-description/page")
    @Cacheable
    public PageResponse<AppealDescriptionDTO> getPage(AppealDescriptionQueryDTO queryDTO, PageRequest request) {
        Page<AppealDescription> page = this.getAppealDescriptionLambdaQueryChainWrapper(queryDTO).page(request.page());
        List<AppealDescriptionDTO> dtos = new AppealDescriptionDTO(page.getRecords()).list();
        return new PageResponse<>(dtos, page.getTotal(), page.getPages());
    }

    @GetMapping("/appeal-description/list")
    public List<AppealDescriptionDTO> getList(AppealDescriptionQueryDTO queryDTO) {
        return new AppealDescriptionDTO(this.getAppealDescriptionLambdaQueryChainWrapper(queryDTO).list()).list();
    }

    @GetMapping("/appeal-description/{id}")
    public AppealDescriptionDTO getById(@PathVariable("id") String id) {
        AppealDescription appealDescription = appealDescriptionService.getById(id);
        if (appealDescription == null) {
            return null;
        }
        AppealDescriptionDTO dto = new AppealDescriptionDTO();
        BeanUtils.copyProperties(appealDescription, dto);
        return dto;
    }

    @PostMapping("/appeal-description")
    @CacheEvict(allEntries = true)
    public void save(@Valid @RequestBody AppealDescriptionDTO dto, LoginUser loginUser) {
        appealDescriptionService.save(dto, loginUser);
    }

    @PutMapping("/appeal-description")
    @CacheEvict(allEntries = true)
    public void update(@Valid @RequestBody AppealDescriptionDTO dto) {
        appealDescriptionService.update(dto);
    }

    @DeleteMapping("/appeal-description/{id}")
    @CacheEvict(allEntries = true)
    public void remove(@PathVariable("id") String id) {
        appealDescriptionService.removeById(id);
    }

    @DeleteMapping("/appeal-description")
    @CacheEvict(allEntries = true)
    public void removeBatch(@RequestParam(name = "ids") List<String> ids) {
        appealDescriptionService.removeByIds(ids);
    }

    private LambdaQueryChainWrapper<AppealDescription> getAppealDescriptionLambdaQueryChainWrapper(AppealDescriptionQueryDTO queryDTO) {
        return appealDescriptionService.lambdaQuery()
                .and(StringUtils.isNotBlank(queryDTO.getSearch()), q -> q
                        .like(AppealDescription::getCode, queryDTO.getSearch())
                        .or()
                        .like(AppealDescription::getName, queryDTO.getSearch())
                    )
                .eq(StringUtils.isNotEmpty(queryDTO.getType()), AppealDescription::getType, queryDTO.getType())
                .between(queryDTO.getBeginTime() != null &&
                         queryDTO.getEndTime() != null, AppealDescription::getCreateTime, queryDTO.getBeginTime(), queryDTO.getEndTime())
                .orderByDesc(AppealDescription::getCreateTime);
    }

}


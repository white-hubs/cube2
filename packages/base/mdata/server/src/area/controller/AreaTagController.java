package com.guhecloud.cube.biz.mdata.server.area.controller;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.FileOperateClient;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.mdata.server.area.entity.AreaTag;
import com.guhecloud.cube.biz.mdata.server.area.param.AreaSpaceQueryParam;
import com.guhecloud.cube.biz.mdata.server.area.service.AreaTagService;
import com.guhecloud.cube.biz.mdata.server.area.vo.AreaTagVO;
import com.guhecloud.cube.biz.mdata.server.area.vo.KeyValueLongStringVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 区域标签
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AreaTagController {

    private final String SCENE_CODE = "MDATA";

    private final FileOperateClient fileOperateClient;
    private final FileClient fileClient;
    private final AreaTagService areaTagService;

    /**
     * 详情
     *
     * @param id id
     *
     * @return 详情
     */
    @GetMapping("/area-tag/detail")
    public AreaTagVO areaTagDetail(@RequestParam("id") Long id) {
        AreaTag tag = areaTagService.getById(id);
        Assert.isNotFound(tag, "数据不存在！");
        AreaTagVO vo = ObjectUtils.copying(tag, AreaTagVO.class);
        if (vo.getIcoFileId() != null) {
            vo.setFileVO(fileClient.getById(vo.getIcoFileId()));
        }
        return vo;
    }

    /**
     * 分页列表
     *
     * @param pageParam 分页参数
     *
     * @return 分页列表
     */
    @GetMapping("/area-tag/page")
    public Pagination<AreaTagVO> areaTagPage(AreaSpaceQueryParam pageParam) {
        Page<AreaTag> page = areaTagService.page(PagingUtils.convert(pageParam), Wrappers.<AreaTag>lambdaQuery()
                                                         .and(StringUtils.isNotEmpty(pageParam.getName())
                                                                 , i -> i.like(AreaTag::getName, pageParam.getName())
                                                                         .or().like(AreaTag::getDescribe, pageParam.getName())
                                                             )

                                                );
        Pagination<AreaTagVO> pagination = PagingUtils.convert(page, AreaTagVO.class);
        // 扩展文件对象
        this.extFileVO(pagination.getRecords());
        return pagination;
    }

    /**
     * 列表
     *
     * @param name 名称
     *
     * @return 分页列表
     */
    @GetMapping("/area-tag/list")
    public List<KeyValueLongStringVO> areaTagList(@Nullable @RequestParam("name") String name) {
        List<AreaTag> list = areaTagService.list(Wrappers.<AreaTag>lambdaQuery()
                                                         .like(StringUtils.isNotEmpty(name), AreaTag::getName, name)
                                                );
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }
        return list.stream().map(t -> KeyValueLongStringVO.builder().key(t.getId()).value(t.getName()).build())
                .collect(Collectors.toList());
    }

    /**
     * 新增
     *
     * @param areaTag 新增对象
     *
     * @return 成功
     */
    @PostMapping("/area-tag/add")
    public Long areaTagAdd(@RequestBody AreaTag areaTag) {
        Long id = IdWorker.getId();
        areaTag.setId(id);
        areaTagService.save(areaTag);
        this.fileOperate(areaTag.getIcoFileId());
        return id;
    }

    /**
     * 编辑
     *
     * @param areaTag 编辑对象
     *
     * @return 成功
     */
    @PutMapping("/area-tag/edit")
    public Boolean areaTagEdit(@RequestBody AreaTag areaTag) {
        Assert.isNotFound(areaTagService.getById(areaTag.getId()), "数据不存在！");
        areaTagService.update(Wrappers.<AreaTag>lambdaUpdate()
                                      .eq(AreaTag::getId, areaTag.getId())
                                      .set(AreaTag::getName, areaTag.getName())
                                      .set(AreaTag::getDescribe, areaTag.getDescribe())
                                      .set(AreaTag::getIcoFileId, areaTag.getIcoFileId())
                             );
        this.fileOperate(areaTag.getIcoFileId());
        return true;
    }

    /**
     * 删除
     *
     * @param id id
     *
     * @return 成功
     */
    @DeleteMapping("/area-tag/remove")
    public Boolean areaTagRemove(@RequestParam("id") Long id) {
        DatabaseContextHolder.softDelete(areaTagService, id);
        return true;
    }

    /**
     * 文件持久化
     *
     * @param fileId 附件id
     */
    private void fileOperate(Long fileId) {
        if (fileId != null) {
            String fileSceneCode = "MDATA::SPACE_TAG";
            fileOperateClient.fileInduceSingle(fileSceneCode, null, fileId);
        }
    }

    /**
     * 扩展文件对象
     *
     * @param list 集合
     */
    private void extFileVO(List<AreaTagVO> list) {
        if (list != null && !list.isEmpty()) {
            // 获取fileId集合
            List<Long> fileIdList = new ArrayList<>();
            list.forEach(vo -> {
                if (vo.getIcoFileId() != null) {
                    fileIdList.add(vo.getIcoFileId());
                }
            });
            if (!fileIdList.isEmpty()) {
                // 获取fileMap
                Map<Long, FileVO> fileVOMap = new HashMap<>();
                List<FileVO> fileVOList = fileClient.getByIds(fileIdList);
                if (fileVOList != null && !fileVOList.isEmpty()) {
                    fileVOList.forEach(f -> fileVOMap.put(f.getId(), f));
                }
                // 填充fileVo
                list.forEach(r -> {
                    if (r.getIcoFileId() != null) {
                        r.setFileVO(fileVOMap.get(r.getIcoFileId()));
                    }
                });
            }
        }
    }
}

package com.guhecloud.cube.base.iot.product.model.v3.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.base.iot.product.model.v3.constant.ModelConstants;
import com.guhecloud.cube.base.iot.product.model.v3.enums.DictEnum;
import com.guhecloud.cube.base.iot.product.model.v3.param.DictItemModifyParam;
import com.guhecloud.cube.base.iot.product.model.v3.param.DictItemPageParam;
import com.guhecloud.cube.base.iot.product.model.v3.param.ProductBrandModifyParam;
import com.guhecloud.cube.base.iot.product.model.v3.service.V3DictItemService;
import com.guhecloud.cube.base.iot.product.model.v3.service.V3ProductBrandService;
import com.guhecloud.cube.base.iot.product.model.v3.vo.DictItemVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.guhecloud.cube.base.iot.product.model.v3.constant.FileStoreKeys.FILE_SCENE_DEVICE_CENTER;

/**
 * 产品品牌服务实现类
 * 提供品牌管理能力,支持文件关联
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class V3ProductBrandServiceImpl implements V3ProductBrandService {

    private final V3DictItemService dictItemService;
    private final FileClient fileClient;
    private final FileRefClient fileRefClient;

    @Override
    public Page<DictItemVO> page(DictItemPageParam param) {
        log.info("分页查询品牌, param={}", param);

        // 设置固定的dictId为品牌字典ID
        param.setDictId(DictEnum.PRODUCT_BRAND.getId());

        // 调用通用字典服务
        Page<DictItemVO> result = dictItemService.page(param);

        // 可选: 加载文件信息
        if (Boolean.TRUE.equals(param.getWithAssociatedItem())) {
            for (DictItemVO vo : result.getRecords()) {
                loadFiles(vo);
            }
        }

        log.info("品牌分页查询完成, total={}", result.getTotal());
        return result;
    }

    @Override
    public DictItemVO getById(Long id) {
        log.info("查询品牌详情, id={}", id);

        DictItemVO vo = dictItemService.getById(id);
        if (vo != null) {
            // 加载文件信息
            loadFiles(vo);
        }

        return vo;
    }

    @Override
    public void save(ProductBrandModifyParam param) {
        log.info("新增品牌, param={}", param);

        // 转换为通用字典修改参数
        DictItemModifyParam dictParam = param.toCommonDictItemModifyParam(
                DictEnum.PRODUCT_BRAND.getId()
        );

        // 调用通用字典服务
        Long brandId = dictItemService.save(dictParam);

        // 建立文件关联
        bindBrandFile(brandId, param.getItemValue());

        log.info("品牌新增成功");
    }

    @Override
    public void update(Long id, ProductBrandModifyParam param) {
        log.info("修改品牌, id={}, param={}", id, param);

        // 转换为通用字典修改参数
        DictItemModifyParam dictParam = param.toCommonDictItemModifyParam(
                DictEnum.PRODUCT_BRAND.getId()
        );

        // 调用通用字典服务
        dictItemService.update(id, dictParam);

        // 更新文件关联
        bindBrandFile(id, param.getItemValue());

        log.info("品牌修改成功, id={}", id);
    }

    @Override
    public void delete(Long id) {
        log.info("删除品牌, id={}", id);

        // 调用通用字典服务
        dictItemService.delete(id);

        try {
            fileRefClient.removeByBusinessKeyAndScene(id, FILE_SCENE_DEVICE_CENTER);
        } catch (Exception e) {
            log.warn("删除品牌后清理文件关联失败, brandId={}", id, e);
        }

        log.info("品牌删除成功, id={}", id);
    }

    /**
     * 加载文件信息
     */
    private void loadFiles(DictItemVO vo) {
        try {
            List<FileVO> files = fileClient.getByBusinessKey(vo.getId());
            vo.setFileVOs(files != null ? files : new ArrayList<>());
        } catch (Exception e) {
            log.warn("加载品牌文件失败, brandId={}", vo.getId(), e);
            vo.setFileVOs(new ArrayList<>());
        }
    }

    /**
     * 建立或更新品牌文件关联
     */
    private void bindBrandFile(Long brandId, String itemValue) {
        if (brandId == null) {
            return;
        }

        try {
            fileRefClient.removeByBusinessKeyAndScene(brandId, FILE_SCENE_DEVICE_CENTER);
        } catch (Exception e) {
            log.warn("清理品牌文件关联失败, brandId={}", brandId, e);
        }

        Long fileId = parseFileId(itemValue);
        if (fileId == null) {
            return;
        }

        try {
            fileRefClient.refInduceSingle(FILE_SCENE_DEVICE_CENTER, brandId, fileId, null);
        } catch (Exception e) {
            log.error("关联品牌文件失败, brandId={}, fileId={}", brandId, fileId, e);
        }
    }

    /**
     * 将itemValue解析为文件ID
     */
    private Long parseFileId(String itemValue) {
        if (!StringUtils.hasText(itemValue)) {
            return null;
        }

        try {
            return Long.valueOf(itemValue);
        } catch (NumberFormatException ex) {
            log.warn("itemValue不是有效的文件ID, value={}", itemValue);
            return null;
        }
    }
}

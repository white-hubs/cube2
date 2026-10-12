package com.guhecloud.cube.base.iot.product.model.v3.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.base.iot.product.model.v3.enums.EntityTypeEnum;
import com.guhecloud.cube.base.iot.product.model.v3.enums.OperationTypeEnum;
import com.guhecloud.cube.base.iot.product.model.v3.event.ModelImportedEvent;
import com.guhecloud.cube.base.iot.product.model.v3.event.ProductCreatedEvent;
import com.guhecloud.cube.base.iot.product.model.v3.event.ProductUpdatedEvent;
import com.guhecloud.cube.base.iot.product.model.v3.guard.DeletionGuardManager;
import com.guhecloud.cube.base.iot.product.model.v3.mapper.V3MdProductMapper;
import com.guhecloud.cube.base.iot.product.model.v3.mapper.V3MdProductMethodMapper;
import com.guhecloud.cube.base.iot.product.model.v3.mapper.V3MdProductPropMapper;
import com.guhecloud.cube.base.iot.product.model.v3.model.MdProduct;
import com.guhecloud.cube.base.iot.product.model.v3.param.ProductPageParam;
import com.guhecloud.cube.base.iot.product.model.v3.param.ProductSaveParam;
import com.guhecloud.cube.base.iot.product.model.v3.service.V3MdProductEventService;
import com.guhecloud.cube.base.iot.product.model.v3.service.V3MdProductService;
import com.guhecloud.cube.base.iot.product.model.v3.service.V3ProductModelVersionService;
import com.guhecloud.cube.base.iot.product.model.v3.service.V3SysDictService;
import com.guhecloud.cube.base.iot.product.model.v3.util.CodeGenerator;
import com.guhecloud.cube.base.iot.product.model.v3.vo.ProductVO;
import com.guhecloud.cube.base.iot.product.model.v3.vo.ProductEventVO;
import com.guhecloud.cube.base.iot.product.model.v3.vo.ProductMethodVO;
import com.guhecloud.cube.base.iot.product.model.v3.vo.ProductPropVO;
import com.guhecloud.platform.base.util.PinYinUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.guhecloud.cube.base.iot.product.model.v3.constant.FileStoreKeys.FILE_SCENE_DEVICE_CENTER;
import static com.guhecloud.cube.base.iot.product.model.v3.util.CodeGenerator.incrementVersion;

/**
 * 产品管理Service实现 v3版本
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class V3MdProductServiceImpl extends ServiceImpl<V3MdProductMapper, MdProduct> implements V3MdProductService {

    private final V3MdProductMapper productMapper;
    private final V3MdProductPropMapper propMapper;
    private final V3MdProductEventService productEventService;
    private final V3MdProductMethodMapper methodMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final DeletionGuardManager guardManager;
    private final V3SysDictService sysDictService;
    private final V3ProductModelVersionService productModelVersionService;
    private final FileClient fileClient;
    private final FileRefClient fileRefClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveProduct(ProductSaveParam param) {
        log.info("开始创建产品, prodName={}", param.getProdName());

        // 1. 参数校验
        validateProduct(param);

        // 2. 生成产品编号(业务编码)
        String prodNumber = generateProdNumber();

        // 3. 生成拼音首字母
        String pinyin = PinYinUtils.getHead(param.getProdName());

        // 4. 构建产品实体(显式设置字段,避免隐式拷贝)
        MdProduct product = new MdProduct();
        product.setProdNumber(prodNumber);
        product.setProdName(param.getProdName());
        product.setProdNamePinyin(pinyin);
        // 设置品类ID和Code(通过ID查询Code)
        product.setProdCategoryId(param.getProdCategoryId());
        product.setNodeType(param.getNodeType());
        // 设置品牌ID和Code
        product.setProdBrandId(param.getProdBrandId());
        product.setProdModel(param.getProdModel());
        // 设置协议ID和Code
        product.setProtocolId(param.getProtocolId());
        product.setRemark(param.getRemark());
        product.setExtendConfig(param.getExtendConfig());
        product.setStatus(param.getStatus());
        product.setProdVersion(incrementVersion(null));
        product.setProdIcon(param.getProdIcon());
        product.setProdPic(param.getProdPic());

        // 5. 保存产品
        save(product);

        log.info("产品创建成功, prodId={}, prodNumber={}", product.getId(), prodNumber);

        // 6. 建立文件关联
        bindProductFiles(product.getId(), param.getProdIcon(), param.getProdPic());

        // 7. 发布产品创建事件(触发版本快照创建)
        eventPublisher.publishEvent(ProductCreatedEvent.builder()
                .prodId(product.getId())
                .timestamp(new Date())
                .changeDesc("创建产品")
                .build());

        return product.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProduct(Long prodId, ProductSaveParam param) {
        log.info("开始更新产品, prodId={}", prodId);

        // 1. 检查产品是否存在
        MdProduct product = getById(prodId);
        if (product == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "产品不存在");
        }

        // 2. 参数校验
        validateProduct(param);

        // 3. 保存旧的图标和图片ID，用于增量更新文件关联
        Long oldIcon = product.getProdIcon();
        Long oldPic = product.getProdPic();

        // 4. 更新产品信息(显式设置字段,避免隐式拷贝)
        product.setProdName(param.getProdName());
        product.setProdNamePinyin(PinYinUtils.getHead(param.getProdName()));
        // 设置品类ID和Code
        product.setProdCategoryId(param.getProdCategoryId());
        product.setNodeType(param.getNodeType());
        // 设置品牌ID和Code
        product.setProdBrandId(param.getProdBrandId());
        product.setProdModel(param.getProdModel());
        // 设置协议ID和Code
        product.setProtocolId(param.getProtocolId());
        product.setRemark(param.getRemark());
        product.setExtendConfig(param.getExtendConfig());
        product.setStatus(param.getStatus());
        product.setProdVersion(incrementVersion(param.getProdVersion()));
        // 更新图标和图片(支持设置为null来删除)
        product.setProdIcon(param.getProdIcon());
        product.setProdPic(param.getProdPic());

        // 5. 保存更新
        updateById(product);

        log.info("产品更新成功, prodId={}", prodId);

        // 6. 增量更新文件关联(只处理发生变化的文件)
        updateProductFiles(prodId, oldIcon, param.getProdIcon(), oldPic, param.getProdPic());

        // 7. 发布产品更新事件(触发版本快照创建)
        eventPublisher.publishEvent(ProductUpdatedEvent.builder()
                .prodId(prodId)
                .timestamp(new Date())
                .changeDesc("更新产品")
                .build());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteProduct(Long prodId) {
        log.info("开始删除产品, prodId={}", prodId);

        // 1. 检查产品是否存在
        MdProduct product = getById(prodId);
        if (product == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "产品不存在");
        }

        // 2. 删除前关联检查(检查设备实例、属性/事件/方法、规则引用等)
        guardManager.checkBeforeDelete(prodId, EntityTypeEnum.PRODUCT.getCode());

        // 3. 软删除
        DatabaseContextHolder.softDelete(this, product.getId());

        log.info("产品删除成功, prodId={}", prodId);
    }

    @Override
    public ProductVO getProductDetail(Long prodId) {
        log.info("查询产品详情, prodId={}", prodId);

        // 查询产品详情(包含属性、事件、方法)
        ProductVO vo = productMapper.selectProductDetail(prodId);

        if (vo == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "产品不存在");
        }

        List<ProductPropVO> props = propMapper.selectByProdId(prodId);
        List<ProductEventVO> events = productEventService.listByProdId(prodId);
        List<ProductMethodVO> methods = methodMapper.selectByProdId(prodId);
        vo.setProperties(props);
        vo.setEvents(events);
        vo.setMethods(methods);

        fillProductFileVOs(Collections.singletonList(vo));

        return vo;
    }

    @Override
    public IPage<ProductVO> queryProductPage(ProductPageParam param) {
        log.info("分页查询产品, param={}", param);

        // 构建分页对象
        Page<ProductVO> page = new Page<>(param.getPageNumber(), param.getPageSize());

        // 分页查询
        IPage<ProductVO> result = productMapper.selectProductPage(page, param);
        fillProductFileVOs(result.getRecords());
        return result;
    }

    @Override
    public MdProduct getByProdNumber(String prodNumber) {
        return productMapper.selectByProdNumber(prodNumber);
    }

    /**
     * 参数校验
     */
    private void validateProduct(ProductSaveParam param) {
        if (param.getStatus() == null) {
            param.setStatus(1);
        }

        String categoryCode = sysDictService.getItemCodeById(param.getProdCategoryId(), false);
        if (categoryCode == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "品类不存在");
        }

        if (param.getProdBrandId() != null) {
            String brandCode = sysDictService.getItemCodeById(param.getProdBrandId(), false);
            if (brandCode == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "品牌不存在");
            }
        }

        String protocolCode = sysDictService.getItemCodeById(param.getProtocolId(), true);
        if (protocolCode == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "协议不存在");
        }
    }

    /**
     * 生成产品编号
     * 格式: PROD_{timestamp}_{随机数}
     */
    private String generateProdNumber() {
        return CodeGenerator.generateProdNumber();
    }

    /**
     * 建立产品文件关联(用于新建场景)
     * 直接创建图标和图片的文件关联
     */
    private void bindProductFiles(Long prodId, Long prodIcon, Long prodPic) {
        if (prodId == null) {
            return;
        }

        if (prodIcon != null) {
            try {
                fileRefClient.refInduceSingle(FILE_SCENE_DEVICE_CENTER, prodId, prodIcon, null);
            } catch (Exception e) {
                log.error("关联产品图标文件失败, prodId={}, fileId={}", prodId, prodIcon, e);
            }
        }
        if (prodPic != null) {
            try {
                fileRefClient.refInduceSingle(FILE_SCENE_DEVICE_CENTER, prodId, prodPic, null);
            } catch (Exception e) {
                log.error("关联产品图片文件失败, prodId={}, fileId={}", prodId, prodPic, e);
            }
        }
    }

    /**
     * 增量更新产品文件关联(用于更新场景)
     * 比较新旧值，只处理发生变化的文件关联
     *
     * @param prodId 产品ID
     * @param oldIcon 原图标文件ID
     * @param newIcon 新图标文件ID (null表示要删除图标)
     * @param oldPic 原图片文件ID
     * @param newPic 新图片文件ID (null表示要删除图片)
     */
    private void updateProductFiles(Long prodId, Long oldIcon, Long newIcon, Long oldPic, Long newPic) {
        if (prodId == null) {
            return;
        }

        // 处理图标的变化
        if (isNotSameFile(oldIcon, newIcon)) {
            // 删除旧图标关联
            if (oldIcon != null) {
                try {
                    fileClient.remove(oldIcon);
                    log.info("删除产品图标关联, prodId={}, oldIconId={}", prodId, oldIcon);
                } catch (Exception e) {
                    log.error("删除产品图标关联失败, prodId={}, fileId={}", prodId, oldIcon, e);
                }
            }
            // 创建新图标关联
            if (newIcon != null) {
                try {
                    fileRefClient.refInduceSingle(FILE_SCENE_DEVICE_CENTER, prodId, newIcon, null);
                    log.info("创建产品图标关联, prodId={}, newIconId={}", prodId, newIcon);
                } catch (Exception e) {
                    log.error("创建产品图标关联失败, prodId={}, fileId={}", prodId, newIcon, e);
                }
            }
        }

        // 处理图片的变化
        if (isNotSameFile(oldPic, newPic)) {
            // 删除旧图片关联
            if (oldPic != null) {
                try {
                    fileClient.remove(oldPic);
                    log.info("删除产品图片关联, prodId={}, oldPicId={}", prodId, oldPic);
                } catch (Exception e) {
                    log.error("删除产品图片关联失败, prodId={}, fileId={}", prodId, oldPic, e);
                }
            }
            // 创建新图片关联
            if (newPic != null) {
                try {
                    fileRefClient.refInduceSingle(FILE_SCENE_DEVICE_CENTER, prodId, newPic, null);
                    log.info("创建产品图片关联, prodId={}, newPicId={}", prodId, newPic);
                } catch (Exception e) {
                    log.error("创建产品图片关联失败, prodId={}, fileId={}", prodId, newPic, e);
                }
            }
        }
    }

    /**
     * 判断两个文件ID是否相同
     */
    private boolean isNotSameFile(Long fileId1, Long fileId2) {
        if (fileId1 == null && fileId2 == null) {
            return false;
        }
        if (fileId1 == null || fileId2 == null) {
            return true;
        }
        return !fileId1.equals(fileId2);
    }

    /**
     * 批量填充产品文件信息
     */
    private void fillProductFileVOs(List<ProductVO> records) {
        if (CollectionUtils.isEmpty(records)) {
            return;
        }

        Set<Long> fileIds = new HashSet<>();
        for (ProductVO vo : records) {
            if (vo.getProdIcon() != null) {
                fileIds.add(vo.getProdIcon());
            }
            if (vo.getProdPic() != null) {
                fileIds.add(vo.getProdPic());
            }
        }

        Map<Long, FileVO> fileMap = fetchFiles(fileIds);
        for (ProductVO vo : records) {
            if (vo == null) {
                continue;
            }
            if (vo.getProdIcon() != null) {
                vo.setIconFileVO(fileMap.get(vo.getProdIcon()));
            }
            if (vo.getProdPic() != null) {
                vo.setPicFileVO(fileMap.get(vo.getProdPic()));
            }
        }
    }

    /**
     * 查询文件信息并转换为map
     */
    private Map<Long, FileVO> fetchFiles(Set<Long> fileIds) {
        if (CollectionUtils.isEmpty(fileIds)) {
            return Collections.emptyMap();
        }
        Map<Long, FileVO> map = new HashMap<>();
        try {
            List<FileVO> files = fileClient.getByIds(new ArrayList<>(fileIds));
            if (!CollectionUtils.isEmpty(files)) {
                for (FileVO file : files) {
                    map.put(file.getId(), file);
                }
            }
        } catch (Exception e) {
            log.error("查询文件信息失败, fileIds={}", fileIds, e);
        }
        return map;
    }

    /**
     * 监听产品创建事件,创建版本快照
     *
     * <p>使用@TransactionalEventListener(phase = BEFORE_COMMIT)确保在同一事务中执行</p>
     */
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onProductCreated(ProductCreatedEvent event) {
        log.info("监听到产品创建事件, 开始创建版本快照: prodId={}", event.getProdId());
        createSnapshot(event.getProdId(), OperationTypeEnum.CREATE.getCode(), event.getChangeDesc());
    }

    /**
     * 监听产品更新事件,创建版本快照
     *
     * <p>使用@TransactionalEventListener(phase = BEFORE_COMMIT)确保在同一事务中执行</p>
     */
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onProductUpdated(ProductUpdatedEvent event) {
        log.info("监听到产品更新事件, 开始创建版本快照: prodId={}", event.getProdId());
        createSnapshot(event.getProdId(), OperationTypeEnum.UPDATE.getCode(), event.getChangeDesc());
    }

    /**
     * 监听模型导入事件,创建版本快照
     *
     * <p>使用@TransactionalEventListener(phase = BEFORE_COMMIT)确保在同一事务中执行</p>
     */
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onModelImported(ModelImportedEvent event) {
        log.info("监听到模型导入事件, 开始创建版本快照: prodId={}", event.getProdId());
        createSnapshot(event.getProdId(), OperationTypeEnum.UPDATE.getCode(), event.getChangeDesc());
    }

    private void createSnapshot(Long prodId, String changeType, String changeDesc) {
        log.info("开始创建版本快照, prodId={}, changeType={}", prodId, changeType);

        productModelVersionService.createSnapshot(prodId, changeType, changeDesc);
    }
}

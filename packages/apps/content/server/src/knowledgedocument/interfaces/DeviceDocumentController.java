package com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.interfaces;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceProductInfoDTO;
import com.guhecloud.cube.base.device.center.std.devcenter.ProductClient;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.commandgateway.DeviceDocumentCommandService;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.commandgateway.DeviceDocumentRefProductService;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.commandgateway.command.SubmitDeviceDocumentCommand;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.interfaces.vo.DeviceDocumentRefProductVO;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.interfaces.vo.DeviceDocumentVO;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.po.DeviceDocument;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.po.DeviceDocumentRefProduct;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.querygateway.DeviceDocumentQueryService;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.querygateway.query.DeviceDocumentCollectionQuery;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 设备文档接口
 * 原来的设备文档和建筑资料整合成知识库(KnowledgeBaseController)
 * 所以 设备文档和建筑资料 废弃使用
 * 原型地址：<a href="https://alidocs.dingtalk.com/i/nodes/Y1OQX0akWm3ZebbjhE02k53aJGlDd3mE?doc_type=wiki_doc">...</a>
 *
 * @author lzy
 * @date 2022/5/30
 */
@Deprecated
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeviceDocumentController {

    private final DeviceDocumentCommandService deviceDocumentCommandService;
    private final DeviceDocumentQueryService deviceDocumentQueryService;
    private final DeviceDocumentRefProductService deviceDocumentRefProductService;
    private final ProductClient productClient;

    private final FileClient fileClient;

    /**
     * 设备文档新增
     *
     * @param command 新增接口
     */
    @PostMapping("/device-document")
    public void save(@Valid @RequestBody SubmitDeviceDocumentCommand command) {
        this.deviceDocumentCommandService.save(command);
    }

    /**
     * 设备文档删除
     *
     * @param id 主键
     */
    @DeleteMapping("/device-document/{id}")
    public void remove(@PathVariable("id") Long id) {
        this.deviceDocumentCommandService.remove(id);
    }

    /**
     * 设备文档删除批量
     *
     * @param ids 主键
     */
    @DeleteMapping("/device-document/batch")
    public void batchRemove(@RequestParam("ids") List<Long> ids) {
        this.deviceDocumentCommandService.batchRemove(ids);
    }

    /**
     * @Description app设备台账文档
     * @Author lrj
     * @Param
     * @Return
     * @Exception
     */
    @GetMapping("/appDeviceDocument/{deviceProductId}")
    public List<DeviceDocumentVO> appDeviceDocument(@PathVariable("deviceProductId") Long deviceProductId,
                                                    @RequestParam(required = false, value = "keyword") String keyword
                                                   ) {
        DeviceDocumentCollectionQuery deviceDocumentCollectionQuery = new DeviceDocumentCollectionQuery();
        deviceDocumentCollectionQuery.setDeviceProdId(deviceProductId);
        deviceDocumentCollectionQuery.setKeyword(keyword);
        List<DeviceDocument> deviceDocuments = this.deviceDocumentQueryService.deviceDocumentList(deviceDocumentCollectionQuery);
        List<DeviceDocumentVO> deviceDocumentVOS = ObjectUtils.copying(deviceDocuments, DeviceDocumentVO.class);
        deviceDocumentVOS.forEach(this::extendDeviceDocumentFile);
        return deviceDocumentVOS;
    }

    /**
     * 设备文档分页信息
     *
     * @param query 查询参数
     *
     * @return 设备文档信息
     */
    @GetMapping("/device-document/page")
    public Pagination<DeviceDocumentVO> pageDeviceDocument(@Valid DeviceDocumentCollectionQuery query) {
        // 获取信息
        Page<DeviceDocument> page = this.deviceDocumentQueryService.pageDeviceDocument(query);
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }
        // 获取设备信息和获取文件信息
        List<DeviceDocumentVO> records = extend(page.getRecords());
        // 构建信息
        return PagingUtils.convert(page, records);
    }

    public List<DeviceDocumentVO> extend(List<DeviceDocument> deviceDocuments) {
        // 获取设备信息和获取文件信息
        List<DeviceDocumentVO> records = ObjectUtils.copying(deviceDocuments, DeviceDocumentVO.class);
        //获取关联的产品信息
        Set<Long> ids = records.stream().map(DeviceDocumentVO::getId).collect(Collectors.toSet());
        Map<Long, List<DeviceDocumentRefProduct>> deviceDocumentRefProducts = this.deviceDocumentRefProductService
                .getRefProductByDocumentIds(ids)
                .stream()
                .collect(Collectors.groupingBy(DeviceDocumentRefProduct::getDeviceDocumentId));
        records.forEach(record -> {
            if (deviceDocumentRefProducts.containsKey(record.getId())) {
                extendProductConcatInfo(deviceDocumentRefProducts, record);
            }
            this.extendDeviceDocumentFile(record);
        });
        return records;
    }

    public void extendProductConcatInfo(Map<Long, List<DeviceDocumentRefProduct>> deviceDocumentRefProducts, DeviceDocumentVO record) {
        List<DeviceDocumentRefProduct> documentRefProducts = deviceDocumentRefProducts.get(record.getId());
        Set<Long> productIds = documentRefProducts.stream().map(DeviceDocumentRefProduct::getDeviceProdId).collect(Collectors.toSet());
        List<DeviceProductInfoDTO> prodcts = this.productClient.getProductList(productIds);
        List<DeviceDocumentRefProductVO> deviceDocumentRefProductVOS = prodcts.stream().map(deviceProductInfoDTO -> {
            DeviceDocumentRefProductVO deviceDocumentRefProductVO = new DeviceDocumentRefProductVO();
            deviceDocumentRefProductVO.setDeviceProdId(deviceProductInfoDTO.getId());
            deviceDocumentRefProductVO.setDeviceProdName(deviceProductInfoDTO.getName());
            return deviceDocumentRefProductVO;
        }).toList();
        if (!CollectionUtils.isEmpty(deviceDocumentRefProductVOS)) {
            DeviceDocumentVO.RefProductConcat refProductConcat = new DeviceDocumentVO.RefProductConcat();
            StringBuilder productIdsBuilder = new StringBuilder();
            StringBuilder productNamesBuilder = new StringBuilder();
            for (int separatorNum = 0; separatorNum < deviceDocumentRefProductVOS.size(); separatorNum++) {
                DeviceDocumentRefProductVO deviceDocumentRefProductVO = deviceDocumentRefProductVOS.get(separatorNum);
                productIdsBuilder.append(deviceDocumentRefProductVO.getDeviceProdId().toString());
                productNamesBuilder.append(deviceDocumentRefProductVO.getDeviceProdName());
                if (separatorNum != deviceDocumentRefProductVOS.size() - 1) {
                    productIdsBuilder.append("-");
                    productNamesBuilder.append(",");
                }
            }
            refProductConcat.setProductIds(productIdsBuilder.toString());
            refProductConcat.setProductNames(productNamesBuilder.toString());
            record.setProduct(refProductConcat);
        }
    }

    public void extendDeviceDocumentFile(DeviceDocumentVO record) {
        List<FileVO> fileVOS = fileClient.getByBusinessKey(record.getId());
        if (CollectionUtils.isNotEmpty(fileVOS)) {
            record.setFileVO(fileVOS.getFirst());
            record.setFormat(fileVOS.getFirst().getSuffix());
            record.setSize(BigDecimal.valueOf((double) fileVOS.getFirst().getSize() / 1048576));
        }
    }

}

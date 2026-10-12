package com.guhecloud.cube.srv.gen.maintain.server.repair.interfaces;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceDetailDTO;
import com.guhecloud.cube.base.device.center.metadata.dto.ProductCategoryInfoDTO;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.base.device.center.std.devcenter.ProductCategoryClient;
import com.guhecloud.cube.srv.gen.maintain.server.repair.commandgateway.command.company.ChangeOutsourceCompanyCommand;
import com.guhecloud.cube.srv.gen.maintain.server.repair.commandgateway.command.company.IncreaseOutsourceCompanyCommand;
import com.guhecloud.cube.srv.gen.maintain.server.repair.commandgateway.impl.OutsourceCompanyService;
import com.guhecloud.cube.srv.gen.maintain.server.repair.dao.OutsourceCompanyMapper;
import com.guhecloud.cube.srv.gen.maintain.server.repair.event.ChangeOutsourceCompanyModelEvent;
import com.guhecloud.cube.srv.gen.maintain.server.repair.event.ChangeOutsourceCompanyScopeEvent;
import com.guhecloud.cube.srv.gen.maintain.server.repair.event.DeleteOutsourceCompanyEvent;
import com.guhecloud.cube.srv.gen.maintain.server.repair.event.IncreaseOutsourceCompanyEvent;
import com.guhecloud.cube.srv.gen.maintain.server.repair.interfaces.vo.OutsourceCompanyDetailVO;
import com.guhecloud.cube.srv.gen.maintain.server.repair.interfaces.vo.OutsourceCompanyVO;
import com.guhecloud.cube.srv.gen.maintain.server.repair.po.OutsourceCompanyPO;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author pengjian
 * @date 2022/5/18 15:23
 * @description 委外公司
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OutsourceCompanyController {
    private final OutsourceCompanyMapper outsourceCompanyMapper;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final OutsourceCompanyService companyService;
    private final DeviceCenterStdClient deviceInstanceClient;
    private final ProductCategoryClient productCategoryClient;

    @GetMapping("/outsource/company/list")
    public List<OutsourceCompanyVO> list(@Nullable Long deviceId) {
        String productCategoryCode = null;
        if (ObjectUtils.isNotEmpty(deviceId)) {
            DeviceDetailDTO detail = deviceInstanceClient.getDeviceById(deviceId);
            if (ObjectUtils.isNotEmpty(detail) && ObjectUtils.isNotNull(detail.getProductInfo())
                && ObjectUtils.isNotNull(detail.getProductInfo().getProductCategory())) {
                productCategoryCode = detail.getProductInfo().getProductCategory().getPathCode();
            }
        }
        List<OutsourceCompanyVO> list = outsourceCompanyMapper.list(productCategoryCode);
        if (ObjectUtils.isNotEmpty(list)) {
            list.forEach(vo -> {
                if (ObjectUtils.isNotEmpty(vo.getOutsourceScope())) {
                    Set<String> set = vo.getOutsourceScope().stream().map(scope -> {
                        if (StringUtils.isBlank(scope.getDeviceGenreId())) {
                            return "";
                        }
                        String[] split = scope.getDeviceGenreId().split("-");
                        return split.length > 0 ? split[split.length - 1] : "";
                    }).collect(Collectors.toSet());
                    List<ProductCategoryInfoDTO> infoDTOS = productCategoryClient.list(set);
                    if (ObjectUtils.isNotEmpty(infoDTOS)) {
                        Map<String, String> collect = infoDTOS.stream().filter(ObjectUtils::isNotEmpty)
                                .collect(Collectors.toMap(ProductCategoryInfoDTO::getPathCode, ProductCategoryInfoDTO::getName));
                        vo.getOutsourceScope().forEach(scope -> {
                            scope.setName(collect.get(scope.getDeviceGenreId()));
                        });
                    }
                }
            });
        }
        return list;
    }

    @GetMapping("/outsource/company/{id}")
    public OutsourceCompanyDetailVO get(@PathVariable("id") Long id) {
        OutsourceCompanyPO outsourceCompanyPO = this.outsourceCompanyMapper.selectById(id);
        if (Objects.nonNull(outsourceCompanyPO)) {
            OutsourceCompanyDetailVO outsourceCompanyDetailVO = ObjectUtils.copying(outsourceCompanyPO, OutsourceCompanyDetailVO.class);
            outsourceCompanyDetailVO.setOutsourceModelIdList(outsourceCompanyPO.getOutsourceModelList());
            outsourceCompanyDetailVO.setOutsourceScopeIdList(outsourceCompanyPO.getOutsourceScopeList());

            return outsourceCompanyDetailVO;
        }

        return null;
    }

    @PostMapping("/outsource/company")
    @TransactionalEventListener
    public void increase(@RequestBody @Valid IncreaseOutsourceCompanyCommand increaseOutsourceCompanyCommand) {
        OutsourceCompanyPO outsourceCompanyPO = ObjectUtils.copying(increaseOutsourceCompanyCommand, OutsourceCompanyPO.class);
        outsourceCompanyPO.setOutsourceModel(increaseOutsourceCompanyCommand.getOutsourceModelId());
        outsourceCompanyPO.setOutsourceScope(increaseOutsourceCompanyCommand.getOutsourceScopeId());

        this.outsourceCompanyMapper.insert(outsourceCompanyPO);

        applicationEventPublisher.publishEvent(new IncreaseOutsourceCompanyEvent(
                this,
                increaseOutsourceCompanyCommand,
                outsourceCompanyPO.getId()
        ));
    }

    @PutMapping("/outsource/company/change")
    public void change(@RequestBody @Valid ChangeOutsourceCompanyCommand changeOutsourceCompanyCommand) {
        OutsourceCompanyPO outsourceCompanyPO = outsourceCompanyMapper.selectOne(
                Wrappers.<OutsourceCompanyPO>lambdaQuery()
                        .eq(OutsourceCompanyPO::getName, changeOutsourceCompanyCommand.getName())
                                                                                );
        if (Objects.nonNull(outsourceCompanyPO) && !outsourceCompanyPO.getId().equals(changeOutsourceCompanyCommand.getId())) {
            throw new ServiceException("公司名称不能重复");
        }
        OutsourceCompanyPO companyPO = this.outsourceCompanyMapper.selectById(changeOutsourceCompanyCommand.getId());
        ObjectUtils.copying(changeOutsourceCompanyCommand, companyPO);
        companyPO.setOutsourceModel(changeOutsourceCompanyCommand.getOutsourceModelId());
        companyPO.setOutsourceScope(changeOutsourceCompanyCommand.getOutsourceScopeId());
        this.outsourceCompanyMapper.updateById(companyPO);

        applicationEventPublisher.publishEvent(new ChangeOutsourceCompanyModelEvent(
                this,
                changeOutsourceCompanyCommand.getId(),
                changeOutsourceCompanyCommand.getOutsourceModelIdList()
        ));

        applicationEventPublisher.publishEvent(new ChangeOutsourceCompanyScopeEvent(
                this,
                changeOutsourceCompanyCommand.getId(),
                changeOutsourceCompanyCommand.getOutsourceScopeIdList()
        ));
    }

    @DeleteMapping("/outsource/company/{id}")
    public void delete(@PathVariable("id") Long id) {
        DatabaseContextHolder.softDelete(this.companyService, id);
        applicationEventPublisher.publishEvent(
                new DeleteOutsourceCompanyEvent(
                        this,
                        id
                )
                                              );
    }
}

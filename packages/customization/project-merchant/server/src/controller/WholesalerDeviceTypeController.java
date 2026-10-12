package com.guhecloud.cube.deliver.project.merchant.controller;

import com.guhecloud.cube.deliver.project.merchant.dto.WholesalerDeviceTypeDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerDeviceType;
import com.guhecloud.cube.deliver.project.merchant.service.WholesalerDeviceTypeService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.List;

/**
 * <p>
 * 商户设备类型 前端控制器
 * </p>
 *
 * @author bq
 * @since 2022-03-08
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WholesalerDeviceTypeController {

    private final WholesalerDeviceTypeService deviceTypeService;

    @GetMapping("/wholesaler-device-types/{id}")
    public WholesalerDeviceTypeDTO getById(@PathVariable("id") String id) {
        return new WholesalerDeviceTypeDTO(deviceTypeService.getById(id));
    }

    @GetMapping("/wholesaler-device-types/list")
    public List<WholesalerDeviceTypeDTO> getList() {
        List<WholesalerDeviceType> types = deviceTypeService.lambdaQuery().list();
        return new WholesalerDeviceTypeDTO(types).list();
    }

    @GetMapping("/wholesaler-device-types/all-graph")
    public List<WholesalerDeviceTypeDTO> getAllGraph() {
        List<WholesalerDeviceType> rootTypes = deviceTypeService.lambdaQuery()
                .eq(WholesalerDeviceType::getPid, "")
                .orderByAsc(WholesalerDeviceType::getSort)
                .list();
        List<WholesalerDeviceTypeDTO> rootTypeDTOS = new WholesalerDeviceTypeDTO(rootTypes).list();
        rootTypeDTOS.parallelStream().forEach(this::getChildAreas);
        return rootTypeDTOS;
    }

    @PostMapping("/wholesaler-device-types")
    public void save(@Valid @RequestBody WholesalerDeviceTypeDTO dto) {
        deviceTypeService.save(dto);
    }

    @PutMapping("/wholesaler-device-types")
    public void update(@Valid @RequestBody WholesalerDeviceTypeDTO dto) {
        deviceTypeService.update(dto);
    }

    @DeleteMapping("/wholesaler-device-types/{id}")
    public void remove(@PathVariable("id") String id) {
        deviceTypeService.remove(id);
    }

    private void getChildAreas(WholesalerDeviceTypeDTO rootTypeDTO) {
        List<WholesalerDeviceType> childTypes = deviceTypeService.lambdaQuery()
                .eq(WholesalerDeviceType::getPid, rootTypeDTO.getId())
                .orderByAsc(WholesalerDeviceType::getSort)
                .list();
        List<WholesalerDeviceTypeDTO> childTypeDTOS = new WholesalerDeviceTypeDTO(childTypes).list();
        childTypeDTOS.parallelStream().forEach(this::getChildAreas);
        rootTypeDTO.setChildren(childTypeDTOS);
    }

}


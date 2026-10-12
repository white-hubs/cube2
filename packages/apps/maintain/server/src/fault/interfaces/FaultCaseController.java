package com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.device.center.metadata.dto.ProductCategoryInfoDTO;
import com.guhecloud.cube.base.device.center.metadata.std.param.ProductCategoryQry;
import com.guhecloud.cube.base.device.center.metadata.std.vo.ProductCategoryDTO;
import com.guhecloud.cube.base.device.center.std.devcenter.ProductCategoryClient;
import com.guhecloud.cube.base.device.center.std.devcenter.ProductClient;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.srv.gen.maintain.server.fault.commandgateway.FaultCaseCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.fault.commandgateway.command.FaultCaseCommand;
import com.guhecloud.cube.srv.gen.maintain.server.fault.dao.FaultCaseMapper;
import com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.vo.FaultCaseDeviceVO;
import com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.vo.FaultCaseTypeVO;
import com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.vo.FaultCaseVO;
import com.guhecloud.cube.srv.gen.maintain.server.fault.po.FaultCase;
import com.guhecloud.cube.srv.gen.maintain.server.fault.po.FaultType;
import com.guhecloud.cube.srv.gen.maintain.server.fault.querygateway.FaultCaseQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.fault.querygateway.FaultTypeQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.fault.querygateway.query.FaultCaseCollectionQuery;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import jakarta.validation.Valid;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 故障案例接口
 *
 * @author lzy
 * @date 2022/5/6
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FaultCaseController {

    private final FaultCaseCommandService faultCaseCommandService;
    private final FaultCaseQueryService faultCaseQueryService;
    private final FaultTypeQueryService faultTypeQueryService;
    private final ProductClient productClient;
    private final FaultCaseMapper faultCaseMapper;
    private final FileClient fileClient;
    private final ProductCategoryClient productCategoryClient;

    /**
     * 故障案例新增
     *
     * @param command 新增参数
     */
    @PostMapping("/fault-case")
    public void save(@Valid @RequestBody FaultCaseCommand command) {
        this.faultCaseCommandService.save(command);
    }

    /**
     * 故障案例编辑
     *
     * @param id      主键
     * @param command 编辑参数
     */
    @PutMapping("/fault-case/{id}")
    public void update(@PathVariable("id") Long id, @Valid @RequestBody FaultCaseCommand command) {
        this.faultCaseCommandService.update(id, command);
    }

    /**
     * 故障案例删除
     *
     * @param id 主键
     */
    @DeleteMapping("/fault-case/{id}")
    public void remove(@PathVariable("id") Long id) {
        this.faultCaseCommandService.remove(id);
    }

    /**
     * 故障案例详情
     *
     * @param id 主键
     *
     * @return 故障案例信息
     */
    @GetMapping("/fault-case/{id}")
    public FaultCaseVO getById(@PathVariable("id") Long id) {
        // 获取故障案例信息
        FaultCase faultCase = this.faultCaseQueryService.getById(id);
        Assert.isNotFound(faultCase, "不存在故障案例信息!");
        // 文件信息
        List<FileVO> files = this.fileClient.getByBusinessKey(faultCase.getId());
        // 构建信息
        FaultCaseVO faultCaseVO = ObjectUtils.copying(faultCase, FaultCaseVO.class);
        faultCaseVO.setFiles(files);
        List<ProductCategoryInfoDTO> list = productCategoryClient.list(Collections.singleton(faultCaseVO.getProductCategoryCode()));
        if (ObjectUtils.isNotEmpty(list)) {
            faultCaseVO.setProductCategoryName(list.getFirst().getName());
        }
        return faultCaseVO;
    }

    /**
     * 故障案例分页信息
     *
     * @param query 查询参数
     *
     * @return 故障案例信息
     */
    @GetMapping("/fault-case/page")
    public Page<FaultCaseVO> pageFaultCase(@Valid FaultCaseCollectionQuery query) {
        // 获取故障案例分页信息
        Page<FaultCaseVO> faultCaseVOPage = this.faultCaseQueryService.pageFaultCase(query);
        Set<String> set = faultCaseVOPage.getRecords().stream().map(vo -> {
            if (StringUtils.isBlank(vo.getProductCategoryCode())) {
                return "";
            }
            String[] split = vo.getProductCategoryCode().split("-");
            return split.length > 0 ? split[split.length - 1] : "";
        }).filter(StringUtils::isNotBlank).collect(Collectors.toSet());
        List<ProductCategoryInfoDTO> list = productCategoryClient.list(set);
        Map<String, String> map = new HashMap<>();
        if (ObjectUtils.isNotEmpty(list)) {
            map = list.stream().filter(ObjectUtils::isNotEmpty)
                    .collect(Collectors.toMap(ProductCategoryInfoDTO::getCode, ProductCategoryInfoDTO::getName, (k1, k2) -> k1));
        }
        for (FaultCaseVO s : faultCaseVOPage.getRecords()) {
            s.setProductCategoryName(map.get(s.getProductCategoryCode()));
            if (Objects.nonNull(s.getId())) {
                List<FileVO> files = this.fileClient.getByBusinessKey(s.getId());
                if (files.size() > 0) {
                    s.setFiles(files);
                }
            }
        }
        return faultCaseVOPage;
    }

    /**
     * 故障类型关联故障案例列表
     *
     * @return 故障类型列表信息
     */
    @GetMapping("/fault-case/device-type/list")
    public List<FaultCaseDeviceVO> listFaultCaseDevice(@Valid FaultCaseCollectionQuery query) {
        //  获取所有案例信息
        List<FaultCase> faultCases = this.faultCaseQueryService.listFaultCase(query);
        if (CollectionUtils.isEmpty(faultCases)) {
            return Collections.emptyList();
        }
        // 构建设备类型/故障案例信息 映射关系
        Map<String, Long> collect = faultCases.stream()
                .collect(Collectors.groupingBy(FaultCase::getProductCategoryCode, Collectors.counting()));
        Map<String, Long> firstLevel = new HashMap<>();
        Map<String, Long> secondLevel = new HashMap<>();
        Map<String, Long> thirdLevel = new HashMap<>();
        collect.forEach((key, value) -> {
            String[] split = key.split("-");
            for (int i = 0; i < split.length; i++) {
                if (i == 0) {
                    if (firstLevel.containsKey(split[i])) {
                        firstLevel.put(split[i], firstLevel.get(split[i]) + value);
                    } else {
                        firstLevel.put(split[i], value);
                    }
                } else if (i == 1) {
                    if (secondLevel.containsKey(split[i])) {
                        secondLevel.put(split[i], secondLevel.get(split[i]) + value);
                    } else {
                        secondLevel.put(split[i], value);
                    }
                } else if (i == 2) {
                    if (thirdLevel.containsKey(split[i])) {
                        thirdLevel.put(split[i], thirdLevel.get(split[i]) + value);
                    } else {
                        thirdLevel.put(split[i], value);
                    }
                }
            }
        });
        List<ProductCategoryDTO> productCategoryTree = productClient.getProductCategoryTree(new ProductCategoryQry());
        List<FaultCaseDeviceVO> countCateGoryTree = new ArrayList<>();
        for (ProductCategoryDTO categoryDTO : productCategoryTree) {
            if (firstLevel.containsKey(categoryDTO.getCategoryCode())) {
                FaultCaseDeviceVO caseDeviceVO = new FaultCaseDeviceVO(categoryDTO.getCategoryCode(), categoryDTO.getCategoryName(), firstLevel.get(categoryDTO.getCategoryCode()));
                List<ProductCategoryDTO> children = categoryDTO.getChildren();
                if (ObjectUtils.isNotEmpty(children)) {
                    List<FaultCaseDeviceVO> voList = new ArrayList<>();
                    for (ProductCategoryDTO child : children) {
                        if (secondLevel.containsKey(child.getCategoryCode())) {
                            FaultCaseDeviceVO childVO = new FaultCaseDeviceVO(child.getCategoryCode(), child.getCategoryName(), secondLevel.get(child.getCategoryCode()));
                            List<ProductCategoryDTO> subChild = child.getChildren();
                            if (ObjectUtils.isNotEmpty(subChild)) {
                                List<FaultCaseDeviceVO> childList = new ArrayList<>();
                                for (ProductCategoryDTO dto : subChild) {
                                    if (thirdLevel.containsKey(dto.getCategoryCode())) {
                                        childList.add(new FaultCaseDeviceVO(dto.getCategoryCode(), dto.getCategoryName(), thirdLevel.get(dto.getCategoryCode())));
                                    }
                                }
                                childVO.setChildren(childList);
                            }
                            voList.add(childVO);
                        }
                    }
                    caseDeviceVO.setChildren(voList);
                }
                countCateGoryTree.add(caseDeviceVO);
            }
        }
        return countCateGoryTree;
    }

    /**
     * 故障案例根据故障分类获取列表
     *
     * @param query 查询参数
     *
     * @return 故障案例信息
     */
    @GetMapping("/fault-case/fault-type/list")
    public List<FaultCaseTypeVO> pageFaultCaseType(@Valid FaultCaseCollectionQuery query) {
        // 获取故障案例信息
        List<FaultCase> faultCases = this.faultCaseQueryService.listFaultCase(query);

        // 获取故障分类信息
        List<FaultType> faultTypes = this.faultTypeQueryService.list();

        // 构建信息
        return this.buildFaultCaseTypeInfo(faultCases, faultTypes);
    }

    private List<FaultCaseTypeVO> buildFaultCaseTypeInfo(List<FaultCase> faultCases, List<FaultType> faultTypes) {
        Map<Long, List<FaultCase>> faultCaseMap = faultCases.stream().collect(Collectors.groupingBy(FaultCase::getFaultTypeId));
        return faultTypes.stream().map(faultType -> {
            FaultCaseTypeVO faultCaseTypeVO = new FaultCaseTypeVO();
            faultCaseTypeVO.setFaultTypeId(faultType.getId());
            faultCaseTypeVO.setFaultTypeName(faultType.getName());
            if (faultCaseMap.containsKey(faultType.getId())) {
                List<FaultCase> cases = faultCaseMap.get(faultType.getId());
                faultCaseTypeVO.setFaultCases(ObjectUtils.copying(cases, FaultCaseVO.class));
            } else {
                faultCaseTypeVO.setFaultCases(Collections.emptyList());
            }
            return faultCaseTypeVO;
        }).collect(Collectors.toList());
    }

    /**
     * @Author wql
     * @Description 故障案例总数统计
     * @Date 17:48 2023/11/10
     **/
    @GetMapping("/fault/stat/summary")
    public Long countCase() {
        return faultCaseQueryService.countCase();
    }

}

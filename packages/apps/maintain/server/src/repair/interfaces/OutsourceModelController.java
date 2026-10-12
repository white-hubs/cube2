package com.guhecloud.cube.srv.gen.maintain.server.repair.interfaces;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.maintain.server.repair.commandgateway.command.model.ChangeOutsourceModelCommand;
import com.guhecloud.cube.srv.gen.maintain.server.repair.commandgateway.command.model.IncreaseOutsourceModelCommand;
import com.guhecloud.cube.srv.gen.maintain.server.repair.commandgateway.impl.OutsourceCompanyModelService;
import com.guhecloud.cube.srv.gen.maintain.server.repair.commandgateway.impl.OutsourceCompanyService;
import com.guhecloud.cube.srv.gen.maintain.server.repair.commandgateway.impl.OutsourceModelService;
import com.guhecloud.cube.srv.gen.maintain.server.repair.dao.OutsourceModelMapper;
import com.guhecloud.cube.srv.gen.maintain.server.repair.interfaces.vo.OutsourceModelVO;
import com.guhecloud.cube.srv.gen.maintain.server.repair.po.OutsourceCompanyModelPO;
import com.guhecloud.cube.srv.gen.maintain.server.repair.po.OutsourceCompanyPO;
import com.guhecloud.cube.srv.gen.maintain.server.repair.po.OutsourceModelPO;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author pengjian
 * @date 2022/5/18 15:11
 * @description 委外模式
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OutsourceModelController {
    private final OutsourceModelMapper outsourceModelMapper;
    private final OutsourceModelService modelService;
    private final OutsourceCompanyModelService companyModelCommandService;
    private final OutsourceCompanyService companyService;

    @PostMapping("/outsource/model")
    public void increase(@RequestBody @Valid IncreaseOutsourceModelCommand increaseOutsourceModelCommand) {

        OutsourceModelPO modelPO = outsourceModelMapper.selectOne(
                Wrappers.<OutsourceModelPO>lambdaQuery()
                        .eq(OutsourceModelPO::getName, increaseOutsourceModelCommand.getName())
                                                                 );
        if (Objects.nonNull(modelPO)) {
            throw new ServiceException("名称重复");
        }

        OutsourceModelPO outsourceModelPO = new OutsourceModelPO(IdWorker.getId(), increaseOutsourceModelCommand.getName());
        outsourceModelMapper.insert(outsourceModelPO);
    }

    @GetMapping("/outsource/model/list")
    public List<OutsourceModelVO> list() {
        List<OutsourceModelPO> outsourceModelPOS = outsourceModelMapper.selectList(Wrappers.emptyWrapper());
        return ObjectUtils.copying(outsourceModelPOS, OutsourceModelVO.class);
    }

    /**
     * 根据公司类型查询委外模式
     *
     * @param companyId 公司id
     *
     * @return 委外模式
     */
    @GetMapping("/outsource/model/listByCompany")
    public List<OutsourceModelVO> listByCompany(@Nullable Long companyId) {
        List<OutsourceModelPO> outsourceModelPOS = outsourceModelMapper.listByCompany(companyId);
        return ObjectUtils.copying(outsourceModelPOS, OutsourceModelVO.class);
    }

    @PutMapping("/outsource/model")
    public void change(@RequestBody @Valid ChangeOutsourceModelCommand changeOutsourceModelCommand) {
        OutsourceModelPO modelPO = outsourceModelMapper.selectOne(
                Wrappers.<OutsourceModelPO>lambdaQuery()
                        .eq(OutsourceModelPO::getName, changeOutsourceModelCommand.getName())
                                                                 );
        if (Objects.nonNull(modelPO) && !modelPO.getId().equals(changeOutsourceModelCommand.getId())) {
            throw new ServiceException("名称重复");
        }

        OutsourceModelPO outsourceModelPO = outsourceModelMapper.selectById(changeOutsourceModelCommand.getId());

        ObjectUtils.copying(changeOutsourceModelCommand, outsourceModelPO);
        outsourceModelMapper.updateById(outsourceModelPO);
    }

    @DeleteMapping("/outsource/model/{id}")
    public void delete(@PathVariable("id") Long id) {
        DatabaseContextHolder.softDelete(this.modelService, id);
        List<OutsourceCompanyModelPO> list = this.companyModelCommandService.list(
                Wrappers.<OutsourceCompanyModelPO>lambdaQuery()
                        .eq(OutsourceCompanyModelPO::getOutsourceModelId, id)
                                                                                 );

        companyModelCommandService.remove(
                Wrappers.<OutsourceCompanyModelPO>lambdaQuery()
                        .eq(OutsourceCompanyModelPO::getOutsourceModelId, id)
                                         );

        // 1.找到关联此模式的公司
        if (CollectionUtils.isNotEmpty(list)) {
            Map<Long, List<OutsourceCompanyModelPO>> companyToModel = this.companyModelCommandService.list(
                    Wrappers.<OutsourceCompanyModelPO>lambdaQuery()
                            .in(OutsourceCompanyModelPO::getOutsourceCompanyId,
                                list.stream().map(OutsourceCompanyModelPO::getOutsourceCompanyId).collect(Collectors.toSet()))
                                                                                                          ).stream().collect(
                    Collectors.groupingBy(OutsourceCompanyModelPO::getOutsourceCompanyId)
                                                                                                                            );

            Iterator<Map.Entry<Long, List<OutsourceCompanyModelPO>>> iterator = companyToModel.entrySet().iterator();
            List<OutsourceCompanyPO> waitingForUpdate = new ArrayList<>();
            while (iterator.hasNext()) {
                Map.Entry<Long, List<OutsourceCompanyModelPO>> next = iterator.next();
                Long key = next.getKey();
                List<OutsourceCompanyModelPO> value = next.getValue();
                OutsourceCompanyPO outsourceCompanyPO = new OutsourceCompanyPO();
                outsourceCompanyPO.setId(key);
                outsourceCompanyPO.setOutsourceModel(
                        JSONObject.toJSONString(value.stream().map(OutsourceCompanyModelPO::getOutsourceModelId)
                                                        .collect(Collectors.toSet()))
                                                    );
                waitingForUpdate.add(outsourceCompanyPO);
            }

            this.companyService.updateBatchById(waitingForUpdate);

        }
    }
}

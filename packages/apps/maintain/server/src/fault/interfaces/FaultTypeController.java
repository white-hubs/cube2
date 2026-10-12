package com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces;

import com.guhecloud.cube.srv.gen.maintain.server.fault.commandgateway.FaultTypeCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.fault.commandgateway.command.FaultTypeCommand;
import com.guhecloud.cube.srv.gen.maintain.server.fault.interfaces.vo.FaultTypeVO;
import com.guhecloud.cube.srv.gen.maintain.server.fault.po.FaultType;
import com.guhecloud.cube.srv.gen.maintain.server.fault.querygateway.FaultTypeQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.fault.querygateway.query.FaultTypeCollectionQuery;
import com.guhecloud.platform.base.util.ObjectUtils;
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

import jakarta.validation.Valid;

import java.util.List;

/**
 * 故障类型接口
 *
 * @author lzy
 * @date 2022/5/4
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FaultTypeController {

    private final FaultTypeCommandService faultTypeCommandService;
    private final FaultTypeQueryService faultTypeQueryService;

    /**
     * 故障类型新增
     *
     * @param command 新增参数
     */
    @PostMapping("/fault-type")
    public void save(@Valid @RequestBody FaultTypeCommand command) {
        this.faultTypeCommandService.save(command);
    }

    /**
     * 故障类型编辑
     *
     * @param id      主键
     * @param command 编辑参数
     */
    @PutMapping("/fault-type/{id}")
    public void update(@PathVariable("id") Long id, @Valid @RequestBody FaultTypeCommand command) {
        this.faultTypeCommandService.update(id, command);
    }

    /**
     * 故障类型删除
     *
     * @param id 主键
     */
    @DeleteMapping("/fault-type/{id}")
    public void remove(@PathVariable("id") Long id) {
        this.faultTypeCommandService.remove(id);
    }

    /**
     * 故障类型列表
     *
     * @param query 查询参数
     *
     * @return 故障类型信息
     */
    @GetMapping("/fault-type/list")
    public List<FaultTypeVO> listFaultType(@Valid FaultTypeCollectionQuery query) {
        List<FaultType> faultTypes = this.faultTypeQueryService.listFaultType(query);
        return ObjectUtils.copying(faultTypes, FaultTypeVO.class);
    }

}

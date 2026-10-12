package com.guhecloud.cube.srv.gen.property.management.server.property.controller.assembly;

import com.guhecloud.cube.property.management.client.property.param.property.AssemblyClassifyPageParam;
import com.guhecloud.cube.property.management.client.property.param.property.AssemblyClassifyParam;
import com.guhecloud.cube.property.management.client.property.vo.assembly.AssemblyClassifyPageVO;
import com.guhecloud.cube.property.management.client.property.vo.assembly.AssemblyClassifyTreeVO;
import com.guhecloud.cube.property.management.client.property.vo.assembly.AssemblyClassifyVO;
import com.guhecloud.cube.srv.gen.property.management.server.property.service.assembly.AssemblyClassifyService;
import com.guhecloud.platform.base.model.Pagination;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 通用分类控制层
 *
 * @author : lijie
 * @date : 2023-3-2
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AssemblyClassifyController {
    private final AssemblyClassifyService assemblyClassifyService;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     *
     * @return 实例对象
     */
    @GetMapping("/classify/queryById")
    public AssemblyClassifyVO queryById(Long id) {
        return assemblyClassifyService.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param pageParam 筛选条件
     *
     * @return 查询结果
     */
    @GetMapping("/classify/getAssemblyClassifyPage")
    public Pagination<AssemblyClassifyPageVO> getAssemblyClassifyPage(@Valid AssemblyClassifyPageParam pageParam) {
        return assemblyClassifyService.getAssemblyClassifyPage(pageParam);
    }

    /**
     * 查询树结构
     *
     * @param type 类型
     *
     * @return 树
     */
    @GetMapping("/classify/getAssemblyClassifyTree")
    public List<AssemblyClassifyTreeVO> getAssemblyClassifyTree(@RequestParam("type") String type) {
        return assemblyClassifyService.getAssemblyClassifyTree(null, type);
    }

    /**
     * 新增数据
     *
     * @param assemblyClassify 实例对象
     *
     * @return 实例对象
     */
    @PostMapping("/classify/insert")
    public Boolean insert(@RequestBody @Valid AssemblyClassifyParam assemblyClassify) {
        return assemblyClassifyService.insert(assemblyClassify);
    }

    /**
     * 更新数据
     *
     * @param assemblyClassify 实例对象
     *
     * @return 实例对象
     */
    @PutMapping("/classify/update")
    public Boolean update(@RequestBody @Valid AssemblyClassifyParam assemblyClassify) {
        return assemblyClassifyService.update(assemblyClassify);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     *
     * @return 是否成功
     */
    @DeleteMapping("/classify/deleted/{id}")
    public Boolean deleteById(@PathVariable Long id) {
        return assemblyClassifyService.deleteById(id);
    }
}

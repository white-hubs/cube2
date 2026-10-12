package com.guhecloud.cube.srv.gen.property.management.server.property.service.assembly;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.property.management.client.property.param.property.AssemblyClassifyPageParam;
import com.guhecloud.cube.property.management.client.property.param.property.AssemblyClassifyParam;
import com.guhecloud.cube.property.management.client.property.vo.assembly.AssemblyClassifyPageVO;
import com.guhecloud.cube.property.management.client.property.vo.assembly.AssemblyClassifyTreeVO;
import com.guhecloud.cube.property.management.client.property.vo.assembly.AssemblyClassifyVO;
import com.guhecloud.cube.srv.gen.property.management.server.property.entity.assembly.AssemblyClassify;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 通用分类;服务接口
 *
 * @author :lijie
 * @date : 2023-3-2
 */
public interface AssemblyClassifyService extends IService<AssemblyClassify> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     *
     * @return 实例对象
     */
    AssemblyClassifyVO queryById(Long id);

    /**
     * 分页查询
     *
     * @param pageParam 筛选条件
     *
     * @return
     */
    Pagination<AssemblyClassifyPageVO> getAssemblyClassifyPage(AssemblyClassifyPageParam pageParam);

    List<AssemblyClassifyTreeVO> getAssemblyClassifyTree(String name, String type);

    AssemblyClassifyTreeVO getAssemblyClassifyTree(Long id);

    /**
     * 新增数据
     *
     * @param assemblyClassify 实例对象
     *
     * @return 实例对象
     */
    Boolean insert(AssemblyClassifyParam assemblyClassify);

    /**
     * 更新数据
     *
     * @param assemblyClassify 实例对象
     *
     * @return 实例对象
     */
    Boolean update(AssemblyClassifyParam assemblyClassify);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     *
     * @return 是否成功
     */
    Boolean deleteById(Long id);
}

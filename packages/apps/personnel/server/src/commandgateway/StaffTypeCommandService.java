package com.guhecloud.cube.srv.gen.personnel.management.server.commandgateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.personnel.management.server.interfaces.params.StaffTypeParam;
import com.guhecloud.cube.srv.gen.personnel.management.server.interfaces.vo.StaffTypeVO;
import com.guhecloud.cube.srv.gen.personnel.management.server.po.StaffTypePO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;
import java.util.Map;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/12/5 19:07
 * @Description:
 */
public interface StaffTypeCommandService extends IService<StaffTypePO> {
    /**
     * 新增或更新员工类型
     *
     * @param projectTypeVO
     *
     * @return
     */
    boolean saveOrUpdateStaffType(StaffTypeVO projectTypeVO);

    /**
     * 获取员工类型分页数据
     *
     * @param param
     *
     * @return
     */
    Pagination<StaffTypeVO> getStaffTypePage(StaffTypeParam param);

    /**
     * 获取员工类型映射map
     *
     * @return
     */

    List<Map<String, String>> getStaffTypeMap();

    /**
     * 员工类型解析
     *
     * @param code
     *
     * @return
     */
    String valueOfType(String code);

    /**
     * 根据指定返回对象类型映射
     *
     * @param voClassList 返回对象实体
     * @param codeField   实体中 类型字段名
     * @param <T>
     *
     * @return
     */
    <T> Map<String, String> getStaffTypeByVOList(List<T> voClassList, String codeField);
}

package com.guhecloud.cube.srv.gen.property.management.server.property.controller.depository;

import com.guhecloud.cube.property.management.client.property.client.property.DepositoryDetailedClient;
import com.guhecloud.cube.property.management.client.property.param.property.DepositoryDetailedPageExcelParam;
import com.guhecloud.cube.property.management.client.property.param.property.DepositoryDetailedPageParam;
import com.guhecloud.cube.property.management.client.property.param.property.DepositoryDetailedParam;
import com.guhecloud.cube.property.management.client.property.vo.property.DepositoryDetailedPageVO;
import com.guhecloud.cube.srv.gen.property.management.server.property.service.depository.DepositoryDetailedService;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * 入库基本信息
 *
 * @author : lijie
 * @date : 2023-3-3
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DepositoryDetailedController implements DepositoryDetailedClient {
    private final DepositoryDetailedService depositoryDetailedService;

    /**
     * 通过ID查询详情
     *
     * @param id
     *
     * @return 入库详情
     */
    @GetMapping("/property/depository/queryById/{id}")
    public DepositoryDetailedPageVO queryById(@PathVariable Long id) {
        return depositoryDetailedService.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param depositoryDetailed 筛选条件
     *
     * @return 查询结果
     */
    @GetMapping("/property/depository/getDepositoryDetailedPage")
    public Pagination<DepositoryDetailedPageVO> getDepositoryDetailedPage(@Valid DepositoryDetailedPageParam depositoryDetailed) {
        return depositoryDetailedService.getDepositoryDetailedPage(depositoryDetailed);
    }

    /**
     * 资产入库导出
     *
     * @param param 入库id
     */
    @PostMapping("/property/depository/getDepositoryDetailedExcel")
    public void getDepositoryDetailedExcel(HttpServletResponse response, HttpServletRequest request,
                                           @RequestBody DepositoryDetailedPageExcelParam param
                                          ) throws IOException {
        depositoryDetailedService.getDepositoryDetailedExcel(response, request, param.getIds());
    }

    /**
     * 新增-资产入库
     *
     * @param depositoryDetailed 入库信息
     *
     * @return 入库结果
     */
    @PostMapping("/property/depository/insertd")
    public Long insert(@RequestBody @Valid DepositoryDetailedParam depositoryDetailed) {
        return depositoryDetailedService.insert(depositoryDetailed);
    }

    /**
     * 删除资产入库记录
     *
     * @param id 主键
     *
     * @return 是否成功
     */
    @DeleteMapping("/property/depository/deleteById/{id}")
    public Boolean deleteById(@PathVariable Long id) {
        return depositoryDetailedService.deleteById(id);
    }
}
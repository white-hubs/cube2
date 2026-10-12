package com.guhecloud.cube.srv.gen.supplier.server.supplier.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.biz.mdata.client.dto.SupplierDTO;
import com.guhecloud.cube.srv.gen.supplier.server.supplier.entity.GenSupplier;
import com.guhecloud.cube.srv.gen.supplier.server.supplier.param.GenSupplierModifyParam;
import com.guhecloud.cube.srv.gen.supplier.server.supplier.param.GenSupplierQueryParam;
import com.guhecloud.cube.srv.gen.supplier.server.supplier.param.GenSupplierTemplate;
import com.guhecloud.cube.srv.gen.supplier.server.supplier.param.UpdateDisableParam;
import com.guhecloud.cube.srv.gen.supplier.server.supplier.service.GenSupplierService;
import com.guhecloud.cube.srv.gen.supplier.server.supplier.vo.GenSupplierVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 批发商管理接口
 *
 * @author lzy
 * @date 2021/12/3
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GenSupplierController {

    private final GenSupplierService supplierService;

    /**
     * 新增批发商
     *
     * @param param 新增参数
     */
    @PostMapping("/gen-supplier")
    public void addSupplier(@RequestBody GenSupplierModifyParam param) {
        this.supplierService.addSupplier(param);
    }

    /**
     * @Description 批量导入批发商
     * @Author lrj
     */
    @PostMapping("/gen-import-supplier")
    public void importSupplierTemplate(@RequestPart("file") MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            ExcelReader reader = ExcelUtil.getReader(inputStream);
            List<Map<String, Object>> readAll = reader.readAll();
            if (CollectionUtils.isNotEmpty(readAll)) {
                List<GenSupplierModifyParam> objects = new ArrayList<>();
                for (Map<String, Object> map : readAll) {
                    GenSupplierModifyParam supplierModifyParam = new GenSupplierModifyParam();
                    for (String s : map.keySet()) {
                        Object o = map.get(s);
                        if (s.equals("店铺名称")) {
                            supplierModifyParam.setName(o.toString());
                        } else if (s.equals("档主编号")) {
                            supplierModifyParam.setCode(o.toString());
                        } else if (s.equals("档主姓名")) {
                            supplierModifyParam.setShiftMaster(o.toString());
                        } else if (s.equals("档主电话")) {
                            supplierModifyParam.setShiftMasterPhone(o.toString());
                        } else if (s.equals("档主身份证")) {
                            supplierModifyParam.setShiftMasterCard(o.toString());
                        } else if (s.equals("联系人")) {
                            supplierModifyParam.setLiaison(o.toString());
                        } else if (s.equals("联系人手机号")) {
                            supplierModifyParam.setPhone(o.toString());
                        } else if (s.equals("备注说明")) {
                            supplierModifyParam.setRemark(o.toString());
                        }

                    }
                    objects.add(supplierModifyParam);
                }
                supplierService.importSupplierTemplate(objects);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 批发商导模板
     */
    @GetMapping("/gen-supplier-template")
    public void supplierTemplate(HttpServletResponse response) throws UnsupportedEncodingException {
        GenSupplierTemplate excelTemplate = new GenSupplierTemplate();
        List<GenSupplierTemplate> excelList = new ArrayList<>();
        excelList.add(excelTemplate);
        ExcelWriter writer = ExcelUtil.getWriter();
        writer.addHeaderAlias("name", "店铺名称");
        writer.addHeaderAlias("code", "档主编号");
        writer.addHeaderAlias("shiftMaster", "档主姓名");
        writer.addHeaderAlias("shiftMasterPhone", "档主电话");
        writer.addHeaderAlias("shiftMasterCard", "档主身份证");
        writer.addHeaderAlias("liaison", "联系人");
        writer.addHeaderAlias("phone", "联系人手机号");
        writer.addHeaderAlias("remark", "备注说明");
        writer.setOnlyAlias(true);//排除字段操作
        writer.write(excelList, true);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset:utf-8");
        String name = "批发商导表模板";
        String fileName = URLEncoder.encode(name, "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        try (var out = response.getOutputStream()) {
            writer.flush(out, true);
        } catch (IOException e) {
            log.error("批发商导表模板下载异常: {}", e.getMessage());
        } finally {
            writer.close();
        }
    }

    /**
     * 编辑批发商
     *
     * @param id    批发商主键
     * @param param 编辑参数
     */
    @PutMapping("/gen-supplier/{id}")
    public void updateSupplier(@PathVariable("id") Long id, @RequestBody GenSupplierModifyParam param) {
        this.supplierService.updateSupplier(id, param);
    }

    /**
     * 批发商资料详细信息
     *
     * @param id 批发商主键
     *
     * @return 批发商信息
     */
    @GetMapping("/gen-supplier/{id}")
    public GenSupplierVO getById(@PathVariable("id") Long id) {
        // 获取批发商信息
        GenSupplier supplier = this.supplierService.getById(id);
        Assert.isNotFound(supplier, "不存在批发商信息!");
        GenSupplierVO supplierVO = ObjectUtils.copying(supplier, GenSupplierVO.class);

        // 构建地区信息
        if (StringUtils.isNotEmpty(supplier.getArea())) {
            supplierVO.setAreaIds(Arrays.asList(supplier.getArea().split("-")));
        }

        return supplierVO;
    }

    /**
     * 批发商资料分页信息
     *
     * @param queryParam 查询参数
     *
     * @return 批发商信息
     */
    @GetMapping("/gen-supplier/page")
    public Pagination<GenSupplierVO> pageSupplier(GenSupplierQueryParam queryParam) {
        // 获取批发商资料信息
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        queryParam.setTenant(tenant);
        Pagination<GenSupplierVO> page = this.supplierService.pageSupplierDetail(queryParam);
        return page;
    }

    @GetMapping("/gen-supplier/inner/list")
    public List<SupplierDTO> listSuppliers(@RequestParam("keyword") String keyword, @RequestParam("tenant") Integer tenant) {
        List<GenSupplier> suppliers = this.supplierService.list(Wrappers.<GenSupplier>lambdaQuery()
                                                                        .like(StringUtils.isNotEmpty(keyword), GenSupplier::getName, keyword)
                                                                        .eq(GenSupplier::getDisable, false));
        return ObjectUtils.copying(suppliers, SupplierDTO.class);
    }

    @PostMapping("/update-disable")
    public void updateDisable(@RequestBody List<UpdateDisableParam> updateDisableParams) {
        supplierService.updateDisable(updateDisableParams);
    }

}

package com.guhecloud.cube.srv.gen.stall.server.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.mdata.client.AreaClient;
import com.guhecloud.cube.biz.mdata.client.dto.AreaDTO;
import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.srv.gen.stall.server.dto.GenStallDTO;
import com.guhecloud.cube.srv.gen.stall.server.entity.GenStall;
import com.guhecloud.cube.srv.gen.stall.server.param.GenStallCreateParam;
import com.guhecloud.cube.srv.gen.stall.server.param.GenStallQueryParam;
import com.guhecloud.cube.srv.gen.stall.server.param.GenStallUpdateParam;
import com.guhecloud.cube.srv.gen.stall.server.param.StallExcelTemplate;
import com.guhecloud.cube.srv.gen.stall.server.service.GenStallService;
import com.guhecloud.cube.srv.gen.stall.server.vo.GenStallPageVO;
import com.guhecloud.cube.srv.gen.stall.server.vo.GenStallVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 档位接口
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GenStallController {

    private final GenStallService genStallService;

    private final OrganizationClient organizationClient;

    private final AreaClient areaClient;

    /**
     * 档位新增
     *
     * @param param 档位新增参数
     */
    @PostMapping("/gen-stall")
    public void save(@RequestBody GenStallCreateParam param) {
        this.genStallService.save(param);
    }

    /**
     * @Description 档位导表模板
     * @Author lrj
     */
    @GetMapping("/gen-download-template")
    public void downloadTemplate(HttpServletResponse response) throws UnsupportedEncodingException {
        StallExcelTemplate excelTemplate = new StallExcelTemplate();
        List<StallExcelTemplate> excelList = new ArrayList<>();
        excelList.add(excelTemplate);
        ExcelWriter writer = ExcelUtil.getWriter();
        writer.addHeaderAlias("code", "档位编号");
        writer.addHeaderAlias("name", "档位名称");
        writer.setOnlyAlias(true);//排除字段操作
        writer.write(excelList, true);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset:utf-8");
        String name = "档位导表模板";
        String fileName = URLEncoder.encode(name, "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        try (var out = response.getOutputStream()) {
            writer.flush(out, true);
        } catch (IOException e) {
            log.error("下载档位导表模板失败: {}", e.getMessage());
        } finally {
            writer.close();
        }
    }

    @PostMapping("/gen-import")
    public void importTemplate(@RequestPart("file") MultipartFile file) throws Exception {
        try {
            InputStream inputStream = file.getInputStream();
            ExcelReader reader = ExcelUtil.getReader(inputStream);
            List<Map<String, Object>> readAll = reader.readAll();
            if (CollectionUtils.isNotEmpty(readAll)) {
                List<GenStallCreateParam> objects = new ArrayList<>();
                for (Map<String, Object> map : readAll) {
                    GenStallCreateParam stallCreateParam = new GenStallCreateParam();
                    for (String s : map.keySet()) {
                        Object o = map.get(s);
                        if (s.equals("档位编号")) {
                            stallCreateParam.setCode(o.toString());
                        } else if (s.equals("档位名称")) {
                            stallCreateParam.setName(o.toString());
                        }

                    }
                    objects.add(stallCreateParam);
                }
                genStallService.excelSave(objects);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 档位更新
     *
     * @param param 档位更新参数
     */
    @PutMapping("/gen-stall")
    public void update(@RequestBody GenStallUpdateParam param) {
        this.genStallService.update(param);
    }

    /**
     * 档位ID查询
     *
     * @param id 档位ID
     *
     * @return 档位
     */
    @GetMapping("/gen-stall/{id}")
    public GenStallVO getById(@NotNull(message = "主键不能为空") @Min(value = 1, message = "主键不能小于1")
                              @PathVariable("id") Long id
                             ) {
        GenStall stall = this.genStallService.getById(id);
        Assert.isNotFound(stall, "档位信息不存在");
        GenStallVO stallVO = ObjectUtils.copying(stall, GenStallVO.class);
        // 扩展获取部门名称
        if (StringUtils.isNotBlank(stall.getDeptId())) {
            List<Long> deptIds = Arrays.stream(stall.getDeptId().split(",")).map(Long::parseLong).collect(Collectors.toList());
            stallVO.setDeptIds(deptIds);
            List<OrganizationDTO> organizationsByIds = organizationClient.getOrganizationsByIds(deptIds);
            List<String> collect = organizationsByIds.stream().map(OrganizationDTO::getName).collect(Collectors.toList());
            stallVO.setDeptNames(collect);
        }
        if (StringUtils.isNotEmpty(stall.getArea())) {
            // 扩展区域信息
            List<String> areaIds = Arrays.asList(stall.getArea().split(","));
            stallVO.setAreaIds(areaIds);
            List<Long> areaId = areaIds.stream().map(s -> {
                return Long.valueOf(s);
            }).collect(Collectors.toList());
            List<AreaDTO> byIds = areaClient.getByIds(areaId);
            List<String> collect1 = byIds.stream().map(AreaDTO::getName).collect(Collectors.toList());
            stallVO.setAreaNames(collect1);
        }

        return stallVO;
    }

    /**
     * 档位ID查询
     *
     * @param id 档位ID
     *
     * @return 档位
     */
    @GetMapping("/gen-stall/byId")
    public GenStallVO getByIdInner(@RequestParam("id") Long id) {
        GenStall stall = this.genStallService.getById(id);
        Assert.isNotFound(stall, "档位信息不存在");
        GenStallVO stallVO = ObjectUtils.copying(stall, GenStallVO.class);
        // 扩展获取部门名称
        if (StringUtils.isNotBlank(stall.getDeptId())) {
            List<Long> deptIds = Arrays.stream(stall.getDeptId().split(",")).map(Long::parseLong).collect(Collectors.toList());
            stallVO.setDeptIds(deptIds);
            List<OrganizationDTO> organizationsByIds = organizationClient.getOrganizationsByIds(deptIds);
            List<String> collect = organizationsByIds.stream().map(OrganizationDTO::getName).collect(Collectors.toList());
            stallVO.setDeptNames(collect);
        }
        if (StringUtils.isNotEmpty(stall.getArea())) {
            // 扩展区域信息
            List<String> areaIds = Arrays.asList(stall.getArea().split(","));
            stallVO.setAreaIds(areaIds);
            List<Long> areaId = areaIds.stream().map(s -> {
                return Long.valueOf(s);
            }).collect(Collectors.toList());
            List<AreaDTO> byIds = areaClient.getByIds(areaId);
            List<String> collect1 = byIds.stream().map(AreaDTO::getName).collect(Collectors.toList());
            stallVO.setAreaNames(collect1);
        }

        return stallVO;
    }

    /**
     * 档位分页查询
     *
     * @param queryParam 档位查询参数
     *
     * @return 档位分页集合
     */
    @GetMapping("/gen-stall/page")
    public Pagination<GenStallPageVO> page(@Valid GenStallQueryParam queryParam) {
        Page<GenStall> page = this.genStallService.page(queryParam);
        List<GenStallPageVO> pageVOS = ObjectUtils.copying(page.getRecords(), GenStallPageVO.class);
        if (CollectionUtils.isEmpty(pageVOS)) {
            return Pagination.empty();
        }
        // 扩展区域信息
        List<AreaDTO> areas = areaClient.listAreas();
        Map<Long, List<AreaDTO>> areaMap = areas.stream().collect(Collectors.groupingBy(AreaDTO::getId));
        for (GenStallPageVO pageVO : pageVOS) {
            // 部门信息
            if (StringUtils.isNotEmpty(pageVO.getDeptId())) {
                List<Long> deptIds = Arrays.stream(pageVO.getDeptId().split(",")).map(Long::parseLong).collect(Collectors.toList());
                pageVO.setDeptIds(deptIds);
                List<OrganizationDTO> organizationsByIds = organizationClient.getOrganizationsByIds(deptIds);
                List<String> collect = organizationsByIds.stream().map(OrganizationDTO::getName).collect(Collectors.toList());
                pageVO.setDeptNames(collect);
            }

            // 扩展档位区域信息
            if (StringUtils.isNotEmpty(pageVO.getArea())) {
                List<Long> areaIds = Arrays.stream(pageVO.getArea().split(",")).map(Long::parseLong).collect(Collectors.toList());
                List<String> areaNames = areaIds.stream().map(areaId -> {
                    if (areaMap.containsKey(areaId)) {
                        return areaMap.get(areaId).get(0).getName();
                    }
                    return null;
                }).filter(Objects::nonNull).collect(Collectors.toList());
                pageVO.setAreaNames(String.join("/", areaNames));
            }
        }
        return PagingUtils.convert(page, pageVOS);
    }

    /**
     * 档位列表
     */
    @PostMapping("/gen-stall/ids")
    public List<GenStallPageVO> listStall(@RequestBody List<Long> ids) {
        List<GenStall> list = this.genStallService.list(Wrappers.<GenStall>lambdaQuery()
                                                                .in(GenStall::getId, ids));
        List<GenStallPageVO> pageVOS = ObjectUtils.copying(list, GenStallPageVO.class);
        if (CollectionUtils.isEmpty(pageVOS)) {
            return new ArrayList<>();
        }
        // 扩展区域信息
        List<AreaDTO> areas = areaClient.listAreas();
        Map<Long, List<AreaDTO>> areaMap = areas.stream().collect(Collectors.groupingBy(AreaDTO::getId));
        for (GenStallPageVO pageVO : pageVOS) {
            // 部门信息
            if (StringUtils.isNotEmpty(pageVO.getDeptId())) {
                List<Long> deptIds = Arrays.stream(pageVO.getDeptId().split(",")).map(Long::parseLong).collect(Collectors.toList());
                pageVO.setDeptIds(deptIds);
                List<OrganizationDTO> organizationsByIds = organizationClient.getOrganizationsByIds(deptIds);
                List<String> collect = organizationsByIds.stream().map(OrganizationDTO::getName).collect(Collectors.toList());
                pageVO.setDeptNames(collect);
            }

            // 扩展档位区域信息
            if (StringUtils.isNotEmpty(pageVO.getArea())) {
                List<Long> areaIds = Arrays.stream(pageVO.getArea().split(",")).map(Long::parseLong).collect(Collectors.toList());
                List<String> areaNames = areaIds.stream().map(areaId -> {
                    if (areaMap.containsKey(areaId)) {
                        return areaMap.get(areaId).get(0).getName();
                    }
                    return null;
                }).filter(Objects::nonNull).collect(Collectors.toList());
                String collect = areaIds.stream().map(s -> {
                    return String.valueOf(s);
                }).collect(Collectors.joining("-"));
                pageVO.setArea(collect);
                pageVO.setAreaNames(areaNames.stream().collect(Collectors.joining("/")));
            }
        }
        return pageVOS;
    }

    @GetMapping("/gen-stall/list")
    public List<GenStallDTO> getStallList(GenStallQueryParam queryParam) {
        List<GenStall> stalls = this.genStallService.list(queryParam);
        // 构建信息
        List<GenStallDTO> records = ObjectUtils.copying(stalls, GenStallDTO.class);
        return records;
    }

    /**
     * 档位删除
     *
     * @param id 档位ID
     */
    @DeleteMapping("/gen-stall/{id}")
    public void genRemove(@PathVariable("id") Long id) {
        this.genStallService.genRemove(id);
    }

    /**
     * @Description 区域筛选
     * @Author lrj
     */
    @GetMapping("/stall-by-area")
    public List<GenStallDTO> getStallByArea(@RequestParam("area") String area) {
        List<GenStall> genStalls = this.genStallService.listByArea(area);
        // 构建信息
        List<GenStallDTO> records = ObjectUtils.copying(genStalls, GenStallDTO.class);
        return records;
    }

}

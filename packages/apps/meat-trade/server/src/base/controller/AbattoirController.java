package com.guhecloud.cube.biz.meattrade.server.base.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.mdata.client.SupplierClient;
import com.guhecloud.cube.biz.mdata.client.dto.SupplierDTO;
import com.guhecloud.cube.biz.meattrade.server.base.dto.AbattoirStatDTO;
import com.guhecloud.cube.biz.meattrade.server.base.entity.Abattoir;
import com.guhecloud.cube.biz.meattrade.server.base.param.AbattoirCreateParam;
import com.guhecloud.cube.biz.meattrade.server.base.param.AbattoirQueryParam;
import com.guhecloud.cube.biz.meattrade.server.base.param.AbattoirUpdateParam;
import com.guhecloud.cube.biz.meattrade.server.base.service.AbattoirService;
import com.guhecloud.cube.biz.meattrade.server.base.vo.AbattoirPageVO;
import com.guhecloud.cube.biz.meattrade.server.base.vo.AbattoirStatVO;
import com.guhecloud.cube.biz.meattrade.server.base.vo.AbattoirVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
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

import java.math.BigDecimal;
import java.util.List;

/**
 * 屠宰场接口
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AbattoirController {

    private final AbattoirService abattoirService;
    private final SupplierClient supplierClient;

    /**
     * 分页查询
     *
     * @param queryParam 查询参数
     *
     * @return 屠宰场分页集合
     */
    @GetMapping("/abattoir/page")
    public Pagination<AbattoirPageVO> getPage(@Valid AbattoirQueryParam queryParam) {
        Page<Abattoir> page = abattoirService.page(queryParam);
        List<AbattoirPageVO> pageVoList = ObjectUtils.copying(page.getRecords(), AbattoirPageVO.class);
        return PagingUtils.convert(page, pageVoList);
    }

    /**
     * 屠宰场列表
     *
     * @return 屠宰场列表集合
     */
    @GetMapping("/abattoir/list")
    public List<AbattoirPageVO> getList(AbattoirQueryParam queryParam) {
        List<Abattoir> abattoirList = abattoirService.list(queryParam);
        return ObjectUtils.copying(abattoirList, AbattoirPageVO.class);
    }

    /**
     * 小程序屠宰场列表
     *
     * @param queryParam 查询参数
     *
     * @return 屠宰场信息
     */
    @GetMapping("/abattoir/mp/list")
    public List<AbattoirPageVO> listMyAbattoir(@NonNull AbattoirQueryParam queryParam) {
        Integer supplierTenant = SessionContextHolder.getRequester().getTenant();
        SupplierDTO supplier = this.supplierClient.getBySupplierTenant(supplierTenant);
        Assert.isNotFound(supplier, "不存在批发商信息!");
        SessionContextHolder.requestTenant(supplier.getTenant());
        List<Abattoir> abattoirs = this.abattoirService.list(queryParam);
        return ObjectUtils.copying(abattoirs, AbattoirPageVO.class);
    }

    /**
     * 根据屠宰场主键获取屠宰场集合
     *
     * @param queryParam 查询参数
     *
     * @return 屠宰场信息集合
     */
    @GetMapping("/abattoir/ids")
    public List<AbattoirVO> getByIds(@Valid AbattoirQueryParam queryParam) {
        return ObjectUtils.copying(abattoirService.listByIds(queryParam.getAbattoirIds()), AbattoirVO.class);
    }

    /**
     * 屠宰场报表信息
     *
     * @param queryParam 查询参数
     *
     * @return 报表统计信息
     */
    @GetMapping("/abattoir/stat/page")
    public AbattoirStatVO abattoirStat(@Valid AbattoirQueryParam queryParam) {
        AbattoirStatVO abattoirStatVO = new AbattoirStatVO();
        Page<AbattoirStatDTO> page = this.abattoirService.statGoodsCategoryPage(queryParam);
        List<AbattoirStatVO.AbattoirStat> records = ObjectUtils.copying(page.getRecords(), AbattoirStatVO.AbattoirStat.class);
        // 构建屠宰场统计信息
        abattoirStatVO.setDealAmount(records.stream().map(AbattoirStatVO.AbattoirStat::getDealAmount)
                                             .reduce(BigDecimal.ZERO, BigDecimal::add));
        abattoirStatVO.setDealWeight(records.stream().map(AbattoirStatVO.AbattoirStat::getDealWeight)
                                             .reduce(BigDecimal.ZERO, BigDecimal::add));
        abattoirStatVO.setCommission(records.stream().map(AbattoirStatVO.AbattoirStat::getCommission)
                                             .reduce(BigDecimal.ZERO, BigDecimal::add));
        abattoirStatVO.setTotal((int) page.getTotal());
        abattoirStatVO.setTradeTotalSlices(records.stream().map(AbattoirStatVO.AbattoirStat::getTradeSlices).reduce(0, Integer::sum));
        abattoirStatVO.setRecords(records);
        return abattoirStatVO;
    }

    /**
     * 新增屠宰场
     *
     * @param createParam 屠宰场新增参数
     */
    @PostMapping("/abattoir")
    public void save(@Valid @RequestBody AbattoirCreateParam createParam) {
        this.abattoirService.save(createParam);
    }

    /**
     * 更新屠宰场
     *
     * @param updateParam 屠宰场更新参数
     */
    @PutMapping("/abattoir")
    public void update(@Valid @RequestBody AbattoirUpdateParam updateParam) {
        this.abattoirService.update(updateParam);
    }

    /**
     * ID查询
     *
     * @param id 屠宰场ID
     */
    @GetMapping("/abattoir/{id}")
    public AbattoirVO getById(@NotNull(message = "主键不能为空") @Min(value = 1, message = "主键不能小于1")
                              @PathVariable("id") Long id
                             ) {
        Abattoir abattoir = this.abattoirService.getById(id);
        return ObjectUtils.copying(abattoir, AbattoirVO.class);
    }

    /**
     * 删除屠宰场
     *
     * @param id
     */
    @DeleteMapping("/abattoir/{id}")
    public void remove(@NotNull(message = "主键不能为空") @Min(value = 1, message = "主键不能小于1")
                       @PathVariable("id") Long id
                      ) {
        this.abattoirService.remove(id);
    }

    /**
     * 生成屠宰场编号与简称
     *
     * @param name 屠宰场名称
     *
     * @return
     */
    @GetMapping("/abattoir/generate-code")
    public AbattoirVO generateCodeAndAttr(@RequestParam("name") String name) {
        Abattoir abattoir = this.abattoirService.generateCodeAndAttr(name);
        return ObjectUtils.copying(abattoir, AbattoirVO.class);
    }

}

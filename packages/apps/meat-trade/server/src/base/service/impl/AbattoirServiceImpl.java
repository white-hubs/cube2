package com.guhecloud.cube.biz.meattrade.server.base.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.meattrade.server.base.dao.AbattoirMapper;
import com.guhecloud.cube.biz.meattrade.server.base.dto.AbattoirBimStatDTO;
import com.guhecloud.cube.biz.meattrade.server.base.dto.AbattoirStatDTO;
import com.guhecloud.cube.biz.meattrade.server.base.entity.Abattoir;
import com.guhecloud.cube.biz.meattrade.server.base.entity.Config;
import com.guhecloud.cube.biz.meattrade.server.base.enumeration.ConfigEnum;
import com.guhecloud.cube.biz.meattrade.server.base.param.AbattoirCreateParam;
import com.guhecloud.cube.biz.meattrade.server.base.param.AbattoirQueryParam;
import com.guhecloud.cube.biz.meattrade.server.base.param.AbattoirUpdateParam;
import com.guhecloud.cube.biz.meattrade.server.base.service.AbattoirService;
import com.guhecloud.cube.biz.meattrade.server.base.service.ConfigService;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkReport;
import com.guhecloud.cube.biz.meattrade.server.pork.service.PorkReportService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.PinYinUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.cloud.logger.constant.OperationType;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.entity.BasicEntity;
import com.guhecloud.platform.database.util.PagingUtils;
import com.mzt.logapi.starter.annotation.LogRecord;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AbattoirServiceImpl extends ServiceImpl<AbattoirMapper, Abattoir> implements AbattoirService {

    private final PorkReportService porkReportService;
    private final ConfigService configService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(success = "新增屠宰场", fail = "新增屠宰场失败，失败原因：「{{#_errorMsg}}」",
               type = "MEATTRADE", typeName = "肉类滑轨",
               subType = "ABATTOIR_CONFIG", subTypeName = "屠宰场设置",
               extra = "{{#createParam}}",
               operationType = OperationType.ADD,
               bizNo = ""
    )
    public Long save(@NonNull AbattoirCreateParam createParam) {
        Assert.isConflict(Objects.nonNull(this.getByName(createParam.getName())), "存在名称相同的屠宰场");
        Abattoir abattoir = ObjectUtils.copying(createParam, Abattoir.class);
        this.save(abattoir);
        return abattoir.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(success = "编辑屠宰场", fail = "编辑屠宰场失败，失败原因：「{{#_errorMsg}}」",
               type = "MEATTRADE", typeName = "肉类滑轨",
               subType = "ABATTOIR_CONFIG", subTypeName = "屠宰场设置",
               extra = "{{#updateParam}}",
               operationType = OperationType.UPDATE,
               bizNo = "{{#updateParam.id}}"
    )
    public void update(@NonNull AbattoirUpdateParam updateParam) {
        // 获取屠宰场信息
        Abattoir abattoir = this.getById(updateParam.getId());
        Assert.isNotFound(abattoir, "不存在屠宰场信息");
        Abattoir abattoirByName = this.getByName(updateParam.getName());
        Assert.isConflict(Objects.nonNull(abattoirByName) && !abattoir.getId().equals(abattoirByName.getId()), "存在相同屠宰场名称");
        abattoir.setAbbr(PinYinUtils.getHead(updateParam.getName()).toUpperCase());
        ObjectUtils.copying(updateParam, abattoir);
        this.updateById(abattoir);
    }

    @Override
    public List<Abattoir> list(@NonNull AbattoirQueryParam queryParam) {
        return this.lambdaQuery()
                .like(StringUtils.isNotEmpty(queryParam.getKeyword()), Abattoir::getName, queryParam.getKeyword())
                .list();
    }

    @Override
    public Page<Abattoir> page(@NonNull AbattoirQueryParam queryParam) {
        Page<Abattoir> page = this.lambdaQuery()
                .like(StringUtils.isNotEmpty(queryParam.getKeyword()), Abattoir::getName, queryParam.getKeyword())
                .orderByDesc(BasicEntity::getCreateTime)
                .page(PagingUtils.convert(queryParam));
        return page;
    }

    @Override
    public Page<AbattoirStatDTO> statPage(@NonNull AbattoirQueryParam queryParam) {
        // 获取市场最低价格
        Config sellingPrice = this.configService.getByType(ConfigEnum.SELLING_PRICE);
        queryParam.setLowerPrice(Objects.isNull(sellingPrice) ? BigDecimal.ZERO : BigDecimal.valueOf(sellingPrice.getValue()));
        // 获取市场规定标准重量
        Config porkStandardWeight = this.configService.getByType(ConfigEnum.PORK_STANDARD_WEIGHT);
        queryParam.setStandardWeight(
                Objects.isNull(porkStandardWeight) ? BigDecimal.valueOf(30) : BigDecimal.valueOf(porkStandardWeight.getValue()));
        if (StringUtils.isNotEmpty(queryParam.getItem())) {
            queryParam.setItem(this.underline(new StringBuffer(queryParam.getItem())));
        }
        Page<AbattoirStatDTO> abattoirStatDTOPage = this.baseMapper.statPage(PagingUtils.convert(queryParam), queryParam);
        //计算金额
        calculateAmount(abattoirStatDTOPage);
        return abattoirStatDTOPage;
    }

    @Override
    public Page<AbattoirStatDTO> statGoodsCategoryPage(AbattoirQueryParam queryParam) {
        // 获取市场最低价格
        Config sellingPrice = this.configService.getByType(ConfigEnum.SELLING_PRICE);
        queryParam.setLowerPrice(Objects.isNull(sellingPrice) ? BigDecimal.ZERO : BigDecimal.valueOf(sellingPrice.getValue()));
        if (StringUtils.isNotEmpty(queryParam.getItem())) {
            queryParam.setItem(this.underline(new StringBuffer(queryParam.getItem())));
        }
        if (!CollectionUtils.isEmpty(queryParam.getGoodsCategoryIds())) {
            queryParam.setGoodsCategoryId(queryParam.getGoodsCategoryIds().stream().collect(Collectors.joining("-")));
        }
        Page<AbattoirStatDTO> abattoirStatDTOPage = this.baseMapper.selectStatGoodsCategoryPage(PagingUtils.convert(queryParam), queryParam);
        //计算金额
        calculateAmount(abattoirStatDTOPage);
        return abattoirStatDTOPage;
    }

    private void calculateAmount(Page<AbattoirStatDTO> abattoirStatDTOPage) {
        if (abattoirStatDTOPage != null && !CollectionUtils.isEmpty(abattoirStatDTOPage.getRecords())) {
            abattoirStatDTOPage.getRecords().stream().forEach(s -> {
                if (s.getDealWeight().floatValue() > 0) {
                    s.setAverageAmount(s.getDealAmount().divide(s.getDealWeight(), 2, RoundingMode.DOWN));
                } else {
                    s.setAverageAmount(null);
                }
                if (s.getActualWeight().floatValue() > 0) {
                    s.setActualAmount(s.getActualAmount().divide(s.getActualWeight(), 2, RoundingMode.DOWN));
                } else {
                    s.setActualAmount(null);
                }
            });
        }
    }

    @Override
    public Page<AbattoirBimStatDTO> bimStatPage(@NonNull AbattoirQueryParam queryParam) {
        return this.baseMapper.bimStatPage(PagingUtils.convert(queryParam), queryParam);
    }

    @Override
    public List<AbattoirBimStatDTO> bimStatList(@NonNull AbattoirQueryParam queryParam) {
        return this.baseMapper.bimStatList(queryParam);
    }

    @Override
    public List<Abattoir> getByIds(@NonNull Collection<Long> ids, @NonNull Integer tenant) {
        SessionContextHolder.requestTenant(tenant);
        return CollectionUtils.isEmpty(ids) ? Collections.emptyList() :
               this.lambdaQuery().in(!CollectionUtils.isEmpty(ids), Abattoir::getId, ids).list();
    }

    @Override
    @LogRecord(success = "删除屠宰场", fail = "删除屠宰场失败，失败原因：「{{#_errorMsg}}」",
               type = "MEATTRADE", typeName = "肉类滑轨",
               subType = "ABATTOIR_CONFIG", subTypeName = "屠宰场设置",
               operationType = OperationType.DELETE,
               bizNo = "{{#id}}"
    )
    public void remove(@NonNull Long id) {
        List<PorkReport> porkReports = this.porkReportService.listByAbattoir(id, SessionContextHolder.getRequester().getTenant());
        Assert.isNotAcceptable(CollectionUtils.isNotEmpty(porkReports), "存在屠宰场被使用，无法删除!");
        DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    public Abattoir getByName(@NonNull String name) {
        return this.lambdaQuery().eq(Abattoir::getName, name).one();
    }

    @Override
    public Abattoir getByCode(@NonNull String code) {
        return this.lambdaQuery().eq(Abattoir::getCode, code).one();
    }

    @Override
    public Abattoir generateCodeAndAttr(@NonNull String name) {
        Abattoir abattoir = new Abattoir();
        abattoir.setName(name);
        abattoir.setCode(this.getAbattoirCode());
        abattoir.setAbbr(PinYinUtils.getHead(name).toUpperCase());
        return abattoir;
    }

    private String getAbattoirCode() {
        String code = "tzc" + ThreadLocalRandom.current().nextInt(1000, 10000);
        while (true) {
            Abattoir abattoir = this.getByCode(code);
            if (Objects.isNull(abattoir)) {
                return code;
            }
            code = "tzc" + ThreadLocalRandom.current().nextInt(1000, 10000);
        }
    }

    public String underline(StringBuffer str) {
        Pattern pattern = Pattern.compile("[A-Z]");
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer(str);
        if (matcher.find()) {
            sb = new StringBuffer();
            //将当前匹配子串替换为指定字符串，并且将替换后的子串以及其之前到上次匹配子串之后的字符串段添加到一个StringBuffer对象里。
            //正则之前的字符和被替换的字符
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
            //把之后的也添加到StringBuffer对象里
            matcher.appendTail(sb);
        } else {
            return sb.toString();
        }
        return underline(sb);
    }
}

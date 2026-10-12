package com.guhecloud.cube.biz.meattrade.server.base.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.meattrade.server.base.dao.DictionaryMapper;
import com.guhecloud.cube.biz.meattrade.server.base.entity.Dictionary;
import com.guhecloud.cube.biz.meattrade.server.base.param.DictionaryCreateParam;
import com.guhecloud.cube.biz.meattrade.server.base.param.DictionaryQueryParam;
import com.guhecloud.cube.biz.meattrade.server.base.param.DictionaryUpdateParam;
import com.guhecloud.cube.biz.meattrade.server.base.service.DictionaryService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service("meattrade_dictionaryService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DictionaryServiceImpl extends ServiceImpl<DictionaryMapper, Dictionary> implements DictionaryService {

    private final DictionaryMapper dictionaryMapper;

    @Override
    public void save(DictionaryCreateParam param) {
        Dictionary dictionary = ObjectUtils.copying(param, Dictionary.class);
        dictionaryMapper.insert(dictionary);
    }

    @Override
    public void update(DictionaryUpdateParam param) {
        Dictionary dictionary = ObjectUtils.copying(param, Dictionary.class);
        dictionaryMapper.updateById(dictionary);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void initSpotCheckItem(@NonNull Integer tenant) {
        Dictionary d1 = new Dictionary();
        d1.setCode("CLENBUTEROL_HYDROCHLORIDE");
        d1.setType("CLENBUTEROL");
        d1.setName("盐酸克伦特罗");
        d1.setSort(1f);

        Dictionary d2 = new Dictionary();
        d2.setCode("CLENBUTEROL_RACTOPAMINE");
        d2.setType("CLENBUTEROL");
        d2.setName("莱克多巴胺");
        d2.setSort(1f);

        Dictionary d3 = new Dictionary();
        d3.setCode("CLENBUTEROL_SALBUTAMOL");
        d3.setType("CLENBUTEROL");
        d3.setName("沙丁胺醇");
        d3.setSort(1f);

        List<Dictionary> dictionaries = Stream.of(d1, d2, d3).collect(Collectors.toList());
        SessionContextHolder.requestTenant(tenant);
        this.saveBatch(dictionaries);
    }

    @Override
    public Page<Dictionary> page(DictionaryQueryParam queryParam) {
        return this.lambdaQuery()
                .and(StringUtils.isNotEmpty(queryParam.getKeyword()), q -> q
                        .like(Dictionary::getCode, queryParam.getKeyword())
                        .or()
                        .like(Dictionary::getName, queryParam.getKeyword())
                        .or()
                        .like(Dictionary::getType, queryParam.getKeyword())
                    ).page(PagingUtils.convert(queryParam));
    }

    @Override
    public List<Dictionary> getByType(String type) {
        return this.lambdaQuery().eq(Dictionary::getType, type).list();
    }
}

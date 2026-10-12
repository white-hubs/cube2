package com.guhecloud.cube.biz.meattrade.server.pork.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.meattrade.server.base.entity.Dictionary;
import com.guhecloud.cube.biz.meattrade.server.base.service.DictionaryService;
import com.guhecloud.cube.biz.meattrade.server.pork.dao.SpotCheckDetailMapper;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.SpotCheckDetail;
import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.SpotCheckEnum;
import com.guhecloud.cube.biz.meattrade.server.pork.service.SpotCheckDetailService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service("meattrade-spotCheckDetailService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SpotCheckDetailServiceImpl extends ServiceImpl<SpotCheckDetailMapper, SpotCheckDetail> implements SpotCheckDetailService {

    private final static String SPOT_CHECK_ITEM_TYPE = "CLENBUTEROL";

    private final DictionaryService dictionaryService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addSpotCheckDetailBySpotCheck(@NonNull Long spotCheckId) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        List<SpotCheckDetail> spotCheckDetails = this.getBySpotCheck(spotCheckId, tenant);
        if (CollectionUtils.isEmpty(spotCheckDetails)) {
            List<Dictionary> dictionaries = this.dictionaryService.getByType(SPOT_CHECK_ITEM_TYPE);
            Assert.isNotFound(CollectionUtils.isEmpty(dictionaries), "食安抽检项为空,请联系管理员添加抽检项!");
            // 食安抽检项信息初始化
            List<SpotCheckDetail> details = dictionaries.stream().map(dictionary -> {
                return SpotCheckDetail.builder()
                        .spotCheckId(spotCheckId)
                        .spotCheckItemId(dictionary.getId())
                        .tenant(tenant)
                        .passed(SpotCheckEnum.UNSELECTED)
                        .build();
            }).collect(Collectors.toList());
            this.saveBatch(details);
        }
    }

    @Override
    public List<SpotCheckDetail> getBySpotCheck(@NonNull Long spotCheckId, @NonNull Integer tenant) {
        return this.baseMapper.selectList(Wrappers.<SpotCheckDetail>lambdaQuery().eq(SpotCheckDetail::getSpotCheckId, spotCheckId)
                                                  .eq(SpotCheckDetail::getTenant, tenant));
    }
}

package com.guhecloud.cube.deliver.project.merchant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.deliver.project.merchant.entity.SensitiveWord;
import com.guhecloud.cube.deliver.project.merchant.service.SensitiveWordService;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.BatchDTO;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.ErrorExcelDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.SensitiveWordDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.SensitiveWordExcelDTO;
import com.guhecloud.cube.deliver.project.merchant.mapper.SensitiveWordMapper;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 敏感词库 服务实现类
 * </p>
 *
 * @author bq
 * @since 2022-06-14
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SensitiveWordServiceImpl extends ServiceImpl<SensitiveWordMapper, SensitiveWord> implements SensitiveWordService {

    @Override
    public void saveOrUpdate(SensitiveWordDTO word) {
        SensitiveWord sensitiveWord = word.convert();
        this.checkName(word);
        if (StringUtils.isNotEmpty(word.getId())) {
            this.updateById(sensitiveWord);
        } else {
            sensitiveWord.setOrder(this.countOrder());
            this.save(sensitiveWord);
        }
        this.updateSensitiveWordInit();
    }

    @Override
    public void removeBatch(BatchDTO dto) {
        List<String> ids = dto.getIds();
        if (ids != null && ids.size() > 0) {
            ids.forEach(this::removeById);
        }
        this.updateSensitiveWordInit();
    }

    @Async
    @Override
    public void updateSensitiveWordInit() {
        this.initSensitiveWord();
    }

    @Override
    public List<ErrorExcelDTO> importData(List<SensitiveWordExcelDTO> dtos) {
        List<ErrorExcelDTO> excelDTOS = new ArrayList<>();
        int i = 2;
        for (SensitiveWordExcelDTO dto : dtos) {
            SensitiveWord word = dto.convert();
            SensitiveWord savedName = this.lambdaQuery().eq(SensitiveWord::getName, word.getName()).one();
            // 根据name判断，如果存在就不导入
            if (savedName == null) {
                // 新增
                word.setOrder(this.countOrder());
                this.save(word);
            } else {
                excelDTOS.add(new ErrorExcelDTO(i, "名称", "名称已经存在"));
            }
            i++;
        }
        this.updateSensitiveWordInit();
        return excelDTOS;
    }

    /**
     * 初始化数据库中的敏感词到内存中
     */
    @PostConstruct
    public void initSensitiveWord() {
        //        List<SensitiveWord> words = this.lambdaQuery().list();
        //        List<String> datas = words.stream().map(SensitiveWord::getName).collect(Collectors.toList());
        //        SensitiveWordInit.init(datas);
    }

    /**
     * 校验敏感词是否重复
     *
     * @param wordDTO
     */
    private void checkName(SensitiveWordDTO wordDTO) {
        SensitiveWord word = this.lambdaQuery().eq(SensitiveWord::getName, wordDTO.getName()).one();
        if (word != null && !word.getId().equals(wordDTO.getId())) {
            throw new ServiceException("该敏感词已存在，请您重新输入!");
        }
    }

    private synchronized int countOrder() {
        int order = 0;
        List<SensitiveWord> words = this.lambdaQuery().orderByDesc(SensitiveWord::getOrder).list();
        if (words.size() > 0) {
            order = words.get(0).getOrder() + 1;
        }
        return order;
    }

}

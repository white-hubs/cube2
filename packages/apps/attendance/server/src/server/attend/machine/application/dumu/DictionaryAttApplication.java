package com.guhecloud.cube.srv.gen.attendance.management.server.attend.machine.application.dumu;

import com.guhecloud.cube.base.dictionary.client.DataDictionaryClient;
import com.guhecloud.cube.base.dictionary.dto.DataDictionaryDTO;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryaramParam;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2023-11-22  13:49
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DictionaryAttApplication {
    private final DataDictionaryClient dataDictionaryClient;

    public DataDictionaryDTO getDataDictionaryByItem(String itemCode, String code) {
        DataDictionaryaramParam param = new DataDictionaryaramParam();
        param.setCode(code);
        param.setItemCode(itemCode);
        List<DataDictionaryDTO> list = dataDictionaryClient.getDataDictionaryByItem(param);
        if (ObjectUtils.isNotEmpty(list)) {
            return list.get(0);
        }
        log.info("{}，{}字典不存在", itemCode, code);
        return null;
    }
}

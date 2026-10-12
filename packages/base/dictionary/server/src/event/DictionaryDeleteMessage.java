package com.guhecloud.cube.base.dictionary.server.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author pengjian
 * @date 2022/10/20 16:49
 * @description
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DictionaryDeleteMessage {
    public static final String TOPIC = "DICTIONARY_DELETE";
    private String itemCode;
    private String code;
    private String tyep;
}

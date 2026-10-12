package com.guhecloud.platform.dcm.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.dcm.exception.DcmRuntimeException;
import com.guhecloud.platform.dcm.param.DcmDeclarationParam;
import com.guhecloud.platform.dcm.param.DcmRequirementParam;
import com.guhecloud.platform.dcm.param.ProvidedConfigSpecParam;
import com.guhecloud.platform.dcm.param.RequestedConfigItemParam;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * DCM(Declarative Configuration Management) 相关的一系列工具类与方法
 */
public final class DcmTools {
    public static List<String> parseStrColl(Object raw, String separatorRegex) {
        if (raw instanceof CharSequence) {
            return Arrays.stream(raw.toString().split(separatorRegex))
                    .filter(e -> !e.trim().isEmpty())
                    .collect(Collectors.toList());
        } else if (raw instanceof Collection) {
            return ((Collection<?>) raw).stream()
                    .filter(Objects::nonNull)
                    .map(String::valueOf)
                    .filter(e -> !(e).trim().isEmpty())
                    .collect(Collectors.toList());
        }
        if (raw.getClass().isArray()) {
            return Arrays.stream((Object[]) raw)
                    .filter(Objects::nonNull)
                    .map(String::valueOf)
                    .filter(e -> !(e).trim().isEmpty()).
                    collect(Collectors.toList());
        } else { // Number and any other unknown staff.
            return Collections.singletonList(raw.toString());
        }
    }

    public static String join(String[] input) {
        return Escape.join(input);
    }

    public static String[] split(String input) {
        return Escape.split(input);
    }

    /**
     * 提供一种方便进行安全可靠的字符串拼接/分拆需要的方法.
     * 拼接只需要对字符串进行escape之后使用逗号分割即可。
     */

    public static class Escape {
        public static String escape(String input) {
            String csvEscaped = StringUtils.escapeCsv(input);
            if (csvEscaped.length() != input.length()) {
                return csvEscaped;
            }
            // 确保用双引号包裹字符串， 即使不包含特殊字符。
            return "\"" + csvEscaped + "\"";
        }

        public static String unescape(String input) {
            return StringUtils.unescapeCsv(input);
        }

        private static int lookupContinuousDoubleQuote(String input, int startIndex) {
            int count = 0;
            for (int i = startIndex; i < input.length(); ++i) {
                if (input.charAt(i) == '"') {
                    ++count;
                } else {
                    return count;
                }
            }
            return count;
        }

        public static String join(String[] input) {
            return Arrays.stream(input).map(Escape::escape).collect(Collectors.joining(","));
        }

        public static String join(Collection<String> input) {
            return input.stream().map(Escape::escape).collect(Collectors.joining(","));
        }

        public static String[] split(String input) {
            if (input == null || input.isEmpty()) {
                return new String[0];
            }
            List<String> segments = new ArrayList<>();
            StringBuilder currentSegment = new StringBuilder();
            int doubleQuotesCount = 0;
            for (int i = 0; i < input.length(); i++) {
                char c = input.charAt(i);
                if (c == '"') {
                    doubleQuotesCount++;
                    currentSegment.append(c);
                } else {
                    if (c == ',' && isOdd(doubleQuotesCount)) {
                        // 当遇到逗号且双引号个数为奇数时，检查后面的双引号个数
                        int nextDoubleQuotesCount = lookupContinuousDoubleQuote(input, i + 1);
                        if (isOdd(nextDoubleQuotesCount)) {
                            // 如果后面的双引号个数也是奇数，添加当前片段并重置
                            segments.add(currentSegment.toString());
                            currentSegment.setLength(0); // 清空当前片段
                        } else {
                            // 如果后面的双引号个数不是奇数，将逗号和后面的连续双引号添加到当前片段.. 但貌似不应该? 使用我们的escape方法后的字符串, 应该可以安全地使用"," 进行连接。
                            currentSegment.append(input, i, i + nextDoubleQuotesCount + 1);
                            i += nextDoubleQuotesCount;
                        }
                    } else {
                        // 添加当前字符到当前片段
                        currentSegment.append(c);
                    }
                    doubleQuotesCount = 0;
                }
            }
            // 添加最后一个片段
            segments.add(currentSegment.toString());
            return segments.stream()
                    .map(Escape::unescape).toArray(String[]::new);
        }

        private static boolean isOdd(int number) {
            return (number & 1) == 1;
        }
    }

    /**
     * Parser from content stream or content string.
     */
    public static class Parser {
        private static final YAMLMapper MAPPER = new YAMLMapper();
        private static final TypeReference<DcmDeclarationParam> TYPE_REF_DCM_DECLARATION_PARM =
                new TypeReference<DcmDeclarationParam>() {};
        private static final TypeReference<DcmRequirementParam> TYPE_REF_DCM_REQUIREMENTS_PARM =
                new TypeReference<DcmRequirementParam>() {};

        private static final TypeReference<ProvidedConfigSpecParam> TYPE_REF_PROVIDED_CONFIG_SPEC_PARM =
                new TypeReference<ProvidedConfigSpecParam>() {};

        private static final TypeReference<List<RequestedConfigItemParam>> TYPE_REF_REQUEST_CONFIG_ITEMS = new TypeReference<List<RequestedConfigItemParam>>() {
        };

        public static DcmDeclarationParam declarationParamFromYamlContent(InputStream inputStream) {
            try {
                return MAPPER.readValue(inputStream, TYPE_REF_DCM_DECLARATION_PARM);
            } catch (IOException e) {
                throw new DcmRuntimeException("Parse DCM Declaration Failed", e);
            }
        }

        public static DcmDeclarationParam declarationParamFromYamlContent(String yamlContent) {
            try {
                return MAPPER.readValue(yamlContent, TYPE_REF_DCM_DECLARATION_PARM);
            } catch (IOException e) {
                throw new DcmRuntimeException("Parse DCM Declaration Failed", e);
            }
        }

        public static DcmRequirementParam requirementsParamFromYamlContent(String yamlContent) {
            try {
                return MAPPER.readValue(yamlContent, TYPE_REF_DCM_REQUIREMENTS_PARM);
            } catch (IOException e) {
                throw new DcmRuntimeException("Parse DCM RequirementsParam Failed", e);
            }
        }

        public static ProvidedConfigSpecParam providerConfigSpecParamFromYamlContent(String yamlContent) {
            try {
                return MAPPER.readValue(yamlContent, TYPE_REF_PROVIDED_CONFIG_SPEC_PARM);
            } catch (IOException e) {
                throw new DcmRuntimeException("Parse ProvidedConfigSpecParam Failed", e);
            }
        }

        public static List<RequestedConfigItemParam> requestConfigItemsFromContent(String yamlContent) {
            try {
                return MAPPER.readValue(yamlContent, TYPE_REF_REQUEST_CONFIG_ITEMS);
            } catch (IOException e) {
                throw new DcmRuntimeException("Parse RequestConfigItems Failed", e);
            }
        }

        public static List<RequestedConfigItemParam> requestConfigItemsFromContent(InputStream inputStream) {
            try {
                return MAPPER.readValue(inputStream, TYPE_REF_REQUEST_CONFIG_ITEMS);
            } catch (IOException e) {
                throw new DcmRuntimeException("Parse RequestConfigItems Failed", e);
            }
        }
    }
}

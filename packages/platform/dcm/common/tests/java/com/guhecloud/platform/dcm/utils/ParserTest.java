package com.guhecloud.platform.dcm.utils;

import com.guhecloud.platform.dcm.entity.DcmDeclaration;
import com.guhecloud.platform.dcm.entity.RequestedConfigItem;
import com.guhecloud.platform.dcm.param.DcmDeclarationParam;
import com.guhecloud.platform.dcm.param.DcmRequirementParam;
import com.guhecloud.platform.dcm.param.ProvidedConfigSpecParam;
import com.guhecloud.platform.dcm.param.RequestedConfigItemParam;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

class ParserTest {

    private String readContentsByClassPathResource(String path) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(path);
        return new String(FileCopyUtils.copyToByteArray(classPathResource.getInputStream()));
    }

    @Test
    void declarationParamFromYamlContent() throws IOException {
        DcmDeclarationParam declarationParam = DcmTools.Parser.declarationParamFromYamlContent(readContentsByClassPathResource("example/config/declaration_overall.yml"));
        Assertions.assertNotNull(declarationParam);
        DcmDeclaration declaration = declarationParam.toEntity();
        Assertions.assertNotNull(declaration);
    }

    @Test
    void requirementsParamFromYamlContent() throws IOException {
        String content = readContentsByClassPathResource("example/config/requirements_overall.yml");
        DcmRequirementParam param = DcmTools.Parser.requirementsParamFromYamlContent(content);
        Assertions.assertNotNull(param);
        Assertions.assertNotNull(param.getRequirements());

        List<RequestedConfigItem> convertedItems = param.getRequirements().stream().map(RequestedConfigItemParam::toEntity)
                .collect(Collectors.toList());
        Assertions.assertTrue(convertedItems.size() > 0, "requirements is not empty");
        // system case
        Assertions.assertEquals(Boolean.TRUE, convertedItems.get(0).getIsSystem());  // No Tenants specified.
        Assertions.assertEquals(0, convertedItems.get(0).getTenants().size(), "should be empty collection");

        // All tenant cases
        Assertions.assertEquals(Boolean.FALSE, convertedItems.get(1).getIsSystem());
        Assertions.assertEquals(Collections.singletonList("all"), convertedItems.get(1).getTenants());

        Assertions.assertNotNull(convertedItems.get(2).getKwargs());
        Assertions.assertFalse(convertedItems.get(2).getVargs().isEmpty(), "varargs is not empty");
        Assertions.assertFalse(convertedItems.get(2).getKwargs().isEmpty(), "kwargs is not empty");
        Assertions.assertEquals(Arrays.asList("1", "2"), convertedItems.get(2).getTenants());

        Assertions.assertNotNull(convertedItems.get(3).getKwargs());
        Assertions.assertEquals(2, convertedItems.get(3).getKwargs().size());
        Assertions.assertTrue(convertedItems.get(3).getIsRemoval(), "the case should be removal");
        Assertions.assertEquals(Arrays.asList("1", "2"), convertedItems.get(3).getTenants());
        Assertions.assertEquals(3, convertedItems.get(3).getVargs().size());

    }

    private List<String>[] providerConfigSpecCases = new List[]{
            Arrays.asList("TypeNumber", "{ type: TypeNumber, versions: 1.0 }"),
            Arrays.asList("TypeString", "{ type: TypeString, versions: \"1.0\" }"),
            Arrays.asList("TypeCommaSeparatedString", "{ type: TypeCommaSeparatedString, versions: \"1.0,1.1\" }"),
            Arrays.asList("TypeArrayNumber1", "{ type: TypeArrayNumber1, versions: [1.0]}"),
            Arrays.asList("TypeArrayNumber2", "{ type: TypeArrayNumber2, versions: [1.0,1.1] }"),
            Arrays.asList("TypeArrayString", "{ type: TypeArrayString, versions: [\"1.0\",\"1.1\"] }"),
            };

    @Test
    void providerConfigSpecParamFromYamlContent() {
        Class<?> elementTypes[] = { String.class, Number.class };
        for (List<String> caseItem : providerConfigSpecCases) {
            try {
                ProvidedConfigSpecParam param = DcmTools.Parser.providerConfigSpecParamFromYamlContent(caseItem.get(1));
                Assertions.assertNotNull(param.getVersions(), "case: " + (String) caseItem.get(0) + " should not parsed as null");
                assertSingleOrContainerOfTypes(param.getVersions(), elementTypes,
                                               "case: " + (String) caseItem.get(0) + " parsed elements type mismatched");
            } catch (Exception e) {
                Assertions.fail("case: " + (String) caseItem.get(0) + " should not throw exception: " + e.getMessage());
            }
        }
    }

    @Test
    void requestedConfigItemParamFromYamlContent() throws IOException {

    }

    private static boolean isContainer(Object container) {
        return container instanceof Collection || container.getClass().isArray();
    }

    private static boolean elementConforms(Object container, Class<?> expectedType) {
        if (container.getClass().isArray()) {
            // 如果是数组，验证数组元素类型
            Class<?> arrayComponentType = container.getClass().getComponentType();
            return expectedType.isAssignableFrom(arrayComponentType);
        } else if (container instanceof Collection) {
            // 如果是集合，验证集合元素类型
            Collection<?> collection = (Collection<?>) container;
            for (Object element : collection) {
                if (element != null && !expectedType.isAssignableFrom(element.getClass())) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void assertSingleOrContainerOfTypes(Object container, Class<?>[] expectedTypes, String msg) {
        if (isContainer(container)) {
            for (Class<?> expectedType : expectedTypes) {
                if (elementConforms(container, expectedType)) {
                    return;
                }
            }
            Assertions.fail(msg != null ? msg : "Expected Element Type not matched with any of " + Arrays.toString(expectedTypes));
        } else {
            for (Class<?> expectedType : expectedTypes) {
                if (expectedType.isAssignableFrom(container.getClass())) {
                    return;
                }
            }
            Assertions.fail(msg != null ? msg : "Is singular object and type not matched with any of " + Arrays.toString(expectedTypes));
        }
    }
}

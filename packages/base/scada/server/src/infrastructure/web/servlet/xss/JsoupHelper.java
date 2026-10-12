package com.guhecloud.kaihe.scada.server.infrastructure.web.servlet.xss;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.safety.Safelist;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;

public class JsoupHelper {

    private static Safelist safelist = Safelist.relaxed();

    private static final OutputSettings outputSettings = new OutputSettings().charset(StandardCharsets.UTF_8).prettyPrint(false);

    static {
        safelist.addTags("figure");
        safelist.addAttributes(":all", "class");
        safelist.addAttributes(":all", "style");
    }

    public static final String clean(String str) {
        return StringUtils.isEmpty(str) ? str : Jsoup.clean(str, "", safelist, outputSettings);
    }

}

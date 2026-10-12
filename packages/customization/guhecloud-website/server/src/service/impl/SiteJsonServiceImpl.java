package com.guhecloud.cube.customized.guhecloudwebsite.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.guhecloud.cube.customized.guhecloudwebsite.dao.SysArticleCategoryMapper;
import com.guhecloud.cube.customized.guhecloudwebsite.dao.SysArticleMapper;
import com.guhecloud.cube.customized.guhecloudwebsite.dao.SysSiteLayoutMapper;
import com.guhecloud.cube.customized.guhecloudwebsite.dao.SysSitePageMapper;
import com.guhecloud.cube.customized.guhecloudwebsite.dao.SysSiteSettingMapper;
import com.guhecloud.cube.customized.guhecloudwebsite.entity.SysArticle;
import com.guhecloud.cube.customized.guhecloudwebsite.entity.SysArticleCategory;
import com.guhecloud.cube.customized.guhecloudwebsite.entity.SysSiteLayout;
import com.guhecloud.cube.customized.guhecloudwebsite.entity.SysSitePage;
import com.guhecloud.cube.customized.guhecloudwebsite.entity.SysSiteSetting;
import com.guhecloud.cube.customized.guhecloudwebsite.service.SiteJsonService;
import com.guhecloud.platform.base.constants.DateFormat;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 站点JSON压缩包生成服务
 *
 * ZIP结构（简化版）:
 *   site-setting.json      - 站点设置
 *   site-pages.json        - 页面列表
 *   site-layouts.json      - 页面布局
 *   article-categories.json - 文章分类
 *   articles.json          - 文章列表
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SiteJsonServiceImpl implements SiteJsonService {

    private final SysArticleMapper sysArticleMapper;
    private final SysArticleCategoryMapper sysArticleCategoryMapper;
    private final SysSiteSettingMapper sysSiteSettingMapper;
    private final SysSitePageMapper sysSitePageMapper;
    private final SysSiteLayoutMapper sysSiteLayoutMapper;
    private final ObjectMapper objectMapper;
    private Path staticPath = Path.of(System.getProperty("user.dir"), "resources", "static");

    @Override
    public String getSiteJsonUrl() {
        try {
            // 1. 查询站点设置
            SysSiteSetting setting = sysSiteSettingMapper.selectOne(null);

            // 2. 查询所有页面
            LambdaQueryWrapper<SysSitePage> sitePageWrapper = new LambdaQueryWrapper<>();
            sitePageWrapper.eq(SysSitePage::getStatus, 1);
            List<SysSitePage> pageList = sysSitePageMapper.selectList(sitePageWrapper);

            // 3. 查询所有布局
            List<SysSiteLayout> layoutList = sysSiteLayoutMapper.selectList(null);

            // 4. 查询所有文章分类
            List<SysArticleCategory> categories = sysArticleCategoryMapper.selectList(null);

            // 5. 查询所有已发布文章
            LambdaQueryWrapper<SysArticle> articleWrapper = new LambdaQueryWrapper<>();
            articleWrapper.eq(SysArticle::getStatus, 1)
                          .orderByDesc(SysArticle::getCreateTime);
            List<SysArticle> articles = sysArticleMapper.selectList(articleWrapper);

            // 6. 生成ZIP
            byte[] zipBytes = createZip(setting, pageList, layoutList, categories, articles);

            // 7. 保存
            if (!Files.exists(staticPath)) {
                Files.createDirectories(staticPath);
                log.info("创建静态资源目录: {}", staticPath);
            }
            String fileName = "site-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ".zip";
            Files.write(staticPath.resolve(fileName), zipBytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            log.info("站点JSON压缩包已保存: path={}", staticPath.resolve(fileName));
            return fileName;
        } catch (Exception e) {
            log.error("生成站点JSON压缩包失败: {}", e.getMessage(), e);
            throw new ServiceException("生成站点JSON压缩包失败: " + e.getMessage(), e);
        }
    }

    private byte[] createZip(SysSiteSetting setting, List<SysSitePage> pageList,
                              List<SysSiteLayout> layoutList, List<SysArticleCategory> categories,
                              List<SysArticle> articles) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ZipOutputStream zos = new ZipOutputStream(baos)) {

            // 1. 站点设置
            writeSiteSetting(zos, setting);

            // 2. 页面列表
            writeSitePages(zos, pageList, layoutList);

            // 3. 页面布局
            writeSiteLayouts(zos, layoutList);

            // 4. 文章分类
            writeArticleCategories(zos, categories);

            // 5. 文章列表
            writeArticles(zos, articles, categories);

            zos.finish();
            return baos.toByteArray();
        }
    }

    /** 1. 站点设置 */
    private void writeSiteSetting(ZipOutputStream zos, SysSiteSetting setting) throws IOException {
        ObjectNode root = objectMapper.createObjectNode();

        if (setting != null) {
            root.put("siteName", setting.getSiteName());
            root.put("url", setting.getUrl());
            root.put("ogImage", setting.getOgImage());
            root.put("favicon", setting.getFavicon());
            root.put("primaryColor", setting.getPrimaryColor());
            root.put("navStyle", setting.getNavStyle());
            root.put("defaultTitle", setting.getDefaultTitle());
            root.put("defaultDescription", setting.getDefaultDescription());
            root.put("defaultKeywords", setting.getDefaultKeywords());
            root.put("trackingScripts", setting.getTrackingScripts());
        }

        String jsonStr = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(root);
        zos.putNextEntry(new ZipEntry("site-setting.json"));
        zos.write(jsonStr.getBytes(StandardCharsets.UTF_8));
        zos.closeEntry();
    }

    /** 2. 页面列表 */
    private void writeSitePages(ZipOutputStream zos, List<SysSitePage> pageList,
                                 List<SysSiteLayout> layoutList) throws IOException {
        ArrayNode pages = objectMapper.createArrayNode();

        // 构建布局ID到名称的映射
        Map<Long, String> layoutNameMap = layoutList.stream()
                .collect(Collectors.toMap(SysSiteLayout::getId, SysSiteLayout::getLayoutName, (a, b) -> a));

        for (SysSitePage page : pageList) {
            ObjectNode pageNode = objectMapper.createObjectNode();
            pageNode.put("id", page.getId());
            pageNode.put("pageRoute", page.getPageRoute());

            // layoutId -> layoutName
            if (page.getLayoutId() != null) {
                String layoutName = layoutNameMap.getOrDefault(page.getLayoutId(), page.getLayoutId().toString());
                pageNode.put("layout", layoutName);
            }

            pageNode.put("metaTitle", page.getMetaTitle());
            pageNode.put("metaDescription", page.getMetaDescription());
            pageNode.put("metaKeywords", page.getMetaKeywords());
            pageNode.put("metaNavStyle", page.getMetaNavStyle());
            pageNode.put("components", page.getComponents());

            pages.add(pageNode);
        }

        String jsonStr = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(pages);
        zos.putNextEntry(new ZipEntry("site-pages.json"));
        zos.write(jsonStr.getBytes(StandardCharsets.UTF_8));
        zos.closeEntry();
    }

    /** 3. 页面布局 */
    private void writeSiteLayouts(ZipOutputStream zos, List<SysSiteLayout> layoutList) throws IOException {
        ArrayNode layouts = objectMapper.createArrayNode();

        for (SysSiteLayout layout : layoutList) {
            ObjectNode layoutNode = objectMapper.createObjectNode();
            layoutNode.put("id", layout.getId());
            layoutNode.put("layoutName", layout.getLayoutName());
            layoutNode.put("config", layout.getConfig());

            layouts.add(layoutNode);
        }

        String jsonStr = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(layouts);
        zos.putNextEntry(new ZipEntry("site-layouts.json"));
        zos.write(jsonStr.getBytes(StandardCharsets.UTF_8));
        zos.closeEntry();
    }

    /** 4. 文章分类 */
    private void writeArticleCategories(ZipOutputStream zos, List<SysArticleCategory> categories) throws IOException {
        ArrayNode cats = objectMapper.createArrayNode();

        for (SysArticleCategory cat : categories) {
            ObjectNode catNode = objectMapper.createObjectNode();
            catNode.put("id", cat.getId());
            catNode.put("categoryName", cat.getCategoryName());
            catNode.put("sort", cat.getSort());

            cats.add(catNode);
        }

        String jsonStr = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(cats);
        zos.putNextEntry(new ZipEntry("article-categories.json"));
        zos.write(jsonStr.getBytes(StandardCharsets.UTF_8));
        zos.closeEntry();
    }

    /** 5. 文章列表 */
    private void writeArticles(ZipOutputStream zos, List<SysArticle> articles,
                                List<SysArticleCategory> categories) throws IOException {
        // 构建分类ID到名称的映射
        Map<Long, String> categoryNameMap = categories.stream()
                .collect(Collectors.toMap(SysArticleCategory::getId, SysArticleCategory::getCategoryName, (a, b) -> a));

        ArrayNode arts = objectMapper.createArrayNode();

        for (SysArticle article : articles) {
            ObjectNode artNode = objectMapper.createObjectNode();
            artNode.put("id", article.getId());
            artNode.put("title", article.getTitle());

            LocalDateTime createTime = article.getCreateTime();
            artNode.put("date", createTime != null ? DateUtils.formatDate(createTime, DateFormat.DEFAULT_DATE_FORMAT) : null);
            artNode.put("createTime", createTime != null ? DateUtils.formatDate(createTime, DateFormat.DEFAULT_DATETIME_FORMAT) : null);

            LocalDateTime updateTime = article.getUpdateTime();
            artNode.put("updateTime", updateTime != null ? DateUtils.formatDate(updateTime, DateFormat.DEFAULT_DATETIME_FORMAT) : null);

            artNode.put("summary", article.getSummary());
            artNode.put("content", article.getContent());
            artNode.put("coverImageUrl", article.getCoverImageUrl());
            artNode.put("author", article.getAuthor());
            artNode.put("viewCount", article.getViewCount());

            // categoryId -> categoryName
            String categoryName = article.getCategoryId() != null
                    ? categoryNameMap.getOrDefault(article.getCategoryId(), null) : null;
            artNode.put("category", categoryName);
            artNode.put("categoryId", article.getCategoryId());

            artNode.put("href", "/information/detail-" + article.getId() + ".html");

            arts.add(artNode);
        }

        String jsonStr = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(arts);
        zos.putNextEntry(new ZipEntry("articles.json"));
        zos.write(jsonStr.getBytes(StandardCharsets.UTF_8));
        zos.closeEntry();
    }
}

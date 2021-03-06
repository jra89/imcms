package com.imcode.imcms.domain.component;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.constructs.web.PageInfo;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Serhii Maksymchuk from Ubrainians for imCode
 * @author Victor Pavlenko from Ubrainians for imCode
 * 06.09.18.
 */
public interface DocumentsCache {

    String calculateKey(HttpServletRequest request);

    String calculateKey(final String documentIdString, final String langCode);

    void setCache(Ehcache cache);

    PageInfo getPageInfoFromCache(String key);

    void invalidateDoc(Integer id, String alias);

    void invalidateItem(String key);

    void invalidateCache();

    void invalidateDoc(HttpServletRequest request);

    boolean isDocumentAlreadyCached(String cacheKey);

    void setDisableCachesByProperty();

    long getAmountOfCachedDocuments();

    void setAmountOfCachedDocuments(Integer number);

    String getDisabledCacheValue();
}

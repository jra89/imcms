package com.imcode.imcms.domain.component;

import com.imcode.imcms.domain.dto.PageRequestDTO;
import com.imcode.imcms.domain.dto.SearchQueryDTO;
import imcode.server.Imcms;
import imcode.server.document.index.DocumentIndex;
import imcode.server.user.RoleId;
import imcode.server.user.UserDomainObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.data.domain.Sort.Direction;
import static org.springframework.data.domain.Sort.Order;

@Component
public class DocumentSearchQueryConverter {

    private static final Sort DEFAULT_SORT = new Sort(new Order(Direction.DESC, DocumentIndex.FIELD__META_ID));

    public SolrQuery convertToSolrQuery(SearchQueryDTO searchQuery) {
        final UserDomainObject searchingUser = Imcms.getUser();
        final StringBuilder indexQuery = new StringBuilder();

        indexQuery.append(
                StringUtils.isNotBlank(searchQuery.getTerm())
                        ? Arrays.stream(new String[]{
                        DocumentIndex.FIELD__META_ID,
                        DocumentIndex.FIELD__META_HEADLINE + "_" + searchingUser.getLanguage(),
                        DocumentIndex.FIELD__META_TEXT,
                        DocumentIndex.FIELD__KEYWORD,
                        DocumentIndex.FIELD__TEXT,
                        DocumentIndex.FIELD__ALIAS,
                        DocumentIndex.FIELD_URL})
                        .map(field -> String.format("%s:*%s*", field, searchQuery.getTerm()))
                        .collect(Collectors.joining(" "))
                        : "*:*"
        );

        if (searchQuery.getCategoriesId() != null) {
            final String categoriesIdStringValues = searchQuery.getCategoriesId()
                    .stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(" AND "));

            indexQuery.insert(0, "(")
                    .append(") AND (" + DocumentIndex.FIELD__CATEGORY_ID + ":(") // don't be so sad
                    .append(categoriesIdStringValues)
                    .append("))");
        }

        final SolrQuery solrQuery = new SolrQuery(indexQuery.toString());

        if (searchQuery.getUserId() != null) {
            final String userFilter = DocumentIndex.FIELD__CREATOR_ID + ":" + searchQuery.getUserId();
            solrQuery.addFilterQuery(userFilter);
        }

        prepareSolrQueryPaging(searchQuery, solrQuery);

        if (!searchingUser.isSuperAdmin()) {
            solrQuery.addFilterQuery(DocumentIndex.FIELD__SEARCH_ENABLED + ":true");

            final String userRoleIdsFormatted = Stream.of(searchingUser.getRoleIds())
                    .map(RoleId::toString)
                    .collect(Collectors.joining(" ", "(", ")"));

            solrQuery.addFilterQuery(DocumentIndex.FIELD__ROLE_ID + ":" + userRoleIdsFormatted);
        }

        return solrQuery;
    }

    private void prepareSolrQueryPaging(SearchQueryDTO searchQuery, SolrQuery solrQuery) {
        PageRequestDTO page = searchQuery.getPage();

        if (page == null) {
            page = new PageRequestDTO();
        }

        solrQuery.setStart(page.getSkip());
        solrQuery.setRows(page.getSize());

        final Order order = Optional.ofNullable(page.getSort())
                .orElse(DEFAULT_SORT)
                .iterator()
                .next();

        solrQuery.addSort(order.getProperty(), SolrQuery.ORDER.valueOf(order.getDirection().name().toLowerCase()));
    }
}
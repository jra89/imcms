<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="imcms" uri="imcms" %>
<%@ attribute name="no" required="false" %><%-- old index name --%>
<%@ attribute name="index" required="false" %>
<%@ attribute name="document" required="false" %>
<%@ attribute name="style" required="false" %>
<%@ attribute name="pre" required="false" %>
<%@ attribute name="post" required="false" %>

<%-- do not remove - it helps Idea to understand var types --%>
<%--@elvariable id="currentDocument" type="imcode.server.document.textdocument.TextDocumentDomainObject"--%>
<%--@elvariable id="imageService" type="com.imcode.imcms.domain.service.ImageService"--%>
<%--@elvariable id="image" type="com.imcode.imcms.domain.dto.ImageDTO"--%>
<%--@elvariable id="isEditMode" type="boolean"--%>
<%--@elvariable id="isPreviewMode" type="boolean"--%>
<%--@elvariable id="loopEntryRef" type="com.imcode.imcms.model.LoopEntryRef"--%>
<%--@elvariable id="language" type="java.lang.String"--%>

<c:if test="${empty index}">
    <c:set var="index" value="${no}"/><%-- old attribute "no" support --%>
</c:if>

<c:set var="targetDocId" value="${empty document ? currentDocument.id : document}"/>

<c:set var="imageContent">
    <c:set var="image" value="${isEditMode || isPreviewMode
     ? imageService.getImage(targetDocId, index, language, loopEntryRef)
     : imageService.getPublicImage(targetDocId, index, language, loopEntryRef)}"/>
    <c:set var="imgPath" value="${image.generatedFilePath}"/>
    <c:set var="style" value="${empty style ? '' : ' style=\"'.concat(style).concat('\"')}"/>
    ${pre}<img src="${empty imgPath ? '' : contextPath}${imgPath}"${style}/>${post}
</c:set>

<c:if test="${isEditMode}">
    <div class="imcms-editor-area imcms-editor-area--image" data-doc-id="${targetDocId}" data-lang-code="${language}"
         data-index="${no}"${empty loopEntryRef
            ? '' : ' data-loop-index="'.concat(loopEntryRef.loopIndex).concat('" data-loop-entry-index="')
            .concat(loopEntryRef.loopEntryIndex).concat('"')}>
        <div class="imcms-editor-area__content imcms-editor-content">${imageContent}</div>
        <div class="imcms-editor-area__control-wrap">
            <div class="imcms-editor-area__control-edit imcms-control imcms-control--edit imcms-control--image">
                <div class="imcms-editor-area__control-title">Image Editor</div>
            </div>
        </div>
    </div>
</c:if>
<c:if test="${not isEditMode}">${imageContent}</c:if>

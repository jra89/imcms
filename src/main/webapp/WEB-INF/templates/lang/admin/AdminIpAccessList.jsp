<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<input type="HIDDEN" name="IP_ACCESS_ID_${RECORD_COUNTER}" value="${IP_ACCESS_ID}">
<input type="HIDDEN" name="IP_USER_ID_${RECORD_COUNTER}" value="${USER_ID}">
<tr>
    <td align="center"><input type="checkbox" name="EDIT_IP_ACCESS" value="${RECORD_COUNTER}"></td>
    <td>${LOGIN_NAME}</td>
    <td><input type="text" name="IP_START_${RECORD_COUNTER}" value="${IP_START}" size="15" maxlength="15"></td>
    <td align="center">-</td>
    <td><input type="text" name="IP_END_${RECORD_COUNTER}" value="${IP_END}" size="15" maxlength="15"></td>
</tr>
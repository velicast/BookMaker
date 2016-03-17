<%-- 
    Document   : search_employee
    Created on : Feb 22, 2016, 10:12:37 PM
    Author     : eduarc
--%>
<%@page import="co.com.bookmaker.business_logic.controller.FinalUserController"%>
<%@page import="co.com.bookmaker.business_logic.controller.AgencyController"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="Param" class="co.com.bookmaker.util.type.Parameter"></jsp:useBean>
<jsp:useBean id="Info" class="co.com.bookmaker.util.type.Information"></jsp:useBean>
<jsp:useBean id="Attr" class="co.com.bookmaker.util.type.Attribute"></jsp:useBean>

<c:set var="agency" value="${requestScope[Attr.AGENCY]}"></c:set>
<jsp:useBean id="agency" class="co.com.bookmaker.data_access.entity.Agency"></jsp:useBean>
    
<c:if test="${requestScope[Attr.ADD_EMPLOYEE] != null}">
<h2 class="main_content_title"> Search New Employee </h2>
</c:if>
<c:if test="${requestScope[Attr.REM_EMPLOYEE] != null}">
<h2 class="main_content_title"> Search Employee to Remove </h2>
</c:if>

<form id="searchEmployee" role="form" class="form-horizontal" action="<%=FinalUserController.URL%>" method="GET">
    <input type="hidden" name="do" value="<%=FinalUserController.SEARCH_EMPLOYEE%>">
    <input type="hidden" name="${Param.AGENCY}" value="${agency.id}">
    <c:if test="${requestScope[Attr.ADD_EMPLOYEE] != null}">
    <input type="hidden" name="${Param.ADD_EMPLOYEE}" value="">
    </c:if>
    <c:if test="${requestScope[Attr.REM_EMPLOYEE] != null}">
    <input type="hidden" name="${Param.REM_EMPLOYEE}" value="">
    </c:if>
    <div class="form-group">
        <label class="control-label col-md-2">Agency: </label>
        <div class="col-md-4">
            <p class="form-control-static">${agency.name}</p>
        </div>
    </div>
    <div class="form-group">
        <label class="control-label col-md-2">Employee: </label>
        <div class="col-md-4">
            <div class="input-group">
                <input type="text" class="form-control input-sm" id="employeeToSearch" name="${Param.USERNAME}" placeholder="User Name">
                <span class="input-group-btn">
                    <button class="btn btn-sm btn-submit" id="btnSearchEmployee"><span class="glyphicon glyphicon-search"></span></button>
                </span>
            </div>
        </div>
    </div>
    <output style="color: red">${requestScope[Info.USERNAME]}</output>
</form>
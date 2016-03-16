<%-- 
    Document   : ebalance
    Created on : Mar 13, 2016, 8:12:27 PM
    Author     : eduarc
--%>
<%@page import="co.com.bookmaker.business_logic.controller.AccountController"%>
<%@page import="co.com.bookmaker.business_logic.controller.AdminController"%>
<%@page import="co.com.bookmaker.business_logic.controller.ManagerController"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setLocale value="en_US"></fmt:setLocale>

<style>
    .form-group {
        margin-top: 0px;
        margin-bottom: 0px;
        padding-bottom: 0px;
        padding-top: 0px;
    }
</style>

<jsp:useBean id="Param" class="co.com.bookmaker.util.type.Parameter"></jsp:useBean>
<jsp:useBean id="Attr" class="co.com.bookmaker.util.type.Attribute"></jsp:useBean>
<jsp:useBean id="Info" class="co.com.bookmaker.util.type.Information"></jsp:useBean>
<jsp:useBean id="Role" class="co.com.bookmaker.util.type.Role"></jsp:useBean>
    
<c:set var="employee" value="${requestScope[Attr.EMPLOYEE]}"></c:set>
<jsp:useBean id="employee" class="co.com.bookmaker.data_access.entity.FinalUser"></jsp:useBean>

<h2 class="main_content_title"> User Balance </h2>

<div class="row">
    <c:if test="${requestScope[Attr.ROLE] == Role.MANAGER}">
    <form id="balanceForm" role="form" class="form-inline" action="<%=ManagerController.URL%>" method="POST">
        <input type="hidden" name="to" value="<%=ManagerController.EMPLOYEE_BALANCE%>">
    </c:if>
    <c:if test="${requestScope[Attr.ROLE] == Role.ADMIN}">
    <form id="balanceForm" role="form" class="form-inline" action="<%=AdminController.URL%>" method="POST">
        <input type="hidden" name="to" value="<%=AdminController.EMPLOYEE_BALANCE%>">
    </c:if>
    <c:if test="${requestScope[Attr.ROLE] == Role.ALL}">
    <form id="balanceForm" role="form" class="form-inline" action="<%=AccountController.URL%>" method="POST">
        <input type="hidden" name="to" value="<%=AccountController.BALANCE%>">
    </c:if>
        <input type="hidden" name="${Param.USERNAME}" value="${employee.username}">
        <div class="form-group col-md-8">
            <label class="control-label">From: </label>
            <input type="text" class="form-control input-sm" name="${Param.TIME_FROM}" value="${requestScope[Attr.TIME_FROM]}" placeholder="dd/MM/yyyy">
            <label class="control-label">To: </label>
            <input type="text" class="form-control input-sm" name="${Param.TIME_TO}" value="${requestScope[Attr.TIME_TO]}" placeholder="dd/MM/yyyy">
            <button id="btnBalance" type="submit" class="btn btn-submit"><span class="glyphicon glyphicon-stats"></span> Calculate</button>
            <output style="color: red">${requestScope[Info.STATUS]}</output>
        </div>
    </form>
</div>
<form role="form" class="form-horizontal">
    <div class="form-group">
        <label class="col-md-2 control-label">User: </label>
        <div class="col-md-4">
            <c:if test="${requestScope[Attr.ROLE] == Role.MANAGER}">
            <p class="form-control-static">
                <a href="<%=ManagerController.URL%>?to=<%=ManagerController.EMPLOYEE_SUMMARY%>&${Param.USERNAME}=${employee.username}">
                    ${employee.username}</a> - ${employee.firstName} ${employee.lastName}</p>
            </c:if>
            <c:if test="${requestScope[Attr.ROLE] == Role.ADMIN}">
            <p class="form-control-static">
                <a href="<%=AdminController.URL%>?to=<%=AdminController.USER_SUMMARY%>&${Param.USERNAME}=${employee.username}">
                    ${employee.username}</a> - ${employee.firstName} ${employee.lastName}</p>
            </c:if>
            <c:if test="${requestScope[Attr.ROLE] == Role.ALL}">
            <c:set var="employee" value="${sessionScope[Attr.SESSION_USER]}"></c:set>
            <p class="form-control-static">
                <a href="<%=AccountController.URL%>?to=<%=AccountController.SUMMARY%>">
                    ${employee.username}</a> - ${employee.firstName} ${employee.lastName}</p>
            </c:if>
        </div>
    </div>
    <c:if test="${employee.inRole(Role.SELLER)}">
    <div class="form-group">
        <label class="col-md-2 control-label">Sold Parlays: </label>
        <div class="col-md-4">
            <p class="form-control-static">${requestScope[Attr.PARLAYS]}</p>
        </div>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">Total Revenue: </label>
        <div class="col-md-4">
            <p class="form-control-static">$ <fmt:formatNumber value="${requestScope[Attr.REVENUE]}" maxFractionDigits="0"/></p>
        </div>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">Total Cost: </label>
        <div class="col-md-4">
            <p class="form-control-static">$ <fmt:formatNumber value="${requestScope[Attr.COST]}" maxFractionDigits="0"/></p>
        </div>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">Total Profit: </label>
        <div class="col-md-4">
        <c:if test="${requestScope[Attr.PROFIT] < 0}">
            <p class="form-control-static" style="color: red">$ <fmt:formatNumber value="${requestScope[Attr.PROFIT]}" maxFractionDigits="0"/></p>
        </c:if>
        <c:if test="${requestScope[Attr.PROFIT] >= 0}">
            <p class="form-control-static" style="color: green">$ <fmt:formatNumber value="${requestScope[Attr.PROFIT]}" maxFractionDigits="0"/></p>
        </c:if>
        </div>
    </div>
    </c:if>
    <c:if test="${employee.inRole(Role.ANALYST)}">
    <div class="form-group">
        <label class="col-md-2 control-label">Matches: </label>
        <div class="col-md-4">
            <p class="form-control-static">${requestScope[Attr.MATCHES]}</p>
        </div>
    </div>
        <div class="form-group">
        <label class="col-md-2 control-label">Active Matches: </label>
        <div class="col-md-4">
            <p class="form-control-static">${requestScope[Attr.ACTIVE_MATCHES]}</p>
        </div>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">Inactive Matches: </label>
        <div class="col-md-4">
            <p class="form-control-static">${requestScope[Attr.INACTIVE_MATCHES]}</p>
        </div>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">Cancelled Matches: </label>
        <div class="col-md-4">
            <p class="form-control-static">${requestScope[Attr.CANCELLED_MATCHES]}</p>
        </div>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">Pending Matches: </label>
        <div class="col-md-4">
            <p class="form-control-static">${requestScope[Attr.PENDING_MATCHES]}</p>
        </div>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">Finished Matches: </label>
        <div class="col-md-4">
            <p class="form-control-static">${requestScope[Attr.FINISHED_MATCHES]}</p>
        </div>
    </div>
    </c:if>
</form>
    

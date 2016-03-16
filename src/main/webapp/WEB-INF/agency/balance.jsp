<%-- 
    Document   : balance
    Created on : Mar 13, 2016, 11:48:36 AM
    Author     : eduarc
--%>

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

<h2 class="main_content_title"> Agency Balance </h2>

<div class="row">
    <c:if test="${requestScope[Attr.ROLE] == Role.MANAGER}">
    <form id="balanceForm" role="form" class="form-inline" action="<%=ManagerController.URL%>" method="POST">    
        <input type="hidden" name="to" value="<%=ManagerController.AGENCY_BALANCE%>">
    </c:if>
    <c:if test="${requestScope[Attr.ROLE] == Role.ADMIN}">
    <form id="balanceForm" role="form" class="form-inline" action="<%=AdminController.URL%>" method="POST">    
        <input type="hidden" name="to" value="<%=AdminController.AGENCY_BALANCE%>">
        <input type="hidden" name="${Param.AGENCY}" value="${requestScope[Attr.AGENCY].id}">
    </c:if>
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
<div class="row">
    <form class="form-horizontal">
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
    </form>
</div>
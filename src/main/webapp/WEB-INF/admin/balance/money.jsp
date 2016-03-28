<%-- 
    Document   : money
    Created on : Mar 17, 2016, 11:39:16 AM
    Author     : eduarc
--%>

<%@page import="co.com.bookmaker.business_logic.controller.AgencyController"%>
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

<h2 class="main_content_title"> Money Balance </h2>

<div class="row">
    <form id="balanceForm" role="form" class="form-inline" action="<%=AdminController.URL%>" method="GET">    
        <input type="hidden" name="to" value="<%=AdminController.MONEY_BALANCE%>">
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
    <div class="col-md-6">
        <form role="form" class="form-horizontal">
            <div class="form-group" style="text-align: center">
                <h4 class="col-md-12">Parlay</h4>
            </div>
            <div class="form-group">
                <label class="col-md-3 control-label">In Queue: </label>
                <div class="col-md-9">
                    <p class="form-control-static">${requestScope[Attr.IN_QUEUE]}</p>
                </div>
            </div>
            <div class="form-group">
                <label class="col-md-3 control-label">Win: </label>
                <div class="col-md-9">
                    <p class="form-control-static">${requestScope[Attr.WIN]}</p>
                </div>
            </div>
            <div class="form-group">
                <label class="col-md-3 control-label">Lose: </label>
                <div class="col-md-9">
                    <p class="form-control-static">${requestScope[Attr.LOSE]}</p>
                </div>
            </div>
            <div class="form-group">
                <label class="col-md-3 control-label">Cancelled: </label>
                <div class="col-md-9">
                    <p class="form-control-static">${requestScope[Attr.CANCELLED]}</p>
                </div>
            </div>
            <div class="form-group">
                <label class="col-md-3 control-label">Sold: </label>
                <div class="col-md-9">
                    <p class="form-control-static">${requestScope[Attr.PARLAYS]}</p>
                </div>
            </div>
            <div class="form-group">
                <label class="col-md-3 control-label">Total Sold: </label>
                <div class="col-md-9">
                    <p class="form-control-static">$ <fmt:formatNumber value="${requestScope[Attr.REVENUE]}" maxFractionDigits="0"/></p>
                </div>
            </div>
            <div class="form-group">
                <label class="col-md-3 control-label">Total Cost: </label>
                <div class="col-md-9">
                    <p class="form-control-static">$ <fmt:formatNumber value="${requestScope[Attr.COST]}" maxFractionDigits="0"/></p>
                </div>
            </div>
            <div class="form-group">
                <label class="col-md-3 control-label">Total Profit: </label>
                <div class="col-md-9">
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
</div>
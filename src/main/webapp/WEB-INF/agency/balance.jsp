<%-- 
    Document   : balance
    Created on : Mar 13, 2016, 11:48:36 AM
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

<h2 class="main_content_title"> Balance de Agencia </h2>

<div class="row">
    <form id="balanceForm" role="form" class="form-inline" action="<%=AgencyController.URL%>" method="GET">    
        <input type="hidden" name="do" value="<%=AgencyController.BALANCE%>">
        <input type="hidden" name="${Param.AGENCY}" value="${requestScope[Attr.AGENCY].id}">
        <input type="hidden" name="${Param.ROLE}" value="${param.roleRequester}">
        <div class="form-group col-md-8">
            <label class="control-label">Desde: </label>
            <input type="text" class="form-control input-sm" name="${Param.TIME_FROM}" value="${requestScope[Attr.TIME_FROM]}" placeholder="dd/MM/yyyy">
            <label class="control-label">Hasta: </label>
            <input type="text" class="form-control input-sm" name="${Param.TIME_TO}" value="${requestScope[Attr.TIME_TO]}" placeholder="dd/MM/yyyy">
            <button id="btnBalance" type="submit" class="btn btn-submit"><span class="glyphicon glyphicon-stats"></span> Calcular</button>
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
                <label class="col-md-3 control-label">En Cola: </label>
                <div class="col-md-9">
                    <p class="form-control-static">${requestScope[Attr.IN_QUEUE]}</p>
                </div>
            </div>
            <div class="form-group">
                <label class="col-md-3 control-label">Gana: </label>
                <div class="col-md-9">
                    <p class="form-control-static">${requestScope[Attr.WIN]}</p>
                </div>
            </div>
            <div class="form-group">
                <label class="col-md-3 control-label">Pierde: </label>
                <div class="col-md-9">
                    <p class="form-control-static">${requestScope[Attr.LOSE]}</p>
                </div>
            </div>
            <div class="form-group">
                <label class="col-md-3 control-label">Cancelado: </label>
                <div class="col-md-9">
                    <p class="form-control-static">${requestScope[Attr.CANCELLED]}</p>
                </div>
            </div>
            <div class="form-group">
                <label class="col-md-3 control-label">Vendido: </label>
                <div class="col-md-9">
                    <p class="form-control-static">${requestScope[Attr.PARLAYS]}</p>
                </div>
            </div>
            <div class="form-group">
                <label class="col-md-3 control-label">Total: </label>
                <div class="col-md-9">
                    <p class="form-control-static">$ <fmt:formatNumber value="${requestScope[Attr.REVENUE]}" maxFractionDigits="0"/></p>
                </div>
            </div>
            <div class="form-group">
                <label class="col-md-3 control-label">Costo: </label>
                <div class="col-md-9">
                    <p class="form-control-static">$ <fmt:formatNumber value="${requestScope[Attr.COST]}" maxFractionDigits="0"/></p>
                </div>
            </div>
            <div class="form-group">
                <label class="col-md-3 control-label">Ganancia: </label>
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
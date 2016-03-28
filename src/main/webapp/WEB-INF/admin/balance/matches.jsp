<%-- 
    Document   : accounts
    Created on : Mar 17, 2016, 11:39:25 AM
    Author     : eduarc
--%>
<%@page import="co.com.bookmaker.business_logic.controller.AdminController"%>
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

<h2 class="main_content_title"> Matches Balance </h2>

<div class="row">
    <form id="balanceForm" role="form" class="form-inline" action="<%=AdminController.URL%>" method="GET">
        <input type="hidden" name="to" value="<%=AdminController.MATCHES_BALANCE%>">
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
                <h4 class="col-md-12">Match</h4>
            </div>
            <div class="form-group">
                <label class="col-md-3 control-label">Total: </label>
                <div class="col-md-9">
                    <p class="form-control-static">${requestScope[Attr.MATCHES]}</p>
                </div>
            </div>
                <div class="form-group">
                <label class="col-md-3 control-label">Active: </label>
                <div class="col-md-9">
                    <p class="form-control-static">${requestScope[Attr.ACTIVE_MATCHES]}</p>
                </div>
            </div>
            <div class="form-group">
                <label class="col-md-3 control-label">Inactive: </label>
                <div class="col-md-9">
                    <p class="form-control-static">${requestScope[Attr.INACTIVE_MATCHES]}</p>
                </div>
            </div>
            <div class="form-group">
                <label class="col-md-3 control-label">Cancelled: </label>
                <div class="col-md-9">
                    <p class="form-control-static">${requestScope[Attr.CANCELLED_MATCHES]}</p>
                </div>
            </div>
            <div class="form-group">
                <label class="col-md-3 control-label">Pending: </label>
                <div class="col-md-9">
                    <p class="form-control-static">${requestScope[Attr.PENDING_MATCHES]}</p>
                </div>
            </div>
            <div class="form-group">
                <label class="col-md-3 control-label">Finished: </label>
                <div class="col-md-9">
                    <p class="form-control-static">${requestScope[Attr.FINISHED_MATCHES]}</p>
                </div>
            </div>
        </form>
    </div>
</div>

    


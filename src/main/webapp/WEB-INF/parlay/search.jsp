<%-- 
    Document   : search
    Created on : Feb 19, 2016, 6:03:01 PM
    Author     : eduarc
--%>
<%@page import="co.com.bookmaker.business_logic.controller.parlay.ParlayController"%>
<%@page import="co.com.bookmaker.business_logic.controller.SellerController"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="Param" class="co.com.bookmaker.util.type.Parameter"></jsp:useBean>
<jsp:useBean id="Info" class="co.com.bookmaker.util.type.Information"></jsp:useBean>
<jsp:useBean id="Attr" class="co.com.bookmaker.util.type.Attribute"></jsp:useBean>
<jsp:useBean id="Status" class="co.com.bookmaker.util.type.Status"></jsp:useBean>

<c:set var="data" value="${requestScope[Attr.PARLAY]}"></c:set>
<jsp:useBean id="data" class="co.com.bookmaker.util.form.bean.SearchParlayBean"></jsp:useBean>

<h2 class="main_content_title"> Search Parlay </h2>

<form id="searchAgencyForm" role="form" class="form-horizontal" action="<%=ParlayController.URL%>" method="GET">
    <input type="hidden" name="do" value="<%=ParlayController.SEARCH%>">
    <input type="hidden" name="${Param.ROLE}" value="${param.roleRequester}">
    <div class="form-group">
        <label class="control-label col-md-2">Parlay: </label>
        <div class="col-md-4">
            <input type="text" class="form-control input-sm" id="parlayToSearch" name="${Param.PARLAY}" value="${data.id}" placeholder="Ticket ID">
        </div>
        <output style="color: red">${requestScope[Info.PARLAY]}</output>
    </div>
    <div class="form-group">
        <label class="control-label col-md-2">Seller:</label>
        <div class="col-md-4">
            <input type="text" class="form-control input-sm" id="sellerToSearch" name="${Param.USERNAME}" value="${data.username}" placeholder="User Name">
        </div>
        <output style="color: red">${requestScope[Info.USERNAME]}</output>
    </div>
    <div class="form-group">
        <label class="control-label col-md-2">From: </label>
        <div class="col-md-4">
            <input type="text" class="form-control input-sm" id="timeFrom" name="${Param.TIME_FROM}" value="${data.from}" placeholder="dd/MM/yyyy">
        </div>
        <output style="color: red">${requestScope[Info.TIME_FROM]}</output>
    </div>
    <div class="form-group">
        <label class="control-label col-md-2">To: </label>
        <div class="col-md-4">
            <input type="text" class="form-control input-sm" id="timeTo" name="${Param.TIME_TO}" value="${data.to}" placeholder="dd/MM/yyyy">
        </div>
        <output style="color: red">${requestScope[Info.TIME_TO]}</output>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">Status: </label>
        <div class="col-md-4">
            <select name="${Param.STATUS}" class="form-control">
                <option value="">Any</option>
                <option value="${Status.IN_QUEUE}">${Status.str(Status.IN_QUEUE)}</option>
                <option value="${Status.PENDING}">${Status.str(Status.PENDING)}</option>
                <option value="${Status.WIN}">${Status.str(Status.WIN)}</option>
                <option value="${Status.LOSE}">${Status.str(Status.LOSE)}</option>
                <option value="${Status.CANCELLED}">${Status.str(Status.CANCELLED)}</option>
            </select>
        </div>
    </div>
    <div class="col-md-6" style="text-align: center">
        <button class="btn btn-submit" id="btnSearchUser"><span class="glyphicon glyphicon-search"></span> Search</button>
    </div>
    <output style="color: red">${requestScope[Info.SEARCH_RESULT]}</output>
</form>

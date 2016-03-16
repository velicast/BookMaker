<%-- 
    Document   : summary
    Created on : Feb 23, 2016, 3:28:49 PM
    Author     : eduarc
--%>
<%@page import="co.com.bookmaker.business_logic.controller.AnalystController"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="Param" class="co.com.bookmaker.util.type.Parameter"></jsp:useBean>
<jsp:useBean id="Attr" class="co.com.bookmaker.util.type.Attribute"></jsp:useBean>
<jsp:useBean id="Status" class="co.com.bookmaker.util.type.Status"></jsp:useBean>

<c:set var="sUser" value="${sessionScope[Attr.SESSION_USER]}"></c:set>
<jsp:useBean id="sUser" class="co.com.bookmaker.data_access.entity.FinalUser"></jsp:useBean>
    
<c:set var="tournament" value="${requestScope[Attr.TOURNAMENT]}"></c:set>
<jsp:useBean id="tournament" class="co.com.bookmaker.data_access.entity.event.Tournament"></jsp:useBean>

<style>
    .form-group {
        margin-top: 0px;
        margin-bottom: 0px;
        padding-bottom: 0px;
        padding-top: 0px;
    }
</style>

<h2 class="main_content_title"> Tournament Summary </h2>

<form role="form" class="form-horizontal">
    <c:if test="${tournament.author.id == sUser.id}">
    <div class="form-group">
        <div class="col-md-8">
        <a class="btn btn-default" 
           href="<%=AnalystController.URL %>?to=<%=AnalystController.EDIT_TOURNAMENT%>&${Param.TOURNAMENT}=${tournament.id}">
            <span class="glyphicon glyphicon-edit"></span> Edit</a>
        </div>
    </div>
    </c:if>
    <div class="form-group">
        <label class="col-md-2 control-label">Sport: </label>
        <div class="col-md-4">
            <p class="form-control-static">${tournament.sport.name}</p>
        </div>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">Name: </label>
        <div class="col-md-4">
            <p class="form-control-static">${tournament.name}</p>
        </div>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">Status: </label>
        <div class="col-md-4">
            <p class="form-control-static">${Status.str(tournament.status)}</p>
        </div>
    </div>
    <c:if test="${tournament.author.id == sUser.id}">
    <div class="form-group">
        <div class="col-md-8">
        <a class="btn btn-default" 
           href="<%=AnalystController.URL %>?to=<%=AnalystController.EDIT_TOURNAMENT%>&${Param.TOURNAMENT}=${tournament.id}">
            <span class="glyphicon glyphicon-edit"></span> Edit</a>
        </div>
    </div>
    </c:if>
</form>


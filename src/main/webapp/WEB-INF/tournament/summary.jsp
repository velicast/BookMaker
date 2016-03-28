<%-- 
    Document   : summary
    Created on : Feb 23, 2016, 3:28:49 PM
    Author     : eduarc
--%>
<%@page import="co.com.bookmaker.business_logic.controller.AdminController"%>
<%@page import="co.com.bookmaker.business_logic.controller.ManagerController"%>
<%@page import="co.com.bookmaker.business_logic.controller.AnalystController"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="Param" class="co.com.bookmaker.util.type.Parameter"></jsp:useBean>
<jsp:useBean id="Attr" class="co.com.bookmaker.util.type.Attribute"></jsp:useBean>
<jsp:useBean id="Status" class="co.com.bookmaker.util.type.Status"></jsp:useBean>
<jsp:useBean id="Role" class="co.com.bookmaker.util.type.Role"></jsp:useBean>

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

<h2 class="main_content_title"> Torneo </h2>

<form role="form" class="form-horizontal">
    <c:if test="${tournament.author.id == sUser.id}">
    <div class="form-group">
        <div class="col-md-8">
        <a class="btn btn-default" 
           href="<%=AnalystController.URL %>?to=<%=AnalystController.EDIT_TOURNAMENT%>&${Param.TOURNAMENT}=${tournament.id}">
            <span class="glyphicon glyphicon-edit"></span> Editar</a>
        </div>
    </div>
    </c:if>
    <c:if test="${sUser.agency == tournament.author.agency}">
        <div class="form-group">
            <label class="col-md-2 control-label">Autor: </label>
            <div class="col-md-4">
                <c:set var="else" value="${true}"></c:set>
                <c:if test="${else && param.roleRequester == Role.MANAGER}">
                <c:set var="else" value="${false}"></c:set>
                <p class="form-control-static">
                    <a href="<%=ManagerController.URL%>?to=<%=ManagerController.EMPLOYEE_SUMMARY%>&${Param.USERNAME}=${tournament.author.username}">
                        ${tournament.author.username}</a> - ${tournament.author.firstName} ${tournament.author.lastName}</p>
                </c:if>
                <c:if test="${else && param.roleRequester == Role.ANALYST && Role.inRole(sessionScope[Attr.SESSION_ROLE], Role.ADMIN)}">
                <c:set var="else" value="${false}"></c:set>
                <p class="form-control-static">
                    <a href="<%=AdminController.URL%>?to=<%=AdminController.USER_SUMMARY%>&${Param.USERNAME}=${tournament.author.username}">
                        ${tournament.author.username}</a> - ${tournament.author.firstName} ${tournament.author.lastName}</p>
                </c:if>
                <c:if test="${else}">
                <p class="form-control-static">${tournament.author.username} - ${tournament.author.firstName} ${tournament.author.lastName}</p>
                </c:if>
            </div>
        </div>
    </c:if>
    <div class="form-group">
        <label class="col-md-2 control-label">Deporte: </label>
        <div class="col-md-4">
            <p class="form-control-static">${tournament.sport.name}</p>
        </div>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">Nombre: </label>
        <div class="col-md-4">
            <p class="form-control-static">${tournament.name}</p>
        </div>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">Estado: </label>
        <div class="col-md-4">
            <p class="form-control-static">${Status.str(tournament.status)}</p>
        </div>
    </div>
    <c:if test="${tournament.author.id == sUser.id}">
    <div class="form-group">
        <div class="col-md-8">
        <a class="btn btn-default" 
           href="<%=AnalystController.URL %>?to=<%=AnalystController.EDIT_TOURNAMENT%>&${Param.TOURNAMENT}=${tournament.id}">
            <span class="glyphicon glyphicon-edit"></span> Editar</a>
        </div>
    </div>
    </c:if>
</form>


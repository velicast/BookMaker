<%-- 
    Document   : new
    Created on : Feb 23, 2016, 3:10:27 PM
    Author     : eduarc
--%>
<%@page import="co.com.bookmaker.business_logic.controller.AnalystController"%>
<%@page import="co.com.bookmaker.business_logic.controller.event.TournamentController"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="Info" class="co.com.bookmaker.util.type.Information"></jsp:useBean>
<jsp:useBean id="Param" class="co.com.bookmaker.util.type.Parameter"></jsp:useBean>
<jsp:useBean id="Attr" class="co.com.bookmaker.util.type.Attribute"></jsp:useBean>
<jsp:useBean id="Status" class="co.com.bookmaker.util.type.Status"></jsp:useBean>
<jsp:useBean id="SportID" class="co.com.bookmaker.util.type.SportID"></jsp:useBean>

<c:set var="tournament" value="${requestScopet[Attr.TOURNAMENT]}"></c:set>
<jsp:useBean id="tournament" class="co.com.bookmaker.data_access.entity.event.Tournament"></jsp:useBean>

<h2 class="main_content_title"> Nuevo Torneo </h2>
<form id="newTournamentForm" role="form" class="form-horizontal" action="<%=TournamentController.URL%>" method="POST">
    <input type="hidden" name="do" value="<%=TournamentController.NEW%>">
    <div class="form-group">
        <label class="control-label col-md-2">Deporte: </label>
        <div class="col-md-4">
            <select name="${Param.SPORT}" class="form-control">
                <c:forEach var="sport" items="${requestScope[Attr.SPORTS]}">
                <jsp:useBean id="sport" class="co.com.bookmaker.data_access.entity.event.Sport"></jsp:useBean>
                    <option value="${sport.id}" ${tournament.sport.id == sport.id ? "selected" : ""}>${SportID.str(sport.id)}</option>
                </c:forEach>
            </select>
        </div>
        <output style="color: red">${requestScope[Info.SPORT]}</output>
    </div>
    <div class="form-group">
        <label class="control-label col-md-2">Nombre: </label>
        <div class="col-md-4">
            <input type="text" class="form-control input-sm" name="${Param.NAME}" value="${tournament.name}" placeholder="Nombre">
        </div>
        <output style="color: red">${requestScope[Info.NAME]}</output>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">Estado: </label>
        <div class="col-md-4">
            <select class="form-control input-sm" name="${Param.STATUS}">
                <option value="${Status.ACTIVE}" ${tournament.status == Status.ACTIVE ? 'selected' : ''}> ${Status.str(Status.ACTIVE)} </option>
                <option value="${Status.INACTIVE}" ${tournament.status == Status.INACTIVE ? 'selected' : ''}> ${Status.str(Status.INACTIVE)} </option>
            </select>
        </div>
        <output style="color: red">${requestScope[Info.STATUS]}</output>
    </div>
    <div class="col-md-6" style="text-align: center">
        <button class="btn btn-submit" id="btnNewTournament"><span class="glyphicon glyphicon-check"></span> Crear</button>
    </div>
</form>
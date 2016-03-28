<%-- 
    Document   : match event search
    Created on : Feb 25, 2016, 8:31:25 PM
    Author     : eduarc
--%>

<%@page import="co.com.bookmaker.business_logic.controller.event.MatchEventController"%>
<%@page import="co.com.bookmaker.business_logic.controller.parlay.ParlayController"%>
<%@page import="co.com.bookmaker.business_logic.controller.SellerController"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="Param" class="co.com.bookmaker.util.type.Parameter"></jsp:useBean>
<jsp:useBean id="Info" class="co.com.bookmaker.util.type.Information"></jsp:useBean>
<jsp:useBean id="Attr" class="co.com.bookmaker.util.type.Attribute"></jsp:useBean>
<jsp:useBean id="Status" class="co.com.bookmaker.util.type.Status"></jsp:useBean>
<jsp:useBean id="SportID" class="co.com.bookmaker.util.type.SportID"></jsp:useBean>

<c:set var="sports" value="${requestScope[Attr.SPORTS]}"></c:set>

<h2 class="main_content_title"> Buscar Juego </h2>

<form id="searchAgencyForm" role="form" class="form-horizontal" action="<%=MatchEventController.URL%>" method="GET">
    <input type="hidden" name="do" value="<%=MatchEventController.SEARCH%>">
    <input type="hidden" name="${Param.ROLE}" value="${param.roleRequester}">
    <div class="form-group">
        <label class="control-label col-md-2">Deporte: </label>
        <div class="col-md-4">
            <select id="selectSport" name="${Param.SPORT}" class="form-control">
                <option value="">Cualquiera</option>
                <c:forEach var="sport" items="${sports}">
                    <jsp:useBean id="sport" class="co.com.bookmaker.data_access.entity.event.Sport"></jsp:useBean> 
                    <option value="${sport.id}">${SportID.str(sport.id)}</option>
                </c:forEach>
            </select>
        </div>
        <output style="color: red">${requestScope[Info.SPORT]}</output>
    </div>
    <div class="form-group">
        <label class="control-label col-md-2">Torneo: </label>
        <div class="col-md-4">
            <select id="selectTournament" name="${Param.TOURNAMENT}" class="form-control">
                <option value="">Cualquiera</option>
            </select>
        </div>
        <output style="color: red">${requestScope[Info.TOURNAMENT]}</output>
    </div>
    <div class="form-group">
        <label class="control-label col-md-2">Autor: </label>
        <div class="col-md-4">
            <input type="text" class="form-control input-sm" id="sellerToSearch" name="${Param.USERNAME}" placeholder="User Name">
        </div>
        <output style="color: red">${requestScope[Info.USERNAME]}</output>
    </div>
    <div class="form-group">
        <label class="control-label col-md-2">Desde: </label>
        <div class="col-md-4">
            <input type="text" class="form-control input-sm" id="timeFrom" name="${Param.TIME_FROM}" value="${requestScope[Attr.TIME_FROM]}" placeholder="dd/MM/yyyy">
        </div>
        <output style="color: red">${requestScope[Info.TIME_FROM]}</output>
    </div>
    <div class="form-group">
        <label class="control-label col-md-2">Hasta: </label>
        <div class="col-md-4">
            <input type="text" class="form-control input-sm" id="timeTo" name="${Param.TIME_TO}" value="${requestScope[Attr.TIME_TO]}" placeholder="dd/MM/yyyy">
        </div>
        <output style="color: red">${requestScope[Info.TIME_TO]}</output>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">Estado: </label>
        <div class="col-md-4">
            <select name="${Param.STATUS}" class="form-control">
                <option value="">Cualquiera</option>
                <option value="${Status.ACTIVE}">${Status.str(Status.ACTIVE)}</option>
                <option value="${Status.INACTIVE}">${Status.str(Status.INACTIVE)}</option>
                <option value="${Status.PENDING_RESULT}">${Status.str(Status.PENDING_RESULT)}</option>
                <option value="${Status.FINISHED}">${Status.str(Status.FINISHED)}</option>
                <option value="${Status.CANCELLED}">${Status.str(Status.CANCELLED)}</option>
            </select>
        </div>
    </div>
    <div class="col-md-6" style="text-align: center">
        <button class="btn btn-submit" id="btnSearchMatch"><span class="glyphicon glyphicon-search"></span> Buscar</button>
    </div>
    <output style="color: red">${requestScope[Info.SEARCH_RESULT]}</output>
</form>
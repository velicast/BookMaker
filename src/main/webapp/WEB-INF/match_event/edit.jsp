<%-- 
    Document   : soccer edit
    Created on : Feb 24, 2016, 10:12:47 AM
    Author     : eduarc
--%>
<%@page import="co.com.bookmaker.business_logic.controller.event.MatchEventController"%>
<%@page import="co.com.bookmaker.business_logic.controller.AnalystController"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="Info" class="co.com.bookmaker.util.type.Information"></jsp:useBean>
<jsp:useBean id="Param" class="co.com.bookmaker.util.type.Parameter"></jsp:useBean>
<jsp:useBean id="Attr" class="co.com.bookmaker.util.type.Attribute"></jsp:useBean>
<jsp:useBean id="Status" class="co.com.bookmaker.util.type.Status"></jsp:useBean>
<jsp:useBean id="SportID" class="co.com.bookmaker.util.type.SportID"></jsp:useBean>
<jsp:useBean id="Type" class="co.com.bookmaker.util.type.OddType"></jsp:useBean>

<style>
    .tp_th_1 {
        width: 60%;
    }
    .tp_th_2 {
        width: 20%;
    }
    .tp_th_3 {
        width: 20%;
    }
    .tp_th_4 {
        width: 80%;
    }
    .tp_th_5 {
        width: 20%;
    }
    table td {
        text-align: center;
    }
    table th {
        text-align: center;
    }
</style>

<c:set var="match" value="${requestScope[Attr.MATCH_EVENT]}"></c:set>
<jsp:useBean id="match" class="co.com.bookmaker.util.form.bean.MatchEventBean"></jsp:useBean>

<c:set var="tournamentService" value="${requestScope[Attr.TOURNAMENT_SERVICE]}"></c:set>
<jsp:useBean id="tournamentService" class="co.com.bookmaker.business_logic.service.event.TournamentService"></jsp:useBean>

<h2 class="main_content_title"> Editar Juego de ${SportID.str(match.sportId)} </h2>
<form id="newMatchForm" role="form" class="form-horizontal" action="<%=MatchEventController.URL%>" method="POST">
    <input type="hidden" name="do" value="<%=MatchEventController.EDIT %>">
    <input type="hidden" name="${Param.MATCH_EVENT}" value="${match.id}">
    <input type="hidden" name="${Param.SPORT}" value="${match.sportId}">
    <div class="form-group">
        <label class="control-label col-md-2">Torneo: </label>
        <div class="col-md-4">
            <select name="${Param.TOURNAMENT}" class="form-control">
                <c:forEach var="tournament" items="${tournamentService.getTournaments(match.sportId, Status.ACTIVE)}">
                <jsp:useBean id="tournament" class="co.com.bookmaker.data_access.entity.event.Tournament"></jsp:useBean>
                <option value="${tournament.id}" ${match.tournament.id == tournament.id ? "selected" : ""}>${tournament.name}</option>
                </c:forEach>
            </select>
        </div>
        <output style="color: red">${requestScope[Info.TOURNAMENT]}</output>
    </div>
    <div class="form-group">
        <label class="control-label col-md-2">Nombre: </label>
        <div class="col-md-4">
            <input type="text" class="form-control input-sm" name="${Param.NAME}" value="${match.name}" placeholder="Nombre">
        </div>
        <output style="color: red">${requestScope[Info.NAME]}</output>
    </div>
    <div class="form-group">
        <label class="control-label col-md-2">Fecha de Inicio: </label>
        <div class="col-md-4">
            <input type="text" class="form-control input-sm" name="${Param.START_DATE}" value="${match.startDate}" placeholder="dd/MM/yyyy HH:mm">
        </div>
            <output style="color: red">${requestScope[Info.START_DATE]}</output>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">Estado: </label>
        <div class="col-md-4">
            <select class="form-control input-sm" name="${Param.STATUS}">
                <option value="${Status.ACTIVE}" ${match.status == Status.ACTIVE ? 'selected' : ''}> ${Status.str(Status.ACTIVE)} </option>
                <option value="${Status.INACTIVE}" ${match.status == Status.INACTIVE ? 'selected' : ''}> ${Status.str(Status.INACTIVE)} </option>
            </select>
        </div>
        <output style="color: red">${requestScope[Info.STATUS]}</output>
    </div>
    
    <c:forEach var="i" begin="0" end="${match.getnTeams()-1}">
    <div class="form-group">
        <label class="control-label col-md-2">Equipo ${i}: </label>
        <div class="col-md-4">
            <input id="nt${i}" type="text" class="team_name form-control input-sm" name="${Param.NAME} ${i}" value="${match.getTeam(i)}" placeholder="Nombre">
        </div>
        <c:set var="teamIdx" value="${Info.TEAM} ${i}"></c:set>
        <output style="color: red">${requestScope[teamIdx]}</output>
    </div>
    </c:forEach>
    
    <c:forEach var="i" begin="0" end="${match.getnPeriods()-1}">
    <div class="form-group">
        <label class="control-label col-md-2">${match.getPeriodName(i)}: </label>
        <c:if test="${match.getnTeams() == 2}">
        <div class="col-md-5">
            <table class="table table-hover table-bordered table-condensed">
                <caption>Puntos Totales</caption>
                <thead>
                    <tr>
                        <th class="tp_th_1">Lado</th>
                        <th class="tp_th_2">Puntos</th>
                        <th class="tp_th_3">Linea</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>Alta</td>
                        <td rowspan="2"><input type="text" class="form-control input-sm" name="${Param.TOTAL_POINTS} ${i}" value="${match.getTotalPoints(i)}"></td>
                        <td><input type="text" class="form-control input-sm" name="${Param.TOTAL_OVER_LINE} ${i}" value="${match.getLineOver(i)}"></td>
                    </tr>
                    <tr>
                        <td>Baja</td>
                        <td><input type="text" class="form-control input-sm" name="${Param.TOTAL_UNDER_LINE} ${i}" value="${match.getLineUnder(i)}"></td>
                    </tr>
                </tbody>
            </table>
            <c:set var="totalIdx" value="${Info.TOTAL} ${i}"></c:set>
            <output style="color: red">${requestScope[totalIdx]}</output>
        </div>
        <div class="col-md-5">
            <table class="table table-hover table-bordered table-condensed">
                <caption>Handicap</caption>
                <thead>
                    <tr>
                        <th class="tp_th_1">Equipo</th>
                        <th class="tp_th_2">Puntos</th>
                        <th class="tp_th_3">Linea</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td class="t0">${match.getTeam(0)}</td>
                        <td><input type="text" class="form-control input-sm" name="${Param.SPREAD} ${i}" value="${match.getSpreadPoints(i)}"></td>
                        <td><input type="text" class="form-control input-sm" name="${Param.SPREAD_TEAM0_LINE} ${i}" value="${match.getLineTeam0(i)}"></td>
                    </tr>
                    <tr>
                        <td class="t1">${match.getTeam(1)}</td>
                        <td></td>
                        <td><input type="text" class="form-control input-sm" name="${Param.SPREAD_TEAM1_LINE} ${i}" value="${match.getLineTeam1(i)}"></td>
                    </tr>
                </tbody>
            </table>
            <c:set var="spreadIdx" value="${Info.SPREAD} ${i}"></c:set>
            <output style="color: red">${requestScope[spreadIdx]}</output>
        </div>
        </c:if>
    </div>
    <div class="form-group">
        <div class="col-md-2"></div>
        <div class="col-md-5">
        <table class="table table-hover table-bordered table-condensed">
            <caption>Linea de Dinero</caption>
            <thead>
                <tr>
                    <th class="tp_th_4">Equipo</th>
                    <th class="tp_th_5">Linea</th>
                </tr>
            </thead>
            <tbody>
            <c:forEach var="j" begin="0" end="${match.getnTeams()-1}">
                <tr>
                    <td class="t${j}">${match.getTeam(j)}</td>
                    <td><input type="text" class="form-control input-sm" name="${Param.MONEY_LINE} ${i} ${j}" value="${match.getMoneyLine(i, j)}"></td>
                </tr>
                <c:set var="moneyLineIdx" value="${Info.MONEY_LINE} ${i} ${j}"></c:set>
                <output style="color: red">${requestScope[moneyLineIdx]}</output>
            </c:forEach>
            </tbody>
        </table>
        <c:set var="periodIdx" value="${Info.PERIOD} ${i}"></c:set>
        <output style="color: red">${requestScope[periodIdx]}</output>
        </div>
        <div class="col-md-5">
            <table class="table table-hover table-bordered table-condensed">
                <caption>Linea de Empate</caption>
                <thead>
                    <tr>
                        <th class="tp_th_1">Equipo</th>
                        <th class="tp_th_2">Linea</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>Empate</td>
                        <td><input type="text" class="form-control input-sm" name="${Param.DRAW_LINE} ${i}" value="${match.getDrawLine(i)}"></td>
                    </tr>
                </tbody>
            </table>
            <c:set var="drawIdx" value="${Info.DRAW_LINE} ${i}"></c:set>
            <output style="color: red">${requestScope[drawIdx]}</output>
        </div>
    </div>
    </c:forEach>
    <div class="col-md-6" style="text-align: center">
        <button class="btn btn-submit" id="btnEditMatch"><span class="glyphicon glyphicon-save"></span> Guardar</button>
    </div>
</form>

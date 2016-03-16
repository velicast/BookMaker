<%-- 
    Document   : new
    Created on : Feb 25, 2016, 8:31:08 PM
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

<c:set var="sUser" value="${sessionScope[Attr.SESSION_USER]}"></c:set>
<jsp:useBean id="sUser" class="co.com.bookmaker.data_access.entity.FinalUser"></jsp:useBean>

<c:set var="tournamentService" value="${requestScope[Attr.TOURNAMENT_SERVICE]}"></c:set>
<jsp:useBean id="tournamentService" class="co.com.bookmaker.business_logic.service.event.TournamentService"></jsp:useBean>

<c:set var="data" value="${requestScope[Attr.MATCH_EVENT]}"></c:set>
<jsp:useBean id="data" class="co.com.bookmaker.util.form.bean.MatchEventBean"></jsp:useBean>

<h2 class="main_content_title"> New ${SportID.str(sportId)} Match </h2>
<form id="newMatchForm" role="form" class="form-horizontal" action="<%=MatchEventController.URL%>" method="POST">
    <input type="hidden" name="do" value="<%=MatchEventController.NEW %>">
    <input type="hidden" name="${Param.SPORT}" value="${data.sportId}">
    <div class="form-group">
        <label class="control-label col-md-2">Tournament: </label>
        <div class="col-md-4">
            <select name="${Param.TOURNAMENT}" class="form-control">
                <c:forEach var="tournament" items="${tournamentService.getTournaments(sUser.agency.id, data.sportId, Status.ACTIVE)}">
                <jsp:useBean id="tournament" class="co.com.bookmaker.data_access.entity.event.Tournament"></jsp:useBean>
                <option value="${tournament.id}" ${data.tournamentId == tournament.id ? 'selected' : ''}>${tournament.name}</option>
                </c:forEach>
            </select>
        </div>
        <output style="color: red">${requestScope[Info.TOURNAMENT]}</output>
    </div>
    <div class="form-group">
        <label class="control-label col-md-2">Name: </label>
        <div class="col-md-4">
            <input type="text" class="form-control input-sm" name="${Param.NAME}" value="${data.name}" placeholder="Name">
        </div>
        <output style="color: red">${requestScope[Info.NAME]}</output>
    </div>
    <div class="form-group">
        <label class="control-label col-md-2">Start Date: </label>
        <div class="col-md-4">
            <input type="text" class="form-control input-sm" name="${Param.START_DATE}" value="${data.startDate}" placeholder="dd/MM/yyyy HH:mm">
        </div>
        <output style="color: red">${requestScope[Info.START_DATE]}</output>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">Status: </label>
        <div class="col-md-4">
            <select class="form-control input-sm" name="${Param.STATUS}">
                <option value="${Status.ACTIVE}" ${data.status == Status.ACTIVE ? 'selected' : ''}> ${Status.str(Status.ACTIVE)} </option>
                <option value="${Status.INACTIVE}" ${data.status == Status.INACTIVE ? 'selected' : ''}> ${Status.str(Status.INACTIVE)} </option>
            </select>
        </div>
        <output style="color: red">${requestScope[Info.STATUS]}</output>
    </div>
    
    <c:forEach var="i" begin="0" end="${data.getnTeams()-1}">
    <div class="form-group">
        <label class="control-label col-md-2">Team ${i}: </label>
        <div class="col-md-4">
            <input id="nt${i}" type="text" class="team_name form-control input-sm" name="${Param.NAME} ${i}" value="${data.getTeam(i)}" placeholder="Name">
        </div>
        <c:set var="teamIdx" value="${Info.TEAM} ${i}"></c:set>
        <output style="color: red">${requestScope[teamIdx]}</output>
    </div>
    </c:forEach>
    
    <c:forEach var="i" begin="0" end="${data.getnPeriods()-1}">
    <div class="form-group">
        <label class="control-label col-md-2">${data.getPeriodName(i)}: </label>
        <c:if test="${data.getnTeams() == 2}">
        <div class="col-md-5">
            <table class="table table-hover table-bordered table-condensed">
                <caption>Total Points</caption>
                <thead>
                    <tr>
                        <th class="tp_th_1">Side</th>
                        <th class="tp_th_2">Points</th>
                        <th class="tp_th_3">Line</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>Over</td>
                        <td rowspan="2"><input type="text" class="form-control input-sm" name="${Param.TOTAL_POINTS} ${i}" value="${data.getTotalPoints(i)}"></td>
                        <td><input type="text" class="form-control input-sm" name="${Param.TOTAL_OVER_LINE} ${i}" value="${data.getLineOver(i)}"></td>
                    </tr>
                    <tr>
                        <td>Under</td>
                        <td><input type="text" class="form-control input-sm" name="${Param.TOTAL_UNDER_LINE} ${i}" value="${data.getLineUnder(i)}"></td>
                    </tr>
                </tbody>
            </table>
            <c:set var="totalIdx" value="${Info.TOTAL} ${i}"></c:set>
            <output style="color: red">${requestScope[totalIdx]}</output>
        </div>
        <div class="col-md-5">
            <table class="table table-hover table-bordered table-condensed">
                <caption>Spread</caption>
                <thead>
                    <tr>
                        <th class="tp_th_1">Team</th>
                        <th class="tp_th_2">Points</th>
                        <th class="tp_th_3">Line</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td class="t0">${data.getTeam(0)}</td>
                        <td><input type="text" class="form-control input-sm" name="${Param.SPREAD} ${i}" value="${data.getSpreadPoints(i)}"></td>
                        <td><input type="text" class="form-control input-sm" name="${Param.SPREAD_TEAM0_LINE} ${i}" value="${data.getLineTeam0(i)}"></td>
                    </tr>
                    <tr>
                        <td class="t1">${data.getTeam(1)}</td>
                        <td></td>
                        <td><input type="text" class="form-control input-sm" name="${Param.SPREAD_TEAM1_LINE} ${i}" value="${data.getLineTeam1(i)}"></td>
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
                <caption>Money Line</caption>
                <thead>
                    <tr>
                        <th class="tp_th_4">Team</th>
                        <th class="tp_th_5">Line</th>
                    </tr>
                </thead>
                <tbody>
                <c:forEach var="j" begin="0" end="${data.getnTeams()-1}">
                    <tr>
                        <td class="t${j}">${data.getTeam(j)}</td>
                        <td><input type="text" class="form-control input-sm" name="${Param.MONEY_LINE} ${i} ${j}" value="${data.getMoneyLine(i, j)}"></td>
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
                <caption>Draw Line</caption>
                <thead>
                    <tr>
                        <th class="tp_th_1">Team</th>
                        <th class="tp_th_2">Line</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>Draw Line</td>
                        <td><input type="text" class="form-control input-sm" name="${Param.DRAW_LINE} ${i}" value="${data.getDrawLine(i)}"></td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
    </c:forEach>
    <div class="col-md-6" style="text-align: center">
        <button class="btn btn-submit" id="btnNewMatch"><span class="glyphicon glyphicon-check"></span> Create</button>
    </div>
</form>
<%-- 
    Document   : summary
    Created on : Feb 25, 2016, 9:18:02 AM
    Author     : eduarc
--%>
<%@page import="co.com.bookmaker.business_logic.controller.event.MatchEventController"%>
<%@page import="co.com.bookmaker.business_logic.controller.AnalystController"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="en_US"></fmt:setLocale>
    
<jsp:useBean id="Info" class="co.com.bookmaker.util.type.Information"></jsp:useBean>
<jsp:useBean id="Param" class="co.com.bookmaker.util.type.Parameter"></jsp:useBean>
<jsp:useBean id="Attr" class="co.com.bookmaker.util.type.Attribute"></jsp:useBean>
<jsp:useBean id="Status" class="co.com.bookmaker.util.type.Status"></jsp:useBean>
<jsp:useBean id="SportID" class="co.com.bookmaker.util.type.SportID"></jsp:useBean>
<jsp:useBean id="Type" class="co.com.bookmaker.util.type.OddType"></jsp:useBean>

<style>
    .form-group {
        margin-top: 0px;
        margin-bottom: 0px;
        padding-bottom: 0px;
        padding-top: 0px;
    }
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
    
<c:set var="match" value="${requestScope[Attr.MATCH_EVENT]}"></c:set>
<jsp:useBean id="match" class="co.com.bookmaker.data_access.entity.event.MatchEvent"></jsp:useBean>

<c:set var="matchPeriodService" value="${requestScope[Attr.MATCH_PERIOD_SERVICE]}"></c:set>
<jsp:useBean id="matchPeriodService" class="co.com.bookmaker.business_logic.service.event.MatchEventPeriodService"></jsp:useBean>

<c:set var="teamService" value="${requestScope[Attr.TEAM_SERVICE]}"></c:set>
<jsp:useBean id="teamService" class="co.com.bookmaker.business_logic.service.event.TeamService"></jsp:useBean>

<c:set var="oddService" value="${requestScope[Attr.PARLAYODD_SERVICE]}"></c:set>
<jsp:useBean id="oddService" class="co.com.bookmaker.business_logic.service.parlay.ParlayOddService"></jsp:useBean>

<h2 class="main_content_title"> ${SportId.str(match.tournament.sport.id)} Match Summary </h2>
<form id="newMatchForm" role="form" class="form-horizontal">
    <c:if test="${match.author.id == sUser.id}">
    <div class="form-group">
        <c:if test="${match.status == Status.ACTIVE || match.status == Status.INACTIVE}">
        <div class="col-md-6">
            <a id="btnEditMatch" class="btn btn-default" 
                   href="<%=AnalystController.URL%>?to=<%=AnalystController.EDIT_MATCH%>&${Param.MATCH_EVENT}=${match.id}">
                    <span class="glyphicon glyphicon-edit"></span> Edit</a>
            <a id="btnCancelMatch" class="btn btn-default" style="color: red"
                   href="<%=MatchEventController.URL%>?do=<%=MatchEventController.CANCEL%>&${Param.MATCH_EVENT}=${match.id}">
                    <span class="glyphicon glyphicon-remove"></span> Cancel</a>
        </div>
        </c:if>
        <c:if test="${match.status == Status.PENDING_RESULT || match.status == Status.FINISHED}">
        <div class="col-md-6">
            <a id="btnMatchResult" class="btn btn-default"
                   href="<%=AnalystController.URL%>?to=<%=AnalystController.MATCH_RESULT%>&${Param.MATCH_EVENT}=${match.id}">
                    <span class="glyphicon glyphicon-stats"></span> Result</a>
        </div>
        </c:if>
    </div>
    </c:if>
    <div class="form-group">
        <label class="control-label col-md-2">Author: </label>
        <div class="col-md-4">
            <p class="form-control-static">${match.author.username} - ${match.author.firstName} ${match.author.lastName}</p>
        </div>
    </div>
    <div class="form-group">
        <label class="control-label col-md-2">Tournament: </label>
        <div class="col-md-4">
            <p class="form-control-static">${match.tournament.name}</p>
        </div>
    </div>
    <div class="form-group">
        <label class="control-label col-md-2">Name: </label>
        <div class="col-md-4">
            <p class="form-control-static">${match.name}</p>
        </div>
    </div>
    <div class="form-group">
        <label class="control-label col-md-2">Start Date: </label>
        <div class="col-md-4">
            <fmt:formatDate type="date" pattern="dd/MM/yyyy" value="${match.startDate.getTime()}" var="startDate"/>
            <fmt:formatDate type="time" pattern="HH:mm" value="${match.startDate.getTime()}" var="startTime"/>
            <p class="form-control-static">${startDate}<br/>${startTime}</p>
        </div>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">Status: </label>
        <div class="col-md-4">
            <p class="form-control-static">${Status.str(match.status)}</p>
        </div>
    </div>
    <c:set var="periods" value="${matchPeriodService.getPeriods(match)}"></c:set>    
    <c:set var="teams" value="${teamService.getTeams(match)}"></c:set>
    
    <c:forEach var="i" begin="0" end="${teams.size()-1}">
    <c:set var="team" value="${teams.get(i)}"></c:set>
    <jsp:useBean id="team" class="co.com.bookmaker.data_access.entity.event.Team"></jsp:useBean>
    <div class="form-group">
        <label class="control-label col-md-2">Team ${i}: </label>
        <div class="col-md-4">
            <p class="form-control-static">${team.name}</p>
        </div>
    </div>
    </c:forEach>

    <c:forEach var="i" begin="0" end="${periods.size()-1}">
    <c:set var="period" value="${periods.get(i)}"></c:set>
    <jsp:useBean id="period" class="co.com.bookmaker.data_access.entity.event.MatchEventPeriod"></jsp:useBean>
    <div class="form-group">
        <label class="control-label col-md-2">${period.name}: </label>
        <c:if test="${teams.size() == 2}">
        <c:set var="over" value="${oddService.getOddByPeriod(period, Type.TOTAL_OVER, Status.SELLING)}"></c:set>
        <c:if test="${over != null}">
        <c:set var="under" value="${oddService.getOddByPeriod(period, Type.TOTAL_UNDER, Status.SELLING)}"></c:set>
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
                        
                        <jsp:useBean id="over" class="co.com.bookmaker.data_access.entity.parlay.ParlayOdd"></jsp:useBean>
                        <fmt:formatNumber var="oPoints" value="${over.points}" maxFractionDigits="${1}" ></fmt:formatNumber>
                        <fmt:formatNumber var="oLine" value="${over.line}" maxFractionDigits="${1}" ></fmt:formatNumber>
                        <td rowspan="2">${oPoints > 0.0 ? "+" : ""}${oPoints}</td>
                        <td>${oLine > 0.0 ? "+" : ""}${oLine}</td>
                    </tr>
                    <tr>
                        <td>Under</td>
                        
                        <jsp:useBean id="under" class="co.com.bookmaker.data_access.entity.parlay.ParlayOdd"></jsp:useBean>
                        <fmt:formatNumber var="uLine" value="${under.line}" maxFractionDigits="${1}" ></fmt:formatNumber>
                        <td>${uLine > 0.0 ? "+" : ""}${uLine}</td>
                    </tr>
                </tbody>
            </table>
        </div>
        </c:if>
        <c:set var="sp0" value="${oddService.getOdd(teams.get(0), period, Type.SPREAD_TEAM0, Status.SELLING)}"></c:set>
        <c:if test="${sp0 != null}">
        <c:set var="sp1" value="${oddService.getOdd(teams.get(1), period, Type.SPREAD_TEAM1, Status.SELLING)}"></c:set>
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
                        <td>${teams.get(0).name}</td>
                        <jsp:useBean id="sp0" class="co.com.bookmaker.data_access.entity.parlay.ParlayOdd"></jsp:useBean>
                        <fmt:formatNumber var="sp0Points" value="${sp0.points}" maxFractionDigits="${1}" ></fmt:formatNumber>
                        <td>${sp0Points > 0.0 ? "+" : ""}${sp0Points}</td>
                        <fmt:formatNumber var="sp0Line" value="${sp0.line}" maxFractionDigits="${1}" ></fmt:formatNumber>
                        <td>${sp0Line > 0.0 ? "+" : ""}${sp0Line}</td>
                    </tr>
                    <tr>
                        <td>${teams.get(1).name}</td>
                        <jsp:useBean id="sp1" class="co.com.bookmaker.data_access.entity.parlay.ParlayOdd"></jsp:useBean>
                        <fmt:formatNumber var="sp1Points" value="${sp1.points}" maxFractionDigits="${1}" ></fmt:formatNumber>
                        <td>${sp1Points > 0.0 ? "+" : ""}${sp1Points}</td>
                        <fmt:formatNumber var="sp1Line" value="${sp1.line}" maxFractionDigits="${1}" ></fmt:formatNumber>
                        <td>${sp1Line > 0.0 ? "+" : ""}${sp1Line}</td>
                    </tr>
                </tbody>
            </table>
        </div>
        </c:if>
        </c:if>
    </div>
    <div class="form-group">  
        <c:set var="ml" value="${oddService.getOdd(teams.get(j), period, Type.MONEY_LINE, Status.SELLING)}"></c:set>
        <c:if test="${ml != null}">
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
                <c:forEach var="team" items="${teams}">
                    <tr>
                        <td>${team.name}</td>
                        <c:set var="ml" value="${oddService.getOdd(team, period, Type.MONEY_LINE, Status.SELLING)}"></c:set>
                        <jsp:useBean id="ml" class="co.com.bookmaker.data_access.entity.parlay.ParlayOdd"></jsp:useBean>
                        <fmt:formatNumber var="line" value="${ml.line}" maxFractionDigits="${1}" ></fmt:formatNumber>
                        <td>${line > 0.0 ? "+" : ""}${line}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
        </c:if>
        <c:set var="draw" value="${oddService.getOddByPeriod(periods.get(i), Type.DRAW_LINE, Status.SELLING)}"></c:set>
        <c:if test="${draw != null}">
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
                        <jsp:useBean id="draw" class="co.com.bookmaker.data_access.entity.parlay.ParlayOdd"></jsp:useBean>
                        <fmt:formatNumber var="line" value="${draw.line}" maxFractionDigits="${1}" ></fmt:formatNumber>
                        <td>${line > 0.0 ? "+" : ""}${line}</td>
                    </tr>
                </tbody>
            </table>
            <c:set var="drawIdx" value="${Info.DRAW_LINE} ${i}"></c:set>
            <output style="color: red">${requestScope[drawIdx]}</output>
        </div>
        </c:if>
    </div>
    </c:forEach>
    <c:if test="${match.author.id == sUser.id}">
    <div class="form-group">
        <c:if test="${match.status == Status.ACTIVE || match.status == Status.INACTIVE}">
        <div class="col-md-6">
            <a id="btnEditMatch" class="btn btn-default" 
                   href="<%=AnalystController.URL%>?to=<%=AnalystController.EDIT_MATCH%>&${Param.MATCH_EVENT}=${match.id}">
                    <span class="glyphicon glyphicon-edit"></span> Edit</a>
            <a id="btnCancelMatch" class="btn btn-default" style="color: red"
                   href="<%=MatchEventController.URL%>?do=<%=MatchEventController.CANCEL%>&${Param.MATCH_EVENT}=${match.id}">
                    <span class="glyphicon glyphicon-remove"></span> Cancel</a>
        </div>
        </c:if>
        <c:if test="${match.status == Status.PENDING_RESULT || match.status == Status.FINISHED}">
        <div class="col-md-6">
            <a id="btnMatchResult" class="btn btn-default"
                   href="<%=AnalystController.URL%>?to=<%=AnalystController.MATCH_RESULT%>&${Param.MATCH_EVENT}=${match.id}">
                    <span class="glyphicon glyphicon-stats"></span> Result</a>
        </div>
        </c:if>
    </div>
    </c:if>
</form>

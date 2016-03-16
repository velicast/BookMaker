<%-- 
    Document   : matches
    Created on : Feb 27, 2016, 10:11:07 PM
    Author     : eduarc
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="en_US"></fmt:setLocale>
    
<jsp:useBean id="Attr" class="co.com.bookmaker.util.type.Attribute"></jsp:useBean>
<jsp:useBean id="Status" class="co.com.bookmaker.util.type.Status"></jsp:useBean>
<jsp:useBean id="Type" class="co.com.bookmaker.util.type.OddType"></jsp:useBean>

<c:set var="tournament" value="${requestScope[Attr.TOURNAMENT]}"></c:set>
<jsp:useBean id="tournament" class="co.com.bookmaker.data_access.entity.event.Tournament"></jsp:useBean>
    
<c:set var="matchEventService" value="${requestScope[Attr.MATCH_EVENT_SERVICE]}"></c:set>
<jsp:useBean id="matchEventService" class="co.com.bookmaker.business_logic.service.event.MatchEventService"></jsp:useBean>

<c:set var="matchPeriodService" value="${requestScope[Attr.MATCH_PERIOD_SERVICE]}"></c:set>
<jsp:useBean id="matchPeriodService" class="co.com.bookmaker.business_logic.service.event.MatchEventPeriodService"></jsp:useBean>

<c:set var="teamService" value="${requestScope[Attr.TEAM_SERVICE]}"></c:set>
<jsp:useBean id="teamService" class="co.com.bookmaker.business_logic.service.event.TeamService"></jsp:useBean>

<c:set var="oddService" value="${requestScope[Attr.PARLAYODD_SERVICE]}"></c:set>
<jsp:useBean id="oddService" class="co.com.bookmaker.business_logic.service.parlay.ParlayOddService"></jsp:useBean>

<table class="table mit">
<tr>
    <th class="th1">Date</th>
    <th class="th2">Match</th>
    <th class="th3">Money Line</th>
    <th class="th4">Spread</th>
    <th class="th5">Total</th>
</tr>
</table>
<c:forEach var="match" items="${matchEventService.getMatchesByTournament(tournament, Status.ACTIVE)}">
<jsp:useBean id="match" class="co.com.bookmaker.data_access.entity.event.MatchEvent"></jsp:useBean>
<c:set var="teams" value="${teamService.getTeams(match)}"></c:set>
<c:forEach var="period" items="${matchPeriodService.getPeriods(match, Status.ACTIVE)}">
<jsp:useBean id="period" class="co.com.bookmaker.data_access.entity.event.MatchEventPeriod"></jsp:useBean>
<table class="table table-bordered table-condensed mit">
    <tr>
        <th class="td_t" colspan="5">${period.name}</th>
    </tr>
    
    <c:set var="draw" value="${oddService.getOddByPeriod(period, Type.DRAW_LINE, Status.SELLING)}"></c:set>
    
    <c:forEach var="idx" begin="0" end="${teams.size()-1}">
    <tr>
        <c:set var="team" value="${teams.get(idx)}"></c:set>
        <jsp:useBean id="team" class="co.com.bookmaker.data_access.entity.event.Team"></jsp:useBean>

        <c:if test="${idx == 0}">
        <td class="td_t th1" rowspan="${teams.size()+(draw != null ? 1 : 0)}"><fmt:formatDate type="time" pattern="dd/MM/yyyy HH:mm" value="${period.cutoff.getTime()}"/></td>    
        </c:if>

        <td class="td_t th2">${team.name}</td>

        <td class="td_t th3">
            <c:set var="moneyLine" value="${oddService.getOdd(team, period, Type.MONEY_LINE, Status.SELLING)}"></c:set>
            <c:if test="${moneyLine != null}">
            <jsp:useBean id="moneyLine" class="co.com.bookmaker.data_access.entity.parlay.ParlayOdd"></jsp:useBean>
            <fmt:formatNumber var="line" value="${moneyLine.line}" maxFractionDigits="${1}" ></fmt:formatNumber>
            <input type="checkbox" id="chk${moneyLine.id}" value="${moneyLine.id}"> ${line > 0.0 ? '+' : ''}${line}
            </c:if>
        </td>
        <td class="td_t th4">
            <c:if test="${idx == 0}">
            <c:set var="spread" value="${oddService.getOdd(team, period, Type.SPREAD_TEAM0, Status.SELLING)}"></c:set>
            </c:if>
            <c:if test="${idx == 1}">
            <c:set var="spread" value="${oddService.getOdd(team, period, Type.SPREAD_TEAM1, Status.SELLING)}"></c:set>
            </c:if>
            <c:if test="${spread != null}">
            <jsp:useBean id="spread" class="co.com.bookmaker.data_access.entity.parlay.ParlayOdd"></jsp:useBean>
            <fmt:formatNumber var="line" value="${spread.line}" maxFractionDigits="${1}" ></fmt:formatNumber>
            <fmt:formatNumber var="points" value="${spread.points}" maxFractionDigits="${1}" ></fmt:formatNumber>
            <input type="checkbox" class="chk" id="chk${spread.id}" value="${spread.id}"> ${points > 0.0 ? "+" : ""}${points} ${line > 0.0 ? "+" : ""}${line}
            </c:if>
        </td>

        <c:if test="${idx == 0}">
        <td class="td_t th5" rowspan="${teams.size()}">
            <c:set var="over" value="${oddService.getOddByPeriod(period, Type.TOTAL_OVER, Status.SELLING)}"></c:set>
            <c:if test="${over != null}">
            <jsp:useBean id="over" class="co.com.bookmaker.data_access.entity.parlay.ParlayOdd"></jsp:useBean>
            <fmt:formatNumber var="line" value="${over.line}" maxFractionDigits="${1}" ></fmt:formatNumber>
            <fmt:formatNumber var="points" value="${over.points}" maxFractionDigits="${1}" ></fmt:formatNumber>
            <input type="checkbox" id="chk${over.id}" value="${over.id}"> O ${points > 0.0 ? '+' : ''}${points} ${line > 0.0 ? '+' : ''}${line} <br/>
            <c:set var="under" value="${oddService.getOddByPeriod(period, Type.TOTAL_UNDER, Status.SELLING)}"></c:set>
            <jsp:useBean id="under" class="co.com.bookmaker.data_access.entity.parlay.ParlayOdd"></jsp:useBean>
            <fmt:formatNumber var="line" value="${under.line}" maxFractionDigits="${1}" ></fmt:formatNumber>
            <fmt:formatNumber var="points" value="${under.points}" maxFractionDigits="${1}" ></fmt:formatNumber>
            <input type="checkbox" id="chk${under.id}" value="${under.id}"> U ${points > 0.0 ? '+' : ''}${points} ${line > 0.0 ? '+' : ''}${line}
            </c:if>
        </td>
        </c:if>
    </tr>
    </c:forEach>
    <c:if test="${draw != null}">
    <tr>
        <td class="td_t th2">Draw</td>
        <jsp:useBean id="draw" class="co.com.bookmaker.data_access.entity.parlay.ParlayOdd"></jsp:useBean>
        <fmt:formatNumber var="line" value="${draw.line}" maxFractionDigits="${1}" ></fmt:formatNumber>
        <td class="td_t th3"><input type="checkbox" id="chk${draw.id}" value="${draw.id}"> ${line > 0.0 ? '+' : ''}${line}</td>
    </tr>
    </c:if>
</table>
</c:forEach>
</c:forEach>
<script src="/BookMaker/js/client/matches.js"></script>
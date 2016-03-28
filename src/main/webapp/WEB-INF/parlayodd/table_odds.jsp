<%-- 
    Document   : table_odds
    Created on : Feb 19, 2016, 6:37:59 PM
    Author     : eduarc
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:useBean id="Attr" class="co.com.bookmaker.util.type.Attribute"></jsp:useBean>
<jsp:useBean id="Status" class="co.com.bookmaker.util.type.Status"></jsp:useBean>
<jsp:useBean id="Type" class="co.com.bookmaker.util.type.OddType"></jsp:useBean>

<fmt:setLocale value="en_US"></fmt:setLocale>

<style>
    .table_odds_th1 {
        width: 12%;
    }
    .table_odds_th2 {
        width: 20%;
    }
    .table_odds_th3 {
        width: 28%;
    }
    .table_odds_th4 {
        width: 14%;
    }
    .table_odds_th5 {
        width: 14%;
    }
    .table_odds_th6 {
        width: 12%;
    }
    .table td {
        text-align: center;
    }
    .table th {
        text-align: center;
    }
    .status_label {
        margin: 0px;
        padding: 0px;
    }
    .table .td_t {
        font-size: 12px;
        padding-top : 2px;
        padding-bottom : 5px;
    }
</style>

<table class="table table-hover table-bordered table-condensed">
    <thead>
        <tr>
            <th class="table_odds_th1">Line</th>
            <th class="table_odds_th2">Team</th>
            <th class="table_odds_th3">Match</th>
            <th class="table_odds_th4">Period</th>
            <th class="table_odds_th5">Date</th>
            <th class="table_odds_th6">Status</th>
        </tr>
    </thead>
    <tbody>
    <c:forEach var="odd" items="${requestScope[Attr.PARLAY_ODDS]}">
        <jsp:useBean id="odd" class="co.com.bookmaker.data_access.entity.parlay.ParlayOdd"></jsp:useBean>
        <tr>
            <td class="td_t">
            <fmt:formatNumber var="line" value="${odd.line}" maxFractionDigits="${1}" ></fmt:formatNumber>
            <fmt:formatNumber var="points" value="${odd.points}" maxFractionDigits="${1}" ></fmt:formatNumber>
            <c:if test="${odd.type == Type.MONEY_LINE}">
            ${line > 0.0 ? "+" : ""}${line}
            </c:if>
            <c:if test="${odd.type == Type.SPREAD_TEAM0 || odd.type == Type.SPREAD_TEAM1}">
            ${points > 0.0 ? "+" : ""}${points} ${line > 0.0 ? "+" : ""}${line}
            </c:if>
            <c:if test="${odd.type == Type.TOTAL_OVER || odd.type == Type.TOTAL_UNDER}">
            ${odd.type == Type.TOTAL_OVER ? "O " : "U "} ${points > 0.0 ? "+" : ""}${points} ${line > 0.0 ? "+" : ""}${line}
            </c:if>
            <c:if test="${odd.type == Type.DRAW_LINE}">
            Draw ${line > 0.0 ? "+" : ""}${line}
            </c:if>
            </td>
            <td class="td_t">${odd.team.name}</td>
            <td class="td_t">${odd.period.match.name}</td>
            <td class="td_t">${odd.period.name}</td>
            <td class="td_t"><fmt:formatDate type="both" pattern="dd/MM/yyyy HH:mm" value="${odd.period.match.startDate.getTime()}"/></td>
            <td class="td_t">
                <c:if test="${odd.status == Status.WIN}">
                <h4 class="status_label"><span class = "label label-success">${Status.str(odd.status)}</span></h4>
                </c:if>
                <c:if test="${odd.status == Status.LOSE}">
                <h4 class="status_label"><span class = "label label-danger">${Status.str(odd.status)}</span></h4>
                </c:if>
                <c:if test="${odd.status == Status.PUSH}">
                <h4 class="status_label"><span class = "label label-primary">${Status.str(odd.status)}</span></h4>
                </c:if>
                <c:if test="${odd.status == Status.CANCELLED}">
                <h4 class="status_label"><span class = "label label-info">${Status.str(odd.status)}</span></h4>
                </c:if>
                <c:if test="${odd.status == Status.INVALID}">
                <h4 class="status_label"><span class = "label label-warning">${Status.str(odd.status)}</span></h4>
                </c:if>
                <c:if test="${odd.status == Status.PENDING || odd.status == Status.SELLING}">
                <h4 class="status_label"><span class = "label label-default">${Status.str(odd.status)}</span></h4>
                </c:if>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

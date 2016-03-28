<%-- 
    Document   : ticket
    Created on : Feb 20, 2016, 4:01:35 PM
    Author     : eduarc
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="en_US"></fmt:setLocale>
    
<jsp:useBean id="Attr" class="co.com.bookmaker.util.type.Attribute"></jsp:useBean>
<jsp:useBean id="Type" class="co.com.bookmaker.util.type.OddType"></jsp:useBean>

<c:set var="odd" value="${requestScope[Attr.PARLAY_ODD]}"></c:set>
<jsp:useBean id="odd" class="co.com.bookmaker.data_access.entity.parlay.ParlayOdd"></jsp:useBean>

<div id="d${odd.id}" class="alert alert-info ticket_bet">
    <a href="#" class="close" data-dismiss="alert">&times;</a>
    <b>${odd.team.name}
        <span class="oddvalue">
            <fmt:formatNumber var="line" value="${odd.line}" maxFractionDigits="${1}" ></fmt:formatNumber>
            <fmt:formatNumber var="points" value="${odd.points}" maxFractionDigits="${1}" ></fmt:formatNumber>
            <c:if test="${odd.type == Type.MONEY_LINE}">
            ${line > 0.0 ? "+" : ""}${line}
            </c:if>
            <c:if test="${odd.type == Type.SPREAD_TEAM0 || odd.type == Type.SPREAD_TEAM1}">
            ${points > 0.0 ? "+" : ""}${points} ${line > 0.0 ? "+" : ""}${line}
            </c:if>
            <c:if test="${odd.type == Type.TOTAL_OVER || odd.type == Type.TOTAL_UNDER}">
            ${odd.type == Type.TOTAL_OVER ? "Over " : "Under "} ${points > 0.0 ? "+" : ""}${points} ${line > 0.0 ? "+" : ""}${line}
            </c:if>
            <c:if test="${odd.type == Type.DRAW_LINE}">
            Draw ${line > 0.0 ? "+" : ""}${line}
            </c:if>
        </span>
    </b> <br/>
    <small>${odd.period.match.name} | ${odd.period.name}</small> <br/>
    <small><b><fmt:formatDate type="time" pattern="dd/MM/yyyy HH:mm" value="${odd.period.cutoff.getTime()}"/></b></small>
</div>
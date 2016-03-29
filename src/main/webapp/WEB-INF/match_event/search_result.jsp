<%-- 
    Document   : search_result
    Created on : Feb 26, 2016, 12:37:44 AM
    Author     : eduarc
--%>
<%@page import="co.com.bookmaker.business_logic.controller.ManagerController"%>
<%@page import="co.com.bookmaker.business_logic.controller.AnalystController"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="en_US"></fmt:setLocale>

<!-- Define constant wrappers here -->
<jsp:useBean id="Param" class="co.com.bookmaker.util.type.Parameter"></jsp:useBean>
<jsp:useBean id="Attr" class="co.com.bookmaker.util.type.Attribute"></jsp:useBean>
<jsp:useBean id="Status" class="co.com.bookmaker.util.type.Status"></jsp:useBean>
<jsp:useBean id="Role" class="co.com.bookmaker.util.type.Role"></jsp:useBean>

<style>
    .match_th1 {
        text-align: center;
        width: 32%;
    }
    .match_th2 {
        text-align: center;
        width: 27%;
    }
    .match_th3 {
        text-align: center;
        width: 15%;
    }
    .match_th4 {
        text-align: center;
        width: 12%;
    }
    .match_th5 {
        text-align: center;
        width: 15%;
    }
    .table td {
        text-align: center;
    }  
</style>

<c:set var="matches" value="${requestScope[Attr.MATCH_EVENT]}"></c:set>

<table class="table table-hover table-bordered table-condensed">
    <thead>
    <capition>${matches.size()} resultados encontrados</caption> 
    <tr>
        <th class="match_th1">Juego</th>
        <th class="match_th2">Torneo</th>
        <th class="match_th3">Deporte</th>
        <th class="match_th4">Inicio</th>
        <th class="match_th5">Estado</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="match" items="${matches}">
        <jsp:useBean id="match" class="co.com.bookmaker.data_access.entity.event.MatchEvent"></jsp:useBean>
        <tr>
            <c:if test="${param.roleRequester == Role.ANALYST}">
            <td><a href="<%=AnalystController.URL%>?to=<%=AnalystController.MATCH_SUMMARY%>&${Param.MATCH_EVENT}=${match.id}">${match.name}</a></td>
            </c:if>
            <c:if test="${param.roleRequester == Role.MANAGER}">
            <td><a href="<%=ManagerController.URL%>?to=<%=ManagerController.MATCH_SUMMARY%>&${Param.MATCH_EVENT}=${match.id}">${match.name}</a></td>
            </c:if>
            <c:if test="${param.roleRequester == Role.ANALYST}">
            <td><a href="<%=AnalystController.URL%>?to=<%=AnalystController.TOURNAMENT_SUMMARY%>&${Param.TOURNAMENT}=${match.tournament.id}">${match.tournament.name}</a></td>
            </c:if>
            <c:if test="${param.roleRequester == Role.MANAGER}">
            <td><a href="<%=ManagerController.URL%>?to=<%=ManagerController.TOURNAMENT_SUMMARY%>&${Param.TOURNAMENT}=${match.tournament.id}">${match.tournament.name}</a></td>
            </c:if>
            <td>${match.tournament.sport.name}</td>
            <fmt:formatDate type="date" pattern="dd/MM/yyyy" value="${match.startDate.getTime()}" var="sDate"></fmt:formatDate>
            <fmt:formatDate type="time" pattern="HH:mm" value="${match.startDate.getTime()}" var="sTime"></fmt:formatDate>
            <td>${sDate}<br/>${sTime}</td>
            <td>${Status.str(match.status)}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>

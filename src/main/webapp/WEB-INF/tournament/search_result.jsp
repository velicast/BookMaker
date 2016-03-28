<%-- 
    Document   : search_result
    Created on : Feb 23, 2016, 9:33:33 PM
    Author     : eduarc
--%>
<%@page import="co.com.bookmaker.business_logic.controller.AnalystController"%>
<%@page import="co.com.bookmaker.business_logic.controller.SellerController"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="Param" class="co.com.bookmaker.util.type.Parameter"></jsp:useBean>
<jsp:useBean id="Attr" class="co.com.bookmaker.util.type.Attribute"></jsp:useBean>
<jsp:useBean id="Status" class="co.com.bookmaker.util.type.Status"></jsp:useBean>
<jsp:useBean id="Role" class="co.com.bookmaker.util.type.Role"></jsp:useBean>

<style>
    .status_label {
        margin: 0px;
        padding: 0px;
    }
    .tour_th1 {
        text-align: center;
        width: 25%;
    }
    .tour_th2 {
        text-align: center;
        width: 45%;
    }
    .tour_th3 {
        text-align: center;
        width: 15%;
    }
    .tour_th4 {
        text-align: center;
        width: 15%;
    }
    .table td {
        text-align: center;
    }
</style>

<c:set var="tournaments" value="${requestScope[Attr.TOURNAMENTS]}"></c:set>

<h2 class="main_content_title"> Buscar Torneo </h2>

<table class="table table-hover table-bordered table-condensed">
    <thead>
    <capition>${tournaments.size()} resultados encontrados</caption> 
    <tr>
        <th class="tour_th1">Deporte</th>
        <th class="tour_th2">Torneo</th>
        <th class="tour_th3">Juegos Activos</th>
        <th class="tour_th4">Estado</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="pair" items="${tournaments}">
        <jsp:useBean id="pair" class="co.com.bookmaker.util.type.Pair"></jsp:useBean>
        <c:set var="tournament" value="${pair.first}"></c:set>
        <jsp:useBean id="tournament" class="co.com.bookmaker.data_access.entity.event.Tournament"></jsp:useBean>
        <tr>
            <td>${tournament.sport.name}</td>
            <td><a href="<%=AnalystController.URL%>?to=<%=AnalystController.TOURNAMENT_SUMMARY%>&${Param.TOURNAMENT}=${tournament.id}">${tournament.name}</a></td>
            <td>${pair.second}</td>
            <td>${Status.str(tournament.status)}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<%--
    Document   : result.jsp
    Created on : Feb 29, 2016, 12:58:35 AM
    Author     : eduarc
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="co.com.bookmaker.business_logic.controller.event.MatchEventController"%>

<style>
    .form-group {
        margin-top: 0px;
        margin-bottom: 0px;
        padding-bottom: 0px;
        padding-top: 0px;
    }
    .tp_th_1 {
        width: 25%;
    }
    table td {
        text-align: center;
    }
    table th {
        text-align: center;
    }
</style>

<jsp:useBean id="Param" class="co.com.bookmaker.util.type.Parameter"></jsp:useBean>
<jsp:useBean id="Attr" class="co.com.bookmaker.util.type.Attribute"></jsp:useBean>
<jsp:useBean id="Info" class="co.com.bookmaker.util.type.Information"></jsp:useBean>

<c:set var="result" value="${requestScope[Attr.MATCH_RESULT]}"></c:set>
<jsp:useBean id="result" class="co.com.bookmaker.util.form.bean.MatchResultBean"></jsp:useBean>

<h2 class="main_content_title">Resultado de Juego</h2>

<form id="matchResultForm" role="form" class="form-horizontal" action="<%=MatchEventController.URL%>" method="POST">
    <input type="hidden" name="do" value="<%=MatchEventController.RESULT%>">
    <input type="hidden" name="${Param.SPORT}" value="${result.sportId}">
    <input type="hidden" name="${Param.MATCH_EVENT}" value="${result.matchId}">
    <div class="form-group">
        <label class="control-label col-md-2">Deporte: </label>
        <p class="form-control-static">${result.sportName}</p>
    </div>
    <div class="form-group">
        <label class="control-label col-md-2">Torneo: </label>
        <p class="form-control-static">${result.tournamentName}</p>
    </div>
    <div class="form-group">
        <label class="control-label col-md-2">Juego: </label>
        <p class="form-control-static">${result.matchName}</p>
    </div>
    <div class="form-group">
        <label class="control-label col-md-2">Resultado: </label>
        <div class="col-md-10">
            <table class="table table-bordered table-condensed table-hover">
                <thead>
                    <tr>
                        <th class="tp_th_1">Equipo</>
                        <c:forEach var="p" begin="0" end="${result.getnPeriods()-1}">
                        <th>${result.getPeriodName(p)}</th>
                        </c:forEach>
                    </tr>
                </thead>
                <tbody>
                <c:forEach var="t" begin="0" end="${result.getnTeams()-1}">
                    <tr>
                        <td class="tp_th_1">${result.getTeamName(t)}</td>
                    <c:forEach var="p" begin="0" end="${result.getnPeriods()-1}">
                        <td><input type="text" class="form-control input-sm" name="${Param.SCORE} ${p} ${t}" value="${result.getScore(p, t)}"></td>
                    </c:forEach>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <output style="color: red">${requestScope[Info.SCORE]}</output>
        </div>
    </div>
    <div class="col-md-12" style="text-align: center">
        <button type="submit" id="btnUpdateResult" class="btn btn-submit"><span class="glyphicon glyphicon-save"></span> Guardar</button>
    </div>
</form>

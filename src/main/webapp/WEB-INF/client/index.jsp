<%-- 
    Document   : index
    Created on : Dec 22, 2015, 11:47:09 PM
    Author     : eduarc
--%>
<%@page import="co.com.bookmaker.business_logic.controller.ClientController"%>
<%@page import="co.com.bookmaker.business_logic.service.event.TeamService"%>
<%@page import="co.com.bookmaker.business_logic.controller.parlay.ParlayController"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="Param" class="co.com.bookmaker.util.type.Parameter"></jsp:useBean>
<jsp:useBean id="Attr" class="co.com.bookmaker.util.type.Attribute"></jsp:useBean>
<jsp:useBean id="Status" class="co.com.bookmaker.util.type.Status"></jsp:useBean>
<jsp:useBean id="SportID" class="co.com.bookmaker.util.type.SportID"></jsp:useBean>

<c:set var="sUser" value="${sessionScope[Attr.SESSION_USER]}"></c:set>
<jsp:useBean id="sUser" class="co.com.bookmaker.data_access.entity.FinalUser"></jsp:useBean>

<c:set var="sports" value="${requestScope[Attr.SPORTS]}"></c:set>
<c:set var="tournaments" value="${requestScope[Attr.TOURNAMENTS]}"></c:set>

<c:set var="countSports" value="${requestScope[Attr.COUNT_MATCHES_SPORT]}"></c:set>
<c:set var="countTournaments" value="${requestScope[Attr.COUNT_MATCHES_TOURNAMENT]}"></c:set>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        
        <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
        <link rel="stylesheet" href="css/client/client.css">
        
        <title>BookMaker - Cliente</title>
    </head>
    <body>
        <jsp:include page="/WEB-INF/navbar.jsp"></jsp:include>
        <div class="container-fluid main_div">
            <div class="row">
                <div class="col-md-8 panel_padding">
                    <div class="panel panel-default">
                        <div class="panel-heading panel_heading"><h4>TARJETA DE PARLAY</h4></div>
                        <div class="panel-body pcard">
                            <c:if test="${sports.size() > 0}">
                            <c:forEach var="s" begin="0" end="${sports.size()-1}">
                            <c:set var="sport" value="${sports.get(s)}"></c:set>
                            <jsp:useBean id="sport" class="co.com.bookmaker.data_access.entity.event.Sport"></jsp:useBean>
                            
                                <button class="btn btn-block sport_btn" data-toggle="collapse" data-target="#sp${sport.id}">${SportID.str(sport.id)} <span class = "badge s_badge">${countSports.get(s)}</span></button>
                            <div id="sp${sport.id}" class="collapse league_div">
                                <c:set var="sCountTournaments" value="${countTournaments.get(s)}"></c:set>
                                <c:set var="sTournaments" value="${tournaments.get(s)}"></c:set>
                                <c:if test="${sTournaments.size() > 0}">
                                <c:forEach var="t" begin="0" end="${sTournaments.size()-1}">
                                <c:set var="tournament" value="${sTournaments.get(t)}"></c:set>
                                <jsp:useBean id="tournament" class="co.com.bookmaker.data_access.entity.event.Tournament"></jsp:useBean>
                                <button class="btn btn-block league_btn" data-toggle="collapse" data-target="#lg${tournament.id}">${tournament.name} <span class = "badge t_badge">${sCountTournaments.get(t)}</span></button>
                                <div id="lg${tournament.id}" class="collapse tournament">
                                    <var id="st${tournament.id}" hidden>0</var>
                                </div>
                                </c:forEach>
                                </c:if>
                            </div>
                            </c:forEach>
                            </c:if>
                        </div>
                    </div>
                </div>
                <div class="col-md-4 panel_padding">
                    <div class="panel panel-default">
                        <div class="panel-heading panel_heading">
                            <h4>TIQUETE DE APUESTA</h4>
                            <label>Seleccione entre ${sUser.agency.minOddsParlay} y ${sUser.agency.maxOddsParlay} logros</label>
                        </div>
                        <div id="ticket" class="panel-body ticket_body">
                            
                        </div>
                        <div class="panel-footer">
                            <form id="betform" role="form" class="form-horizontal" action="<%=ParlayController.URL%>" method="POST">
                                <input type="hidden" name="do" value="<%=ParlayController.BUY%>">
                                <input type="hidden" id="oddselection" name="${Param.ODDS}" value="">
                                <div class="form-group">
                                    <label class="col-md-4 control-label">Riesgo $</label>
                                    <div class="input-group col-md-6">
                                        <input type="text" class="form-control input-sm" id="risk" name="${Param.RISK}" placeholder="0.0">
                                        <span class="input-group-btn">
                                            <button class="btn btn-sm btn-white" id="btn_risk" type="button">Calcular Ganancia</button>
                                        </span>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-md-4 control-label">Ganancia $</label>
                                    <div class="input-group col-md-6">
                                        <input type="text" class="form-control input-sm" id="profit" name="${Param.PROFIT}" placeholder="0.0">
                                        <span class="input-group-btn">
                                            <button class="btn btn-sm btn-white" id="btn_profit" type="button">Calcular Riesgo</button>
                                        </span>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-md-4 control-label">Nombre </label>
                                    <div class="input-group col-md-6">
                                        <input type="text" class="form-control input-sm" id="name" name="${Param.NAME}" placeholder="Su nombre">
                                    </div>
                                </div>
                                <button type="submit" class="btn btn-submit btn-block"><i class="glyphicon glyphicon-shopping-cart"></i> <b>Comprar</b></button>
                            </form>
                            <var id="nOdds" hidden>0</var>
                            <var id="minOddsLimit" hidden>${sUser.agency.minOddsParlay}</var>
                            <var id="maxOddsLimit" hidden>${sUser.agency.maxOddsParlay}</var>
                            <var id="maxProfit" hidden>${sUser.agency.maxProfit}</var>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div id="infoModalLocal" class="modal fade" role="dialog">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 id="infoModalTitle" class="modal-title" style="color: dodgerblue">Información</h4>
                    </div>
                    <div class="modal-body container-fluid">
                        <span class="col-md-2 glyphicon glyphicon-info-sign" style="color: dodgerblue; font-size:50px;"></span>
                        <p class="col-md-10" id="infoModalContent" style="color: dodgerblue"></p>
                    </div>
                </div>
            </div>
        </div>
        <%@include file="/WEB-INF/footer.jsp" %>
                        
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
        <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
        <script src="js/client/client.js"></script>
        <script type = "text/javascript">
            $(document).ready(function() {
                $('#pClient').addClass("active");
            });
        </script>
    </body>
    <jsp:include page="/WEB-INF/info_modal.jsp"></jsp:include>
    <jsp:include page="/WEB-INF/warning_modal.jsp"></jsp:include>
    <jsp:include page="/WEB-INF/error_modal.jsp"></jsp:include>
</html>

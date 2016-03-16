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

<c:set var="sUser" value="${sessionScope[Attr.SESSION_USER]}"></c:set>
<jsp:useBean id="sUser" class="co.com.bookmaker.data_access.entity.FinalUser"></jsp:useBean>

<c:set var="tournamentService" value="${requestScope[Attr.TOURNAMENT_SERVICE]}"></c:set>
<jsp:useBean id="tournamentService" class="co.com.bookmaker.business_logic.service.event.TournamentService"></jsp:useBean>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        
        <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
        <link rel="stylesheet" href="/BookMaker/css/client/client.css">
        
        <title>BookMaker - Client</title>
    </head>
    <body>
        <jsp:include page="/WEB-INF/navbar.jsp"></jsp:include>
        <div class="container-fluid main_div">
            <div class="row">
                <div class="col-md-8 panel_padding">
                    <div class="panel panel-default">
                        <div class="panel-heading panel_heading"><h4>PARLAY CARDS</h4></div>
                        <div class="panel-body pcard">
                            <c:forEach var="sport" items="${requestScope[Attr.SPORT]}">
                            <jsp:useBean id="sport" class="co.com.bookmaker.data_access.entity.event.Sport"></jsp:useBean>
                            <button class="btn btn-block sport_btn" data-toggle="collapse" data-target="#sp${sport.id}">${sport.name}</button>
                            <div id="sp${sport.id}" class="collapse league_div">
                                <c:forEach var="tournament" items="${tournamentService.getClientTournaments(sUser.agency, sport, Status.ACTIVE)}">
                                <jsp:useBean id="tournament" class="co.com.bookmaker.data_access.entity.event.Tournament"></jsp:useBean>
                                <button class="btn btn-block league_btn" data-toggle="collapse" data-target="#lg${tournament.id}">${tournament.name}</button>
                                <div id="lg${tournament.id}" class="collapse tournament">
                                    <var id="st${tournament.id}" hidden>0</var>
                                </div>
                                </c:forEach>
                            </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
                <div class="col-md-4 panel_padding">
                    <div class="panel panel-default">
                        <div class="panel-heading panel_heading">
                            <h4>BET TICKET</h4>
                            <label>Select between ${sUser.agency.minOddsParlay} and ${sUser.agency.maxOddsParlay} odds</label>
                        </div>
                        <div id="ticket" class="panel-body ticket_body">
                            
                        </div>
                        <div class="panel-footer">
                            <form id="betform" role="form" class="form-horizontal" action="<%=ParlayController.URL%>" method="POST">
                                <input type="hidden" name="do" value="<%=ParlayController.BUY%>">
                                <input type="hidden" id="oddselection" name="${Param.ODDS}" value="">
                                <div class="form-group">
                                    <label class="col-md-3 control-label">Risk $</label>
                                    <div class="input-group col-md-5">
                                        <input type="text" class="form-control input-sm" id="risk" name="${Param.RISK}" placeholder="0.0">
                                        <span class="input-group-btn">
                                            <button class="btn btn-sm btn-white" id="btn_risk" type="button">Get Profit</button>
                                        </span>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-md-3 control-label">Profit $</label>
                                    <div class="input-group col-md-5">
                                        <input type="text" class="form-control input-sm" id="profit" name="${Param.PROFIT}" placeholder="0.0">
                                        <span class="input-group-btn">
                                            <button class="btn btn-sm btn-white" id="btn_profit" type="button">Get Risk</button>
                                        </span>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-md-3 control-label">Name </label>
                                    <div class="input-group col-md-5">
                                        <input type="text" class="form-control input-sm" id="name" name="${Param.NAME}" placeholder="Your name">
                                    </div>
                                </div>
                                <button type="submit" class="btn btn-submit btn-block"><i class="glyphicon glyphicon-shopping-cart"></i> <b>Buy</b></button>
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
                        <h4 id="infoModalTitle" class="modal-title" style="color: dodgerblue">Information</h4>
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
        <script src="/BookMaker/js/client/client.js"></script>
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

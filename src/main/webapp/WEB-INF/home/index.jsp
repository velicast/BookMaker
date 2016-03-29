<%-- 
    Document   : index
    Created on : Jan 19, 2016, 3:35:05 PM
    Author     : eduarc
--%>
<%@page import="co.com.bookmaker.business_logic.controller.parlay.ParlayController"%>
<%@page import="co.com.bookmaker.business_logic.controller.HomeController"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="Param" class="co.com.bookmaker.util.type.Parameter"></jsp:useBean>
<jsp:useBean id="Info" class="co.com.bookmaker.util.type.Information"></jsp:useBean>
<jsp:useBean id="Attr" class="co.com.bookmaker.util.type.Attribute"></jsp:useBean>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        
        <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
        <link rel="stylesheet" href="css/client/client.css">
        
        <style>
            .track_parlay {
                margin: 0px; 
                padding-left: 10px; 
                padding-right: 10px; 
                padding-top: 0px; 
                padding-bottom: 0px;
                background-color: #f6f6f6;
            }
        </style>

        <title>BookMaker</title>
    </head>
    <body>
        <jsp:include page="/WEB-INF/navbar.jsp"></jsp:include>
        <div class="container-fluid main_div">
            <div class="jumbotron_padding"><h1 style="text-align: center; font-size: 60px; color: #275c7c"><span class="glyphicon glyphicon-tower"></span> BookMaker <span class="glyphicon glyphicon-tower"></span></h1></div>
            <div class="row" style="text-align: center">
                <h4 style="color: #275c7c">Sigue tu Apuesta</h4>
            </div>
            <div class="row">
                <div class="col-md-4"></div>
                <div class="col-md-4">
                    <form id="trackParlayForm" role="form" class="form-default" action="<%=HomeController.URL%>">
                        <input type="hidden" name="to" value="<%=HomeController.TRACK_PARLAY%>">
                        <div class="input-group">
                            <input type="text" class="form-control" id="parlayToSearch" name="${Param.PARLAY}" placeholder="Tiquete No.">
                            <span class="input-group-btn">
                                <button class="btn btn-submit" id="btnSearchAgency"><span class="glyphicon glyphicon-search"></span></button>
                            </span>
                        </div>
                        <output style="color: red">${requestScope[Info.PARLAY]}</output>
                    </form>
                </div>
                <div class="col-md-4"></div>
            </div>
            <c:if test="${requestScope[Attr.PARLAY] != null}">        
            <div class="row track_parlay">
                <jsp:include page="<%=ParlayController.getJSP(ParlayController.SUMMARY)%>"></jsp:include>
            </div>
            </c:if>
        </div>
        <%@include file="/WEB-INF/footer.jsp" %>
                    
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
        <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
        <script>
            $(document).ready(function() {
                $('#pHome').addClass("active");
            });
        </script>
    </body>
    <jsp:include page="/WEB-INF/info_modal.jsp"></jsp:include>
    <jsp:include page="/WEB-INF/warning_modal.jsp"></jsp:include>
    <jsp:include page="/WEB-INF/error_modal.jsp"></jsp:include>
</html>

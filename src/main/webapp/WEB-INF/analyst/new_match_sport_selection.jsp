<%-- 
    Document   : sports_list
    Created on : Feb 23, 2016, 11:37:26 AM
    Author     : eduarc
--%>
<%@page import="co.com.bookmaker.business_logic.controller.AnalystController"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:useBean id="Param" class="co.com.bookmaker.util.type.Parameter"></jsp:useBean>
<jsp:useBean id="Info" class="co.com.bookmaker.util.type.Information"></jsp:useBean>
<jsp:useBean id="Attr" class="co.com.bookmaker.util.type.Attribute"></jsp:useBean>
<jsp:useBean id="Role" class="co.com.bookmaker.util.type.Role"></jsp:useBean>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        
        <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
        <link rel="stylesheet" href="css/analyst/analyst.css">
        
        <title>BookMaker - New Match</title>
    </head>
    <body>
        <jsp:include page="/WEB-INF/navbar.jsp"></jsp:include>
        <div class="container-fluid">
            <div class="row">
                <div class="col-md-3 sidebar">
                    <jsp:include page="<%=AnalystController.getJSP(AnalystController.SIDEBAR)%>"></jsp:include>
                </div>
                <!-- Main Content -->
                <div id="mainContent" class="col-md-9 side-body">
                    <h2 class="main_content_title"> New Match </h2>
                    <form id="newMatchSportSelectionForm" role="form" class="form-horizontal" action="<%=AnalystController.URL%>">
                        <input type="hidden" name="to" value="<%=AnalystController.NEW_MATCH%>">
                        <div class="form-group">
                            <label class="control-label col-md-2">Sport: </label>
                            <div class="col-md-4">
                                <select name="${Param.SPORT}" class="form-control">
                                    <c:forEach var="sport" items="${requestScope[Attr.SPORTS]}">
                                    <jsp:useBean id="sport" class="co.com.bookmaker.data_access.entity.event.Sport"></jsp:useBean>
                                        <option value="${sport.id}">${sport.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="col-md-6" style="text-align: center">
                            <button class="btn btn-submit" id="btnSelectSport"><span class="glyphicon glyphicon-check"></span> Select</button>
                        </div>
                        <output style="color: red">${requestScope[Info.SPORT]}</output>
                    </form>
                </div>
            </div>
        </div>
        <%@include file="/WEB-INF/footer.jsp" %>
                
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
        <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
        <script>
            $(document).ready(function() {
                $('#menuMatch').collapse("show");
                $('#newMatch').addClass("active");
                $('#pAnalyst').addClass("active");
            });
        </script>
    </body>
    <jsp:include page="/WEB-INF/info_modal.jsp"></jsp:include>
    <jsp:include page="/WEB-INF/warning_modal.jsp"></jsp:include>
    <jsp:include page="/WEB-INF/error_modal.jsp"></jsp:include>
</html>


<%-- 
    Document   : summary
    Created on : Feb 19, 2016, 11:58:26 AM
    Author     : eduarc
--%>
<%@page import="co.com.bookmaker.business_logic.controller.parlay.ParlayOddController"%>
<%@page import="co.com.bookmaker.business_logic.controller.parlay.ParlayController"%>
<%@page import="co.com.bookmaker.business_logic.controller.SellerController"%>
<%@page import="co.com.bookmaker.business_logic.controller.ManagerController"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="en_US"></fmt:setLocale>

<!-- Define constant wrappers here -->
<jsp:useBean id="Param" class="co.com.bookmaker.util.type.Parameter"></jsp:useBean>
<jsp:useBean id="Attr" class="co.com.bookmaker.util.type.Attribute"></jsp:useBean>
<jsp:useBean id="Info" class="co.com.bookmaker.util.type.Information"></jsp:useBean>
<jsp:useBean id="Status" class="co.com.bookmaker.util.type.Status"></jsp:useBean>
<jsp:useBean id="Role" class="co.com.bookmaker.util.type.Role"></jsp:useBean>

<style>
    .form-group {
        margin-top: 0px;
        margin-bottom: 0px;
        padding-bottom: 0px;
        padding-top: 0px;
    }
    .p_status_label {
        margin: 0px;
        padding-top: 5px;
        padding-bottom: 10px;
    }
</style>

<c:set var="userSession" value="${sessionScope[Attr.SESSION_USER]}"></c:set>
<jsp:useBean id="userSession" class="co.com.bookmaker.data_access.entity.FinalUser"></jsp:useBean>
    
<c:set var="parlay" value="${requestScope[Attr.PARLAY]}"></c:set>
<jsp:useBean id="parlay" class="co.com.bookmaker.data_access.entity.parlay.Parlay"></jsp:useBean>

<form role="form" class="form-horizontal">
    <c:if test="${Role.inRole(sessionScope[Attr.SESSION_ROLE], Role.SELLER) && requestScope[Attr.TRACK_PARLAY] == null}">
    <div class="form-group">
        <div class="col-md-6">
            <c:if test="${parlay.status == Status.IN_QUEUE}">
            <a id="btnAcceptParlay" class="btn btn-default" style="color: green"
               href="<%=ParlayController.URL%>?do=<%=ParlayController.ACCEPT%>&${Param.PARLAY}=${parlay.id}">
                <span class="glyphicon glyphicon-ok"></span> Accept</a>
            <a id="btnCancelParlay" class="btn btn-default" style="color: red"
               href="<%=ParlayController.URL%>?do=<%=ParlayController.CANCEL%>&${Param.PARLAY}=${parlay.id}">
                <span class="glyphicon glyphicon-remove"></span> Cancel</a>
            </c:if>
            <c:if test="${parlay.status == Status.PENDING && parlay.seller.id == userSession.id}">
            <a id="btnPrintTicket" class="btn btn-default" 
               href="<%=ParlayController.URL%>?do=<%=ParlayController.PRINT%>&${Param.PARLAY}=${parlay.id}">
                <span class="glyphicon glyphicon-print"></span> Print Ticket</a>
            </c:if>
        </div>
    </div>
    </c:if>
    <div class="form-group">
        <label class="col-md-2 control-label">Ticket ID:</label>
        <div class="col-md-4">
            <p class="form-control-static">${parlay.id}</p>
        </div>
    </div>
    <c:if test="${requestScope[Attr.TRACK_PARLAY] == null}">
    <c:if test="${parlay.status != Status.IN_QUEUE}">
    <div class="form-group">
        <label class="col-md-2 control-label">Seller:</label>
        <div class="col-md-4">
            <c:if test="${param.roleRequester == Role.MANAGER}">
            <p class="form-control-static">
                <a href="<%=ManagerController.URL%>?to=<%=ManagerController.EMPLOYEE_SUMMARY%>&${Param.USERNAME}=${parlay.seller.username}">
                    ${parlay.seller.username}</a> - ${parlay.seller.firstName} ${parlay.seller.lastName}</p>
            </c:if>
            <c:if test="${param.roleRequester != Role.MANAGER}">
            <p class="form-control-static">${parlay.seller.username} - ${parlay.seller.firstName} ${parlay.seller.lastName} </p>
            </c:if>
        </div>
    </div>
    </c:if>
    </c:if>
    <div class="form-group">
        <label class="col-md-2 control-label">Date:</label>
        <div class="col-md-4">
            <p class="form-control-static"><fmt:formatDate type="time" pattern="dd/MM/yyyy" value="${parlay.purchaseDate.getTime()}"/><br/>
                <fmt:formatDate type="time" pattern="HH:mm" value="${parlay.purchaseDate.getTime()}"/></p>
        </div>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">Risk:</label>
        <div class="col-md-4">
            <p class="form-control-static">$ <fmt:formatNumber value="${parlay.risk}" maxFractionDigits="0"/></p>
        </div>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">Profit:</label>
        <div class="col-md-4">
            <p class="form-control-static">$ <fmt:formatNumber value="${parlay.profit}" maxFractionDigits="0"/></p>
        </div>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">Status:</label>
        <div class="col-md-4">
            <c:if test="${parlay.status == Status.WIN}">
                <h4 class="p_status_label"><span class = "label label-success">${Status.str(parlay.status)}</span></h4>
            </c:if>
            <c:if test="${parlay.status == Status.LOSE}">
                <h4 class="p_status_label"><span class = "label label-danger">${Status.str(parlay.status)}</span></h4>
            </c:if>
            <c:if test="${parlay.status == Status.PUSH}">
                <h4 class="p_status_label"><span class = "label label-primary">${Status.str(parlay.status)}</span></h4>
            </c:if>
            <c:if test="${parlay.status == Status.CANCELLED}">
                <h4 class="p_status_label"><span class = "label label-info">${Status.str(parlay.status)}</span></h4>
            </c:if>
            <c:if test="${parlay.status == Status.INVALID}">
                <h4 class="p_status_label"><span class = "label label-warning">${Status.str(parlay.status)}</span></h4>
            </c:if>
            <c:if test="${parlay.status == Status.PENDING || parlay.status == Status.IN_QUEUE}">
                <h4 class="p_status_label"><span class = "label label-default">${Status.str(parlay.status)}</span></h4>
            </c:if>
        </div>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">Odds: </label>
    </div>    
    <div class="col-md-12">
        <jsp:include page="<%=ParlayOddController.getJSP(ParlayOddController.TABLE_ODDS)%>"></jsp:include>
    </div>
    <c:if test="${Role.inRole(sessionScope[Attr.SESSION_ROLE], Role.SELLER) && requestScope[Attr.TRACK_PARLAY] == null}">
    <div class="form-group">
        <div class="col-md-6">
            <c:if test="${parlay.status == Status.IN_QUEUE}">
            <a id="btnAcceptParlay" class="btn btn-default" style="color: green"
               href="<%=ParlayController.URL%>?do=<%=ParlayController.ACCEPT%>&${Param.PARLAY}=${parlay.id}">
                <span class="glyphicon glyphicon-ok"></span> Accept</a>
            <a id="btnCancelParlay" class="btn btn-default" style="color: red"
               href="<%=ParlayController.URL%>?do=<%=ParlayController.CANCEL%>&${Param.PARLAY}=${parlay.id}">
                <span class="glyphicon glyphicon-remove"></span> Cancel</a>
            </c:if>
            <c:if test="${parlay.status == Status.PENDING && parlay.seller.id == userSession.id}">
            <a id="btnPrintTicket" class="btn btn-default" 
               href="<%=ParlayController.URL%>?do=<%=ParlayController.PRINT%>&${Param.PARLAY}=${parlay.id}">
                <span class="glyphicon glyphicon-print"></span> Print Ticket</a>
            </c:if>
        </div>
    </div>
    </c:if>
</form>

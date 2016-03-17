<%-- 
    Document   : table_summary
    Created on : Feb 19, 2016, 12:47:22 PM
    Author     : eduarc
--%>
<%@page import="co.com.bookmaker.business_logic.controller.ManagerController"%>
<%@page import="co.com.bookmaker.business_logic.controller.SellerController"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="en_US"></fmt:setLocale>

<!-- Define constant wrappers here -->
<jsp:useBean id="Param" class="co.com.bookmaker.util.type.Parameter"></jsp:useBean>
<jsp:useBean id="Attr" class="co.com.bookmaker.util.type.Attribute"></jsp:useBean>
<jsp:useBean id="Info" class="co.com.bookmaker.util.type.Information"></jsp:useBean>
<jsp:useBean id="Status" class="co.com.bookmaker.util.type.Status"></jsp:useBean>
<jsp:useBean id="Role" class="co.com.bookmaker.util.type.Role"></jsp:useBean>

<link rel="stylesheet" href="/BookMaker/css/parlay/table_summary.css">

<c:set var="parlays" value="${requestScope[Attr.PARLAYS]}"></c:set>
   
<table class="table table-hover table-bordered table-condensed">
   <thead>
      <capition>${parlays.size()} result(s) found</caption> 
      <tr>
         <th class="parlay_th1">Parlay</th>
         <th class="parlay_th2">Date</th>
         <th class="parlay_th3">Client's Name</th>
         <th class="parlay_th4">Risk</th>
         <th class="parlay_th5">Profit</th>
         <th class="parlay_th6">Status</th>
      </tr>
   </thead>
   <tbody>
   <c:forEach var="parlay" items="${parlays}">
       <jsp:useBean id="parlay" class="co.com.bookmaker.data_access.entity.parlay.Parlay"></jsp:useBean>
        <tr>
            <c:if test="${param.roleRequester == Role.SELLER}">
            <td class="td_t"><a href="<%=SellerController.URL%>?to=<%=SellerController.PARLAY_SUMMARY%>&${Param.PARLAY}=${parlay.id}">${parlay.id}</a></td>
            </c:if>
            <c:if test="${param.roleRequester == Role.MANAGER}">
            <td class="td_t"><a href="<%=ManagerController.URL%>?to=<%=ManagerController.PARLAY_SUMMARY%>&${Param.PARLAY}=${parlay.id}">${parlay.id}</a></td>
            </c:if>
            <td class="td_t"><fmt:formatDate type="time" pattern="dd/MM/yyyy" value="${parlay.purchaseDate.getTime()}"/>
                <fmt:formatDate type="time" pattern="HH:mm" value="${parlay.purchaseDate.getTime()}"/></td>
            <td class="td_t">${parlay.clientName}</td>
            <td class="td_t">$<fmt:formatNumber value="${parlay.risk}" maxFractionDigits="0"/></td>
            <td class="td_t">$<fmt:formatNumber value="${parlay.profit}" maxFractionDigits="0"/></td>
            <td class="td_t">
            <c:if test="${parlay.status == Status.WIN}">
                <h4 class="status_label"><span class = "label label-success">${Status.str(parlay.status)}</span></h4>
            </c:if>
            <c:if test="${parlay.status == Status.LOSE}">
                <h4 class="status_label"><span class = "label label-danger">${Status.str(parlay.status)}</span></h4>
            </c:if>
            <c:if test="${parlay.status == Status.PUSH}">
                <h4 class="status_label"><span class = "label label-primary">${Status.str(parlay.status)}</span></h4>
            </c:if>
            <c:if test="${parlay.status == Status.CANCELLED}">
                <h4 class="status_label"><span class = "label label-info">${Status.str(parlay.status)}</span></h4>
            </c:if>
            <c:if test="${parlay.status == Status.INVALID}">
                <h4 class="status_label"><span class = "label label-warning">${Status.str(parlay.status)}</span></h4>
            </c:if>
            <c:if test="${parlay.status == Status.PENDING || parlay.status == Status.IN_QUEUE}">
                <h4 class="status_label"><span class = "label label-default">${Status.str(parlay.status)}</span></h4>
            </c:if>
            </td>
        </tr>
   </c:forEach>
   </tbody>
</table>
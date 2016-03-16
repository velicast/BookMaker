<%-- 
    Document   : list
    Created on : Feb 18, 2016, 10:13:03 AM
    Author     : eduarc
--%>
<%@page import="co.com.bookmaker.business_logic.controller.AgencyController"%>
<%@page import="co.com.bookmaker.business_logic.controller.AdminController"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- Define constant wrappers here -->
<jsp:useBean id="Param" class="co.com.bookmaker.util.type.Parameter"></jsp:useBean>
<jsp:useBean id="Attr" class="co.com.bookmaker.util.type.Attribute"></jsp:useBean>
<jsp:useBean id="Info" class="co.com.bookmaker.util.type.Information"></jsp:useBean>
<jsp:useBean id="Status" class="co.com.bookmaker.util.type.Status"></jsp:useBean>

<style>
    table th {
        text-align: center;
    }
    table td {
        text-align: center;
    }
</style>
<c:set var="agencies" value="${requestScope[Attr.LIST_AGENCIES]}"></c:set>

<h2 class="main_content_title"> Registered Agencies </h2>

<table class="table table-hover table-condensed table-bordered">
   <thead>
      <tr>
         <th>Name</th>
         <th>Status</th>
      </tr>
   </thead>
   <tbody>
   <c:forEach var="agency" items="${agencies}">
       <jsp:useBean id="agency" class="co.com.bookmaker.data_access.entity.Agency"></jsp:useBean>
        <tr>
            <td><a href="<%=AdminController.URL%>?to=<%=AdminController.AGENCY_SUMMARY%>&${Param.AGENCY}=${agency.id}">${agency.name}</a></td>
        <td>${Status.str(agency.status)}</td>
        </tr>
   </c:forEach>
   </tbody>
</table>
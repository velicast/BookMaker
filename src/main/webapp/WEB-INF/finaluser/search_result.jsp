<%-- 
    Document   : result_list
    Created on : Feb 22, 2016, 12:26:23 PM
    Author     : eduarc
--%>
<%@page import="co.com.bookmaker.business_logic.controller.AgencyController"%>
<%@page import="co.com.bookmaker.business_logic.controller.FinalUserController"%>
<%@page import="co.com.bookmaker.business_logic.controller.AdminController"%>
<%@page import="co.com.bookmaker.business_logic.controller.SellerController"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- Define constant wrappers here -->
<jsp:useBean id="Param" class="co.com.bookmaker.util.type.Parameter"></jsp:useBean>
<jsp:useBean id="Attr" class="co.com.bookmaker.util.type.Attribute"></jsp:useBean>
<jsp:useBean id="Info" class="co.com.bookmaker.util.type.Information"></jsp:useBean>
<jsp:useBean id="Status" class="co.com.bookmaker.util.type.Status"></jsp:useBean>
<jsp:useBean id="Role" class="co.com.bookmaker.util.type.Role"></jsp:useBean>

<link rel="stylesheet" href="/css/parlay/table_summary.css">
<style>
    .table_employee_th1 {
        text-align: center;
        width: 20%;
    }
    .table_employee_th2 {
        text-align: center;
        width: 45%;
    }
    .table_employee_th3 {
        text-align: center;
        width: 20%;
    }
    .table_employee_th4 {
        text-align: center;
        width: 15%;
    }
    .table td {
        text-align: center;
    }
</style>

<c:set var="users" value="${requestScope[Attr.USERS]}"></c:set>

<table class="table table-hover table-bordered table-condensed">
    <caption>${users.size()} result(s) found</caption>
    <thead>
        <tr>
            <th class="table_employee_th1">Username</th>
            <th class="table_employee_th2">Full Name</th>
            <th class="table_employee_th3">Roles</th>
            <th class="table_employee_th4">Status</th>
        </tr>
    </thead>
    <tbody>
    <c:forEach var="user" items="${users}">
        <jsp:useBean id="user" class="co.com.bookmaker.data_access.entity.FinalUser"></jsp:useBean>
        <tr>
            <td><a href="<%=AdminController.URL%>?to=<%=AdminController.USER_SUMMARY%>&${Param.USERNAME}=${user.username}">${user.username}</a></td>
            <td>${user.firstName} ${user.lastName}</td>
            <td>
            <c:if test="${user.inRole(Role.ADMIN)}">Administrator<br/></c:if>
            <c:if test="${user.inRole(Role.MANAGER)}">Manager<br/></c:if>
            <c:if test="${user.inRole(Role.ANALYST)}">Analyst<br/></c:if>
            <c:if test="${user.inRole(Role.SELLER)}">Seller<br/></c:if>
            <c:if test="${user.inRole(Role.CLIENT)}">Client<br/></c:if>
            </td>
            <td>${Status.str(user.status)}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>
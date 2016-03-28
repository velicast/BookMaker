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

<link rel="stylesheet" href="css/parlay/table_summary.css">
<style>
    .table_employee_th1 {
        width: 20%;
    }
    .table_employee_th2 {
        width: 40%;
    }
    .table_employee_th3 {
        width: 15%;
    }
    .table_employee_th4 {
        width: 15%;
    }
    .table_employee_th5 {
        width: 10%;
    }
</style>

<c:set var="users" value="${requestScope[Attr.USERS]}"></c:set>
<c:set var="auth" value="${requestScope[Attr.AUTHENTICATION_SERVICE]}"></c:set>

<table class="table table-hover table-bordered table-condensed">
    <caption>${users.size()} resultados encontrados</caption>
    <thead>
        <tr>
            <th class="table_employee_th1">Usuario</th>
            <th class="table_employee_th2">Nombre</th>
            <th class="table_employee_th3">Roles</th>
            <th class="table_employee_th4">Estado</th>
            <th class="table_employee_th5">Online</th>
        </tr>
    </thead>
    <tbody>
    <c:forEach var="user" items="${users}">
        <jsp:useBean id="user" class="co.com.bookmaker.data_access.entity.FinalUser"></jsp:useBean>
        <tr>
            <td><a href="<%=AdminController.URL%>?to=<%=AdminController.USER_SUMMARY%>&${Param.USERNAME}=${user.username}">${user.username}</a></td>
            <td>${user.firstName} ${user.lastName}</td>
            <td>
            <c:if test="${user.inRole(Role.ADMIN)}">Administrador<br/></c:if>
            <c:if test="${user.inRole(Role.MANAGER)}">Gerente<br/></c:if>
            <c:if test="${user.inRole(Role.ANALYST)}">Analista<br/></c:if>
            <c:if test="${user.inRole(Role.SELLER)}">Vendedor<br/></c:if>
            <c:if test="${user.inRole(Role.CLIENT)}">Cliente<br/></c:if>
            </td>
            <td>${Status.str(user.status)}</td>
            <td>${auth.isOnline(user, pageContext.request) ? "Sí" : "No"}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>
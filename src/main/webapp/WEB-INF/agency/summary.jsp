<%-- 
    Document   : summary
    Created on : Feb 16, 2016, 9:11:21 PM
    Author     : eduarc
--%>
<%@page import="co.com.bookmaker.business_logic.controller.ManagerController"%>
<%@page import="co.com.bookmaker.business_logic.controller.AdminController"%>
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
    .table td {
        text-align: center;
    }
    .table th {
        text-align: center;
    }
</style>
    <!-- Define global variables here -->
<c:set var="agency" value="${requestScope[Attr.AGENCY]}"></c:set>
<jsp:useBean id="agency" class="co.com.bookmaker.data_access.entity.Agency"></jsp:useBean>

<c:set var="sUser" value="${sessionScope[Attr.SESSION_USER]}"></c:set>
<jsp:useBean id="sUser" class="co.com.bookmaker.data_access.entity.FinalUser"></jsp:useBean>
    
<c:set var="auth" value="${requestScope[Attr.AUTHENTICATION_SERVICE]}"></c:set>

<h2 class="main_content_title"> Agency </h2>

<form role="form" class="form-horizontal">
    <div class="form-group">
        <div class="col-md-8">
        <c:if test="${param.roleRequester == Role.ADMIN}">
            <a id="btnEditAgency" class="btn btn-default" 
               href="<%=AdminController.URL%>?to=<%=AdminController.EDIT_AGENCY%>&${Param.AGENCY}=${agency.id}">
                <span class="glyphicon glyphicon-edit"></span> Edit</a>
            <a id="btnAddAgency" class="btn btn-default" 
               href="<%=AdminController.URL%>?to=<%=AdminController.SEARCH_AGENCY_EMPLOYEE%>&${Param.AGENCY}=${agency.id}&${Param.ADD_EMPLOYEE}=}">
                <span class="glyphicon glyphicon-plus"></span> Add Employee</a>
            <a id="btnRemAgency" class="btn btn-default" 
               href="<%=AdminController.URL%>?to=<%=AdminController.SEARCH_AGENCY_EMPLOYEE%>&${Param.AGENCY}=${agency.id}&${Param.REM_EMPLOYEE}=">
                <span class="glyphicon glyphicon-minus"></span> Remove Employee</a>
            <a id="btnAgencyBalance" class="btn btn-default" 
               href="<%=AdminController.URL%>?to=<%=AdminController.AGENCY_BALANCE%>&${Param.AGENCY}=${agency.id}">
            <span class="glyphicon glyphicon-stats"></span> Balance</a>
        </c:if>
        <c:if test="${param.roleRequester == Role.MANAGER}">
            <a id="btnEditAgency" class="btn btn-default" 
               href="<%=ManagerController.URL%>?to=<%=ManagerController.EDIT_AGENCY%>&${Param.AGENCY}=${agency.id}">
                <span class="glyphicon glyphicon-edit"></span> Edit</a>
            <a id="btnAgencyBalance" class="btn btn-default" 
                href="<%=ManagerController.URL%>?to=<%=ManagerController.AGENCY_BALANCE%>">
                <span class="glyphicon glyphicon-stats"></span> Balance</a>
        </c:if>
        </div>
    </div>
    <c:if test="${sUser.author == null}">
    <div class="form-group">
        <label class="col-md-2 control-label">Author: </label>
        <div class="col-md-4">
            <p class="form-control-static"><a href="<%=AdminController.URL%>?to=<%=AdminController.USER_SUMMARY%>&${Param.USERNAME}=${agency.author.username}">
                    ${agency.author.username}</a> - ${agency.author.firstName} ${agency.author.lastName}</p>
        </div>
    </div>
    </c:if>
    <div class="form-group">
        <label class="col-md-2 control-label">Name: </label>
        <div class="col-md-4">
            <p class="form-control-static">${agency.name}</p>
        </div>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">E-mail: </label>
        <div class="col-md-4">
            <p class="form-control-static">${agency.email}</p>
        </div>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">Telephone: </label>
        <div class="col-md-4">
            <p class="form-control-static">${agency.telephone}</p>
        </div>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">City: </label>
        <div class="col-md-4">
            <p class="form-control-static">${agency.city}</p>
        </div>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">Address: </label>
        <div class="col-md-4">
            <p class="form-control-static">${agency.address}</p>
        </div>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">Min. number of odds per Parlay: </label>
        <div class="col-md-4">
            <p class="form-control-static">${agency.minOddsParlay}</p>
        </div>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">Max. number of odds per Parlay: </label>
        <div class="col-md-4">
            <p class="form-control-static">${agency.maxOddsParlay}</p>
        </div>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">Max. profit per Parlay: </label>
        <div class="col-md-4">
            <p class="form-control-static">$ <fmt:formatNumber value="${agency.maxProfit}"/></p>
        </div>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">Accept Global Odds: </label>
        <div class="col-md-4">
            <p class="form-control-static">${agency.acceptGlobalOdds ? "Yes" : "No"}</p>
        </div>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">Status: </label>
        <div class="col-md-4">
            <p class="form-control-static">${Status.str(agency.status)}</p>
        </div>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">Employees: </label>
        <div class="col-md-10">
            <table class="table table-hover table-bordered table-condensed">
                <thead>
                   <tr>
                      <th class="table_employee_th1">Username</th>
                      <th class="table_employee_th2">Full Name</th>
                      <th class="table_employee_th3">Roles</th>
                      <th class="table_employee_th4">Status</th>
                      <th class="table_employee_th5">Online</th>
                   </tr>
                </thead>
                <tbody>
                    <c:forEach var="employee" items="${requestScope[Attr.EMPLOYEES]}">
                    <jsp:useBean id="employee" class="co.com.bookmaker.data_access.entity.FinalUser"></jsp:useBean>
                     <tr>
                        <c:if test="${param.roleRequester == Role.ADMIN}">
                        <td><a href="<%=AdminController.URL%>?to=<%=AdminController.USER_SUMMARY%>&${Param.USERNAME}=${employee.username}">${employee.username}</a></td>
                        </c:if>
                        <c:if test="${param.roleRequester == Role.MANAGER}">
                        <td><a href="<%=ManagerController.URL%>?to=<%=ManagerController.EMPLOYEE_SUMMARY%>&${Param.USERNAME}=${employee.username}">${employee.username}</a></td>
                        </c:if>
                        <td>${employee.firstName} ${employee.lastName}</td>
                        <td>
                             <c:if test="${employee.inRole(Role.ADMIN)}">Administrator<br/></c:if>
                             <c:if test="${employee.inRole(Role.MANAGER)}">Manager<br/></c:if>
                             <c:if test="${employee.inRole(Role.ANALYST)}">Analyst<br/></c:if>
                             <c:if test="${employee.inRole(Role.SELLER)}">Seller<br/></c:if>
                             <c:if test="${employee.inRole(Role.CLIENT)}">Client<br/></c:if>
                        </td>
                        <td>${Status.str(employee.status)}</td>
                        <td>${auth.isOnline(employee, pageContext.request) ? "Yes" : "No"}</td>
                     </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
    <div class="form-group">
        <div class="col-md-8">
        <c:if test="${param.roleRequester == Role.ADMIN}">
            <a id="btnEditAgency" class="btn btn-default" 
               href="<%=AdminController.URL%>?to=<%=AdminController.EDIT_AGENCY%>&${Param.AGENCY}=${agency.id}">
                <span class="glyphicon glyphicon-edit"></span> Edit</a>
            <a id="btnAddAgency" class="btn btn-default" 
               href="<%=AdminController.URL%>?to=<%=AdminController.SEARCH_AGENCY_EMPLOYEE%>&${Param.AGENCY}=${agency.id}&${Param.ADD_EMPLOYEE}=}">
                <span class="glyphicon glyphicon-plus"></span> Add Employee</a>
            <a id="btnRemAgency" class="btn btn-default" 
               href="<%=AdminController.URL%>?to=<%=AdminController.SEARCH_AGENCY_EMPLOYEE%>&${Param.AGENCY}=${agency.id}&${Param.REM_EMPLOYEE}=">
                <span class="glyphicon glyphicon-minus"></span> Remove Employee</a>
            <a id="btnAgencyBalance" class="btn btn-default" 
               href="<%=AdminController.URL%>?to=<%=AdminController.AGENCY_BALANCE%>&${Param.AGENCY}=${agency.id}">
            <span class="glyphicon glyphicon-stats"></span> Balance</a>
        </c:if>
        <c:if test="${param.roleRequester == Role.MANAGER}">
            <a id="btnEditAgency" class="btn btn-default" 
               href="<%=ManagerController.URL%>?to=<%=ManagerController.EDIT_AGENCY%>&${Param.AGENCY}=${agency.id}">
                <span class="glyphicon glyphicon-edit"></span> Edit</a>
            <a id="btnAgencyBalance" class="btn btn-default" 
                href="<%=ManagerController.URL%>?to=<%=ManagerController.AGENCY_BALANCE%>">
                <span class="glyphicon glyphicon-stats"></span> Balance</a>
        </c:if>
        </div>
    </div>
</form>

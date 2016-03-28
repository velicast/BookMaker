<%-- 
    Document   : user_resume
    Created on : Feb 15, 2016, 11:43:10 AM
    Author     : eduarc
--%>
<%@page import="co.com.bookmaker.business_logic.controller.AccountController"%>
<%@page import="co.com.bookmaker.business_logic.controller.ManagerController"%>
<%@page import="co.com.bookmaker.business_logic.controller.AgencyController"%>
<%@page import="co.com.bookmaker.business_logic.controller.AdminController"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="en_US"></fmt:setLocale>
    
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
</style>
    
<c:set var="user" value="${requestScope[Attr.FINAL_USER]}"></c:set>
<jsp:useBean id="user" class="co.com.bookmaker.data_access.entity.FinalUser"></jsp:useBean>

<c:set var="sUser" value="${sessionScope[Attr.SESSION_USER]}"></c:set>
<jsp:useBean id="sUser" class="co.com.bookmaker.data_access.entity.FinalUser"></jsp:useBean>
    
<c:set var="add" value="${requestScope[Attr.ADD_EMPLOYEE]}"></c:set>
<c:set var="rem" value="${requestScope[Attr.REM_EMPLOYEE]}"></c:set>

<h2 class="main_content_title"> User </h2>

<form role="form" class="form-horizontal">      
    <div class="form-group">
        <div class="col-md-12">
        <c:if test="${param.roleRequester == Role.ADMIN}">
        <c:if test="${add != null}">
            <a id="btnAddEmployee" class="btn btn-default" 
               href="<%=AgencyController.URL%>?do=<%=AgencyController.ADD_EMPLOYEE%>&${Param.EMPLOYEE}=${user.username}&${Param.AGENCY}=${requestScope[Attr.AGENCY].id}">
                <span class="glyphicon glyphicon-plus"></span> Add</a>
        </c:if>
        <c:if test="${rem != null}">
            <a id="btnRemEmployee" class="btn btn-default" 
               href="<%=AgencyController.URL%>?do=<%=AgencyController.REM_EMPLOYEE%>&${Param.EMPLOYEE}=${user.username}&${Param.AGENCY}=${requestScope[Attr.AGENCY].id}">
                <span class="glyphicon glyphicon-minus"></span> Remove</a>
        </c:if>
        <c:if test="${rem == null && add == null}">
            <a id="btnEditEmployee" class="btn btn-default" 
               href="<%=AdminController.URL%>?to=<%=AdminController.EDIT_USER%>&${Param.USERNAME}=${user.username}">
                <span class="glyphicon glyphicon-edit"></span> Edit</a>
        </c:if>
        <a id="btnEmployeeBalance" class="btn btn-default" 
               href="<%=AdminController.URL%>?to=<%=AdminController.EMPLOYEE_BALANCE%>&${Param.USERNAME}=${user.username}">
                <span class="glyphicon glyphicon-stats"></span> Balance</a>
        </c:if>
        <c:if test="${param.roleRequester == Role.MANAGER}">
            <a id="btnEmployeeBalance" class="btn btn-default" 
               href="<%=ManagerController.URL%>?to=<%=ManagerController.EMPLOYEE_BALANCE%>&${Param.USERNAME}=${user.username}">
                <span class="glyphicon glyphicon-stats"></span> Balance</a>
        </c:if>
        <c:if test="${param.roleRequester == Role.ALL}">
            <a id="btnEmployeeBalance" class="btn btn-default" 
               href="<%=AccountController.URL%>?to=<%=AccountController.BALANCE%>">
                <span class="glyphicon glyphicon-stats"></span> Balance</a>
        </c:if>
        </div>
    </div>
    <c:if test="${sUser.author == null}">
    <div class="form-group">
        <label class="col-md-2 control-label">Author: </label>
        <div class="col-md-4">
            <p class="form-control-static"><a href="<%=AdminController.URL%>?to=<%=AdminController.USER_SUMMARY%>&${Param.USERNAME}=${user.author.username}">
                    ${user.author.username}</a> - ${user.author.firstName} ${user.author.lastName}</p>
        </div>
    </div>
    </c:if>
    <c:if test="${requestScope[Attr.ONLINE] == true}">
    <div class="form-group">
        <div class="col-md-2"></div>
        <div class="col-md-4">
            <h4 class="status_label"><span class = "label label-success">Online</span></h4>
        </div>
    </div>
    </c:if>
    <c:if test="${user.agency != null}">
    <div class="form-group">
        <label class="col-md-2 control-label">Agency:</label>
        <div class="col-md-4">
            <c:if test="${param.roleRequester == Role.ADMIN}">
            <p class="form-control-static"><a href="<%=AdminController.URL%>?to=<%=AdminController.AGENCY_SUMMARY%>&${Param.AGENCY}=${user.agency.id}">${user.agency.name}</a></p>
            </c:if>
            <c:if test="${param.roleRequester == Role.MANAGER}">
            <p class="form-control-static"><a href="<%=ManagerController.URL%>?to=<%=ManagerController.AGENCY_SUMMARY%>">${user.agency.name}</a></p>
            </c:if>
            <c:if test="${param.roleRequester == Role.ALL}">
            <p class="form-control-static">${user.agency.name}</p>
            </c:if>
        </div>
    </div>
    </c:if>
    <div class="form-group">
        <label class="col-md-2 control-label">User Name:</label>
        <div class="col-md-4">
            <p class="form-control-static">${user.username}</p>
        </div>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">E-mail: </label>
        <div class="col-md-4">
            <p class="form-control-static">${user.email}</p>
        </div>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">Birth Date: </label>
        <div class="col-md-4">
            <p class="form-control-static"><fmt:formatDate type="time" pattern="dd/MM/yyyy" value="${user.birthDate.getTime()}"/></p>
        </div>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">First Name:</label>
        <div class="col-md-4">
            <p class="form-control-static">${user.firstName}</p>
        </div>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">Last Name:</label>
        <div class="col-md-4">
            <p class="form-control-static">${user.lastName}</p>
        </div>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">City:</label>
        <div class="col-md-4">
            <p class="form-control-static">${user.city}</p>
        </div>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">Address: </label>
        <div class="col-md-4">
            <p class="form-control-static">${user.address}</p>
        </div>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">Telephone: </label>
        <div class="col-md-4">
            <p class="form-control-static">${user.telephone}</p>
        </div>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">Status: </label>
        <div class="col-md-4">
            <p class="form-control-static">${user.status == Status.ACTIVE ? 'Active' : 'Inactive'}</p>
        </div>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">Roles: </label>
        <div class="col-md-2">
            <p class="form-control-static">${user.inRole(Role.ADMIN) ? 'Administrator' : ''}</p>
            <p class="form-control-static">${user.inRole(Role.MANAGER) ? 'Manager' : ''}</p>
            <p class="form-control-static">${user.inRole(Role.ANALYST) ? 'Analyst' : ''}</p>
        </div>
        <div class="col-md-2">
            <p class="form-control-static">${user.inRole(Role.SELLER) ? 'Seller' : ''}</p>
            <p class="form-control-static">${user.inRole(Role.CLIENT) ? 'Client' : ''}</p>
        </div>
    </div>
    <div class="form-group">
        <div class="col-md-12">
        <c:if test="${param.roleRequester == Role.ADMIN}">
        <c:if test="${add != null}">
            <a id="btnAddEmployee" class="btn btn-default" 
               href="<%=AgencyController.URL%>?do=<%=AgencyController.ADD_EMPLOYEE%>&${Param.EMPLOYEE}=${user.username}&${Param.AGENCY}=${requestScope[Attr.AGENCY].id}">
                <span class="glyphicon glyphicon-plus"></span> Add</a>
        </c:if>
        <c:if test="${rem != null}">
            <a id="btnRemEmployee" class="btn btn-default" 
               href="<%=AgencyController.URL%>?do=<%=AgencyController.REM_EMPLOYEE%>&${Param.EMPLOYEE}=${user.username}&${Param.AGENCY}=${requestScope[Attr.AGENCY].id}">
                <span class="glyphicon glyphicon-minus"></span> Remove</a>
        </c:if>
        <c:if test="${rem == null && add == null}">
            <a id="btnEditEmployee" class="btn btn-default" 
               href="<%=AdminController.URL%>?to=<%=AdminController.EDIT_USER%>&${Param.USERNAME}=${user.username}">
                <span class="glyphicon glyphicon-edit"></span> Edit</a>
        </c:if>
        <a id="btnEmployeeBalance" class="btn btn-default" 
               href="<%=AdminController.URL%>?to=<%=AdminController.EMPLOYEE_BALANCE%>&${Param.USERNAME}=${user.username}">
                <span class="glyphicon glyphicon-stats"></span> Balance</a>
        </c:if>
        <c:if test="${param.roleRequester == Role.MANAGER}">
            <a id="btnEmployeeBalance" class="btn btn-default" 
               href="<%=ManagerController.URL%>?to=<%=ManagerController.EMPLOYEE_BALANCE%>&${Param.USERNAME}=${user.username}">
                <span class="glyphicon glyphicon-stats"></span> Balance</a>
        </c:if>
        <c:if test="${param.roleRequester == Role.ALL}">
            <a id="btnEmployeeBalance" class="btn btn-default" 
               href="<%=AccountController.URL%>?to=<%=AccountController.BALANCE%>">
                <span class="glyphicon glyphicon-stats"></span> Balance</a>
        </c:if>
        </div>
    </div>
</form>
   


<%-- 
    Document   : navbar
    Created on : Feb 12, 2016, 11:12:45 AM
    Author     : eduarc
--%>
<%@page import="co.com.bookmaker.business_logic.controller.AccountController"%>
<%@page import="co.com.bookmaker.business_logic.controller.ClientController"%>
<%@page import="co.com.bookmaker.business_logic.controller.SellerController"%>
<%@page import="co.com.bookmaker.business_logic.controller.AnalystController"%>
<%@page import="co.com.bookmaker.business_logic.controller.ManagerController"%>
<%@page import="co.com.bookmaker.business_logic.controller.AdminController"%>
<%@page import="co.com.bookmaker.business_logic.controller.security.AuthenticationController"%>
<%@page import="co.com.bookmaker.business_logic.controller.HomeController"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" href="/css/navbar.css">

<jsp:useBean id="Param" class="co.com.bookmaker.util.type.Parameter"></jsp:useBean>
<jsp:useBean id="Info" class="co.com.bookmaker.util.type.Information"></jsp:useBean>
<jsp:useBean id="Role" class="co.com.bookmaker.util.type.Role"></jsp:useBean>
<jsp:useBean id="Attr" class="co.com.bookmaker.util.type.Attribute"></jsp:useBean>

<c:set var="sUser" value="${sessionScope[Attr.SESSION_USER]}"></c:set>
<jsp:useBean id="sUser" class="co.com.bookmaker.data_access.entity.FinalUser"></jsp:useBean>

<c:set var="sessionRoles" value="${sessionScope[Attr.SESSION_ROLE]}"></c:set>

<c:set var="roleSelection" value="${sessionScope[Attr.ROLE_SELECTION]}"></c:set>


<c:if test="${roleSelection == null}">
<c:if test="${sessionScope[Attr.SESSION_USER] == null}">
    <nav class="navbar navbar-default nav_bar navbar-fixed-top" role="navigation">
        <div class="container-fluid">
            <div class="navbar-header">
                <a id="pHome" class="navbar-brand" href="<%=HomeController.URL%>"><b><span class="glyphicon glyphicon-tower"></span> BookMaker <span class="glyphicon glyphicon-tower"></span></b></a>
            </div>
            <form id="loginForm" class="navbar-form navbar-right" role="form" action="<%=AuthenticationController.URL%>" method="POST">
                <input type="hidden" name="do" value="<%=AuthenticationController.LOGIN%>">
                <div class="input-group">
                    <span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
                    <input id="username" type="text" class="form-control" name="${Param.USERNAME}" value="" placeholder="User Name">                                        
                </div>
                <div class="input-group">
                    <span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
                    <input id="password" type="password" class="form-control" name="${Param.PASSWORD}" value="" placeholder="Password">                                        
                </div>
                <button type="submit" class="btn btn-submit"><i class="glyphicon glyphicon-log-in"></i> <b>Login</b></button>
            </form>
        </div>
    </nav>
</c:if>
<c:if test="${sessionScope[Attr.SESSION_USER] != null}">
    <nav class="navbar navbar-default nav_bar navbar-fixed-top" role="navigation">
        <div class="container-fluid">
            <div class="navbar-header">
                <c:if test="${sUser.agency != null}">
                <a class="navbar-brand" href="<%=HomeController.URL%>"><span class="glyphicon glyphicon-home"></span> <b>${sUser.agency.name}</b></a>
                </c:if>
                <c:if test="${sUser.agency == null}">
                <a class="navbar-brand" href="<%=HomeController.URL%>"><b><span class="glyphicon glyphicon-tower"></span> BookMaker <span class="glyphicon glyphicon-tower"></span></b></a>
                </c:if>
            </div>
            <ul class="nav navbar-nav">
                <c:if test="${Role.inRole(sessionRoles, Role.ADMIN)}">
                    <li id="pAdmin"><a href="<%=AdminController.URL%>">Administrator</a></li>
                </c:if>
                <c:if test="${Role.inRole(sessionRoles, Role.MANAGER) && sUser.agency != null}">
                    <li id="pManager"><a href="<%=ManagerController.URL%>">Manager</a></li>
                </c:if>
                <c:if test="${Role.inRole(sessionRoles, Role.ANALYST)}">
                    <li id="pAnalyst"><a href="<%=AnalystController.URL%>">Analyst</a></li>
                </c:if>
                <c:if test="${Role.inRole(sessionRoles, Role.SELLER) && sUser.agency != null}">
                    <li id="pSeller"><a href="<%=SellerController.URL%>">Seller</a></li>
                </c:if>
                <c:if test="${Role.inRole(sessionRoles, Role.CLIENT) && sUser.agency != null}">
                    <li id="pClient"><a href="<%=ClientController.URL%>">Client</a></li>
                </c:if>
            </ul>
            <form id="loginOut" class="navbar-form navbar-right" role="form" action="<%=AuthenticationController.URL%>">
                <input type="hidden" name="do" value="<%=AuthenticationController.LOGOUT%>">
                <button type="submit" class="btn btn-submit"><i class="glyphicon glyphicon-log-out btn-submit"></i> <b>Logout</b></button>
            </form>
            <c:if test="${sessionRoles != Role.CLIENT}">
            <ul class="nav navbar-nav navbar-right">
                <li id="pAccount"><a href="<%=AccountController.URL%>"><span class="glyphicon glyphicon-user"></span> <b>${sUser.firstName} ${sUser.lastName}</b></a></li>
            </ul>
            </c:if>
        </div>
    </nav>
</c:if>
</c:if>
<c:if test="${roleSelection != null}">
    <c:set var="user" value="${sessionScope[Attr.FINAL_USER]}"></c:set>
    <jsp:useBean id="user" class="co.com.bookmaker.data_access.entity.FinalUser"></jsp:useBean>
    <nav class="navbar navbar-default nav_bar navbar-fixed-top" role="navigation">
        <div class="container-fluid">
            <div class="navbar-header">
                <a class="navbar-brand" href="<%=HomeController.URL%>"><b><span class="glyphicon glyphicon-tower"></span> BookMaker <span class="glyphicon glyphicon-tower"></span></b></a>
            </div>
            <form id="loginOut" class="navbar-form navbar-right" role="form" action="<%=AuthenticationController.URL%>">
                <input type="hidden" name="do" value="<%=AuthenticationController.LOGOUT%>">
                <button type="submit" class="btn btn-submit"><i class="glyphicon glyphicon-log-out btn-submit"></i> <b>Logout</b></button>
            </form>
            <ul class="nav navbar-nav navbar-right">
                <li id="pAccount"><a href="#"><span class="glyphicon glyphicon-user"></span> <b>${user.firstName} ${user.lastName}</b></a></li>
            </ul>
        </div>
    </nav>
</c:if>
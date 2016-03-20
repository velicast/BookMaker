<%-- 
    Document   : role_selection
    Created on : Feb 18, 2016, 2:09:20 PM
    Author     : eduarc
--%>
<%@page import="co.com.bookmaker.business_logic.controller.security.AuthenticationController"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="Param" class="co.com.bookmaker.util.type.Parameter"></jsp:useBean>
<jsp:useBean id="Attr" class="co.com.bookmaker.util.type.Attribute"></jsp:useBean>
<jsp:useBean id="Info" class="co.com.bookmaker.util.type.Information"></jsp:useBean>
<jsp:useBean id="Status" class="co.com.bookmaker.util.type.Status"></jsp:useBean>
<jsp:useBean id="Role" class="co.com.bookmaker.util.type.Role"></jsp:useBean>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        
        <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
        <link rel="stylesheet" href="css/role_selection.css">
        
        <title>BookMaker - Select Roles</title>
    </head>
    <body style="text-align: center">
        <jsp:include page="/WEB-INF/navbar.jsp"></jsp:include>
        <div class="container-fluid">
            <div class="row">
                <div class="col-md-4"></div>
                <div class="col-md-4">
                    <div class="panel panel-default">
                        <div class="form_heading panel-heading">
                            <h3>Roles to Login</h3>
                        </div>
                        <div class="panel-body">
                        <c:set var="user" value="${sessionScope[Attr.FINAL_USER]}"></c:set>
                        <jsp:useBean id="user" class="co.com.bookmaker.data_access.entity.FinalUser"></jsp:useBean>
                        <form id="roleSelectionForm" role="form" class="form-vertical" action="<%=AuthenticationController.URL%>">
                            <input type="hidden" name="do" value="<%=AuthenticationController.ROLE_SELECTION%>">
                            <c:if test="${user.inRole(Role.ADMIN)}">
                            <div>
                                <label class="c-input c-checkbox">
                                    <input type="checkbox" name="${Param.ROLE_ADMIN}" checked/>
                                    <span class="c-indicator"></span>
                                    Administrator
                                </label>                            
                            </div>
                            </c:if>
                            <c:if test="${user.inRole(Role.MANAGER)}">
                            <div>
                                <label class="c-input c-checkbox">
                                    <input type="checkbox" name="${Param.ROLE_MANAGER}" checked/>
                                    <span class="c-indicator"></span>
                                    Manager
                                </label>                            
                            </div>
                            </c:if>
                            <c:if test="${user.inRole(Role.ANALYST)}">
                            <div>
                                <label class="c-input c-checkbox">
                                    <input type="checkbox" name="${Param.ROLE_ANALYST}" checked/>
                                    <span class="c-indicator"></span>
                                    Analyst
                                </label>  
                            </div>
                            </c:if>
                            <c:if test="${user.inRole(Role.SELLER)}">
                            <div>
                                <label class="c-input c-checkbox">
                                    <input type="checkbox" name="${Param.ROLE_SELLER}" checked/>
                                    <span class="c-indicator"></span>
                                    Seller
                                </label>  
                            </div>
                            </c:if>
                            <c:if test="${user.inRole(Role.CLIENT)}">
                            <div>
                                <label class="c-input c-checkbox">
                                    <input type="checkbox" name="${Param.ROLE_CLIENT}" checked/>
                                    <span class="c-indicator"></span>
                                    Client
                                </label>  
                            </div>
                            </c:if>
                            <div class="form-group" style="text-align: center">
                                <button type="submit" class="btn btn-submit"><i class="glyphicon glyphicon-log-in"></i> <b>Login</b></button>
                            </div>
                        </form>
                        </div>
                    </div>
                </div>
                <div class="col-md-4"></div>
            </div>
        </div>
        <%@include file="/WEB-INF/footer.jsp" %>
                            
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
        <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    </body>
    <jsp:include page="/WEB-INF/info_modal.jsp"></jsp:include>
    <jsp:include page="/WEB-INF/warning_modal.jsp"></jsp:include>
    <jsp:include page="/WEB-INF/error_modal.jsp"></jsp:include>
</html>

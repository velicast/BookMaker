<%-- 
    Document   : search_user
    Created on : Feb 15, 2016, 4:10:00 PM
    Author     : eduarc
--%>
<%@page import="co.com.bookmaker.business_logic.controller.FinalUserController"%>
<%@page import="co.com.bookmaker.business_logic.controller.AdminController"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="Param" class="co.com.bookmaker.util.type.Parameter"></jsp:useBean>
<jsp:useBean id="Info" class="co.com.bookmaker.util.type.Information"></jsp:useBean>
<jsp:useBean id="Attr" class="co.com.bookmaker.util.type.Attribute"></jsp:useBean>
<jsp:useBean id="Status" class="co.com.bookmaker.util.type.Status"></jsp:useBean>

<h2 class="main_content_title"> Search User </h2>
<form id="searchUserForm" role="form" class="form-horizontal" action="<%=FinalUserController.URL%>" method="POST">
    <input type="hidden" name="do" value="<%=FinalUserController.SEARCH%>">
    <div class="form-group">
        <label class="control-label col-md-2">Username:</label>
        <div class="col-md-4">
            <input type="text" class="form-control input-sm" id="usernameToSearch" name="${Param.USERNAME}" value="${requestScope[Attr.USERNAME]}" placeholder="User Name">
        </div>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">Roles: </label>
        <div class="col-md-2">
            <input type="checkbox" name="${Param.ROLE_ADMIN}" /> Administrator <br />
            <input type="checkbox" name="${Param.ROLE_MANAGER}" /> Manager <br />
            <input type="checkbox" name="${Param.ROLE_ANALYST}" /> Analyst <br />
        </div>
        <div class="col-md-3">
            <input type="checkbox" name="${Param.ROLE_SELLER}" /> Seller <br />
            <input type="checkbox" name="${Param.ROLE_CLIENT}" /> Client <br />
        </div>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">Status: </label>
        <div class="col-md-4">
            <select name="${Param.STATUS}" class="form-control">
                <option value="">Any</option>
                <option value="${Status.ACTIVE}">${Status.str(Status.ACTIVE)}</option>
                <option value="${Status.INACTIVE}">${Status.str(Status.INACTIVE)}</option>
            </select>
        </div>
    </div>
    <div class="col-md-6" style="text-align: center">
        <button class="btn btn-submit" id="btnSearchUser"><span class="glyphicon glyphicon-search"></span> Search</button>
    </div>
    <output style="color: red">${requestScope[Info.SEARCH_RESULT]}</output>
</form>


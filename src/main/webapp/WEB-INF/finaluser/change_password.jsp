<%-- 
    Document   : change_password
    Created on : Mar 15, 2016, 6:14:45 PM
    Author     : eduarc
--%>
<%@page import="co.com.bookmaker.business_logic.controller.FinalUserController"%>

<jsp:useBean id="Param" class="co.com.bookmaker.util.type.Parameter"></jsp:useBean>
<jsp:useBean id="Info" class="co.com.bookmaker.util.type.Information"></jsp:useBean>

<h2 class="main_content_title"> Change Password </h2>

<div class="row">
    <form id="changePasswordForm" role="form" class="form-horizontal" action="<%=FinalUserController.URL%>" method="POST">
        <input type="hidden" name="do" value="<%=FinalUserController.CHANGE_PASSWORD%>">
        <div class="form-group">
            <label class="control-label col-md-2">Current: </label>
            <div class="col-md-4">
                <input type="password" class="form-control input-sm" name="${Param.PASSWORD}" placeholder="old password">
            </div>
            <output style="color: red">${requestScope[Info.PASSWORD]}</output>
        </div>
        <div class="form-group">
            <label class="control-label col-md-2">New: </label>
            <div class="col-md-4">
                <input type="password" class="form-control input-sm" name="${Param.NEW_PASSWORD}" placeholder="new password">
            </div>
            <output style="color: red">${requestScope[Info.NEW_PASSWORD]}</output>
        </div>
        <div class="form-group">
            <label class="control-label col-md-2">Confirm New: </label>
            <div class="col-md-4">
                <input type="password" class="form-control input-sm" name="${Param.CONFIRMED_NEW_PASSWORD}" placeholder="new password again">
            </div>
            <output style="color: red">${requestScope[Info.CONFIRMED_PASSWORD]}</output>
        </div>
        <div class="col-md-6" style="text-align: center">
            <button id="btnChangePassword" type="submit" class="btn btn-submit"><span class="glyphicon glyphicon-check"></span> Change</button>
        </div>
    </form>
</div>
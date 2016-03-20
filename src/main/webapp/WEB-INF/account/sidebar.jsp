<%-- 
    Document   : sidebar
    Created on : Feb 15, 2016, 5:15:04 PM
    Author     : eduarc
--%>
<%@page import="co.com.bookmaker.business_logic.controller.AccountController"%>

<link rel="stylesheet" href="/css/sidebar.css">

<nav class="navbar navbar-default" role="navigation">
    <div class="main_menu menu_role_title">
        <h3>My Account</h3>
    </div>
    <!-- Main Menu -->
    <ul class="main_menu nav navbar-nav">
        <!-- Dropdown-->
        <li id="accountSummary"><a href="<%=AccountController.URL%>?to=<%=AccountController.SUMMARY%>"><span class="glyphicon glyphicon-user"></span> Summary</a></li>
        <li id="changePassword"><a href="<%=AccountController.URL%>?to=<%=AccountController.CHANGE_PASSWORD%>"><span class="glyphicon glyphicon-lock"></span> Change Password</a></li>
    </ul>
</nav>

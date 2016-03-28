<%-- 
    Document   : sidebar
    Created on : Feb 15, 2016, 5:15:04 PM
    Author     : eduarc
--%>
<%@page import="co.com.bookmaker.business_logic.controller.ManagerController"%>

<link rel="stylesheet" href="css/sidebar.css">

<nav class="navbar navbar-default" role="navigation">
    <div class="main_menu menu_role_title">
        <h3>Gerente</h3>
    </div>
    <!-- Main Menu -->
    <ul class="main_menu nav navbar-nav">
        <!-- Dropdown-->
        <li id="agencySummary"><a href="<%=ManagerController.URL%>?to=<%=ManagerController.AGENCY_SUMMARY%>"><span class="glyphicon glyphicon-user"></span> Agencia</a></li>
        <li id="balance"><a href="<%=ManagerController.URL%>?to=<%=ManagerController.AGENCY_BALANCE%>"><span class="glyphicon glyphicon-stats"></span> Balance</a></li>
        <li id="searchParlay"><a href="<%=ManagerController.URL%>?to=<%=ManagerController.SEARCH_PARLAY%>"><span class="glyphicon glyphicon-search"></span> Parlay</a></li>
        <li id="searchMatch"><a href="<%=ManagerController.URL%>?to=<%=ManagerController.SEARCH_MATCH%>"><span class="glyphicon glyphicon-search"></span> Juego</a></li>
    </ul>
</nav>

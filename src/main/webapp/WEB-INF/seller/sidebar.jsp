<%-- 
    Document   : sidebar
    Created on : Feb 19, 2016, 11:59:00 AM
    Author     : eduarc
--%>
<%@page import="co.com.bookmaker.business_logic.controller.SellerController"%>

<link rel="stylesheet" href="/css/sidebar.css">

<nav class="navbar navbar-default" role="navigation">
    <div class="main_menu menu_role_title">
        <h3>Seller</h3>
    </div>
    <!-- Main Menu -->
    <ul class="main_menu nav navbar-nav">
        <!-- Dropdown-->
        <!--<li id="dashboard"><a href="<%=SellerController.URL%>?to=<%=SellerController.DASHBOARD%>"><span class="glyphicon glyphicon-dashboard"></span> Dashboard</a></li>-->
        <li id="selling_queue"><a href="<%=SellerController.URL%>?to=<%=SellerController.SELLING_QUEUE%>"><span class="glyphicon glyphicon-list"></span> My Selling Queue</a></li>
        <li id="searchParlay"><a href="<%=SellerController.URL%>?to=<%=SellerController.SEARCH_PARLAY%>"><span class="glyphicon glyphicon-search"></span> Parlay</a></li>
    </ul>
</nav>


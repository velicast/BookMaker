<%-- 
    Document   : sidebar
    Created on : Feb 19, 2016, 11:59:00 AM
    Author     : eduarc
--%>
<%@page import="co.com.bookmaker.business_logic.controller.SellerController"%>

<link rel="stylesheet" href="/BookMaker/css/sidebar.css">

<nav class="navbar navbar-default" role="navigation">
    <div class="main_menu menu_role_title">
        <h3>Seller</h3>
    </div>
    <!-- Main Menu -->
    <ul class="main_menu nav navbar-nav">
        <!-- Dropdown-->
        <!--<li id="dashboard"><a href="<%=SellerController.URL%>?to=<%=SellerController.DASHBOARD%>"><span class="glyphicon glyphicon-dashboard"></span> Dashboard</a></li>-->
        <li id="selling_queue"><a href="<%=SellerController.URL%>?to=<%=SellerController.SELLING_QUEUE%>"><span class="glyphicon glyphicon-list"></span> My Selling Queue</a></li>
        <li class="panel panel-default" id="dropdown">
            <a data-toggle="collapse" href="#menuParlay">
                <span class="glyphicon glyphicon-list-alt"></span> Parlay <span class="caret"></span>
            </a>
            <!-- Dropdown level 1 -->
            <div id="menuParlay" class="panel-collapse collapse">
                <div class="panel-body">
                    <ul class="nav navbar-nav">
                        <li id="searchParlay"><a href="<%=SellerController.URL%>?to=<%=SellerController.SEARCH_PARLAY%>"><span class="glyphicon glyphicon-search"></span> Search</a></li>
                    </ul>
                </div>
            </div>
        </li>
    </ul>
</nav>


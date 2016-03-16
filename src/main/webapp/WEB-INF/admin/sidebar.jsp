<%-- 
    Document   : sidebar
    Created on : Feb 15, 2016, 5:15:04 PM
    Author     : eduarc
--%>
<%@page import="co.com.bookmaker.business_logic.controller.AdminController"%>

<link rel="stylesheet" href="/BookMaker/css/sidebar.css">

<nav class="navbar navbar-default" role="navigation">
    <div class="main_menu menu_role_title">
        <h3>Administrator</h3>
    </div>
    <!-- Main Menu -->
    <ul class="main_menu nav navbar-nav">
        <!-- Dropdown-->
        <li id="dashboard"><a href="<%=AdminController.URL%>?to=<%=AdminController.DASHBOARD%>"><span class="glyphicon glyphicon-dashboard"></span> Dashboard</a></li>
        <li class="panel panel-default" id="dropdown">
            <a data-toggle="collapse" href="#menuUser">
                <span class="glyphicon glyphicon-user"></span> User <span class="caret"></span>
            </a>
            <!-- Dropdown level 1 -->
            <div id="menuUser" class="panel-collapse collapse">
                <div class="panel-body">
                    <ul class="nav navbar-nav">
                        <li id="newUser"><a href="<%=AdminController.URL%>?to=<%=AdminController.NEW_USER%>"><span class="glyphicon glyphicon-edit"></span> New</a></li>
                        <li id="searchUser"><a href="<%=AdminController.URL%>?to=<%=AdminController.SEARCH_USER%>"><span class="glyphicon glyphicon-search"></span> Search</a></li>
                    </ul>
                </div>
            </div>
        </li>
        <!-- Dropdown-->
        <li class="panel panel-default" id="dropdown">
            <a data-toggle="collapse" href="#menuAgency">
                <span class="glyphicon glyphicon-user"></span> Agency <span class="caret"></span>
            </a>
            <!-- Dropdown level 1 -->
            <div id="menuAgency" class="panel-collapse collapse">
                <div class="panel-body">
                    <ul class="nav navbar-nav">
                        <li id="newAgency"><a href="<%=AdminController.URL%>?to=<%=AdminController.NEW_AGENCY%>"><span class="glyphicon glyphicon-edit"></span> New</a></li>
                        <li id="searchAgency"><a href="<%=AdminController.URL%>?to=<%=AdminController.SEARCH_AGENCY%>"><span class="glyphicon glyphicon-search"></span> Search</a></li>
                        <li id="listAllAgency"><a href="<%=AdminController.URL%>?to=<%=AdminController.LIST_AGENCIES%>"><span class="glyphicon glyphicon-list"></span> List All</a></li>
                    </ul>
                </div>
            </div>
        </li>
    </ul>
</nav>

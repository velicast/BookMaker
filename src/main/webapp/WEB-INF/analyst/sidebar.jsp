<%-- 
    Document   : sidebar
    Created on : Feb 23, 2016, 11:03:12 AM
    Author     : eduarc
--%>
<%@page import="co.com.bookmaker.business_logic.controller.AnalystController"%>
<%@page import="co.com.bookmaker.business_logic.controller.AdminController"%>

<link rel="stylesheet" href="/css/sidebar.css">

<nav class="navbar navbar-default" role="navigation">
    <div class="main_menu menu_role_title">
        <h3>Analyst</h3>
    </div>
    <!-- Main Menu -->
    <ul class="main_menu nav navbar-nav">
        <!-- Dropdown-->
        <li class="panel panel-default" id="dropdown">
            <a data-toggle="collapse" href="#menuMatch">
                <span class="glyphicon glyphicon-star"></span> Match <span class="caret"></span>
            </a>
            <!-- Dropdown level 1 -->
            <div id="menuMatch" class="panel-collapse collapse">
                <div class="panel-body">
                    <ul class="nav navbar-nav">
                        <li id="newMatch"><a href="<%=AnalystController.URL%>?to=<%=AnalystController.NEW_MATCH_SPORT_SELECTION %>"><span class="glyphicon glyphicon-edit"></span> New</a></li>
                        <li id="searchMatch"><a href="<%=AnalystController.URL%>?to=<%=AnalystController.SEARCH_MATCH%>"><span class="glyphicon glyphicon-search"></span> Search</a></li>
                        <li id="pendingMatch"><a href="<%=AnalystController.URL%>?to=<%=AnalystController.PENDING_RESULT%>"><span class="glyphicon glyphicon-search"></span> Pending Result</a></li>
                    </ul>
                </div>
            </div>
        </li>
        <!-- Dropdown-->
        <li class="panel panel-default" id="dropdown">
            <a data-toggle="collapse" href="#menuTournament">
                <span class="glyphicon glyphicon-star"></span> Tournament <span class="caret"></span>
            </a>
            <!-- Dropdown level 1 -->
            <div id="menuTournament" class="panel-collapse collapse">
                <div class="panel-body">
                    <ul class="nav navbar-nav">
                        <li id="newTournament"><a href="<%=AnalystController.URL%>?to=<%=AnalystController.NEW_TOURNAMENT%>"><span class="glyphicon glyphicon-edit"></span> New</a></li>
                        <li id="searchTournament"><a href="<%=AnalystController.URL%>?to=<%=AnalystController.SEARCH_TOURNAMENT%>"><span class="glyphicon glyphicon-search"></span> Search</a></li>
                    </ul>
                </div>
            </div>
        </li>
    </ul>
</nav>

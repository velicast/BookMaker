<%-- 
    Document   : sidebar
    Created on : Feb 15, 2016, 5:15:04 PM
    Author     : eduarc
--%>
<%@page import="co.com.bookmaker.business_logic.controller.AdminController"%>

<link rel="stylesheet" href="css/sidebar.css">

<nav class="navbar navbar-default" role="navigation">
    <div class="main_menu menu_role_title">
        <h3>Administrador</h3>
    </div>
    <!-- Main Menu -->
    <ul class="main_menu nav navbar-nav">
        <!-- Dropdown-->
        <li class="panel panel-default" id="dropdown">
            <a data-toggle="collapse" href="#menuBalance">
                <span class="glyphicon glyphicon-stats"></span> Balance <span class="caret"></span>
            </a>
            <!-- Dropdown level 1 -->
            <div id="menuBalance" class="panel-collapse collapse">
                <div class="panel-body">
                    <ul class="nav navbar-nav">
                        <li id="moneyBalance"><a href="<%=AdminController.URL%>?to=<%=AdminController.MONEY_BALANCE%>"><span class="glyphicon glyphicon-usd"></span> Dinero</a></li>
                        <li id="matchesBalance"><a href="<%=AdminController.URL%>?to=<%=AdminController.MATCHES_BALANCE%>"><span class="glyphicon glyphicon-star"></span> Juegos</a></li>
                        <li id="accountsBalance"><a href="<%=AdminController.URL%>?to=<%=AdminController.ACCOUNTS_BALANCE%>"><span class="glyphicon glyphicon-user"></span> Cuentas</a></li>
                    </ul>
                </div>
            </div>
        </li>
        <li class="panel panel-default" id="dropdown">
            <a data-toggle="collapse" href="#menuUser">
                <span class="glyphicon glyphicon-user"></span> Usuario <span class="caret"></span>
            </a>
            <!-- Dropdown level 1 -->
            <div id="menuUser" class="panel-collapse collapse">
                <div class="panel-body">
                    <ul class="nav navbar-nav">
                        <li id="newUser"><a href="<%=AdminController.URL%>?to=<%=AdminController.NEW_USER%>"><span class="glyphicon glyphicon-edit"></span> Nuevo</a></li>
                        <li id="searchUser"><a href="<%=AdminController.URL%>?to=<%=AdminController.SEARCH_USER%>"><span class="glyphicon glyphicon-search"></span> Buscar</a></li>
                    </ul>
                </div>
            </div>
        </li>
        <!-- Dropdown-->
        <li class="panel panel-default" id="dropdown">
            <a data-toggle="collapse" href="#menuAgency">
                <span class="glyphicon glyphicon-user"></span> Agencia <span class="caret"></span>
            </a>
            <!-- Dropdown level 1 -->
            <div id="menuAgency" class="panel-collapse collapse">
                <div class="panel-body">
                    <ul class="nav navbar-nav">
                        <li id="newAgency"><a href="<%=AdminController.URL%>?to=<%=AdminController.NEW_AGENCY%>"><span class="glyphicon glyphicon-edit"></span> Nuevo</a></li>
                        <li id="searchAgency"><a href="<%=AdminController.URL%>?to=<%=AdminController.SEARCH_AGENCY%>"><span class="glyphicon glyphicon-search"></span> Buscar</a></li>
                        <li id="listAllAgency"><a href="<%=AdminController.URL%>?to=<%=AdminController.LIST_AGENCIES%>"><span class="glyphicon glyphicon-list"></span> Listar Todos</a></li>
                    </ul>
                </div>
            </div>
        </li>
    </ul>
</nav>

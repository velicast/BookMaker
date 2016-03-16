<%-- 
    Document   : search_agency
    Created on : Feb 16, 2016, 5:43:23 PM
    Author     : eduarc
--%>
<%@page import="co.com.bookmaker.business_logic.controller.AgencyController"%>

<jsp:useBean id="Param" class="co.com.bookmaker.util.type.Parameter"></jsp:useBean>
<jsp:useBean id="Info" class="co.com.bookmaker.util.type.Information"></jsp:useBean>
<jsp:useBean id="Attr" class="co.com.bookmaker.util.type.Attribute"></jsp:useBean>

<h2 class="main_content_title"> Search Agency </h2>

<form id="searchAgencyForm" role="form" class="form-horizontal" action="<%=AgencyController.URL%>">
    <div class="form-group">
        <input type="hidden" name="do" value="<%=AgencyController.SEARCH%>">
        <label class="control-label col-md-2">Employee: </label>
        <div class="col-md-4">
            <div class="input-group">
                <input type="text" class="form-control input-sm" id="managerToSearch" name="${Param.USERNAME}" value="${requestScope[Attr.USERNAME]}" placeholder="User Name">
                <span class="input-group-btn">
                    <button class="btn btn-sm btn-submit" id="btnSearchAgency"><span class="glyphicon glyphicon-search"></span></button>
                </span>
            </div>
        </div>
    </div>
    <output style="color: red">${requestScope[Info.USERNAME]}</output>
</form>

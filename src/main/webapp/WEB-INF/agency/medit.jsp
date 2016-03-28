<%-- 
    Document   : medit
    Created on : Mar 23, 2016, 11:56:44 PM
    Author     : eduarc
--%>

<%@page import="co.com.bookmaker.business_logic.controller.AgencyController"%>
<%@page import="co.com.bookmaker.business_logic.controller.AdminController"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- Define constant wrappers here -->
<jsp:useBean id="Param" class="co.com.bookmaker.util.type.Parameter"></jsp:useBean>
<jsp:useBean id="Attr" class="co.com.bookmaker.util.type.Attribute"></jsp:useBean>
<jsp:useBean id="Info" class="co.com.bookmaker.util.type.Information"></jsp:useBean>
<jsp:useBean id="Status" class="co.com.bookmaker.util.type.Status"></jsp:useBean>
<jsp:useBean id="Role" class="co.com.bookmaker.util.type.Role"></jsp:useBean>

    <!-- Define global variables here -->
<c:set var="agency" value="${requestScope[Attr.AGENCY]}"></c:set>
<jsp:useBean id="agency" class="co.com.bookmaker.util.form.bean.AgencyBean"></jsp:useBean>

<h2 class="main_content_title"> Editar Agencia </h2>

<form id="editAgencyForm" role="form" class="form-horizontal" action="<%=AgencyController.URL%>" method="POST">
    <input type="hidden" name="do" value="<%=AgencyController.MEDIT%>">
    <input type="hidden" name="${Param.AGENCY}" value="${agency.id}">
    <div class="form-group">
        <output class="col-md-7" style="text-align: center">Campos marcados con * son obligatorios</output>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">* Min. logros por Parlay: </label>
        <div class="col-md-4">
            <input type="text" class="form-control input-sm" name="${Param.MIN_ODDS}" value="${agency.minOdds}" placeholder="0">
        </div>
        <output style="color: red">${requestScope[Info.MIN_ODDS]}</output>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">* Max. logros por Parlay: </label>
        <div class="col-md-4">
            <input type="text" class="form-control input-sm" name="${Param.MAX_ODDS}" value="${agency.maxOdds}" placeholder="0">
        </div>
        <output style="color: red">${requestScope[Info.MAX_ODDS]}</output>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">* Max. ganancia por Parlay: </label>
        <div class="col-md-4">
            <input type="text" class="form-control input-sm" name="${Param.MAX_PROFIT}" value="${agency.maxProfit}" placeholder="0.0">
        </div>
        <output style="color: red">${requestScope[Info.MAX_PROFIT]}</output>
    </div>
    <div class="col-md-6" style="text-align: center">
        <button type="submit" id="btnEditAgency" class="btn btn-submit"><span class="glyphicon glyphicon-save"></span> Guardar</button>
    </div>
</form>


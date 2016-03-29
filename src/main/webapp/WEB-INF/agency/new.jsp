<%-- 
    Document   : new_agency
    Created on : Feb 16, 2016, 5:43:08 PM
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

<h2 class="main_content_title"> Nueva Agencia </h2>

<form id="newAgencyForm" role="form" class="form-horizontal" action="<%=AgencyController.URL%>" method="POST">
    <input type="hidden" name="do" value="<%=AgencyController.NEW%>">
    <div class="form-group">
        <output class="col-md-7" style="text-align: center">Campos marcados con * son obligatorios</output>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">* Gerente: </label>
        <div class="col-md-4">
            <input type="text" class="form-control input-sm" name="${Param.MANAGER}" value="${agency.managerUsername}" placeholder="Ususario">
        </div>
        <output style="color: red">${requestScope[Info.MANAGER]}</output>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">* Nombre:</label>
        <div class="col-md-4">
            <input type="text" class="form-control input-sm" name="${Param.NAME}" value="${agency.name}" placeholder="Nombre">
        </div>
        <output style="color: red">${requestScope[Info.NAME]}</output>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label"> E-mail: </label>
        <div class="col-md-4">
            <input type="email" class="form-control input-sm" name="${Param.EMAIL}" value="${agency.email}" placeholder="E-mail">
            <span class = "help-block">Ej: someone@example.com</span>
        </div>
        <output style="color: red">${requestScope[Info.EMAIL]}</output>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">Telefono: </label>
        <div class="col-md-4">
            <input type="text" class="form-control input-sm" name="${Param.TELEPHONE}" value="${agency.telephone}" placeholder="Telefono">
        </div>
        <output style="color: red">${requestScope[Info.TELEPHONE]}</output>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">Ciudad:</label>
        <div class="col-md-4">
            <input list="cities" class="form-control input-sm" name="${Param.CITY}" value="${agency.city}" placeholder="Ciudad">
            <datalist id="cities">
                <option value=""></option>
            </datalist>
        </div>
        <output style="color: red">${requestScope[Info.CITY]}</output>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">Dirección: </label>
        <div class="col-md-4">
            <input type="text" class="form-control input-sm" name="${Param.ADDRESS}" value="${agency.address}" placeholder="Dirección">
        </div>
        <output style="color: red">${requestScope[Info.ADDRESS]}</output>
    </div>
    <hr class="featurette-divider">
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
    <div class="form-group">
        <label for="roles" class="col-md-2 control-label"> </label>
        <div class="col-md-4">
            <input type="checkbox" name="${Param.ACCEPT_GLOBAL_ODDS}" ${agency.acceptGlobalOdds ? 'checked' : ''}/> Acepta logros globales
        </div>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">Estado: </label>
        <div class="col-md-4">
            <select class="form-control input-sm" name="${Param.STATUS}">
                <option value="${Status.ACTIVE}" ${agency.status == Status.ACTIVE ? 'selected' : ''}> ${Status.str(Status.ACTIVE)} </option>
                <option value="${Status.INACTIVE}" ${agency.status == Status.INACTIVE ? 'selected' : ''}> ${Status.str(Status.INACTIVE)} </option>
            </select>
        </div>
        <output style="color: red">${requestScope[Info.STATUS]}</output>
    </div>
    <div class="col-md-6" style="text-align: center">
        <button type="submit" id="btnCreateAgency" class="btn btn-submit"><span class="glyphicon glyphicon-check"></span> Crear</button>
    </div>
</form>
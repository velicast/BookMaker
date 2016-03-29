<%-- 
    Document   : edit_user
    Created on : Feb 15, 2016, 10:52:06 PM
    Author     : eduarc
--%>
<%@page import="co.com.bookmaker.data_access.entity.FinalUser"%>
<%@page import="co.com.bookmaker.business_logic.controller.AdminController"%>
<%@page import="co.com.bookmaker.business_logic.controller.FinalUserController"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- Define constant wrappers here -->
<jsp:useBean id="Param" class="co.com.bookmaker.util.type.Parameter"></jsp:useBean>
<jsp:useBean id="Attr" class="co.com.bookmaker.util.type.Attribute"></jsp:useBean>
<jsp:useBean id="Info" class="co.com.bookmaker.util.type.Information"></jsp:useBean>
<jsp:useBean id="Status" class="co.com.bookmaker.util.type.Status"></jsp:useBean>
<jsp:useBean id="Role" class="co.com.bookmaker.util.type.Role"></jsp:useBean>

<c:set var="user" value="${requestScope[Attr.FINAL_USER]}"></c:set>
<jsp:useBean id="user" class="co.com.bookmaker.util.form.bean.FinalUserBean"></jsp:useBean>

<h2 class="main_content_title"> Editar Usuario </h2>

<form id="editUserForm" role="form" class="form-horizontal" action="<%=FinalUserController.URL%>" method="POST">
    <input type="hidden" name="do" value="<%=FinalUserController.EDIT%>">
    <input type="hidden" name="${Param.TARGET_USERNAME}" value="${user.targetUsername}">
    <div class="form-group">
        <output class="col-md-7" style="text-align: center">Campos marcados con * son obligatorios</output>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">* Usuario:</label>
        <div class="col-md-4">
            <input type="text" class="form-control input-sm" name="${Param.USERNAME}" value="${user.username}" placeholder="Usuario">
        </div>
        <output style="color: red">${requestScope[Info.USERNAME]}</output>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">* Contraseña: </label>
        <div class="col-md-4">
            <input type="password" class="form-control input-sm" name="${Param.PASSWORD}" value="" placeholder="Contraseña">
            <span class = "help-block">Min 4 caracteres de largo. Deje en blanco para conservar la actual.</span>
        </div>
        <output style="color: red">${requestScope[Info.PASSWORD]}</output>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label"> E-mail: </label>
        <div class="col-md-4">
            <input type="email" class="form-control input-sm" name="${Param.EMAIL}" value="${user.email}" placeholder="E-mail">
            <span class = "help-block">Ej: someone@example.com</span>
        </div>
        <output style="color: red">${requestScope[Info.EMAIL]}</output>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">* Fecha de Nacimiento: </label>
        <div class="col-md-4">
            <input class="form-control input-sm" name="${Param.BIRTH_DATE}" value="${user.birthDate}" placeholder="dd/mm/yyyy">
        </div>
        <output style="color: red">${requestScope[Info.BIRTH_DATE]}</output>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">* Nombre:</label>
        <div class="col-md-4">
            <input type="text" class="form-control input-sm" name="${Param.FIRST_NAME}" value="${user.firstName}" placeholder="Nombre">
        </div>
        <output style="color: red">${requestScope[Info.FIRST_NAME]}</output>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">* Apellido:</label>
        <div class="col-md-4">
            <input type="text" class="form-control input-sm" name="${Param.LAST_NAME}" value="${user.lastName}" placeholder="Apellido">
        </div>
        <output style="color: red">${requestScope[Info.LAST_NAME]}</output>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">Ciudad:</label>
        <div class="col-md-4">
            <input list="cities" class="form-control input-sm" name="${Param.CITY}" value="${user.city}" placeholder="Ciudad">
            <datalist id="cities">
                <option value=""></option>
            </datalist>
        </div>
        <output style="color: red">${requestScope[Info.CITY]}</output>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">Dirección: </label>
        <div class="col-md-4">
            <input type="text" class="form-control input-sm" name="${Param.ADDRESS}" value="${user.address}" placeholder="Dirección">
        </div>
        <output style="color: red">${requestScope[Info.ADDRESS]}</output>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">Telefono: </label>
        <div class="col-md-4">
            <input type="text" class="form-control input-sm" name="${Param.TELEPHONE}" value="${user.telephone}" placeholder="Telefono">
        </div>
        <output style="color: red">${requestScope[Info.TELEPHONE]}</output>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">Estado: </label>
        <div class="col-md-4">
            <select class="form-control input-sm" id="status" name="${Param.STATUS}">
                <option value="${Status.ACTIVE}" ${user.status == Status.ACTIVE ? 'selected' : ''}> Activo </option>
                <option value="${Status.INACTIVE}" ${user.status == Status.INACTIVE ? 'selected' : ''}> Inactivo </option>
            </select>
        </div>
        <output style="color: red">${requestScope[Info.STATUS]}</output>
    </div>
    <div class="form-group">
        <label class="col-md-2 control-label">Roles: </label>
        <div class="col-md-2">
            <input type="checkbox" name="${Param.ROLE_ADMIN}" ${user.getrAdmin() ? 'checked' : ''}/> Administrador <br />
            <input type="checkbox" name="${Param.ROLE_MANAGER}" ${user.getrManager() ? 'checked' : ''}/> Gerente <br />
            <input type="checkbox" name="${Param.ROLE_ANALYST}" ${user.getrAnalyst() ? 'checked' : ''}/> Analista <br />
        </div>
        <div class="col-md-3">
            <input type="checkbox" name="${Param.ROLE_SELLER}" ${user.getrSeller() ? 'checked' : ''}/> Vendedor <br />
            <input type="checkbox" name="${Param.ROLE_CLIENT}" ${user.getrClient() ? 'checked' : ''}/> Cliente <br />
        </div>
        <output style="color: red">${requestScope[Info.ROLE]}</output>
    </div>
    <div class="col-md-6" style="text-align: center">
        <button type="submit" id="btnEditUser" class="btn btn-submit"><span class="glyphicon glyphicon-save"></span> Guardar</button>
    </div>
</form>
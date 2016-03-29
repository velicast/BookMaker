<%-- 
    Document   : list
    Created on : Feb 27, 2016, 12:00:00 AM
    Author     : eduarc
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="Attr" class="co.com.bookmaker.util.type.Attribute"></jsp:useBean>
<c:set var="list" value="${requestScope[Attr.TOURNAMENTS]}"></c:set>
    
    <option id="anyTournament" value="">Cualquiera</option>
<c:forEach var="tournament" items="${list}">
    <option value="${tournament.id}">${tournament.name}</option>
</c:forEach>


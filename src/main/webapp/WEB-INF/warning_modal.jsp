<%-- 
    Document   : warning_modal
    Created on : Feb 16, 2016, 12:03:14 PM
    Author     : eduarc
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="Info" class="co.com.bookmaker.util.type.Information"></jsp:useBean>
    
<c:if test="${requestScope[Info.WARNING] != null}">
    <div id="warningModal" class="modal fade" role="dialog">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 id="warningModalTitle" class="modal-title" style="color: orange">Advertencia</h4>
                </div>
                <div class="modal-body container-fluid">
                    <span class="col-md-2 glyphicon glyphicon-warning-sign" style="color: orange; font-size:50px;"></span>
                    <p class="col-md-10" id="warningModalContent" style="color: orange">${requestScope[Info.WARNING]}</p>
                </div>
            </div>
        </div>
    </div>
    <script>
        $('#warningModal').modal('show');
    </script>
</c:if>
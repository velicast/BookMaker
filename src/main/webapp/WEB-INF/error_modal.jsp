<%-- 
    Document   : error_modal
    Created on : Feb 16, 2016, 11:37:44 AM
    Author     : eduarc
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="Info" class="co.com.bookmaker.util.type.Information"></jsp:useBean>
    
<c:if test="${requestScope[Info.ERROR] != null}">
    <div id="errorModal" class="modal fade" role="dialog">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 id="errorModalTitle" class="modal-title" style="color: red">Error</h4>
                </div>
                
                <div class="modal-body container-fluid">
                    <span class="col-md-2 glyphicon glyphicon-remove-sign" style="color: red; font-size:50px;"></span>
                    <p class="col-md-10" id="errorModalContent" style="color: red">${requestScope[Info.ERROR]}</p>
                </div>
            </div>
        </div>
    </div>
    <script>
        $('#errorModal').modal('show');
    </script>
</c:if>
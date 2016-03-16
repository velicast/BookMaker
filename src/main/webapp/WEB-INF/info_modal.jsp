<%-- 
    Document   : info_modal
    Created on : Feb 16, 2016, 12:02:04 PM
    Author     : eduarc
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="Info" class="co.com.bookmaker.util.type.Information"></jsp:useBean>
    
<c:if test="${requestScope[Info.INFO] != null}">
    <div id="infoModal" class="modal fade" role="dialog">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 id="infoModalTitle" class="modal-title" style="color: dodgerblue">Information</h4>
                </div>
                <div class="modal-body container-fluid">
                    <span class="col-md-2 glyphicon glyphicon-info-sign" style="color: dodgerblue; font-size:50px;"></span>
                    <p class="col-md-10" id="infoModalContent" style="color: dodgerblue">${requestScope[Info.INFO]}</p>
                </div>
            </div>
        </div>
    </div>
    <script>
        $('#infoModal').modal('show');
    </script>
</c:if>

/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function () {

$(":checkbox").on('change', function () {
    var checked = $(this).is(":checked");
    var oddId = $(this).prop("value");
    if (checked) {
        var value = $("#oddselection").prop("value");
        value = value + " " + oddId + " ";
        $("#oddselection").prop("value", value);

        var nOdds = parseInt($("#nOdds").text());
        nOdds = nOdds + 1;
        $("#nOdds").text(nOdds);

        $.post("/BookMaker/odds",
                {to: "ticket",
                 p18: oddId},
        function (data) {
            $("#ticket").append(data);
        }
        );
    } else {
        var value = $("#oddselection").prop("value");
        value = value.replace(" " + oddId + " ", "");
        $("#oddselection").prop("value", value);

        var nOdds = parseInt($("#nOdds").text());
        nOdds = nOdds - 1;
        $("#nOdds").text(nOdds);

        var divId = "#d" + oddId;
        $(divId).remove();
    }
    var vRisk = $("#risk").prop("value");
    if (vRisk !== null) {
        $("#btn_risk").click();
    } else {
        $("#btn_profit").click();
    }
    });
});


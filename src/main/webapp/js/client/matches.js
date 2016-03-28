
$(document).ready(function () {

$(":checkbox").off('change');
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

        $.post("odds",
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
    if (vRisk !== null && vRisk !== "") {
        $("#btn_risk").click();
    }
    });
});


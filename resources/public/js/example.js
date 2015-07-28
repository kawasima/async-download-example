$(function() {

    function pollDownload(id) {
        $.ajax({
            type: "GET",
            url: "request-download/" + id,
            dataType: "json",
            success: function(msg) {
                console.log(msg);
                if (msg.status == "working") {
                    startPolling(id, msg.delay);
                } else if (msg.status == "done") {
                    location.href = msg.url;
                    $("#btn-download").removeClass("loading");
                    $("#btn-download+.progress").css("visibility", "hidden");
                }
            }
        });
    }
    
    function startPolling(id, delay) {
        var remaining = delay;
        var timerId = 0;
        timerId = setInterval(function() {
            remaining = remaining - 1;
            if (remaining <= 0) {
                pollDownload(id);
                clearInterval(timerId);
            }
            $("#btn-download+.progress span").html(remaining);
        }, 1000);
        $("#btn-download+.progress span").html(remaining);
        $("#btn-download+.progress").css("visibility", "visible");
    }
    
    $("#btn-download").click(function(e) {
        $.ajax({
            type: "POST",
            url: "request-download",
            dataType: "json",
            success: function(msg) {
                startPolling(msg.id, msg.delay);
                $(e.target).addClass("loading");
            }
        });

    });
});

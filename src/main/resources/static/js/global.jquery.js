jQuery.fn.extend({
    trackClick: function (id) {
        var getUrl = window.location;
        var baseUrl = getUrl .protocol + "//" + getUrl.host + "/" + getUrl.pathname.split('/')[1];
        var url = baseUrl + '/track-click?id=' + id;

        $.ajax({
            url: url
        });
    }
});
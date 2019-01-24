jQuery.fn.extend({
  trackClick: function (id) {
    $.ajax({url: '/track-click?id=' + id});
  },

  changePageSize: function () {
    var pageSize = $('#page-size').val();
    var location = window.location.href;

    if (location.indexOf('size=') !== -1) {
      location = window.location.href.replace(/(page=)(\d+)/g, 'page=0');
      location = location.replace(/(size=)(\d+)/g, 'size=' + pageSize);
    } else {
      location += (location.indexOf('?') !== -1) ? '&' : '?';
      location += 'page=0&size=' + pageSize;
    }

    window.location = location;

    return false;
  },

  updatePageSize: function () {
    var urlParams = {};
    var match;
    var search = /([^&=]+)=?([^&]*)/g;
    var decode = function (s) {
      return decodeURIComponent(s.replace(/\+/g, " "));
    };
    var query = window.location.search.substring(1);

    while (match = search.exec(query)) {
      urlParams[decode(match[1])] = decode(match[2]);
    }

    var pageSize = urlParams['size'] != null ? urlParams['size'] : 25;

    $("#page-size").val(pageSize);
  }
});

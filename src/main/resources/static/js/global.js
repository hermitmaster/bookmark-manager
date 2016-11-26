function confirmDelete() {
    return confirm("Are you sure you wish to delete this bookmark?\nThis action cannot be undone.");
}

function confirmDeleteAll() {
    return confirm("Are you sure you wish to delete all dead bookmarks?\nThis action cannot be undone.");
}

function confirmApproval() {
    return confirm("Are you sure you wish to approve all In-Review bookmarks?\nThis action cannot be undone.");
}

function confirmImport() {
    return confirm("Are you sure?\nThis will destroy all current bookmark data in the database.");
}

function confirmAddBookmark() {
    return confirm("Are you sure?\nYou cannot make any changes once submitted.");
}

function loadBookmarkDetails(id) {
    var getUrl = window.location;
    var baseUrl = getUrl .protocol + "//" + getUrl.host + "/" + getUrl.pathname.split('/')[1];
    var url = baseUrl + '/bookmark-details?id=' + id;

    $("#bookmark-details-panel").load(url);
}
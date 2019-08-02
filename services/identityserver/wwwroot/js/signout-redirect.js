window.addEventListener("load", function () {
	setTimeout(function() {
        var a = document.querySelector("a.PostLogoutRedirectUri");
        if (a) {
            window.location = a.href;
        }
    }, 3000);
});

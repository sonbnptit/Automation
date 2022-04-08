jQuery(document).ready(function () {
    jQuery("td.SUCCESS-text > a").click(function (e) {
         e.preventDefault();
     });
    jQuery("td.SUCCESS-text > a > img").attr("href", "#");

    jQuery("tr.test-SUCCESS").each(function (index) {
        if(jQuery(this).find("td.step-icon > a[onclick*='toggleDiv']").length > 0){
            jQuery(this).find(".SUCCESS-text").css("display", "none");
        }
    });
});
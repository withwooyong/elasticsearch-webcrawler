$(function(){

    $('#aside').height($(document).height());

    $('#aside_gnb_list li a').each(function(i, item) {
        var href = $(item).attr('href');
        if(href && location.href.match(href)) {
            $(item).parent('li').addClass('active');
            $(item).parents('li').addClass('active');
        }
    });

    var collapseAside = function() {
        var isCollapse = false;
        $('#aside_toggle a').on({
            'click': function(e) {
                isCollapse = !isCollapse;
                if(isCollapse) {
                    $('body').addClass('aside_collapse');
                } else {
                    $('body').removeClass('aside_collapse');
                }
                e.preventDefault();
            }
        })
    };

    var expandGnb = function() {
        $('#aside_gnb_list > li > a').click(function(e) {
            var current = $(e.target).parent();
            var isExpand = $(current).hasClass('active');

            if(isExpand) {
                $(current).removeClass('active');
            } else {
                $(current).addClass('active');
            }
        });
    };

    collapseAside(); //aside toggle
    expandGnb(); //expand gnb sub

});
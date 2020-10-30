/**
 *
 */
$('#opener_a').on('click',function(){
	window.opener.location.reload(),window.close();
});

$(window).on("beforeunload", function() {
   window.opener.location.reload(),window.close();
})
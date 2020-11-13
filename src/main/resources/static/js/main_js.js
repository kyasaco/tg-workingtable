/**
 *
 */
var times = document.getElementsByClassName('month_worktime');
var  left_time = parseFloat(times[0].textContent)
var  right_time = parseFloat(times[1].textContent)
if(left_time > right_time){
	times[0].style.color = 'red';
}

$(window).scroll(function() {
  sessionStorage.scrollTop = $(this).scrollTop();
});

$(document).ready(function() {
  if (sessionStorage.scrollTop != "undefined") {
    $(window).scrollTop(sessionStorage.scrollTop);
  }
});
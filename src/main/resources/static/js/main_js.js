/**
 *
 */
var times = document.getElementsByClassName('month_worktime');
var  left_time = parseFloat(times[0].textContent)
var  right_time = parseFloat(times[1].textContent)
if(left_time > right_time){
	times[0].style.color = 'red';
}
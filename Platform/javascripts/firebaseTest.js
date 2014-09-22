window.onload = function(){
var fb = new Firebase('https://henry371.firebaseio.com');
document.getElementById("setValue").addEventListener('click',function(event){
var newVal = document.getElementById("firebaseValue").value
fb.set({test:newVal})
})
//LISTEN FOR REALTIME CHANGES
fb.on("value", function(data) {
  var received = data.val() ? data.val().test : "";
  document.getElementById("testValue").innerHTML = document.getElementById("testValue").innerHTML + "<br/>" + received

});
}

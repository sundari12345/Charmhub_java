
document.addEventListener("DOMContentLoaded", function() {
	 checkLogin();
});

function checkLogin(){
	console.log("check login");
	var xhr = new XMLHttpRequest();
    var logout = "0";
  
    xhr.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            var response = this.responseText;
                if(response == logout){
					window.location.href = "index.html";
				}
            } 
    };

    xhr.open("GET", "http://localhost:8080/CharmHub/com.login.Addservlet", true);
    xhr.send();
}

var login = document.getElementById("showLogIn");

function CallalreadyAccount() {
	var log = document.getElementById("log");
	var sign = document.getElementById("sign");
	if(log.style.display === "none"){
		sign.style.display = "none";
		log.style.display = "block";
		log.style.display = "flex";
	}
	else{
		log.style.display = "none";
	}
}

function callLogin() {

    var emailId = document.getElementById("email2").value;
    var password = document.getElementById("password2").value;
    var phoneno = document.getElementById("phoneno2").value;
    var xhr = new XMLHttpRequest();
    var welcome = "welcome";
    var wrong = "wrong password";
  
    xhr.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            var response = this.responseText;
                var manager = 2+"";
                if(response == manager){
					console.log(response);
					window.location.href = "index.html";
				}else if (response === welcome) {
                    window.location.href = "index.html";
                } else if (response === wrong) {
					document.getElementById("email2").value = "";
   					document.getElementById("password2").value = "";
   					document.getElementById("phoneno2").value = "";
                    alert(wrong);
                } else {
					document.getElementById("email2").value = "";
   					document.getElementById("password2").value = "";
   					document.getElementById("phoneno2").value = "";
                    alert("Something went wrong!! "+response);
                }
            } 
            
    };

    xhr.open("POST", "http://localhost:8080/CharmHub/com.login.Addservlet", true);
    xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhr.send("emailId=" + emailId + "&password=" + password + "&phoneno=" + phoneno);
}

function callSignUp() {

    var emailId = document.getElementById("email1").value;
    var password = document.getElementById("password1").value;
    var phoneno = document.getElementById("phoneno1").value;
    var xhr = new XMLHttpRequest();
    var welcome = "welcome";
    var wrong = "wrong password";
    
    console.log("is it work?");
    xhr.onreadystatechange = function() {
		console.log("is it work?");
        if (this.readyState == 4 && this.status == 200) {
			console.log("is it work?");
            var response = this.responseText;
            
                if (response === welcome) {
					window.location.href = "index.html";
                    alert(welcome);
                } else if (response === wrong) {

                    document.getElementById("email1").value = "";
   					document.getElementById("password1").value = "";
   					document.getElementById("phoneno1").value = "";
   					alert("This account is already exist");
   					
                } else {
                    
                    document.getElementById("email1").value = "";
   					document.getElementById("password1").value = "";
   					document.getElementById("phoneno1").value = "";
   					alert("Something went wrong!! "+response);
                }
            } 
    };

    xhr.open("POST", "http://localhost:8080/CharmHub/com.login.CreateAccount", true);
    xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhr.send("emailId=" + emailId + "&password=" + password + "&phoneno=" + phoneno);
}








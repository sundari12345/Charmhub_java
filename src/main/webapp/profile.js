/**
 * 
 */
document.addEventListener("DOMContentLoaded", function() {
	    getProfileContent();
});

 
 function profile(){
	var firstname = document.getElementById("firstname").textContent;
	var lastname = document.getElementById("lastname").textContent;
    var phoneno = document.getElementById("phoneno").textContent;
    var email = document.getElementById("email").textContent;
    var profile_id = document.getElementById("profile_id").value;
    var date = document.getElementById("DOB").textContent;
    var options = document.getElementsByClassName("gender");
    var gender;
    console.log(date);
    for (var i = 0; i < options.length; i++) {
		console.log(options[i]);
      if (options[i].checked) {
         gender = options[i].value;
         console.log(gender)
         break;
      }
    }
    var xhr = new XMLHttpRequest();
    var jsonObject = {
		"profile_id": profile_id,
		"firstname": firstname,
		"lastname": lastname,
		"gender": gender,
		"phoneno": phoneno,
		"email": email,
		"DOB": date,
	}
    console.log(jsonObject)
    xhr.onreadystatechange = function() {
		console.log("is it work?");
        if (this.readyState == 4 && this.status == 200) {
			console.log("is it work?");
            var response = this.responseText;
            alert(response);
            console.log(response);
        } 
    };

    xhr.open("POST", "http://localhost:8080/CharmHub/com.user.ProfileServlet", true);
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send(JSON.stringify(jsonObject));
 }
 
 function getProfileContent(){
	var xhr = new XMLHttpRequest();
	
    console.log("is it works");
    xhr.onreadystatechange = function() {
		console.log("is it work?");
        if (this.readyState == 4 && this.status == 200) {
			console.log("is it work?");
            var response = this.responseText;
            console.log("Response from server:", response); 
            var empty = "";
            if(response != empty){
				document.getElementById("my_profile_container").style.display = "flex";
	            try {
		            var json = JSON.parse(response);
		            console.log(json);
		            document.getElementById("firstname").innerText = json["firstname"];
					document.getElementById("lastname").innerText = json["lastname"];
				    document.getElementById("phoneno").innerText = json["phoneno"];
				    document.getElementById("email").innerText = json["email_id"];
				    document.getElementById("profile_id").innerText = json["profile_id"];
				    var dateList = json["DOB"].split("-");
				    var date = dateList[1]+"/"+dateList[2]+"/"+dateList[0];
				    document.getElementById("DOB").innerText = date;
				    
				    var options = document.getElementsByClassName('gender');
				    var gender = json["gender"];
				
				    for (var i = 0; i < options.length; i++) {
				      if (options[i].value == gender) {
				         options[i].checked = true;
				         break;
				      }
				    }
		            console.log(response);
		         }catch (error) {
	                console.error("Error parsing JSON:", error);
	            }
	         } else{
				 document.getElementById("my_profile_container").style.display = "none";
			 }
        } 
    };
     
     
     
    xhr.open("GET", "http://localhost:8080/CharmHub/com.user.ProfileServlet", true);
    xhr.send();
 }
 
 function makeEditable(element) {
     element.contentEditable = "true";
 }
 
 
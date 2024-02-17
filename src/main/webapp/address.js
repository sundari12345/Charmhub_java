/**
 * 
 */

document.addEventListener("DOMContentLoaded", function() {
	    getAddressContent();
});

 function getAddressContent(){
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
				document.getElementById("my_address_container").style.display = "flex";
	            try {
		            var json = JSON.parse(response);
		            console.log(json);
		            document.getElementById("address").innerText = json["address"];
					document.getElementById("landmark").innerText = json["landmark"];
				    document.getElementById("pincode").innerText = json["pincode"];
				    document.getElementById("locality").innerText = json["locality"];
				    document.getElementById("state").innerText = json["state"];
				    document.getElementById("city").innerText = json["city"];
				    document.getElementById("user_id").innerText = json["user_id"];
		            console.log(response);
		         }catch (error) {
	                console.error("Error parsing JSON:", error);
	            }
	         } else{
				 document.getElementById("my_address_container").style.display = "none";
			 }
        } 
    };
    
    xhr.open("GET", "http://localhost:8080/CharmHub/com.user.AddressServlet", true);
    xhr.send();
 }
 
 function address(){
	var address = document.getElementById("address").textContent;
	var landmark = document.getElementById("landmark").textContent;
    var pincode = document.getElementById("pincode").textContent;
    var locality = document.getElementById("locality").textContent;
    var city = document.getElementById("city").textContent;
    var state = document.getElementById("state").textContent;
    var user_id = document.getElementById("user_id").value;
    
    var xhr = new XMLHttpRequest();
    var jsonObject = {
		"address": address,
		"landmark": landmark,
		"pincode": pincode,
		"locality": locality,
		"state": state,
		"city": city,
		"user_id": user_id,
	}
    console.log(jsonObject)
    xhr.onreadystatechange = function() {
		console.log("is it work?");
        if (this.readyState == 4 && this.status == 200) {
			console.log("is it work?");
            var response = this.responseText;
            console.log(response);
            alert(response);
        } 
    };

    xhr.open("POST", "http://localhost:8080/CharmHub/com.user.AddressServlet", true);
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send(JSON.stringify(jsonObject));
 }
 
 function makeEditable(element) {
     element.contentEditable = "true";
 }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
/**
 * 
 */
document.addEventListener("DOMContentLoaded", function() {
	insertBrandImage()
	customizeUI();
});

function insertBrandImage(){
	
	console.log("get brand");
	var xhr = new XMLHttpRequest();
    var brandContainer = document.getElementById("brand_visual");
  
    xhr.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            var response = this.responseText;
                var allBrandsWithNameAndImage = JSON.parse(response);
                var listOfBrand = allBrandsWithNameAndImage["allBrand"].split("~");
                var brandWithoutRepitation = new Set();
                listOfBrand.pop();
                listOfBrand.forEach((brandName) => {
					brandWithoutRepitation.add(brandName);
				})
				brandWithoutRepitation.forEach((brandName) => {
					var imageName = allBrandsWithNameAndImage[brandName];
					var element = document.createElement('div')
					element.classList.add("brand_images");
					element.setAttribute("brand_name", brandName);
					element.style.backgroundImage = 'url(Image/' + imageName + ')';
					brandContainer.appendChild(element);
				})
            } 
    };

    xhr.open("GET", "http://localhost:8080/CharmHub/com.user.productView", true);
    xhr.send();
	
}


function getAllBrands(){
	console.log("get brand");
	var xhr = new XMLHttpRequest();
  
    xhr.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            var response = this.responseText;
                if(response == ""){
					var productDetails = JSON.parse(response)
				}
            } 
    };

    xhr.open("GET", "http://localhost:8080/CharmHub/com.login.Addservlet", true);
    xhr.send();
}



function customizeUI(){
	var xhr = new XMLHttpRequest();
	
	var login = 1+"";
    console.log("is it works");
    xhr.onreadystatechange = function() {
		console.log("is it work?");
        if (this.readyState == 4 && this.status == 200) {
			var response = this.responseText;
			console.log(this.responseText)
            console.log(response);
            var manager = 2+"";
			var customer = 4+"";
                if (response === manager) {
					console.log("manager role");
                    var customer = document.getElementById("homeIcon");
					customer.style.display = "none";
					var customize = document.getElementById("customize");
					customize.style.display = "flex";
					var logIn = document.getElementById("changeToLogout")
					logIn.innerText = "Log out";
                } 
                else if(response === customer){
					console.log("customer role");
                    var customer = document.getElementById("homeIcon");
					customer.style.display = "flex";
					var customize = document.getElementById("customize");
					customize.style.display = "none";
					var logIn = document.getElementById("changeToLogout")
					logIn.innerText = "Log out";
				}
				else if(response === login){
					console.log(response);
					var customer = document.getElementById("homeIcon");
					customer.style.display = "flex";
					var customize = document.getElementById("customize");
					customize.style.display = "none";
					var logIn = document.getElementById("changeToLogout")
					logIn.innerText = "Log in";
				}
			
		}
	}
	
	xhr.open("GET", "http://localhost:8080/CharmHub/com.user.role", true);
    xhr.send();
}


function showThisBrandProduct(element){
	
	var brand_name = element.getAttribute("brand_name");
	console.log("get brand");
	var xhr = new XMLHttpRequest();
    
  
    xhr.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            var response = this.responseText;
                if(response == logout){
					window.location.href = "index.html";
				}
            } 
    };
	
	xhr.open("GET", "http://localhost:8080/CharmHub/com.user.role?brand_name=" + encodeURIComponent(brand_name), true);
    xhr.send();
	
}










var homeIcon = document.getElementById("homeIcon");
var customize = document.getElementById("customize");

homeIcon.addEventListener('mouseover', function() {
        document.getElementById("features").style.display = 'flex';
});

customize.addEventListener('mouseover', function() {
        document.getElementById("features").style.display = 'flex';
});

var homeIcon = document.getElementById("features");

homeIcon.addEventListener('mouseover', function() {
        document.getElementById("features").style.display = 'flex';
});

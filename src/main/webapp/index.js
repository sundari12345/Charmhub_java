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
					
					var imgData = imageName.split("~");
					var image = 'data:image/'+imgData[1]+';base64,' + imgData[0];
					element.style.backgroundImage = 'url('+image+')';
					if (document.readyState === "complete") {
						brandContainer.appendChild(element);
					}
					element.addEventListener('click', function(event) {
    					event.preventDefault(); // Prevent default behavior (e.g., navigating to another page)
    					var capturedElement = element;
    					var attributeValue = capturedElement.getAttribute('brand_name');
    					var attributeValue = element.getAttribute('brand_name'); // Replace 'yourAttributeName' with the name of the attribute you want to retrieve
    					
    					showThisBrandProduct(attributeValue);
    					
					});	
					
				})
            } 
    };

    xhr.open("GET", "http://localhost:8080/CharmHub/com.user.productView?brand_name=" + encodeURIComponent("All"), true);
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
			console.log(this.responseText, "who is this?")
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
	
	document.getElementById("brand_visual").style.display = "none";
    document.getElementById("topBrands").style.display = "none";
	document.getElementById("showBrand").style.display = "flex";
	
	var brand_name = element;
	console.log("get product");
	var xhr = new XMLHttpRequest();
    
  
    xhr.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            var response = this.responseText;
                if(response != ""){
					var productDetails = JSON.parse(response);
					console.log(productDetails);
					var showBrand = document.getElementById("showBrand");
    				document.getElementById("showBrandname").innerText = brand_name;
					var category = productDetails["allCategory"].split("~");
					category.pop()
					
					var roleId = productDetails.role;
					var wishListProducts = productDetails["wishListProducts"].split("~");
					console.log(wishListProducts, "this wishList id");
					
					category.forEach((categoryname) => {
						var specificProductContainer = document.createElement('div');
						specificProductContainer.classList.add("eachProductContainer");
						var heading = document.createElement('span');
						heading.classList.add("categoryHeading", "category_heading")
						heading.innerText = categoryname;
						specificProductContainer.appendChild(heading);
						console.log(categoryname);
						var eachProductJson = productDetails[categoryname];
						console.log(eachProductJson);
						//console.log(JSON.parse(eachProductJson));
						var eachProduct = eachProductJson["allProduct"].split("~");

						eachProduct.pop();
						console.log(eachProduct)
						eachProduct.forEach((product) => {
							console.log(product);
							var productJson = JSON.parse(eachProductJson[product]);
			
							var productContainer = document.createElement('div');
							productContainer.classList.add("productImageContainer");
							productContainer.setAttribute("product_id", productJson["product_id"]);
							
							var wishList = document.createElement('span');
							wishList.classList.add("myAccount_icons","wishList");
							productContainer.appendChild(wishList);	
							
							var heartIcon = document.createElement('i');
							var productId = productJson["product_id"]+"";
							console.log(productId, productId);
							if(wishListProducts.includes(productId)){
								heartIcon.classList.add("fa-solid", "fa-heart", "myAccount_icons");
							}else{
								heartIcon.classList.add("fa-regular", "fa-heart", "myAccount_icons");
							}
							
							
							heartIcon.style.color = "#ffffff";
							
							heartIcon.addEventListener("click", function() {
								var customer = "4";
								var noLogin = "0";
								if(roleId == customer)
							    	addToWishList(productJson["product_id"], heartIcon, roleId); 
							    else if(roleId == noLogin)
							    	alert("Please Login!!");
							    else
							    	alert("Your are not a valid Customer!!");		
							});
							wishList.appendChild(heartIcon);

							var productImage = document.createElement('div');
							productImage.addEventListener("click", function(){
								showProductDetails(productContainer);
							})
							productImage.classList.add("productImageHolder");
							var imageName = productJson["product_image"];
							var imgData = imageName.split("~");
							var image = 'data:image/'+imgData[1]+';base64,' + imgData[0];
							productImage.style.backgroundImage = 'url('+image+')';
							
							productContainer.appendChild(productImage);
							
							var productName = document.createElement('span');
							productName.classList.add("productDetailsField");
							productName.innerText = productJson["product_name"];
							productContainer.appendChild(productName);
							
							var productPrice = document.createElement('span');
							productPrice.classList.add("productDetailsField");
							productPrice.innerText = "₹ " + productJson["product_price"]
							productContainer.appendChild(productPrice);
							
							var productRatting = document.createElement('span');
							productRatting.classList.add("productDetailsField", "iconSpan");
							var iElement1 = document.createElement("i");
							iElement1.style="color: #f8ffff";
							iElement1.classList.add("fa-regular", "fa-star", "viewRatting");
							productRatting.appendChild(iElement1);
							var iElement2 = document.createElement("i");
							iElement2.style="color: #f8ffff";
							iElement2.classList.add("fa-regular", "fa-star", "viewRatting");
							productRatting.appendChild(iElement2);
							var iElement3 = document.createElement("i");
							iElement3.style="color: #f8ffff";
							iElement3.classList.add("fa-regular", "fa-star", "viewRatting");
							productRatting.appendChild(iElement3);
							var iElement4 = document.createElement("i");
							iElement4.style="color: #f8ffff";
							iElement4.classList.add("fa-regular", "fa-star", "viewRatting");
							productRatting.appendChild(iElement4);
							var iElement5 = document.createElement("i");
							iElement5.style="color: #f8ffff";
							iElement5.classList.add("fa-regular", "fa-star", "viewRatting");
							productRatting.appendChild(iElement5);
							productContainer.appendChild(productRatting);
							specificProductContainer.appendChild(productContainer);
							
						})
						
						showBrand.appendChild(specificProductContainer);
					})
				}
            } 
    };
	
	xhr.open("GET", "http://localhost:8080/CharmHub/com.user.productView?brand_name=" + encodeURIComponent(brand_name), true);
    xhr.send();
	
}

function addToWishList(product_id, element){
	var xhr = new XMLHttpRequest();
	xhr.onreadystatechange = function() {
		console.log("wishList");
        if (this.readyState == 4 && this.status == 200) {
			var response = this.responseText;
			var success = "100"; 
			if(response == success){
				element.classList.remove("fa-regular")
				element.classList.add("fa-solid");
			}
			else{
				element.classList.add("fa-regular")
				element.classList.remove("fa-solid");
			}
				
		}
	}
	xhr.open("POST", "http://localhost:8080/CharmHub/com.customer.wishListServlet", true);
    xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhr.send("product_id="+product_id + "&isInWishList=" + isInWishList(element));
}

function addProductToCart(product_id){
	var xhr = new XMLHttpRequest();
	xhr.onreadystatechange = function() {
		
        if (this.readyState == 4 && this.status == 200) {
			var response = this.responseText;
			var success = "100"
			if(response == success){
				alert("Product added sucessfully!!");
				window.location.href = "cart.html";
					
			}
			else{
				alert("Product is not added");
			}
		}
	}
	xhr.open("POST", "http://localhost:8080/CharmHub/com.customer.cartServlet", true);
    xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhr.send("product_id="+product_id);
}

function isInWishList(element) {
    if (element.classList.contains("fa-solid")) {
        return true
    } else {
        return false;
    }
}



		function showProductDetails(element){
			document.getElementById("brand_visual").style.display = "none";
		    document.getElementById("topBrands").style.display = "none";
			document.getElementById("showBrand").style.display = "none";
			document.getElementById("charmhub_poster-id").style.display = "none";
			
			var product_id = element.getAttribute("product_id");
			console.log("get product  ", product_id);
			var xhr = new XMLHttpRequest();
		    
		  
		    xhr.onreadystatechange = function() {
		        if (this.readyState == 4 && this.status == 200) {
					var product = JSON.parse(this.responseText);
					
					console.log(product);
					
					var productWholeContainer = document.createElement('section');
					productWholeContainer.classList.add("myAccountPage");
					productWholeContainer.classList.add("myProfilePage");
					
					var intro = document.createElement('span');
					intro.classList.add('feeds_heading-navigation');
					intro.innerText = product.category_name + " > " + product.brand_name;
					
					productWholeContainer.appendChild(intro);
					
					var productSpecificDetailscon = document.createElement('div');
					productSpecificDetailscon.classList.add('productSpecificDetails');
					
					var productSpecificDetailsimg = document.createElement('div');
					productSpecificDetailsimg.classList.add('productSpecificDetailsimg');
					var imageName = product["product_image"];
					var imgData = imageName.split("~");
					var image = 'data:image/'+imgData[1]+';base64,' + imgData[0];
					productSpecificDetailsimg.style.backgroundImage = 'url('+image+')';
					productSpecificDetailscon.appendChild(productSpecificDetailsimg);
					
					var productSpecificDetailsholder = document.createElement('div');
					productSpecificDetailsholder.classList.add("productSpecificDetailsholder");
					var productSpecificDetailsname = document.createElement('span');
					productSpecificDetailsname.innerText = product.product_name
					
					var productSpecificDetailsprice = document.createElement('span');
					productSpecificDetailsprice.innerText = "₹ "+product.product_price;
					productSpecificDetailsholder.appendChild(productSpecificDetailsname);
					productSpecificDetailsholder.appendChild(productSpecificDetailsprice);
					
					var buttonContainer = document.createElement('div');
					var buyNow = document.createElement('button');
					var addToCart = document.createElement('button');
					
					addToCart.addEventListener('click', function(){
						addProductToCart(product["product_id"]);
					})
					
					
					buttonContainer.appendChild(buyNow);
					buyNow.innerText = "Buy Now";
					addToCart.innerText = "Add to Cart";
					buttonContainer.appendChild(addToCart);
					buttonContainer.classList.add("buttonContainer");
					buyNow.classList.add("buttonCommon");
					addToCart.classList.add("buttonCommon");
					productSpecificDetailsholder.appendChild(buttonContainer);
					
					
					productSpecificDetailscon.appendChild(productSpecificDetailsholder);
					productWholeContainer.appendChild(productSpecificDetailscon);
					
					var category = document.createElement('div');
					category.classList.add("specificationCon");
					var categoryHeading = document.createElement('span')
					categoryHeading.innerText = "Category";
					categoryHeading.classList.add("specificationHeading");
					var categoryHeadingcontent = document.createElement('span')
					categoryHeadingcontent.innerText = product.category_name
					category.appendChild(categoryHeading);
					category.appendChild(categoryHeadingcontent);
					productWholeContainer.appendChild(category);
					
					var Brand = document.createElement('div');
					Brand.classList.add("specificationCon");
					var brandHeading = document.createElement('span')
					brandHeading.innerText = "Brand"
					brandHeading.classList.add("specificationHeading");
					var brandHeadingcontent = document.createElement('span')
					brandHeadingcontent.innerText = product.brand_name;
					Brand.appendChild(brandHeading);
					Brand.appendChild(brandHeadingcontent);
					productWholeContainer.appendChild(Brand);
					
					var description = document.createElement('div');
					description.classList.add("specificationCon");
					var descriptionHeading = document.createElement('span')
					descriptionHeading.innerText = "Overview"
					descriptionHeading.classList.add("specificationHeading");
					var descriptionHeadingcontent = document.createElement('span')
					descriptionHeadingcontent.innerText = product.product_discription;
					description.appendChild(descriptionHeading);
					description.appendChild(descriptionHeadingcontent);
					productWholeContainer.appendChild(description);
					console.log(productWholeContainer);
					
					document.getElementById("section").appendChild(productWholeContainer);
					


				}
		    }
		    xhr.open("GET", "http://localhost:8080/CharmHub/com.user.productDetails?product_id=" + product_id, true);
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

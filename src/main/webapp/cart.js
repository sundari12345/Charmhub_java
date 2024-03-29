document.addEventListener("DOMContentLoaded", function() {
	 showCartProducts();
});

 function showCartProducts(){

	var xhr = new XMLHttpRequest(); 
  
    xhr.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            var response = this.responseText;
                if(response != ""){
					var cartJosn = JSON.parse(response);
					var cartDetails = cartJosn.productQuantity.split("~");
					var element = document.getElementById("cartCon");
					cartView(cartDetails, cartJosn, element);
				}
            } 
    };
	
	xhr.open("GET", "http://localhost:8080/CharmHub/com.customer.cartServlet", true);
    xhr.send();
	
}


function cartView(ListOfDetails, cartJosn, element){
	
		var productWholeContainer = document.createElement('section');
		productWholeContainer.classList.add("myAccountPage");
		productWholeContainer.classList.add("myProfilePage");
					
		var intro = document.createElement('span');
		intro.classList.add('feeds_heading-navigation');
		intro.innerText = "My Account" + " > " + "My Cart";			
		productWholeContainer.appendChild(intro);
		intro.style.marginBottom = "3.5%";
		
		element.appendChild(productWholeContainer);
		
		ListOfDetails.forEach((producQuantity) => {
					var productList  = producQuantity.split(",");
					var product = JSON.parse(cartJosn[productList[0]]);
				
			
			        var productSpecificDetailscon = document.createElement('div');
					productSpecificDetailscon.classList.add('productSpecificDetails');
					productSpecificDetailscon.style.marginBottom = "2%";
					productSpecificDetailscon.style.border = "1px solid #565657"
					productSpecificDetailscon.style.width = "86%"
					productSpecificDetailscon.style.borderRadius = "10px";
					var productSpecificDetailsimg = document.createElement('div');
					productSpecificDetailsimg.classList.add('productSpecificDetailsimg');
					productSpecificDetailsimg.style.height = "100%";
					var imageName = product["product_image"];
					var imgData = imageName.split("~");
					var image = 'data:image/'+imgData[1]+';base64,' + imgData[0];
					productSpecificDetailsimg.style.backgroundImage = 'url('+image+')';
					productSpecificDetailscon.appendChild(productSpecificDetailsimg);
					
					var productSpecificDetailsholder = document.createElement('div');
					productSpecificDetailsholder.classList.add("productSpecificDetailsholder");
					productSpecificDetailsholder.style.paddingTop = "3.5%";
					var productSpecificDetailsname = document.createElement('span');
					productSpecificDetailsname.innerText = product.product_name
					
					var productSpecificDetailsprice = document.createElement('span');
					productSpecificDetailsprice.innerText = "₹ "+product.product_price;
					
					var productSpecificDetailsquantity= document.createElement('span');
					productSpecificDetailsquantity.innerText = "Quantity: "+ productList[1];
					
					productSpecificDetailsholder.appendChild(productSpecificDetailsname);
					productSpecificDetailsholder.appendChild(productSpecificDetailsprice);
					productSpecificDetailsholder.appendChild(productSpecificDetailsquantity);
					
					
					
					productSpecificDetailscon.appendChild(productSpecificDetailsholder);
					productWholeContainer.appendChild(productSpecificDetailscon);
		})
}

document.addEventListener("DOMContentLoaded", function() {
	 showWishListProducts();
});

 function showWishListProducts(){

	var xhr = new XMLHttpRequest(); 
  
    xhr.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            var response = this.responseText;
           
                if(response != ""){
					var wishListJosn = JSON.parse(response);
					var wishListDetails = wishListJosn.wishListIds.split("~");
					var element = document.getElementById("wishListCon");
					wishListView(wishListDetails, wishListJosn, element);
					
				}
            } 
    };
	
	xhr.open("GET", "http://localhost:8080/CharmHub/com.customer.wishListServlet", true);
    xhr.send();
	
}


function wishListView(ListOfDetails, wishListJosn, element){
	
		var productWholeContainer = document.createElement('section');
		productWholeContainer.classList.add("myAccountPage");
		productWholeContainer.classList.add("myProfilePage");
					
		var intro = document.createElement('span');
		intro.classList.add('feeds_heading-navigation');
		intro.innerText = "My Account" + " > " + "My Wishlist";			
		productWholeContainer.appendChild(intro);
		intro.style.marginBottom = "3.5%";
		
		element.appendChild(productWholeContainer);
		
		ListOfDetails.forEach((productId) => {
			
					var product = JSON.parse(wishListJosn[productId]);
			
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
		})
}

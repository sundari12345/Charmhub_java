/**
 * 
 */

 function categoryContent(element) {
	 console.log("in the");	
	 var xhr = new XMLHttpRequest(); 
    var empty = "";
    xhr.onreadystatechange = function() {
		console.log("in the func");	
        if (this.readyState == 4 && this.status == 200) {
            var response = this.responseText;
            console.log(response);
            if(response != empty){
	            var json = JSON.parse(response);
	            var listOfCategory = json["listOfCategory"].split("~");
	            listOfCategory.pop();
	            addCategoryToDropdown(element, listOfCategory);    
	         }
              
         } 
    };

    xhr.open("GET", "http://localhost:8080/CharmHub/com.Product.add_brand", true);
    xhr.send();
}

function addBrand() {
	var xhr = new XMLHttpRequest(); 
	var category_name = document.getElementById("categoryDropDown").value
    var brand_name = document.getElementById("brand_name").value
    var fileInput = document.getElementById('img');
    var file = fileInput.files[0];
    var brand_image = file.name;
    uploadImage(file);
    var json = {
		"category_name": category_name,
		"brand_name": brand_name,
		"brand_image": brand_image,
	}
    
    xhr.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            var response = this.responseText;
            if(response == "100"){
	            alert("brand is added succesfully");
	         }
	         else{
				 alert("brand is not added");
			 }
              
         } 
    };

    xhr.open("POST", "http://localhost:8080/CharmHub/com.Product.add_brand", true);
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send(JSON.stringify(json));
}

function uploadImage(file){
	console.log(file);
	if (file) {
		console.log("upload");
                var formData = new FormData();
                formData.append('imageFile', file);

                fetch('/CharmHub/com.Product.commonImageUploader', {
                    method: 'POST',
                    body: formData
                })
                .then(response => {
					console.log(response);
                    if (response.ok) {
                        alert('Image uploaded successfully');
                    } else {
                        alert('Failed to upload image');
                    }
                })
                .catch(error => {
                    alert('Error:', error);
                });
            } else {
                alert('No file selected');
            }
	
}

function addCategoryToDropdown(element, content){
	element.innerHTML = '';
    content.forEach((category) => {
		var options = document.createElement('option')
		options.classList.add('optionsForCategory');
		options.text = category;
		element.add(options);
	})
}

function brandContent(){
	var category_name = document.getElementById("category").value;
	var xhr = new XMLHttpRequest(); 
	console.log(category_name);
    var json = {
		"category_name": category_name
	}
	console.log(json);
	var empty = "";
    xhr.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            var response = this.responseText;
            console.log(response);
            if(response != empty){
	            var json = JSON.parse(response);
	            var listOfBrand = json["listOfBrand"].split("~");
	            listOfBrand.pop();
	            var brandBox = document.getElementById("brand")
	            addCategoryToDropdown(brandBox, listOfBrand);    
	         }
              
         } 
    };

    xhr.open("GET", "http://localhost:8080/CharmHub/com.Product.Add_product?category_name=" + encodeURIComponent(category_name), true);
    xhr.send("category_name" + category_name);
	
}


function addCategory(){
	
	var xhr = new XMLHttpRequest(); 
	var category_name = document.getElementById("add_category").value
    
    var json = {
		"category_name": category_name,
	}
    
    xhr.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            var response = this.responseText;
            if(response == "100"){
	            alert("category is added succesfully");
	         }
	         else{
				 alert("Category is not added");
			 }
              
         } 
    };

    xhr.open("POST", "http://localhost:8080/CharmHub/com.Product.Add_Category", true);
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send(JSON.stringify(json));
    
}

function addProduct(){
	
	var xhr = new XMLHttpRequest(); 
	var category_name = document.getElementById("category").value
    var brand_name = document.getElementById("brand").value;
    var product_name = document.getElementById("name").value;
    var product_price = document.getElementById("price").value;
    var discription = document.getElementById("discription").value;
    var fileInput = document.getElementById('image');
    var file = fileInput.files[0];
    var product_image = file.name;
    uploadImage(file);
    var json = {
		"category_name": category_name,
		"brand_name": brand_name,
		"product_name": product_name,
		"product_price": product_price,
		"product_image": product_image,
		"discription": discription,
	}
    
    xhr.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            var response = this.responseText;
            console.log(response)
            if(response != "-1" && response != "0"){
	            alert("product is added succesfully");
	         }
	         else if(response != "-1"){
				 alert("There is no such category found");
			 }
			 else if(response != "0"){
				 alert("There is no such brand found");
			 }
              
         } 
    };

    xhr.open("POST", "http://localhost:8080/CharmHub/com.Product.Add_product", true);
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.send(JSON.stringify(json));
    
}

function showcategory(){
	var product = document.getElementById("show_product");
	var brand = document.getElementById("show_brand");
	var category = document.getElementById("show_category");
	
		product.style.display = "none";
		brand.style.display = "none";
		category.style.display = "flex";
	
}

function showproduct(){
	var product = document.getElementById("show_product");
	var brand = document.getElementById("show_brand");
	var category = document.getElementById("show_category");

		product.style.display = "flex";
		brand.style.display = "none";
		category.style.display = "none";
		
	var element = document.getElementById("category");
		categoryContent(element);
	
}

function showbrand(){
	var product = document.getElementById("show_product");
	var brand = document.getElementById("show_brand");
	var category = document.getElementById("show_category");

		product.style.display = "none";
		brand.style.display = "flex";
		category.style.display = "none";
		
	var element = document.getElementById("categoryDropDown");
		categoryContent(element);
	
}













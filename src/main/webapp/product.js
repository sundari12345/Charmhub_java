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
    
    var json = {
		"category_name": category_name,
		"brand_name": brand_name,
		"brand_image": brand_image,
	}
    
    xhr.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            var response = this.responseText;
            if(response != "50"){
	            alert("brand is added succesfully");
	            uploadImage(file, response);
	            document.getElementById("categoryDropDown").value = "";
     			document.getElementById("brand_name").value = "";
   				document.getElementById('img').value = "";
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

function uploadImage(file, response){
	console.log(file);
	console.log(response);
	if (file) {
		console.log("upload");
                var formData = new FormData();
                formData.append('imageFile', file);
                formData.append('brand_id', response);

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
	
	var options = document.createElement('option')
	options.classList.add('optionsForCategory');
	options.text = "Select an option";
	element.add(options);
	
    content.forEach((category) => {
		options = document.createElement('option')
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
	            document.getElementById("add_category").value = "";
	         }
	         else{
				 
				 alert("Category is not added");
				 document.getElementById("add_category").value = "";
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
	            uploadImage(file);
	            document.getElementById("category").value
    			document.getElementById("brand").value = "";
    			document.getElementById("name").value = "";
    			document.getElementById("price").value = "";
    			document.getElementById("discription").value = "";
  				document.getElementById('image').value = "";
	         }
	         else if(response != "-1"){
				 alert("There is no such category found");
				 document.getElementById("category").value = "";
    			 document.getElementById("brand").value = "";
    			 document.getElementById("name").value = "";
    			 document.getElementById("price").value = "";
    			 document.getElementById("discription").value = "";
  				 document.getElementById('image').value = "";
			 }
			 else if(response != "0"){
				 alert("There is no such brand found");
				 document.getElementById("category").value = "";
    			 document.getElementById("brand").value = "";
    			 document.getElementById("name").value = "";
    			 document.getElementById("price").value = "";
    			 document.getElementById("discription").value = "";
  				 document.getElementById('image').value = "";
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













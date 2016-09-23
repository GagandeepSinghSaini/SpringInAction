<!DOCTYPE html>
<html >
  <head>
    <meta charset="UTF-8">
    <title>Coupon</title>
    <link href='http://fonts.googleapis.com/css?family=Titillium+Web:400,300,600' rel='stylesheet' type='text/css'>
    
    <link rel="stylesheet" href="bootstrap-3.3.2-dist/css/normalize.css">
    
        <link rel="stylesheet" href="bootstrap-3.3.2-dist/css/style.css">
<script src="bootstrap-3.3.2-dist/jquery/jquery-3.1.0.min.js"></script>
<script src="bootstrap-3.3.2-dist/jquery/jquery-ui.min.js"></script>
<script type="text/javascript">
function selectCouponMethod(select) {
	var couponOption = select.options[select.selectedIndex].text;
	if(couponOption === '47') {
		document.getElementById("discValue").style.visibility = "hidden";
		document.getElementById("optradio").style.visibility = "hidden";
		document.getElementById("requiredFlag").style.visibility = "hidden";
		$('#discValue').removeAttr('required');
	}else {
		document.getElementById("discValue").style.visibility = "visible";
		document.getElementById("optradio").style.visibility = "visible";
		document.getElementById("requiredFlag").style.visibility = "visible";
	}
	
}
</script>

     <script type="text/javascript">
     jQuery(function($) {
		    $( "#datetimepicker1" ).datepicker({
		    	dateFormat: 'yy-MM-dd',
		    	beforeShow: function(){    
		    	           $(".ui-datepicker").css('font-size', 20); 
		    	    }
		    });
		    $("#datetimepicker2").datepicker({
				dateFormat: 'yy-MM-dd',
				beforeShow: function(){    
	    	           $(".ui-datepicker").css('font-size', 20); 
	    	    }
            });
		  });     
  
 </script>
    <style>
    .select_style {
    overflow:hidden;
}
.select_style select {
    -webkit-appearance: none;
    appearance:none;
    width:48%;
    border: 1px solid #a0b3b0;
    color: #EEE;
    border-color: #1ab188;
    background:rgba(19, 35, 47, 0.9);
    border:none;
    outline:none;
    height:40px;
}

.select_style1 select {
    -webkit-appearance: none;
    appearance:none;
    width:100%;
    border: 1px solid #a0b3b0;
   color: #EEE;
    border-color: #1ab188;
    background:rgba(19, 35, 47, 0.9);
    border:none;
    outline:none;
    height:40px;
}
    </style>

<style type="text/css">
.ui-datepicker {
   background: #333;
   border: 1px solid #555;
   color: #EEE;
   
 }
</style>    
  </head>

  <body>
	<div id="header">
	<%@include file="header.jsp" %>
	</div>
    <div class="form">
      
      <ul class="tab-group">
        <li class="tab active"><a href="#generate">GENERATE COUPON</a></li>
        <li class="tab"><a href="#delete">DELETE COUPON</a></li>
      </ul>
     
      <div class="tab-content">
      
        <div id="generate">   
          
          <%if(request.getAttribute("msg")!=null) { %>
					<div class="col-sm-8 col-sm-offset-2" style="padding: 0px;text-align:center">
						<%=request.getAttribute("msg")%>
					</div>
					<%if(request.getAttribute("promoCode")!=null) { %>
					<div class="col-sm-8 col-sm-offset-2" style="padding: 0px;text-align:center;">
						<span style='color:#32CD32;  font-weight: bold;'>PROMO CODE: <%=request.getAttribute("promoCode")%></span>
					</div>
				<%} %>
				<%} %>
          
          <form action="RequestDispatcherHandler" method="post">
          <input type="hidden" id="action" name="action" value="generate_coupon">
            <input type="hidden" id="request_source" name="request_source" value="coupon">
          	<div class="field-wrap">
              <input type="text" class="form-control" id="prodId" name="prodId" required placeholder="PRODUCT ID" autofocus pattern="[0-9]{7}" title="Product Id On which coupon is to be applied [Only Number allowed, size=7]">
            </div>
          
          	<div class="top-row">
          	  <div class="field-wrap">
               <input type='text' name="startDate" id='datetimepicker1' required autofocus placeholder="START DATE" title="START DATE OF COUPON"/>
              </div>
            
              <div class="field-wrap">
                <input type='text'  name="endDate" id='datetimepicker2' required placeholder="END DATE" title="END DATE OF COUPON"/>
              </div>
            
           </div>

           <div class="field-wrap select_style">
           	
           			<select name="promotionCode" id="promortionCode" title="Type Of Promotion (Click to select type)" onchange="selectCouponMethod(this)">
					<option value="" disabled selected>PROMOTION TYPE</option>
					<option value="36">36</option>
					<option value="37">37</option>
					<!-- <option value="47">47</option> -->
					</select>
					&nbsp; &nbsp;
          			<select name="requiredFlag" id="requiredFlag" title="Coupon Code Required (Click to select flag)">
					<option value="" disabled selected>REQUIRED COUPON</option>
					<option value="Y">Y</option>
					<option value="N">N</option>
					</select>
								
		   </div>
           <div class="top-row">
            	<div class="field-wrap select_style1">
           	
          			<select name="optradio" id="optradio" title="Coupon Code Required (Click to select flag)">
					<option value="" disabled selected>DISCOUNT TYPE</option>
					<option value="DiscountPercent">Discount Percent</option>
					<option value="FixedDiscount">Fixed Discount</option>  
					</select>
				</div>
					
				<div class="field-wrap">
            		<input type="text" id="discValue" name="discValue" placeholder="Discount" required pattern="[0-9]{1,3}" title="Enter the dicount value or discount percent you want to offer[Only Number allowed]">
            	</div>
		  </div>
       
          <input type="submit" class="button button-block" value="GENERATE"/>
          
          
      </form>

     </div>
       
        <div id="delete">   
          <form action="RequestDispatcherHandler" method="post">
          <input type="hidden" id="action" name="action" value="delete_coupon">
           <input type="hidden" id="request_source" name="request_source" value="coupon">
            <div class="field-wrap">
            <label >
              PRODUCT ID<span class="req">*</span>
            </label>
            <input type="text"required id="prodId" name="prodId" pattern="[0-9]{7}" title="Product Id On which coupon is to be removed [Only Number allowed, size=7]"/>
          </div>
          <input type="submit" class="button button-block" value="DELETE"/>
          
        </form>

      </div>
        
  </div><!-- tab-content -->
      
</div> <!-- /form -->
    <script src='http://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js'></script>

        <script src="bootstrap-3.3.2-dist/js/index.js"></script>

    
    
    
  </body>
</html>

<!DOCTYPE html>
<html >
  <head>
    <meta charset="UTF-8">
    <title>Rebate</title>
    <link href='http://fonts.googleapis.com/css?family=Titillium+Web:400,300,600' rel='stylesheet' type='text/css'>
    
    <link rel="stylesheet" href="bootstrap-3.3.2-dist/css/normalize.css">
    
        <link rel="stylesheet" href="bootstrap-3.3.2-dist/css/style.css">
<script src="bootstrap-3.3.2-dist/jquery/jquery-3.1.0.min.js"></script>
<script src="bootstrap-3.3.2-dist/jquery/jquery-ui.min.js"></script>


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
        <li class="tab active"><a href="#generate">GENERATE REBATE</a></li>
        <li class="tab"><a href="#delete">DELETE REBATE</a></li>
      </ul>
     
      <div class="tab-content">
      
        <div id="generate">   
          
          <%if(request.getAttribute("msg")!=null) { %>
					<div class="col-sm-8 col-sm-offset-2" style="padding: 0px;text-align:center">
						<%=request.getAttribute("msg")%>
					</div>	
				<%} %>
          
          <form action="RequestDispatcherHandler" method="post">
          <input type="hidden" id="action" name="action" value="rebate_generate">
          <input type="hidden" id="request_source" name="request_source" value="rebate">
          	<div class="field-wrap">
              <input type="text" class="form-control" id="prodId" name="prodId" required placeholder="PRODUCT ID" autofocus pattern="[0-9]{7}" title="Product Id On which rebate is to be applied [Only Number allowed, size=7]">
            </div>
          
          	<div class="top-row">
          	  <div class="field-wrap">
               <input type='text' name="startDate" id='datetimepicker1' required autofocus placeholder="START DATE" title="START DATE OF REBATE"/>
              </div>
            
              <div class="field-wrap">
                <input type='text'  name="endDate" id='datetimepicker2' required autofocus placeholder="END DATE" title="END DATE OF REBATE"/>
              </div>
            
           </div>
			<div class="field-wrap">
            		<input type="text" id="rebate_amt" name="rebate_amt" placeholder="REBATE AMOUNT"  required autofocus pattern="[0-9]{1,3}" title="Enter the dicount value you want to offer[Only Number allowed]">
            </div>
           
          <input type="submit" class="button button-block" value="GENERATE"/>
          
          
      </form>

     </div>
       
        <div id="delete">   
          <form action="RequestDispatcherHandler" method="post">
          <input type="hidden" id="action" name="action" value="delete_rebate">
           <input type="hidden" id="request_source" name="request_source" value="rebate">
            <div class="field-wrap">
            <label >
              PRODUCT ID<span class="req">*</span>
            </label>
            <input type="text"required id="prodId" name="prodId" pattern="[0-9]{7}" title="Product Id On which rebate is to be removed [Only Number allowed, size=7]"/>
          </div>
          <input type="submit" class="button button-block" value="DELETE" />
          
        </form>

      </div>
        
  </div><!-- tab-content -->
      
</div> <!-- /form -->
    <script src='http://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js'></script>

        <script src="bootstrap-3.3.2-dist/js/index.js"></script>

    
    
    
  </body>
</html>

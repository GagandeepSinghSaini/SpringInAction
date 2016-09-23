<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Product Type</title>
<link
	href='http://fonts.googleapis.com/css?family=Titillium+Web:400,300,600'
	rel='stylesheet' type='text/css'>

<link rel="stylesheet" href="bootstrap-3.3.2-dist/css/normalize.css">

<link rel="stylesheet" href="bootstrap-3.3.2-dist/css/style.css">
<script src="bootstrap-3.3.2-dist/jquery/jquery-3.1.0.min.js"></script>
<script src="bootstrap-3.3.2-dist/jquery/jquery-ui.min.js"></script>

<style>
.select_style {
	overflow: hidden;
}

.select_style select {
	-webkit-appearance: none;
	appearance: none;
	width: 48%;
	border: 1px solid #a0b3b0;
	color: #EEE;
	border-color: #1ab188;
	background: rgba(19, 35, 47, 0.9);
	border: none;
	outline: none;
	height: 40px;
}

.select_style1 select {
	-webkit-appearance: none;
	appearance: none;
	width: 100%;
	border: 1px solid #a0b3b0;
	color: #EEE;
	border-color: #1ab188;
	background: rgba(19, 35, 47, 0.9);
	border: none;
	outline: none;
	height: 40px;
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
		<%@include file="header.jsp"%>
	</div>
	<div class="form">

		<ul class="tab-group">
			<li class="tab active"><a href="#generate">GENERATE PRODUCT</a></li>
			<li class="tab"><a href="#delete">DELETE PRODUCT</a></li>
		</ul>

		<div class="tab-content">

			<div id="generate">

				<%
					if (request.getAttribute("msg") != null) {
				%>
				<div class="col-sm-8 col-sm-offset-2"
					style="padding: 0px; text-align: center">
					<%=request.getAttribute("msg")%>
				</div>
				<%
					}
				%>

				<form action="RequestDispatcherHandler" method="post">
				<input type="hidden" id="request_source" name="request_source" value="product_type">
					<input type="hidden" id="action" name="action" value="generate_product_type">
					<div class="top-row">
						<div class="field-wrap">
							<input type="text" class="form-control" id="prodId" name="prodId"
								required placeholder="PRODUCT ID" autofocus pattern="[0-9]{7}"
								title="Product Id of which type is to be changed [Only Number allowed, size=7]">
						</div>
						<div class="field-wrap select_style1">

							<select name="typeFlag" id="typeFlag"
								title="Type Of product you want to make (Click to select flag)">
								<option value="" disabled selected>PRODUCT TYPE</option>
								<option value="BackOrder">Back Order</option>
								<option value="SpecialOrder">Special Order(Shipping)</option>
								<option value="StoreSpecialOrder">Special Order(StorePickUp)</option>
								<option value="PreOrder">Pre Order</option>
								<option value="ESDOrder">ESD Order</option>
								<option value="WhileSupplyLast">Whiles Supply Last</option>
							</select>

						</div>


					</div>
					<input type="submit" class="button button-block" value="GENERATE"/>
					


				</form>

			</div>

			<div id="delete">
				<form action="RequestDispatcherHandler" method="post">
					<input type="hidden" id="action" name="action" value="delete_product_type">
					<input type="hidden" id="request_source" name="request_source" value="product_type">
					<div class="top-row">
						<div class="field-wrap">
							<input type="text" id="prodId" name="prodId"
								required placeholder="PRODUCT ID" autofocus pattern="[0-9]{7}"
								title="Product Id to make oit simple product[Only Number allowed, size=7]">
						</div>
						<div class="field-wrap select_style1">

							<select name="gettype" id="gettype"
								title="Transport Type (Click to select type)">
								<option value="" disabled selected>Type(ship/store/esd)</option>
								<option value="store">Store Pickup</option>
								<option value="ship">Shipping</option>
								<option value="esd">ESD</option>
								<option value="WhileSupplyLast">Whiles Supply Last</option>
							</select>

						</div>


					</div>
					<input type="submit" class="button button-block" value="DELETE" />
					
				</form>

			</div>

		</div>
		<!-- tab-content -->

	</div>
	<!-- /form -->
	<script
		src='http://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js'></script>

	<script src="bootstrap-3.3.2-dist/js/index.js"></script>




</body>
</html>

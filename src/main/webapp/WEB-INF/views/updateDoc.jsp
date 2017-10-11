<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri='http://java.sun.com/jsp/jstl/core'%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Bonded Warehouse</title>
<link href="./resources/css/bootstrap.min.css" rel="stylesheet">
<link href="./resources/css/common.css" rel="stylesheet">
<script src="http://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
<script src="./resources/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="http://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.3.0/css/datepicker.min.css" />
<link rel="stylesheet" href="http://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.3.0/css/datepicker3.min.css" />
<script src="http://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.3.0/js/bootstrap-datepicker.min.js"></script>

<script language="javascript">

	
	$(document).ready(function() {
	    $('#datePicker')
	        .datepicker({
	            format: 'mm/dd/yyyy'
	        })
	});
	
	function addRow(tableID) {
	var table = document.getElementById(tableID);

	var rowCount = table.rows.length;
	var row = table.insertRow(rowCount);

	var cell2 = row.insertCell(0);
	var element2 = document.createElement("input");
	element2.type = "text";
	element2.name = "txtbox_" + rowCount + "_1";
	cell2.appendChild(element2);

	var cell3 = row.insertCell(1);
	var element3 = document.createElement("input");
	element3.type = "text";
	element3.name = "txtbox_" + rowCount + "_2";
	cell3.appendChild(element3);

	var cell4 = row.insertCell(2);
	var element4 = document.createElement("input");
	element4.type = "text";
	element4.name = "txtbox_" + rowCount + "_3";
	cell4.appendChild(element4);

	var cell5 = row.insertCell(3);
	var element5 = document.createElement("input");
	element5.type = "text";
	element5.name = "txtbox_" + rowCount + "_4";
	cell5.appendChild(element5);

	document.getElementById("tableRowCount").value = rowCount;
	
	}
	
</script>
<script>

var autocomplete1;
var autocomplete2;
var autocomplete3;

function initAutocomplete() {
  // Create the autocomplete object, restricting the search to geographical
  // location types.
  autocomplete1 = new google.maps.places.Autocomplete(
      /** @type {!HTMLInputElement} */(document.getElementById('source')),
      {types: ['geocode']});
	autocomplete2 = new google.maps.places.Autocomplete(
      /** @type {!HTMLInputElement} */(document.getElementById('destination')),
      {types: ['geocode']});
	autocomplete3 = new google.maps.places.Autocomplete(
      /** @type {!HTMLInputElement} */(document.getElementById('customLocation')),
      {types: ['geocode']});

  // When the user selects an address from the dropdown, populate the address
  // fields in the form.
  autocomplete1.addListener('place_changed', fillInAddress1);
	autocomplete2.addListener('place_changed', fillInAddress2);
	autocomplete3.addListener('place_changed', fillInAddress3);
}

function fillInAddress1() {
  // Get the place details from the autocomplete object.
  var place = autocomplete1.getPlace();
	document.getElementById('lat1').value = place.geometry.location.lat()+","+place.geometry.location.lng();
}

function fillInAddress2() {
  // Get the place details from the autocomplete object.
  var place = autocomplete2.getPlace();
	document.getElementById('lat2').value = place.geometry.location.lat()+","+place.geometry.location.lng();
}

function fillInAddress3() {
  // Get the place details from the autocomplete object.
  var place = autocomplete3.getPlace();
	document.getElementById('lat3').value = place.geometry.location.lat()+","+place.geometry.location.lng();
}

// Bias the autocomplete object to the user's geographical location,
// as supplied by the browser's 'navigator.geolocation' object.
function geolocate1() {
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(function(position) {
      var geolocation = {
        lat: position.coords.latitude,
        lng: position.coords.longitude
      };
      var circle = new google.maps.Circle({
        center: geolocation,
        radius: position.coords.accuracy
      });
      autocomplete1.setBounds(circle.getBounds());
    });
  }
}

function geolocate2() {
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(function(position) {
      var geolocation = {
        lat: position.coords.latitude,
        lng: position.coords.longitude
      };
      var circle = new google.maps.Circle({
        center: geolocation,
        radius: position.coords.accuracy
      });
      autocomplete2.setBounds(circle.getBounds());
    });
  }
}

function geolocate3() {
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(function(position) {
      var geolocation = {
        lat: position.coords.latitude,
        lng: position.coords.longitude
      };
      var circle = new google.maps.Circle({
        center: geolocation,
        radius: position.coords.accuracy
      });
      autocomplete3.setBounds(circle.getBounds());
    });
  }
}
</script>
<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyC-Ja0SVTIm46UW6x06uLCuxmoUjVOMrE8&libraries=places&callback=initAutocomplete"
  async defer></script>
  
</head>
<style type="text/css">
/**
 * Override feedback icon position
 * See http://formvalidation.io/examples/adjusting-feedback-icon-position/
 */
#eventForm .form-control-feedback {
    top: 0;
    right: -15px;
}
.logout-button {
	display: block;
	width: 115px;
	height: 47px;
	background: #ed0c21;
	padding: 10px;
	text-align: center;
	border-radius: 5px;
	color: white;
	margin-left: 1376px;
}

</style>
<body>
	<div class="panel-heading">
			<button class="btn btn-primary" onclick="goBack()">Go Back</button>
			<div class="container-fluid" style="float:right; padding-right:30px">
				<div class="dropdown">
		            <button class="btn btn-primary dropdown-toggle" type="button" data-toggle="dropdown">Welcome ${username}
		            <span class="caret"></span></button>
				    <ul class="dropdown-menu">
				      <li><a href="logout">Logout</a></li>
				    </ul>
	  			</div>
			</div>
	</div>
<script>
function goBack() {
    window.history.back();
}
</script>
	<!-- 	<h1 align="center">Custom Book Document Creation Form</h1> -->
	<div class="panel panel-success">
		<div class="panel-heading">
			<h3 align="center">Custom Book Edit Form</h3>
		</div>
	</div>
	<div class="panel-body">
		<div class="container">
			<h3 align="center">Edit CB</h3>
			<form action="updatecbpdf" method="post">
			<c:forEach items="${docsbyId}" var="doc" varStatus="loop">
					<c:set var="order" value="${docsbyId}" scope="request"></c:set>
				<table class="table table-bordered">
					<tr>
						<td>Source:</td>
						<td>
							<div class="col-xs-8">
								<input class="form-control" name="source" id="source" value="${doc.getSource()}" type="text">
							</div>
						</td>
					</tr>
					<tr>
						<td>Destination:</td>
						<td>
							<div class="col-xs-8">
								<input class="form-control" name="destination" id="destination" value="${doc.getDestination()}" type="text">
							</div>
						</td>
					</tr>
					<tr>
						<td>Delivery Date:</td>
						<td>
							<div class="col-xs-8 date">
								<div class="input-group input-append date" id="datePicker">
									<input type="text" name="deliveryDate" class="form-control" value="${doc.getDeliveryDate()}" /> <span
										class="input-group-addon add-on"><span
										class="glyphicon glyphicon-calendar"></span></span>
								</div>
							</div>
						</td>
					</tr>
					<tr>
						<td>Delivery No:</td>
						<td>
							<div class="col-xs-8">
								<input class="form-control" name="deliveryNo" value="${doc.getDeliveryNo()}" type="text">
							</div>
						</td>
					</tr>
					<tr>
						<td>Customer PO No:</td>
						<td>
							<div class="col-xs-8">
								<input class="form-control" name="custPO" value="${doc.getCustPO()}" type="text">
							</div>
						</td>
					</tr>
					<tr>
						<td>Truck Id:</td>
						<td>
							<!-- 						<input type="text" name="custPO" /> -->
							<div class="col-xs-8">
								<input class="form-control" name="truckId" value="${doc.getTruckId()}" type="text">
							</div>
						</td>
					</tr>
					<tr>
						<td>Custom Location:</td>
						<td>
							<!-- 						<input type="text" name="custPO" /> -->
							<div class="col-xs-8">
								<input class="form-control" name="customLocation" value="${doc.getCustomLocation()}" id="customLocation" type="text">
							</div>
						</td>
					</tr>
				</table>
				</c:forEach>
				
				<input type="button" class="btn btn-warning"
					style="margin: 0px 0px 22px 0px;" value="Add Row"
					onclick="addRow('dataTable')" />
				
				<table id="dataTable" class="table table-bordered">
					<tr>
						<td style="padding-left: 24px;">Item Name</td>
						<td style="padding-left: 24px;">Quantity</td>
						<td style="padding-left: 24px;">Description</td>
						<td style="padding-left: 24px;">Weight & Volume</td>
					</tr>
					<c:forEach items="${items}" var="item" varStatus="loop">
							<c:set var="order" value="${items}" scope="request"></c:set>		
					<tr>
						<td><input type="text" name="txtbox_${loop.index + 1}_1" value="${item.getName()}" /></td>
						<td><input type="text" name="txtbox_${loop.index + 1}_2" value="${item.getQuantity()}" /></td>
						<td><input type="text" name="txtbox_${loop.index + 1}_3" value="${item.getDescription()}" /></td>
						<td><input type="text" name="txtbox_${loop.index + 1}_4" value="${item.getWeightvolume()}" /></td>
					</tr>
					</c:forEach>
				</table>
				<input
					type="hidden" name="lat1" id="lat1" />
					<input
					type="hidden" name="lat2" id="lat2" />
					<input
					type="hidden" name="lat3" id="lat3" />
				<input type="hidden" name="tableRowCount" id="tableRowCount" /> 
				<%-- value="${fn:length(items)}"  --%>
				<input type="hidden" name="username" value=${username} id="username" /> 
				<input type="hidden" name="documentId" value=${documentId} id="documentId" />
				<input type="submit" value="Update CB" class="btn btn-success" />
				<button type="reset" class="btn btn-danger" value="Reset">Reset</button>
			</form>
		</div>
	</div>

	<footer class="footer panel-footer">
	<div class="container-fluid text-center">Copy Right @ IBM</div>
	</footer>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri='http://java.sun.com/jsp/jstl/core'%>
<!DOCTYPE html>
<html lang="en" ng-app="myModule">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
<meta name="description" content="">
<meta name="author" content="">
<title>Bonded Warehouse Third Party Portal</title>
<link href="./resources/css/bootstrap.min.css" rel="stylesheet">
<link href="./resources/css/common.css" rel="stylesheet">
<link href="./resources/css/font-awesome.min.css" rel="stylesheet">
<link href="./resources/css/jquery.dataTables.min.css" rel="stylesheet" />
<script src="./resources/js/angular.min.js"></script>
<script src="./resources/js/script.js"></script>
<script src="./resources/js/bootstrap.min.js"></script>
</head>
<body ng-controller="myController">

	<style type="text/css">
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

.modal-body .form-horizontal .col-sm-2, .modal-body .form-horizontal .col-sm-10
	{
	width: 100%
}

.modal-body .form-horizontal .control-label {
	text-align: left;
}

.modal-body .form-horizontal .col-sm-offset-2 {
	margin-left: 15px;
}
</style>

	<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
	<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
	<script type="text/javascript" src="./resources/js/bootstrap.min.js"></script>
	<script type="text/javascript">
function updateStatuss(dId, dSource, dDestination, dStatus, flag) {
    //alert("id = " + dId + " \nstatus = " +  dStatus + " \nsource = " + dSource + " \n destination = " + dDestination);
    document.getElementById("dDocId").value = dId;
    document.getElementById("dDestination").value = dDestination;
    document.getElementById("dSource").value = dSource;
    document.getElementById("flag").value = flag; 
    
}

function myFunction() {
    location.reload();
}

function showHideReasonCode(){
	
	var elem = document.getElementById("status");
	if (elem.value=='Rejected'){
		document.getElementById("reason").style.display='block';
		document.getElementById("reasonCode1").style.display='block';
	}
	else 
	{
		document.getElementById("reason").style.display='none';
		document.getElementById("reasonCode1").style.display='none';
	}
	
}
</script>
<div id="light" class="white_content" $("body").css("overflow", "hidden");>
   <img style="height: 50px;margin-left:46%; margin-top:-4%; width: 50px; position:fixed; cursor:pointer;" ng-src="./resources/img/close-icon.png" ng-click="StopTimer();"/>
   <div id="loadingIcon" style="display:none; margin-left:20%;"><img ng-src="./resources/img/Loading_icon.gif"/></div>
   <div class="TruckStatus" id="truckStatus" style="font-size:16px;"></div>
   <div id="noData" style="display:none; font-weight:bold; font-size:18px;">No GPS data available!
     <div id="sorry" style="margin-left:38%; margin-top:6%;"><img ng-src="./resources/img/nomap.jpg" style="height:200px; width:200px;"/></div>
   </div>
   <div id="mappop">
   </div>
   </div>
<div id="fade" class="black_overlay"></div>
<div class="panel-heading">
			<button class="btn btn-warning" onclick="myFunction()">Refresh</button>
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
	<div class="panel panel-success">
		<div class="panel-heading">
			<div class="welcomeText"><h3>Welcome Third Party</h3></div>
			<div class="container-fluid docSearch">
					<form class="navbar-form navbar-left" action="showTrxHistory">
						<div class="form-group">
							<input class="form-control" name="searchDocId" id="searchDocId" placeholder="Search by DocId" required>
						</div>
						<button type="submit" class="btn btn-submit">Submit</button>
					</form>
			</div>
		</div>
	</div>

	<div class="panel-header">
		<div class="container-fluid">
			<div class="row">
				<div class="col-md-2">
					<div class="panel panel-primary">
						<div class="panel-heading">
							<div class="row">
								<div class="col-xs-12 text-center">
									<div class="huge">${status.getInitiated()}</div>
									<div>Initiated Docs!</div>
								</div>
							</div>
						</div>
						<a href="${pageContext.request.contextPath}/initiated">
							<div class="panel-footer">
								<span class="pull-center">View Details</span>
							</div>
						</a>
					</div>
				</div>
				<div class="col-md-2">
					<div class="panel panel-blue">
						<div class="panel-heading">
							<div class="row">
								<div class="col-xs-12 text-center">
									<div class="huge">${status.getProcessed()}</div>
									<div>Processed Docs!</div>
								</div>
							</div>
						</div>
						<a href="${pageContext.request.contextPath}/processed">
							<div class="panel-footer">
								<span class="pull-center">View Details</span>
							</div>
						</a>
					</div>
				</div>
				<div class="col-md-2">
					<div class="panel panel-blue">
						<div class="panel-heading">
							<div class="row">
								<div class="col-xs-12 text-center">
									<div class="huge">${status.getVerified()}</div>
									<div>Verified Docs!</div>
								</div>
							</div>
						</div>
						<a href="${pageContext.request.contextPath}/verified">
							<div class="panel-footer">
								<span class="pull-center">View Details</span>
							</div>
						</a>
					</div>
				</div>
				<div class="col-md-2">
					<div class="panel panel-red">
						<div class="panel-heading">
							<div class="row">
								<div class="col-xs-12 text-center">
									<div class="huge">${status.getRejected()}</div>
									<div>Rejected Docs!</div>
								</div>
							</div>
						</div>
						<a href="${pageContext.request.contextPath}/rejected">
							<div class="panel-footer">
								<span class="pull-center">View Details</span>
							</div>
						</a>
					</div>
				</div>
				<div class="col-md-2">
					<div class="panel panel-blue">
						<div class="panel-heading">
							<div class="row">
								<div class="col-xs-12 text-center">
									<div class="huge">${status.getApproved()}</div>
									<div>Approved Docs!</div>
								</div>
							</div>
						</div>
						<a href="${pageContext.request.contextPath}/approved">
							<div class="panel-footer">
								<span class="pull-center">View Details</span>
							</div>
						</a>
					</div>
				</div>
				<div class="col-md-2">
					<div class="panel panel-green">
						<div class="panel-heading">
							<div class="row">
								<div class="col-xs-12 text-center">
									<div class="huge">${status.getReceived()}</div>
									<div>Received Docs!</div>
								</div>
							</div>
						</div>
						<a href="${pageContext.request.contextPath}/received">
							<div class="panel-footer">
								<span class="pull-center">View Details</span>
							</div>
						</a>
					</div>
				</div>
			</div>
		</div>
	</div>
	
		<div class="panel-body">
		<c:if test="${param.statusUpdate == 'success' || statusUpdate == 'success'}">
			<div class="alert alert-success">
				<strong><img title="success" style="width:20px; height:20px;" src="./resources/img/tick.png"/>  Status updated successfully!!</strong>
			</div>
		</c:if>
			<c:if test="${highpriorityDocListCount!=''}">
				<div class="alertHigh alert-info">
						<strong><img title="High Alert" style="width:20px; height:20px;" src="./resources/img/alert.gif"/><a href="./thirdpartyLogin">  Documents that required your immediate attention: ${highpriorityDocListCount}</a></strong>
				</div>
			</c:if>
			<div class="container">
				<c:choose>
					<c:when test="${action == 'initiated'}">
						<h3 align="center"><span class="docStyle">Initiated Custom Book Documents</span></h3>
					</c:when>
					<c:when test="${action == 'processed'}">
						<h3 align="center"><span class="docStyle">Processed Custom Book Documents</span></h3>
					</c:when>
					<c:when test="${action == 'verified'}">
						<h3 align="center"><span class="docStyle">Verified Custom Book Documents</span></h3>
					</c:when>
					<c:when test="${action == 'rejected'}">
						<h3 align="center"><span class="docStyle">Rejected Custom Book Documents</span></h3>
					</c:when>
					<c:when test="${action == 'approved'}">
						<h3 align="center"><span class="docStyle">Approved Custom Book Documents</span></h3>
					</c:when>
					<c:when test="${action == 'received'}">
						<h3 align="center"><span class="docStyle">Received Custom Book Documents</span></h3>
					</c:when>
					<c:otherwise>
						<h3 align="center"><span class="docStyle">High Priority Custom Book Documents</span></h3>
					</c:otherwise>
				</c:choose>
				<c:if test="${action == 'thirdpartyLogin'}">
				<table class="table table-bordered display" id="datatable">
				
					<thead>
						<tr>
							<th><img src="./resources/img/blockchain.png" alt="Block Chain" height="20" width="20"> &nbsp;ID</th>
							<th width="25%"><img src="./resources/img/blockchain.png" alt="Block Chain" height="20" width="20"> &nbsp;Source</th>
							<th width="25%"><img src="./resources/img/blockchain.png" alt="Block Chain" height="20" width="20"> &nbsp;Destination</th>
							<th><img src="./resources/img/blockchain.png" alt="Block Chain" height="20" width="20"> &nbsp;Status</th>
							<th><img src="./resources/img/blockchain.png" alt="Block Chain" height="20" width="20">  &nbsp;ETA(hh:mm:ss)</th>
							<th><img src="./resources/img/blockchain.png" alt="Block Chain" height="20" width="20"> &nbsp;Remarks</th>
							<th><img src="./resources/img/blockchain.png" alt="Block Chain" height="20" width="20"> &nbsp;Rejected By</th>
							<th width="65px">Action</th>
						</tr>
					</thead>
					<!-- Warehouse Login -->
					<tbody>
						<c:forEach items="${documents}" var="prioritydoc" varStatus="priorityloop">
						<c:set var="prioritydoc" value="${prioritydoc}" scope="request"></c:set>
						<tr>
								<td id="tDocId"><a href="openDoc?docId=${prioritydoc.getDocumentId()}" target="_blank">${prioritydoc.getDocumentId()}</a><img title="High Alert" style="width:30px; height:30px;" src="./resources/img/alert.gif"/></td>
								<td>${prioritydoc.getSource()}</td>
								<td>${prioritydoc.getDestination()}</td>
								<td>${prioritydoc.getStatus()}</td>
								<td>${prioritydoc.getEstimatedTime()}</td>
								<c:choose>
									<c:when test="${prioritydoc.getReasonCode() == ''}">
									<td>NA</td>
									</c:when>
									<c:otherwise>
									<td>${prioritydoc.getReasonCode()}</td>
									</c:otherwise>
								</c:choose>
								<td>${updatedByList.get(priorityloop.index)}</td>
								<td>
								<c:if test="${prioritydoc.getStatus() == 'Initiated'}">
									<span id="update_button"
										class=" update_button btn" data-toggle="modal"
										data-target="#updateStatus1" title="Update Status" style="margin-top:8px;"
										onclick="updateStatuss(${prioritydoc.getDocumentId()} , '${prioritydoc.getSource()}' , '${prioritydoc.getDestination()}' , '${prioritydoc.getStatus()}' ,'highPriority')"><img style="width:30px; height:30px; float:left;" src="./resources/img/update-status.png"/></span>
								</c:if>
								    <img ng-src="./resources/img/truck-search.png" title="Locate the truck" alt="Truck Track" style="width:50px; height:50px; cursor:pointer; float:right;" ng-click="initMap('${prioritydoc.getTruckId()}','${prioritydoc.getDocumentId()}');"/>						
								</td>
						</tr>
						</c:forEach>
					</tbody>
				</table> 
				
				</c:if>
				<c:if test="${action != 'thirdpartyLogin'}">
				<table class="table table-bordered display" id="datatable">
				
					<thead>
						<tr>
							<th><img src="./resources/img/blockchain.png" alt="Block Chain" height="20" width="20"> &nbsp;ID</th>
							<th width="25%"><img src="./resources/img/blockchain.png" alt="Block Chain" height="20" width="20"> &nbsp;Source</th>
							<th width="25%"><img src="./resources/img/blockchain.png" alt="Block Chain" height="20" width="20"> &nbsp;Destination</th>
							<th><img src="./resources/img/blockchain.png" alt="Block Chain" height="20" width="20"> &nbsp;Status</th>
							<th><img src="./resources/img/blockchain.png" alt="Block Chain" height="20" width="20">  &nbsp;ETA(hh:mm:ss)</th>
						<c:forEach items="${documents}" var="order" end="0">
							<c:if test="${order.getStatus() == 'Rejected'}">
								<th><img src="./resources/img/blockchain.png" alt="Block Chain" height="20" width="20"> &nbsp;Remarks</th>
								<th><img src="./resources/img/blockchain.png" alt="Block Chain" height="20" width="20"> &nbsp;Rejected By</th>
							</c:if>
						</c:forEach>
						<c:forEach items="${documents}" var="order" end="0">
								<c:if test="${order.getStatus() != 'Received'}"> 
									<th width="65px">Action</th>
								</c:if>
						</c:forEach>
						</tr>
					</thead>
					<tbody>
					
					<c:forEach items="${documents}" var="order" varStatus="loop">
					<c:if test="${order.getStatus() == 'Rejected'}">
						<c:set var="order" value="${order}" scope="request"></c:set>
						<tr>
							<td id="tDocId"><a href="openDoc?docId=${order.getDocumentId()}" target="_blank">${order.getDocumentId()}</a></td>
							<td>${order.getSource()}</td>
							<td>${order.getDestination()}</td>
							<td>${order.getStatus()}</td>
							<td>${order.getEstimatedTime()}</td>
							<td>${order.getReasonCode()}</td>
							<td>${updatedByList.get(loop.index)}</td>
							<td><img ng-src="./resources/img/truck-search.png" title="Locate the truck" alt="Truck Track" style="width:50px; height:50px; cursor:pointer; float:right;" ng-click="initMap('${order.getTruckId()}','${order.getDocumentId()}');"/></td>
						</tr>
						</c:if>
					</c:forEach>
					
					
						<c:forEach items="${documents}" var="order" varStatus="loop">
						<c:if test="${order.getStatus() != 'Rejected'}">
							<c:set var="order" value="${order}" scope="request"></c:set>
							<tr>
								<td id="tDocId"><a href="openDoc?docId=${order.getDocumentId()}" target="_blank">${order.getDocumentId()}</a></td>
								<td>${order.getSource()}</td>
								<td>${order.getDestination()}</td>
								<td>${order.getStatus()}</td>
								<td>${order.getEstimatedTime()}</td>
								<c:if test="${order.getStatus() == 'Initiated'}">
								   <td>
										<span id="update_button"
											class=" update_button btn" data-toggle="modal"
											data-target="#updateStatus1" title="Update Status" style="margin-top:8px;"
											onclick="updateStatuss(${order.getDocumentId()} , '${order.getSource()}' , '${order.getDestination()}' , '${order.getStatus()}' , 'initiated')"><img style="width:30px; height:30px; float:left;" src="./resources/img/update-status.png"/></span>
								        <img ng-src="./resources/img/truck-search.png" title="Locate the truck" alt="Truck Track" style="width:50px; height:50px; cursor:pointer; float:right;" ng-click="initMap('${order.getTruckId()}','${order.getDocumentId()}');"/>
								  </td>
								</c:if>
								<c:if test="${order.getStatus() != 'Received' && order.getStatus() != 'Initiated'}">
								  <td>
									   <img ng-src="./resources/img/truck-search.png" title="Locate the truck" alt="Truck Track" style="width:50px; height:50px; cursor:pointer; float:right;" ng-click="initMap('${order.getTruckId()}','${order.getDocumentId()}');"/>
							      </td>
							    </c:if>
							</tr>
						</c:if>
						</c:forEach>
					

				</tbody>
				</table>
				</c:if>
			</div>
			<div class="modal fade" id="updateStatus1" tabindex="-1"
				role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
				<div class="modal-dialog">
					<div class="modal-content">
						<!-- Modal Header -->
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal">
								<span aria-hidden="true">&times;</span> <span class="sr-only">Close</span>
							</button>
							<h4 class="modal-title" id="myModalLabel">Update Status</h4>
						</div>

						<!-- Modal Body -->
						<div class="modal-body">

							<form role="form" method="post" action="updateStatus" onload="showHideReasonCode()">
								<input type="hidden" name="username" value=${username} id="username" />
								<input type="hidden" id="flag" name="flag" value="" />
								<div class="form-group">
									<table>
										<tr>
											<td><font face="verdana" size="2px">Document Id:</font></td>
											<td>
												<div class="form-group">
													<input type="text" class="form-control" name="id"
														id="dDocId" readonly="readonly">
												</div>
											</td>
										</tr>
										<tr>
											<td><font face="verdana" size="2px">Source:</font></td>
											<td>
												<div class="form-group">
													<input type="text" id="dSource" value=""
														readonly="readonly" class="form-control">
												</div>
											</td>
										</tr>
										<tr>
											<td><font face="verdana" size="2px">Destination:</font></td>
											<td>
												<div class="form-group">
													<input type="text" id="dDestination" value=""
														readonly="readonly" class="form-control" />
												</div>
											</td>
										</tr>
										<tr>
											<td><font face="verdana" size="2px">Status:</font></td>
											<td><select name="status" id="status"
												class="form-control" onchange="showHideReasonCode()">
													<option value="Processed">Processed</option>
													<option value="Rejected">Rejected</option>
											</select></td>
										</tr>
									<tr>
										<td><div id="reason" style="display:none"><font face="verdana" size="2px">Reason for rejection:</font></div></td>
										<td style="padding-top:15px;">
											<div id="reasonCode1" class="form-group" style="display:none">
											<select name="reasonCode" class="form-control">
											<option value="" selected="selected">Select One Reason Code</option>
											<c:forEach items="${rejections}" var="rejectionsItem">
											 	<option value="${rejectionsItem.rejection_description}">${rejectionsItem.rejection_description}</option>
											</c:forEach>
											</select>
										</div>
										</td>
									</tr>
									</table>
								</div>
								<div class="modal-footer">
									<button type="button" class="btn btn-secondary"
										data-dismiss="modal">Close</button>
									<button type="submit" class="btn btn-primary" value="Update" id="statusUpdateSave" onclick="disableUpdate()">Save</button>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
	<footer class="footer panel-footer">
		<div class="container-fluid text-center">Copy Right @ IBM</div>
	</footer>
    <script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyC-Ja0SVTIm46UW6x06uLCuxmoUjVOMrE8"></script>
    <script src="./resources/js/jquery.dataTables.min.js"></script>
	<script>
	$(document).ready(function() {
		$('#datatable').DataTable( {
			"pagingType": "full_numbers"
		} );
	} );
	
	function disableUpdate(){
		document.getElementById('statusUpdateSave').disabled = true;
	}
	</script>
</body>
</html>
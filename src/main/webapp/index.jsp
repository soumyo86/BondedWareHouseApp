<%-- <%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Bonded Warehouse</title>
    <link href="./resources/css/bootstrap.min.css" rel="stylesheet">
    <link href="./resources/css/common.css" rel="stylesheet">
</head>
<body>
<div class="panel panel-success">
	<div class="panel-heading">
		<h2><center>Welcome to Bonded Warehouse</h2></center>
	
		<div class="btn-group btn-group-justified">
		    <div class="btn-group">
		      <button type="button" id="btn_warehouse" name="warehouse" class="btn btn-primary">Warehouse</button>
		    </div>
		    <div class="btn-group">
		      <button type="button" id="btn_custom" name="custom" class="btn btn-info">Custom</button>
		    </div>
		    <div class="btn-group">
		      <button type="button" id="btn_thirdparty" name="thirdparty" class="btn btn-warning">Third Party</button>
		    </div>
		</div>
	</div>
</div>



<%
	String st = request.getParameter("errorStatus");
	out.print(st);
%> 
<div class="alert alert-danger" style="display: none" >
 		<strong>${errorStatus}</strong> Invalid Username or Password
</div>

<div class="login-container">

	<div id="waregouse_login" class="col-lg-6" style="display: none">
	     <div class="panel panel-primary">
	         <div class="panel-heading">
	             Warehouse Login
	         </div>
	         <div class="panel-body">
		         <form method="post" action="warehouseLogin" class="form-signin">
			        <div class="form-group">
			            <input name="username" type="text" class="form-control" placeholder="Username" autofocus="true"/>
			            <input name="password" type="password" class="form-control" placeholder="Password"/>
			            <button class="btn btn-lg btn-primary btn-block" type="submit">Log In</button>
			        </div>
				 </form>
	         </div>
	     </div>
	 </div>
	<!-- <div id="waregouse_login" style="display: none" class="well-warehouse">
	    <form method="post" action="warehouseLogin" class="form-signin">
	        <h2 align="center" class="form-heading">Warehouse Login</h2>	
	        <div class="form-group">
	            <input name="username" type="text" class="form-control" placeholder="Username" autofocus="true"/>
	            <input name="password" type="password" class="form-control" placeholder="Password"/>
	            <button class="btn btn-lg btn-primary btn-block" type="submit">Log In</button>
	        </div>
	    </form>
    </div> -->
	
    <div align="center" id="custom_login" class="col-lg-6" style="display: none">
	     <div class="panel panel-green">
	         <div class="panel-heading">
	             Custom Login
	         </div>
	         <div class="panel-body">
		         <form method="post" action="customLogin" class="form-signin">
			        <div class="form-group">
			            <input name="username" type="text" class="form-control" placeholder="Username" autofocus="true"/>
			            <input name="password" type="password" class="form-control" placeholder="Password"/>
			            <button class="btn btn-lg btn-success btn-block" type="submit">Log In</button>
			        </div>
				 </form>
	         </div>
	     </div>
	 </div>
	
    <!-- <div id="thirdparty_login" class="well-thirdparty" style="display: none">
	    <form method="post" action="thirdpartyLogin" class="form-signin">
	        <h2 align="center" class="form-heading">Third Party Login</h2>
	        <div class="form-group">
	            <input name="username" type="text" class="form-control" placeholder="Username" autofocus="true"/>
	            <input name="password" type="password" class="form-control" placeholder="Password"/>
	            <button class="btn btn-lg btn-warning btn-block" type="submit">Log In</button>
	        </div>
	    </form>
    </div> -->
    <div align="center" id="thirdparty_login" class="col-lg-6" style="display: none">
	     <div class="panel panel-yellow">
	         <div class="panel-heading">
	             Third Party Login
	         </div>
	         <div class="panel-body">
		         <form method="post" action="thirdpartyLogin" class="form-signin">
			        <div class="form-group">
			            <input name="username" type="text" class="form-control" placeholder="Username" autofocus="true"/>
			            <input name="password" type="password" class="form-control" placeholder="Password"/>
			            <button class="btn btn-lg btn-success btn-block" type="submit">Log In</button>
			        </div>
				 </form>
	         </div>
	     </div>
	 </div>

</div>

<footer class="footer panel-footer">
  		<div class="container-fluid text-center">Copy Right @ IBM</div>
</footer>

<!-- /container -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script type="text/javascript">
	$("button").click(function() { 
	    if(this.id === "btn_warehouse")
    	{
	    	document.getElementById("waregouse_login").style.display='block';
	    	document.getElementById("custom_login").style.display = 'none';
	    	document.getElementById("thirdparty_login").style.display = 'none';
    	}
	    else if(this.id === "btn_custom")
    	{
	    	document.getElementById("waregouse_login").style.display = 'none';
	    	document.getElementById("custom_login").style.display = 'block';
	    	document.getElementById("thirdparty_login").style.display = 'none';
    	}
	    else if(this.id === "btn_thirdparty")
	    {
	    	document.getElementById("waregouse_login").style.display = 'none';
	    	document.getElementById("custom_login").style.display = 'none';
	    	document.getElementById("thirdparty_login").style.display = 'block';
	    }
	});
</script>
</body>
</html> --%>
<jsp:forward page="/home"></jsp:forward>
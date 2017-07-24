<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<form id="loginForm">
<input type="text" id="email" />
<input type="password" id="password" />
<button type="submit">Log in</button>
</form>
<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script>
	$(function() {
		$.ajax({
			type: "GET",
			url: "/task-tracker/api/auth/logged-in"
		});
		$("#loginForm").on("submit", function(event) {
			event.preventDefault();
			$.ajax({
				type: "POST",
				url: "/task-tracker/api/auth/login",
				dataType: "json",
				contentType: "application/json",
				data: JSON.stringify({
					email: $("#email").val(),
					password: $("#password").val()
				})
			});
			return false;
		});
	});
</script>
</body>
</html>
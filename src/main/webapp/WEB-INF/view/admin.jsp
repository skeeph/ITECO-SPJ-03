<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>You're admin now</title>

    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css" rel="stylesheet">
</head>

<body>

<div class="container">
<%--    <form method="POST" action="${contextPath}/login" class="form-signin">--%>
<%--        <h2 class="form-heading">Log in</h2>--%>

<%--        <div class="form-group ${error != null ? 'has-error' : ''}">--%>
<%--            <span>${message}</span>--%>
<%--            <input name="username" type="text" class="form-control" placeholder="Username"--%>
<%--                   autofocus="true"/>--%>
<%--            <input name="password" type="password" class="form-control" placeholder="Password"/>--%>
<%--            <span>${error}</span>--%>
<%--            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>--%>

<%--            <button class="btn btn-lg btn-primary btn-block" type="submit">Log In</button>--%>
<%--            <h4 class="text-center"><a href="${contextPath}/registration">Create an account</a></h4>--%>
<%--        </div>--%>
<%--    </form>--%>
    <h1>You're admin now</h1>
</div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.bundle.min.js"></script>
</body>
</html>
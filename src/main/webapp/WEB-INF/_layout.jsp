<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String page_body = (String) request.getAttribute( "page-body" ) ;
    String context = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
    <title>Java web</title>
    <!--Import Google Icon Font-->
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <!-- Compiled and minified CSS -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css">
</head>
<body>
<nav>
    <div class="nav-wrapper light-blue">
        <a href="<%=context%>" class="brand-logo right">Java</a>
        <ul id="nav-mobile">
            <li><a href="sass.html">Sass</a></li>
            <li><a href="badges.html">Components</a></li>
            <li><a href="collapsible.html">JavaScript</a></li>
        </ul>
    </div>
</nav>
<jsp:include page="<%=page_body%>"/>

<!-- Compiled and minified JavaScript -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>
</body>
</html>

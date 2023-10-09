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
            <li><a href="index.jsp">JSP</a></li>
            <li <%= "filters.jsp".equals(page_body) ? "class='active'" : ""%>><a href="<%=context%>/filters">Filters</a></li>
            <li <%= "ioc.jsp".equals(page_body) ? "class='active'" : ""%>><a href="<%=context%>/ioc">Ioc</a></li>
        </ul>
    </div>
</nav>
<jsp:include page="<%=page_body%>"/>
<!-- Compiled and minified JavaScript -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>
</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String page_body = (String) request.getAttribute( "page-body" ) ;
    String context = request.getContextPath();
    String context_culture = context + "/" + request.getAttribute("culture");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Java web</title>
    <!--Import Google Icon Font-->
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <!-- Compiled and minified CSS -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css"/>
    <link rel="stylesheet" href="<%=context%>/css/site.css"/>
</head>
<body>
<nav>
    <div class="nav-wrapper light-blue">
        <!-- Modal Trigger -->
        <a class="auth-trigger modal-trigger right" href="#auth-modal"><i class="material-icons">door_front</i></a>
        <a href="<%=context%>" class="site-logo right">Java</a>
        <ul id="nav-mobile">
            <li <%= "abut.jsp".equals(page_body) ? "class='active'" : ""%>><a href="<%=context%>/jsp">JSP</a></li>
            <li <%= "filters.jsp".equals(page_body) ? "class='active'" : ""%>><a href="<%=context_culture%>/filters">Filters</a></li>
            <li <%= "ioc.jsp".equals(page_body) ? "class='active'" : ""%>><a href="<%=context_culture%>/ioc">Ioc</a></li>
        </ul>
    </div>
</nav>
<jsp:include page="<%=page_body%>"/>
<!-- Modal Structure -->
<div id="auth-modal" class="modal">
    <div class="modal-content">
        <h4>Authentication</h4>
        <p>A bunch of text</p>
    </div>
    <div class="modal-footer">
        <a href="<%=context%>/signup" class="modal-close light-blue btn-flat lighten-2">Sign up</a>
        <a href="#!" class="modal-close waves-effect waves-green light-blue btn-flat">Sign in</a>
    </div>
</div>
<!-- Compiled and minified JavaScript -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>
<script src="<%=context%>/js/site.js"></script>
</body>
<footer class="page-footer light-blue">
    <div class="container">
        <div class="row">
            <div class="col l6 s12">
                <h5 class="white-text">Footer Content</h5>
                <p class="grey-text text-lighten-4">You can use rows and columns here to organize your footer content.</p>
            </div>
            <div class="col l4 offset-l2 s12">
                <h5 class="white-text">Links</h5>
                <ul>
                    <li><a class="grey-text text-lighten-3" href="#!">Link 1</a></li>
                    <li><a class="grey-text text-lighten-3" href="#!">Link 2</a></li>
                    <li><a class="grey-text text-lighten-3" href="#!">Link 3</a></li>
                    <li><a class="grey-text text-lighten-3" href="#!">Link 4</a></li>
                </ul>
            </div>
        </div>
    </div>
    <div class="footer-copyright">
        <div class="container">
            © 2023 Copyright Text
            <a class="grey-text text-lighten-4 right" href="#!">More Links</a>
        </div>
    </div>
</footer>
</html>

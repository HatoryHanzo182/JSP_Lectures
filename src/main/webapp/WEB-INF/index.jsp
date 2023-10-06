<%@ page contentType="text/html;charset=UTF-8" %>
<%String context = request.getContextPath();%>
<img src="<%=context%>/img/Java_logo.png" style="width: 110px; height: 110px; float:left"/>
<h1>Java web. Интро.</h1>
<p>
    Создаем проект с архетипом webapp.
    Перегенерируем индексный файл (index.jsp).<br>
    Настраиваем конфигурацию запуска. Для этого нужен локальный
    сервер: Tomcat, Glassfish, JBoss, WildFly, TomEE.<br>
    Большинство этих серверов устанавливаются простой распаковкой из
    архива.<br>
</p>
<%
String str = "Hello" ;

str += " World" ;

int x = 10 ;
%>
<p>str = <%= str %>, x + 10 = <%= x + 10 %></p>
<ul>
    <% for( int i = 1; i <= 10; ++i ) { %>
        <li>item No <%= i %></li>
    <% } %>
</ul>
<jsp:include page="fragment.jsp">
    <jsp:param name="str" value="<%=str%>"/>
</jsp:include>
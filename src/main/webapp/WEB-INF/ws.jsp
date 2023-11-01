<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<h1>WEBSOCKET</h1>
<div class="row">
    <div class="col s3">
        <ul class="collection" id="chat-container"></ul>
        <input id="chat-input" type="text" value="BLA-BLA-BLA"/>
        <button class="waves-effect light-blue btn" type="button" onclick="SendMessageClick()">Send</button>
    </div>
    <div class="col s9">
        <p>
            WebSocket (веб-сокет) - это технология, которая обеспечивает более интерактивное и мгновенное взаимодействие между веб-браузером и веб-сервером.<br>
            Это позволяет серверу отправлять данные клиенту и наоборот без необходимости постоянно инициировать новые HTTP-запросы.<br>
            WebSocket позволяет устанавливать постоянное соединение между клиентом и сервером, которое остается открытым в течение всего сеанса взаимодействия.<br>
            <br>
            WebSocket можно использовать в JSP, как и в других серверных технологиях, для создания интерактивных веб-приложений.<br>
            Для этого обычно используются JavaScript на стороне клиента и Java на стороне сервера.<br>
            <br>
            Вот как можно использовать WebSocket в JSP:<br>
            На стороне сервера (Java): Создайте серверное приложение, которое поддерживает WebSocket, используя, например, библиотеку Java EE WebSocket API или<br>
            Spring WebSocket.<br>
            На стороне клиента (JavaScript): Внедрите JavaScript-код в вашу JSP-страницу, который устанавливает WebSocket-соединение с сервером и обрабатывает события,<br>
            такие как открытие соединения, получение сообщений и закрытие соединения.
        </p>
        <br>
        <br>
        <h2>Конфигурация.</h2>
        <p>

        </p>
    </div>
</div>
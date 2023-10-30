<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<h1>WEBSOCKET</h1>
<div class="row">
    <div class="col s3">
        <ul class="collection" id="chat-container"></ul>
        <input id="chat-input" type="text" value="BLA-BLA-BLA"/>
        <button type="button" onclick="SendMessageClick()">Send</button>
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
    </div>
</div>
<script>
    document.addEventListener('DOMContentLoaded', InitWebsocket);

    function SendMessageClick() { window.websocket.send(document.getElementById("chat-input").value); }

    function InitWebsocket()
    {
        const host = window.location.host + GetAppContext();
        const ws = new WebSocket(`ws://${host}/chat`);

        ws.onopen = OnWsOpen;
        ws.onclose = OnWsClose;
        ws.onmessage = OnWsMessage;
        ws.onerror = OnWsError;
        window.websocket = ws;
    }

    function OnWsOpen(e) { console.log("OnWsOpen", e); }

    function OnWsClose(e) { console.log("OnWsClose", e); }

    function OnWsMessage(e)
    {
        const li = document.createElement("li");

        li.className = "collection-item";
        li.appendChild(document.createTextNode(e.data));
        document.getElementById("chat-container").appendChild(li);
    }

    function OnWsError(e) { console.log("OnWsError", e); }

    function GetAppContext()
    {
        var is_context_preset = false;
        return is_context_preset ? "" : "/" + window.location.pathname.split('/')[1];
    }
</script>
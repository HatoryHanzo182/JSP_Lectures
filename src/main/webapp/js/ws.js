document.addEventListener('DOMContentLoaded', InitWebsocket);

function SendMessageClick()
{
    window.websocket.send(JSON.stringify({command: 'chat', data: document.getElementById("chat-input").value}));
}

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

function OnWsOpen(e)
{
    window.websocket.send(JSON.stringify({command: 'auth', data: window.localStorage.getItem("token202")}));
}

function OnWsClose(e) { console.log("OnWsClose", e); }

function OnWsMessage(e)
{
    const li = document.createElement("li");

    li.className = "collection-item";

    const data = JSON.parse(e.data).data;

    li.appendChild(document.createTextNode(data));
    document.getElementById("chat-container").appendChild(li);
}

function OnWsError(e) { console.log("OnWsError", e); }

function GetAppContext()
{
    var is_context_preset = false;
    return is_context_preset ? "" : "/" + window.location.pathname.split('/')[1];
}
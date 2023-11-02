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
    const chat_message = JSON.parse(e.data);

    switch(chat_message.status)
    {
        case 201:
            AppendChatMessage(chat_message.data);
            break;
        case 202:
            EnableChat(chat_message.data);
            break;
        case 403:
            DisableChat();
            break;
        case 405:
            console.error(chat_message);
            break;
    }
}

function OnWsError(e) { console.log("OnWsError", e); }

function GetAppContext()
{
    var is_context_preset = false;
    return is_context_preset ? "" : "/" + window.location.pathname.split('/')[1];
}

function AppendChatMessage(message)
{
    const li = document.createElement("li");

    li.className = "collection-item";
    li.appendChild(document.createTextNode(message));
    document.getElementById("chat-container").appendChild(li);
}

function EnableChat(nik)
{
    document.getElementById("chat-nik").innerText = nik;

    for(let child of document.getElementById("chat-block").children)
        child.disabled = false;

    // document.getElementById("chat-input").disabled = false;
    // document.getElementById("chat-send").disabled = false;

    AppendChatMessage(nik + ' join us');
}

function DisableChat()
{
    document.getElementById("chat-nik").innerText = 'OFF';

    for(let child of document.getElementById("chat-block").children)
        child.disabled = true;

    // document.getElementById("chat-input").disabled = true;
    // document.getElementById("chat-send").disabled = true;
}
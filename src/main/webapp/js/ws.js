document.addEventListener('DOMContentLoaded', InitWebsocket);

function SendMessageClick()
{
    window.websocket.send(JSON.stringify({command: 'chat', data: document.getElementById("chat-input").value}));
}

function InitWebsocket()
{
    const token = JSON.parse(atob(window.localStorage.getItem('token202')));
    const exp = new Date(token._exp);

    document.getElementById("chat-token").innerText = "Active until " + DateString(exp)

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
        case 200:
            LoadChatMessage(chat_message.data);
            break;
        case 201:
            AppendChatMessage(chat_message.data);
            break;
        case 202:
            EnableChat(chat_message.data);
            WsSend("load", "")
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

function WsSend(command, data)
{
    window.websocket.send(JSON.stringify({command: command, data: data}));
}

function LoadChatMessage(arr)
{
    console.log(arr);
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

function DateString(date)
{
    if(date.getDate() === new Date().getDate())
        return `${('0' + date.getHours()).slice(-2)}:${('0' + date.getMinutes()).slice(-2)}`;
    return `${('0' + date.getDate()).slice(-2)}.${('0' + (date.getMonth() + 1)).slice(-2)}.${date.getFullYear()} ${('0' + date.getHours()).slice(-2)}:${('0' + date.getMinutes()).slice(-2)}`;
}
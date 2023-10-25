document.addEventListener('DOMContentLoaded', function()
{
    const create_button = document.getElementById("db-create-button");

    if(create_button)
        create_button.addEventListener('click', CreateButtonClick)

    const callme_button = document.getElementById("db-call-me-button");

    if(callme_button)
        callme_button.addEventListener('click', CallmeButtonClick)

    const get_all_button = document.getElementById("db-get-all-button");

    if(get_all_button)
        get_all_button.addEventListener('click', GetAllButtonClick)
});

function CreateButtonClick()
{
    fetch(window.location.href, { method: 'POST' }).then(r => r.json()).then(response =>
    {
        if (response.status === "ok")
            document.getElementById("message").innerHTML = "Создано успешно";
        else
            document.getElementById("message").innerHTML = "Ошибка: " + response.message;
    });
}

function CallmeButtonClick()
{
    const name_input = document.getElementById("db-call-me-name");

    if(!name_input)
        throw "name_input (#db-call-me-name) not found!";
    if(!name_input.value)
    {
        M.toast({html: 'Name is required'});
        return;
    }

    const phone_input = document.getElementById("db-call-me-phone");

    if(!phone_input)
        throw "phone_input (#db-call-me-phone) not found!";
    if(!phone_input.value)
    {
        M.toast({html: 'Phone is required'});
        return;
    }

    fetch(window.location.href,
        {
                method: 'PATCH',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ name: name_input.value, phone: phone_input.value })
        }).then(r => r.json()).then(j => { console.log(j); });
}

function GetAllButtonClick()
{
    fetch(window.location.href, { method: 'LINK' }).then(r => r.json()).then(j=> ShowCalls(j));
}

function ShowCalls(json)
{
    const container = document.getElementById("db-get-all-container");

    if(!container)
        throw "#db-get-all-container not found" ;

    const table = document.createElement('table');

    table.classList.add('striped');

    const thead = document.createElement('thead');
    const tr = document.createElement('tr');
    const th1 = document.createElement('th');

    th1.innerText = 'id';

    const th2 = document.createElement('th');

    th2.innerText = 'name';

    const th3 = document.createElement('th');

    th3.innerText = 'phone';

    const th4 = document.createElement('th');

    th4.innerText = 'call';

    const th5 = document.createElement('th');

    th5.innerText = 'Delete';

    tr.appendChild(th1);
    tr.appendChild(th2);
    tr.appendChild(th3);
    tr.appendChild(th4);
    tr.appendChild(th5);
    thead.appendChild(tr);
    table.appendChild(thead);

    const tbody = document.createElement('tbody');

    json.forEach(call =>
    {
        const tr = document.createElement('tr');
        const td1 = document.createElement('td');

        td1.innerText = call._id;

        const td2 = document.createElement('td');

        td2.innerText = call._name;

        const td3 = document.createElement('td');

        td3.innerText = call._phone;

        tr.appendChild(td1);
        tr.appendChild(td2);
        tr.appendChild(td3);

        const td4 = document.createElement('td');

        if(typeof call._call_moment == 'undefined' || call._call_moment == null)
        {
            const btn = document.createElement('button');

            btn.appendChild(document.createTextNode("call"));
            btn.classList.add('btn');
            btn.classList.add('waves-effect');
            btn.classList.add('waves-light');
            btn.classList.add('light-blue');
            btn.addEventListener('click', MakeCallClick);
            btn.setAttribute('data-call-id', call._id) ;
            td4.appendChild(btn);
        }
        else
            td4.appendChild(document.createTextNode(call._call_moment));

        tr.appendChild(td4);

        const td5 = document.createElement('td');
        const btn5 = document.createElement('button');

        btn5.appendChild(document.createTextNode("DELETE"));
        btn5.classList.add('btn');
        btn5.classList.add('white-text');
        btn5.classList.add('red');
        btn5.addEventListener('click', DeleteCallClick);
        btn5.setAttribute( 'data-call-id', call._id ) ;
        td5.appendChild(btn5);
        tr.appendChild(td5);

        tbody.appendChild(tr);
    });

    table.appendChild(tbody);
    container.innerHTML = "";
    container.appendChild(table) ;
}

function MakeCallClick(e)
{
    const call_id = e.target.getAttribute('data-call-id');

    if(confirm("Make call to order " + call_id))
    {
        fetch(window.location.href + "?call-id=" + call_id, {method: 'CALL'}).then(r => r.json()).then(j =>
        {
            if(typeof j._call_moment == 'undefined')
                alert(j);
            else
                e.target.parentNode.innerHTML = j._call_moment;

            const message_element = document.getElementById("message");
            message_element.innerHTML = "Your order №" + j;
        })
    }
}

function DeleteCallClick(e)
{
    const call_id = e.target.getAttribute('data-call-id');

    if(confirm("Delete order " + call_id))
    {
        fetch(window.location.href + "?call-id=" + call_id, {method: 'DELETE'}).then(r =>
        {
            if (r.status === 204)
            {
                const tr = e.target.parentNode.parentNode;

                tr.parentNode.removeChild(tr);
            }
            else
                r.json().then(alert);
        });
    }
}
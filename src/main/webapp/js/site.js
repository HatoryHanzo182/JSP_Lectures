document.addEventListener('DOMContentLoaded', function()
{
    M.Modal.init(document.querySelectorAll('.modal'),
        {
            opacity: 0.6,
            inDuration: 200,
            outDuration: 200
        });
    const create_button = document.getElementById("db-create-button");

    if(create_button)
        create_button.addEventListener('click', CreateButtonClick)

    const callme_button = document.getElementById("db-call-me-button");

    if(callme_button)
        callme_button.addEventListener('click', CallmeButtonClick)
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

    fetch(window.location.href, {
            method: 'PATCH',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                name: name_input.value,
                phone: phone_input.value,
            })
        }).then(r => r.json()).then(j =>
    {
        console.log(j);
    });
}
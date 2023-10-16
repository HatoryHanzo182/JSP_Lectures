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
});

function CreateButtonClick()
{
    fetch(window.location.href, { method: 'POST' }).then(r => r.json()).then(j => {console.log(j)});
}
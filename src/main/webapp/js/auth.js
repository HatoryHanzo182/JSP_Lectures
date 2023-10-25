﻿document.addEventListener("DOMContentLoaded", () =>
{
    M.Modal.init(document.querySelectorAll('.modal'),
        {
            opacity: 0.6,
            inDuration: 200,
            outDuration: 200,
            onOpenStart: OnModalOpen
        });

    const auth_modal_sign_in_button = document.getElementById("auth-modal-sign-in-button");

    if (auth_modal_sign_in_button)
        auth_modal_sign_in_button.addEventListener('click', SignInButtonClick);

    const spa_container = document.getElementById("spa-container");

    if (spa_container)
    {
        const token = window.localStorage.getItem("token202");

        if (token)
        {
            try
            {
                const tokenData = JSON.parse(atob(token));
                const expirationTime = new Date(tokenData.exp * 1000);
                const currentTime = new Date();

                if (currentTime > expirationTime)
                {
                    alert("Токен истек. Пожалуйста, выполните вход заново.");

                    window.localStorage.removeItem("token202");
                    window.location.reload();
                }
                else
                    spa_container.innerText = "Срок действия токена истекает: " + expirationTime.toLocaleString();
            }
            catch (ex) { console.error("Ошибка при декодировании токена:", ex); }
        }
        else
            spa_container.innerText = "Токен отсутствует";
    }
});

function OnModalOpen()
{
    const [auth_login, auth_password, auth_message] = GetModalElements();

    auth_login.value = '';
    auth_password.value = '';
    auth_message.innerText = '';
}

function GetModalElements()
{
    const auth_login = document.getElementById('auth-login');

    if(!auth_login)
        throw "#auth-login not found";

    const auth_password = document.getElementById('auth-password');

    if(!auth_password)
        throw "#auth-password not found";

    const auth_message = document.getElementById('auth-message-container');

    if(!auth_message)
        throw "#auth-message-container not found";

    return [auth_login, auth_password, auth_message];
}

function SignInButtonClick()
{
    const [auth_login, auth_password, auth_message] = GetModalElements();
    const login = auth_login.value;
    const password = auth_password.value;

    if(login.length === 0)
    {
        auth_message.innerText = 'Поле логин не может быть пустым!';
        return;
    }
    if(password.length === 0)
    {
        auth_message.innerText = 'Поле пароль не может быть пустым!';
        return;
    }

    auth_message.innerText = '';

    const url = `${GetAppContext()}/auth?login=${login}&password=${password}`;

    fetch(url).then(r=>
    {
        if(r.status === 202)
        {
            r.json().then(t =>
            {
                try
                {
                    const token = JSON.parse(atob(t));

                    if (typeof token.jti === 'undefined')
                        auth_message.innerText = "Целостность токена нарушена";
                    else
                    {
                        window.localStorage.setItem("token202", t);
                        window.location.reload();
                    }
                }
                catch (ex) { auth_message.innerText = 'Ошибка данных!'; }

                console.log(t);
            });
        }
        else
            auth_message.innerText = 'В аутентификации отказано';
    })
}

function GetAppContext()
{
    var is_context_preset = false;
    return (is_context_preset) ? "" : "/" + window.location.pathname.split('/')[1];
}
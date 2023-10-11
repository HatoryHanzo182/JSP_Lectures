<%@ page import="step.learning.dto.models.SignupFormModel" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String reg_data = (String) request.getAttribute( "reg-data" ) ;

    if( reg_data == null )
        reg_data = "" ;

    SignupFormModel form_model = (SignupFormModel) request.getAttribute( "reg-model" ) ;

    Map<String, String> validation_errors = form_model == null ? new HashMap<String, String>() : form_model.GetValidationErrorMessage();
%>
<h2>Sign up</h2>
<p>
    <% if (validation_errors.containsKey("login")) { %>
    Есть ошибка проверки логина: <b><%= validation_errors.get("login") %></b> /
    <% } %>
    <% if (validation_errors.containsKey("name")) { %>
    Есть ошибка проверки имени: <b><%= validation_errors.get("name") %></b> /
    <% } %>
    <% if (validation_errors.containsKey("email")) { %>
    Есть ошибка проверки email: <b><%= validation_errors.get("email") %></b> /
    <% } %>
    <% if (validation_errors.containsKey("password")) { %>
    Есть ошибка проверки пароля: <b><%= validation_errors.get("password") %></b> /
    <% } %>
    <% if (validation_errors.containsKey("password_repeat")) { %>
    Есть ошибка проверки повтора пароля: <b><%= validation_errors.get("password_repeat") %></b> /
    <% } %>
    <% if (validation_errors.containsKey("birthdate")) { %>
    Есть ошибка проверки даты рождения: <b><%= validation_errors.get("birthdate") %></b> /
    <% } %>
    <% if (validation_errors.containsKey("agree")) { %>
    Вы должны согласиться с условиями: <b><%= validation_errors.get("agree") %></b> /
    <% } %>
</p>
<div class="row">
    <form class="col s12" action="" method="post">
        <div class="row">
            <div class="input-field col s6">
                <i class="material-icons prefix">person</i>
                <input id="reg-login" name="reg-login" type="text" class="validate" value="UserLogin">
                <label for="reg-login">Login</label>
            </div>
            <div class="input-field col s6">
                <i class="material-icons prefix">badge</i>
                <input id="reg-name" name="reg-name" type="text" class="validate" value="UserName">
                <label for="reg-name">User name</label>
            </div>
        </div>
        <div class="row">
            <div class="input-field col s6">
                <i class="material-icons prefix">alternate_email</i>
                <input id="reg-email" name="reg-email" type="text" class="validate" value="user@gmail.com">
                <label for="reg-email">Email</label>
            </div>
            <div class="input-field col s6">
                <i class="material-icons prefix">child_friendly</i>
                <input id="reg-birthdate" name="reg-birthdate" type="date" class="validate">
                <label for="reg-birthdate">Birthday</label>
            </div>
        </div>
        <div class="row">
            <div class="input-field col s6">
                <i class="material-icons prefix">lock</i>
                <input id="reg-password" name="reg-password" type="password" class="validate" value="123">
                <label for="reg-password">Password</label>
            </div>
            <div class="input-field col s6">
                <i class="material-icons prefix">lock_open</i>
                <input id="reg-repeat" name="reg-repeat" type="password" class="validate" value="123">
                <label for="reg-repeat">Repeat password</label>
            </div>
        </div>
        <div class="row">
            <div class="input-field col s6">
                <i class="material-icons prefix">fact_check</i>
                <label>
                    <input name="reg-agree" type="checkbox" class="filled-in"/>
                    <span>I won't break anything</span>
                </label>
            </div>
            <div class="input-field col s6 right-align">
                <button class="waves-effect light-blue btn">
                    <i class="material-icons left">cloud</i>Sign up
                </button>
            </div>
        </div>
    </form>
</div>
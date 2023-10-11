<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String reg_data = (String) request.getAttribute("reg-data");

    if(reg_data == null)
        reg_data = "";
%>
<h2>Sign up</h2>
<p><%=reg_data%></p>
<div class="row">
    <form class="col s12" action="" method="post">
        <div class="row">
            <div class="input-field col s6">
                <i class="material-icons prefix">account_circle</i>
                <input id="reg-login" name="reg-login" type="text" class="validate" value="UserLogin">
                <label for="reg-login">Login</label>
            </div>
            <div class="input-field col s6">
                <i class="material-icons prefix">phone</i>
                <input id="reg-name" name="reg-login" type="text" class="validate" value="UserName">
                <label for="reg-name">User name</label>
            </div>
        </div>
        <div class="row">
            <div class="input-field col s6">
                <i class="material-icons prefix">account_circle</i>
                <input id="reg-email" name="reg-email" type="text" class="validate" value="user@gmail.com">
                <label for="reg-email">Email</label>
            </div>
            <div class="input-field col s6">
                <i class="material-icons prefix">phone</i>
                <input id="reg-birthday" name="reg-email" type="date" class="validate">
                <label for="reg-birthday">Birthday</label>
            </div>
        </div>
        <div class="row">
            <div class="input-field col s6">
                <i class="material-icons prefix">account_circle</i>
                <input id="reg-password" name="reg-password" type="text" class="validate" value="123">
                <label for="reg-password">Password</label>
            </div>
            <div class="input-field col s6">
                <i class="material-icons prefix">phone</i>
                <input id="reg-repeat" name="reg-password" type="text" class="validate" value="123">
                <label for="reg-repeat">Repeat password</label>
            </div>
        </div>
        <div class="row">
            <div class="input-field col s6">
                <label>
                    <input type="checkbox" class="filled-in"/>
                    <span>I won't break anything</span>
                </label>
            </div>
            <div class="input-field col s6 right-align">
                <button class="waves-effect waves-light btn">
                    <i class="material-icons left">cloud</i>Sign up
                </button>
            </div>
        </div>
    </form>
</div>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<h1>Инверсия управления в веб-приложениях</h1>
<ul class="collection">
    <li class="collection-item">
        Добавляем Maven зависимости для
        <a href="https://mvnrepository.com/artifact/com.google.inject/guice/6.0.0">Guice</a><br>
        Pасширение
        <a href="https://mvnrepository.com/artifact/com.google.inject.extensions/guice-servlet/6.0.0">Guice Servlet</a>
    </li>
    <li class="collection-item">
        Создаем пакет ioc, в нем конфигурационные классы
        IocContextListener - обработчик события создания контекста (
        определенный аналог стартового метода main)<br>
        RouterModule - класс с настройками фильтров и сервлетов,
        ServicesModule - класс с настройками служб (сервисов)
    </li>
    <li class="collection-item">
        Меняем web.xml – оставляем фильтр от Guice и наш
        слушатель события создания контекста (см. web.xml)
    </li>
    <li class="collection-item">
        !!! для всех фильтров и сервлетов, заявленных в RouterModule
        необходимо добавить аннотацию @Singleton
    </li>
    <li class="collection-item">
        Инжекция служб осуществляется так же, как и в консольном проекте.<br>
        Проверка: <%= request.getAttribute("hash") %>
    </li>
</ul>
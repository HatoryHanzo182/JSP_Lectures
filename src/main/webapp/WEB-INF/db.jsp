<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<h1>Работа с базами данных</h1>
<h2>JDBC</h2>
<p>
    JDBC (Java Database Connectivity) - это Java-технология, которая предоставляет стандартный способ взаимодействия с реляционными<br>
    базами данных из приложений, написанных на языке программирования Java. С использованием JDBC, вы можете устанавливать<br>
    соединение с базой данных, выполнять SQL-запросы, получать и обновлять данные в базе данных.<br>
</p>
<p>
    Вот основные компоненты и концепции JDBC:<br><br>
    JDBC API: Это набор интерфейсов и классов, предоставляющих методы для взаимодействия с базами данных.<br>
    Основные пакеты JDBC включают java.sql для основных операций с базой данных и javax.sql для более расширенных функций и поддержки пула соединений.<br><br>
    Драйверы JDBC: Драйвер JDBC - это специальный программный модуль, который позволяет Java-приложению общаться с конкретной СУБД.<br> <br>
    Существует четыре типа драйверов JDBC:<br>
    Тип 1: JDBC-ODBC мост, который использует ODBC (Open Database Connectivity) для связи с базой данных. Тип 1 драйверы устаревшие и редко используются.<br>
    Тип 2: Драйверы, написанные на языке C/C++, которые используют Java Native Interface (JNI) для взаимодействия с базой данных.<br>
    Тип 3: Сетевой драйвер, который взаимодействует с базой данных через промежуточный сервер, обычно использующий протоколы, такие как TCP/IP.<br>
    Тип 4: Чистые Java-драйверы, которые непосредственно общаются с базой данных через сетевые протоколы. Они обычно предпочтительны в современных приложениях.<br><br>
    Установление соединения: Для установления соединения с базой данных используется класс Connection. <br>
    Вы можете указать URL базы данных, имя пользователя и пароль для доступа к базе данных.<br><br>
    Выполнение SQL-запросов: Для выполнения SQL-запросов используется класс Statement. Вы можете выполнять запросы SELECT, INSERT, UPDATE, DELETE, и многие другие.<br>
    Обработка результатов: JDBC предоставляет классы, такие как ResultSet, для обработки результатов запросов.<br>
    Вы можете извлекать данные из результата запроса и обрабатывать их в Java-приложении.<br>
    Обработка исключений: При работе с JDBC, важно обрабатывать исключения, такие как SQLException, которые могут<br>
    возникнуть при взаимодействии с базой данных.<br>
</p>
<h2>ТЕСТ</h2>
<div class="row">
    Создать таблицу БД.
    <button id="db-create-button" class="waves-effect light-blue btn"><i class="material-icons right">cloud</i>Create</button>
    <div id="message"></div>
</div>
<div class="row">
    <form class="col s12">
        <div class="row">
            <div class="input-field col s4">
                <i class="material-icons prefix">badge</i>
                <input placeholder="Input your full name" id="db-call-me-name" type="text" class="validate">
                <label for="db-call-me-name">Name</label>
            </div>
        </div>
        <div class="input-field col s6">
            <i class="material-icons prefix">call</i>
            <input placeholder="+380 XX XXX XX XX" id="db-call-me-phone" type="tel" class="validate">
            <label for="db-call-me-phone">Phone number</label>
        </div>
        <div class="row">
            <button type="button" id="db-call-me-button" class="waves-effect light-blue btn"><i class="material-icons right">cloud</i>Call me</button>
        </div>
    </form>
</div>
<br>
<br>
<h2>Logging</h2>
<p>
    В целях обеспечения возможности отработки ошибок в работе
    программы, сообщения о них должны храниться (на постоянной
    основе, не только в консоли).<br> Эта задача возлагается на систему
    логирование (логер), который средствами Guice поставляется "из коробки".
    Для изменения стандартных настроек можно воспользоваться или
    программными инструкциями или файлом конфигурации.<br> Детали – в
    классе LoggingModule. При работе именно с БД рекомендовано
    сохранять тексты SQL запросов (вместе с сообщениями о
    ошибку), поскольку это упростит исправление их синтаксических
    ошибок.<br>
</p>
<div class="row">
    <button type="button" id="db-get-all-button" class="waves-effect light-blue btn"><i class="material-icons right">cloud</i>Просмотреть</button>
</div>
<div id="db-get-all-container"></div>
<div id="message2"></div>
<#macro page>
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8"/>
        <title>Sweater</title>
        <meta name="viewport"
              content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0"/>
        <meta http-equiv="X-UA-Compatible" content="ie=edge"/>
            <link rel="stylesheet" href="/static/css/bootstrap.min.css"/>
        <link rel="stylesheet" href="/static/css/sidebar.css"/>
        <link rel="stylesheet" href="/static/css/Dashboard.css"/>
        <link rel="stylesheet" href="/static/css/Buzz.css"/>

        <link href='https://unpkg.com/boxicons@2.0.7/css/boxicons.min.css' rel='stylesheet'>
    </head>
    <body>


    <#--        <li class="nav-item">-->
    <#--            <a class="nav-link" aria-current="page" href="/">Home</a>-->
    <#--        </li>-->
    <#--        <#if isAdmin>-->
    <#--            <li class="nav-item">-->
    <#--                <a class="nav-link" aria-current="page" href="/user">User list</a>-->
    <#--            </li>-->
    <#--        </#if>-->
    <#--        <#if user?? && currentUserId != -1>-->
    <#--            <li class="nav-item">-->
    <#--                <a class="nav-link" aria-current="page" href="/user/profile">Profile</a>-->
    <#--            </li>-->
    <#--        </#if>-->

    <#nested>

    <script src="/static/js/sidebar.js"></script>
    </body>
    </html>
</#macro>
<#macro page>
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8"/>
        <title>Social Sprout</title>
        <meta name="viewport"
              content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0"/>
        <meta http-equiv="X-UA-Compatible" content="ie=edge"/>
        <link rel="stylesheet" href="/static/css/bootstrap.min.css"/>
        <link rel="stylesheet" href="/static/css/sidebar.css"/>
        <link rel="stylesheet" href="/static/css/greeting.css"/>
        <link rel="stylesheet" href="/static/css/Auth.css"/>
        <link href='https://unpkg.com/boxicons@2.1.1/css/boxicons.min.css' rel='stylesheet'>
        <script src="https://kit.fontawesome.com/fe369707df.js" crossorigin="anonymous"></script>
        <script type="text/javascript" src="https://unpkg.com/react/umd/react.production.min.js"></script>
        <script type="text/javascript" src="https://unpkg.com/react-dom/umd/react-dom.production.min.js"></script>
<#--        <script src="https://cdnjs.cloudflare.com/ajax/libs/turbolinks/5.2.0/turbolinks.js"></script>-->

<#--        <script defer src="/static/js/jquery-3.6.1.slim.min.js"></script>-->
<#--        <script defer src="/static/js/popper.min.js"></script>-->
<#--        <style>-->
<#--            .turbolinks-progress-bar {-->
<#--                height: 2px;-->
<#--                background-color: navy;-->
<#--            }-->
<#--        </style>-->
    </head>
    <body>
    <#include "navbar.ftl">
    <div class="container mt-5">
        <#nested>
    </div>

    <script src="/static/js/react-select.min.js"></script>
    <script src="/static/js/bootstrap.min.js"></script>
    <script src="/static/js/jquery-3.6.1.slim.min.js"></script>
    <script src="/static/js/popper.min.js"></script>
    </body>
    </html>
</#macro>
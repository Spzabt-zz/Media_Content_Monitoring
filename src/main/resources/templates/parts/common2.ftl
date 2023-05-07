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
        <link rel="stylesheet" href="/static/css/tabsInaminDashboard.css">

        <link href='https://unpkg.com/boxicons@2.0.7/css/boxicons.min.css' rel='stylesheet'>
<#--        <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/4.3.0/chart.min.js"></script>-->
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<#--        <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.9.3/Chart.min.js"></script>-->

    </head>
    <body>

    <#nested>

    <script src="/static/js/bootstrap.min.js"></script>
    <script src="/static/js/sidebar.js"></script>

    <script src="/static/js/tabsInMainDashboard.js"></script>

    <script src="/static/js/jquery-3.6.1.slim.min.js"></script>
    <script src="/static/js/popper.min.js"></script>
    </body>
    </html>
</#macro>
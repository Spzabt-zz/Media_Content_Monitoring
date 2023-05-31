<#macro page>
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8"/>
        <title>Social Monitoring</title>
        <meta name="viewport"
              content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0"/>
        <meta http-equiv="X-UA-Compatible" content="ie=edge"/>
        <link rel="stylesheet" href="/static/css/bootstrap.min.css"/>
        <link rel="stylesheet" href="/static/css/sidebar.css"/>
        <link rel="stylesheet" href="/static/css/greeting.css"/>
        <link rel="stylesheet" href="/static/css/auth.css"/>
        <link rel="stylesheet" href="/static/css/auth.css"/>
        <link rel="stylesheet" href="/static/css/spin.css"/>
        <link rel="stylesheet" href="/static/css/stuffForSpinner.css"/>

        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bulma@0.9.4/css/bulma.min.css">
        <link href="https://unpkg.com/@yaireo/tagify/dist/tagify.css" rel="stylesheet" type="text/css" />

        <link href='https://unpkg.com/boxicons@2.1.1/css/boxicons.min.css' rel='stylesheet'>
        <script src="https://kit.fontawesome.com/fe369707df.js" crossorigin="anonymous"></script>
        <script type="text/javascript" src="https://unpkg.com/react/umd/react.production.min.js"></script>
        <script type="text/javascript" src="https://unpkg.com/react-dom/umd/react-dom.production.min.js"></script>

        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/chartjs-plugin-datalabels@2.0.0"></script>

        <script src="https://d3js.org/d3.v7.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/d3-cloud@1.2.5/build/d3.layout.cloud.min.js"></script>

        <script src="/static/js/spin.js"></script>
    </head>
    <body>
    <div id="spinnerContainer"></div>
    <div id="overlay"></div>
    <#include "navbar.ftl">
    <div class="container mt-5">
        <#nested>
    </div>

    <script src="https://unpkg.com/@yaireo/tagify"></script>
    <script src="https://unpkg.com/@yaireo/tagify@3.1.0/dist/tagify.polyfills.min.js"></script>

    <script src="/static/js/initSpinner.js"></script>
    <script src="/static/js/keyword.js"></script>
    <script src="/static/js/bootstrap.min.js"></script>
    <script src="/static/js/jquery-3.6.1.slim.min.js"></script>
    <script src="/static/js/popper.min.js"></script>
    </body>
    </html>
</#macro>
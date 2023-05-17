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
        <link rel="stylesheet" href="/static/css/dashboard.css"/>
        <link rel="stylesheet" href="/static/css/mentions.css"/>
        <link rel="stylesheet" href="/static/css/tabsInMainDashboard.css"/>
        <link rel="stylesheet" href="/static/css/sentimentChartsCont.css"/>
        <link rel="stylesheet" href="/static/css/mentionsChartCont.css"/>
        <link rel="stylesheet" href="/static/css/wordCloud.css"/>
        <link rel="stylesheet" href="/static/css/loader.css"/>

        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bulma@0.9.4/css/bulma.min.css"/>
        <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'/>
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/chartjs-plugin-datalabels@2.0.0"></script>

        <script src="https://d3js.org/d3.v7.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/d3-cloud@1.2.5/build/d3.layout.cloud.min.js"></script>
    </head>
    <body>

    <#nested>

    <script src="/static/js/sidebar.js"></script>
    <script src="/static/js/sentimentPieGraph.js"></script>
    <script src="/static/js/sentimentOfMentionsChart.js"></script>
    <script src="/static/js/mentionsChart.js"></script>
    <script src="/static/js/reachChart.js"></script>
    <script src="/static/js/wordCloud.js"></script>
    <script src="/static/js/tabsInMainDashboard.js"></script>
    <script src="/static/js/mentionsBySourcesDiagram.js"></script>

    <script src="/static/js/bootstrap.min.js"></script>
    <script src="/static/js/jquery-3.6.1.slim.min.js"></script>
    <script src="/static/js/popper.min.js"></script>
    </body>
    </html>
</#macro>
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
</head>
<body>
<div class="home-content">

    <div class="tab-container">
        <div class="tab">
            <img src="/static/img/assets/browser.png" height="24" width="24"/>
        </div>

        <div class="tabs-container">
            <div id="tab1" class="tabcontent">
                <ol class="list-group">
                    <#if popularMentions??>
                        <#list popularMentions as mention>
                            <li class="list-group-item d-flex justify-content-between align-items-start">
                                <div class="ms-2 me-auto">
                                    <div class="fw-bold">Source - ${mention.platformName}</div>
                                    <#if mention.ytVideoId != "">
                                        Text - <a href="https://www.youtube.com/watch?v=${mention.ytVideoId}">${mention.text}</a>
                                    <#elseif mention.subUrl != "">
                                        Text - <a href="${mention.subUrl}">${mention.text}</a>
                                    <#elseif mention.twLink != "">
                                        Text - <a href="${mention.twLink}">${mention.text}</a>
                                    </#if>
                                </div>
                                <span class="badge bg-primary rounded-pill">
                                     <#if mention.sentiment??>
                                         Sentiment - ${mention.sentiment}
                                     <#else>
                                         no data
                                     </#if>
                                </span>
                            </li>
                        </#list>
                    </#if>
                </ol>
            </div>
        </div>
    </div>

    <div>Total reach - ${totalReachCount}</div>
    <div>Total mentions - ${totalMentionCount}</div>
    <div>Total retweets - ${totalRepostCount}</div>
    <div>Total likes - ${totalLikesCount}</div>
    <div>Negative sentiment - ${totalNegativeCount}</div>
    <div>Positive sentiment - ${totalPositiveCount}</div>
</div>
</body>
</html>
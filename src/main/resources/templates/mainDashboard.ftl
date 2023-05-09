<#import "parts/common2.ftl" as c>


<@c.page>
    <#include "parts/sideBarDashboardMenu.ftl">

    <section class="home-section">
        <nav>
            <div class="sidebar-button">
                <i class='bx bx-menu sidebarBtn'></i>
            </div>
        </nav>
        <div class="home-content">

            <div class="chart-container">
                <canvas id="sentimentPie" width="200" height="200"></canvas>
                <canvas id="sentimentChart" width="600" height="200"></canvas>
            </div>

            <div class="chart-container2">
                <canvas id="mentionsChart" width="600" height="200"></canvas>
            </div>
            <div id="word-cloud"></div>

            <script>
                const sentimentPieData = ${sentimentPieData};
            </script>

            <script>
                const sentimentChartData = ${sentimentChartData}
            </script>

            <script>
                const mentionChartData = ${mentionChartData}
            </script>

            <script>
                const words = ${words};
            </script>

            <div class="tab">
                <button class="tablinks" onclick="openTab(event, 'tab1')">
                    <img src="/static/img/assets/youtube.png"/>
                </button>
                <button class="tablinks" onclick="openTab(event, 'tab2')">
                    <img src="/static/img/assets/reddit.png"/>
                </button>
                <button class="tablinks" onclick="openTab(event, 'tab3')">
                    <img src="/static/img/assets/twitter.png" height="24" width="24"/>
                </button>
            </div>

            <div class="tabs-container" style="height: 400px; overflow: auto;">
                <div id="tab1" class="tabcontent">
                    <ol class="list-group">
                        <#list allYTData as yt>
                            <li class="list-group-item d-flex justify-content-between align-items-start">
                                <div class="ms-2 me-auto">
                                    <div class="fw-bold">YouTube</div>
                                    <a href="https://www.youtube.com/watch?v=${yt.videoId}">${yt.comment}</a>
                                </div>
                                <span class="badge bg-primary rounded-pill">
                                     <#if yt.sentiment??>
                                         ${yt.sentiment}
                                     <#else>
                                         no data
                                     </#if>
                                </span>
                            </li>
                        </#list>
                    </ol>
                </div>

                <div id="tab2" class="tabcontent">
                    <ol class="list-group">
                        <#list allRedditData as reddit>
                            <li class="list-group-item d-flex justify-content-between align-items-start">
                                <div class="ms-2 me-auto">
                                    <div class="fw-bold">Reddit</div>
                                    <a href="${reddit.subUrl}">
                                        <#if reddit.subBody == "">
                                            ${reddit.subTitle}
                                        <#else>
                                            ${reddit.subBody}
                                        </#if>
                                    </a>
                                </div>
                                <span class="badge bg-primary rounded-pill">
                                    <#if reddit.sentiment??>
                                        ${reddit.sentiment}
                                    <#else>
                                        no data
                                    </#if>
                                </span>
                            </li>
                        </#list>
                    </ol>
                </div>

                <div id="tab3" class="tabcontent">
                    <ol class="list-group">
                        <#list allTwitterData as twitter>
                            <li class="list-group-item d-flex justify-content-between align-items-start">
                                <div class="ms-2 me-auto">
                                    <div class="fw-bold">Twitter</div>
                                    <a href="${twitter.link}">${twitter.tweet}</a>
                                </div>
                                <span class="badge bg-primary rounded-pill">
                                    <#if twitter.sentiment??>
                                        ${twitter.sentiment}
                                    <#else>
                                        no data
                                    </#if>
                                </span>
                            </li>
                        </#list>
                    </ol>
                </div>
            </div>
        </div>
    </section>

</@c.page>

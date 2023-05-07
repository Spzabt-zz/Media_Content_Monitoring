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

            <canvas id="sentimentChart"></canvas>
            <script>
                (async function () {
                    const data =
                        ${charData}
                        // {date: 2010, sentiment: {positive: 10, negative: 3}},
                        // {date: 2011, sentiment: {positive: 2, negative: 20}},
                        // {date: 2012, sentiment: {positive: 10, negative: 7}},
                        // {date: 2013, sentiment: {positive: 5, negative: 9}},
                        // {date: 2014, sentiment: {positive: 3, negative: 30}},
                        // {date: 2015, sentiment: {positive: 54, negative: 120}},
                        // {date: 2016, sentiment: {positive: 50, negative: 32}},
                    ;

                    new Chart(
                        document.getElementById('sentimentChart'),
                        {
                            type: 'line',
                            data: {
                                labels: data.map(row => row.date),
                                datasets: [
                                    {
                                        label: 'Positive',
                                        data: data.map(row => row.sentiment.positive),
                                        fill: false,
                                        borderColor: 'rgb(0,255,49)',
                                        tension: 0.3
                                    },
                                    {
                                        label: 'Negative',
                                        data: data.map(row => row.sentiment.negative),
                                        fill: false,
                                        borderColor: 'rgb(255,0,0)',
                                        tension: 0.3
                                    }
                                ]
                            }
                        }
                    );
                })();
            </script>

            <#-------------------------------------------------------------------------->

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

            <div id="tab1" class="tabcontent" style="overflow-y: auto">
                <h3>YouTube</h3>
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

            <div id="tab2" class="tabcontent" style="overflow-y: auto">
                <h3>Reddit</h3>
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

            <div id="tab3" class="tabcontent" style="overflow-y: auto">
                <h3>Twitter</h3>
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
    </section>

</@c.page>

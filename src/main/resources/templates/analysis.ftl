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

            <div class="tab-container">
                <div class="tab">
                    <button class="tablinks" onclick="openTab(event, 'tab1')">
                        <img src="/static/img/assets/browser.png" height="24" width="24"/>
                    </button>
                </div>

                <div class="tabs-container" style="height: 500px; overflow: auto;">
                    <div id="tab1" class="tabcontent">
                        <ol class="list-group">
                            <#if popularMentions??>
                                <#list popularMentions as mention>
                                    <li class="list-group-item d-flex justify-content-between align-items-start">
                                        <div class="ms-2 me-auto">
                                            <div class="fw-bold">All sources</div>
                                            <#if mention.ytVideoId != "">
                                                <a href="https://www.youtube.com/watch?v=${mention.ytVideoId}">${mention.text}</a>
                                            <#elseif mention.subUrl != "">
                                                <a href="${mention.subUrl}">${mention.text}</a>
                                            <#elseif mention.twLink != "">
                                                <a href="${mention.twLink}">${mention.text}</a>
                                            </#if>
                                        </div>
                                        <span class="badge bg-primary rounded-pill">
                                     <#if mention.sentiment??>
                                         ${mention.sentiment}
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

            <div class="chart-container">
                <canvas id="mentionsPie" width="200" height="200"></canvas>
            </div>

            <script>
                const mentionsBysourceCount = ${mentionsBySourcePieData}
            </script>
        </div>
    </section>
</@c.page>
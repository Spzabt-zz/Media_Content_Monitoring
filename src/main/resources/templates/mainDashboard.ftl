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

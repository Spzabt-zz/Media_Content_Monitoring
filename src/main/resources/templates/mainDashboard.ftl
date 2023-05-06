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

            <div class="buzz">
                <#-- Social Media Buzz section -->
                <#assign active = "All">  <#-- Default active section is "All" -->
                <#assign allData = getData()>  <#-- Function call to get all data -->

                <div class="card-title text-left">Social Media Buzz</div>

                <article class="panel is-info">
                    <p class="panel-tabs is-boxed">
                        <#-- All section -->
                        <a href="javascript:void(0);" class="d-flex<#if active=='All'> is-active</#if>"
                           onclick="handleActiveClick('All')">
                    <div class="mr-2"><img src="/static/img/assets/browser.png" style="height: 20px; width: 20px;">
                    </div>
                    All
                    </a>

                    <#-- Reddit section -->
                    <a href="javascript:void(0);" class="d-flex<#if active=='Reddit'> is-active</#if>"
                       onclick="handleActiveClick('Reddit')">
                        <div class="mr-2"><img src="/static/img/assets/reddit.png" style="height: 20px; width: 20px;">
                        </div>
                        Reddit
                    </a>

                    <#-- Twitter section -->
                    <a href="javascript:void(0);" class="d-flex<#if active=='Twitter'> is-active</#if>"
                       onclick="handleActiveClick('Twitter')">
                        <div class="mr-2"><img src="/static/img/assets/twitter.png" style="height: 20px; width: 20px;">
                        </div>
                        Twitter
                    </a>

                    <#-- YouTube section -->
                    <a href="javascript:void(0);" class="d-flex<#if active=='YouTube'> is-active</#if>"
                       onclick="handleActiveClick('YouTube')">
                        <div class="mr-2"><img src="/static/img/assets/youtube.png" style="height: 20px; width: 20px;">
                        </div>
                        YouTube
                    </a>
                    </p>

                    <#-- BuzzBody section -->
                    <#list active=='All' ? allData : allData?filter(d => d.site == active) as filteredData>
                        <div class="panel-block">
                            <div class="buzzcard">
                                <div class="flex-column">
                                    <div class="d-flex ml-2 font-weight-bold">
                                        <div><img src="${imgHandler(filteredData.site)}"
                                                  style="height: 20px; width: 20px; border-radius: 60%;"></div>
                                        <span class="ml-2">${filteredData.site}</span>
                                    </div>
                                    <div class="ml-2" style="width: 520px;">${filteredData.title}</div>
                                </div>
                                <div class="flex-column">
                                    <div class="mr-2"><img src="${basePath}/images/upvote.png"
                                                           style="height: 20px; width: 20px; border-radius: 60%;"></div>
                                    <div>${filteredData.outreach}</div>
                                </div>
                            </div>
                        </div>
                    </#list>
                </article>

                <script>
                    function handleActiveClick(activeSection) {
                        document.querySelectorAll('.panel-tabs a').forEach((el) => {
                            el.classList.remove('is-active');
                        });
                        document.querySelector('.panel-tabs a.' + activeSection).classList.add('is-active');
                        active = activeSection;
                        renderBuzzCards();
                    }

                    function renderBuzzCards() {
                        const buzzBody = document.querySelector('.buzz .panel-blocks');
                        buzzBody.innerHTML = '';
                        allData.forEach((data) => {
                            if (active === 'All' || data.site === active) {
                                buzzBody.innerHTML += `
        <div class="panel-block">
          <div class="buzzcard">
            <div class="flex-column">
              <div class="d-flex ml-2 font-weight-bold">
                <div><img src="${imgHandler(data.site)}" style="height: 20px; width: 20px; border-radius: 60%;"></div>
                <span class="ml-2">${data.site}</span>
              </div>
              <div class="ml-2" style="width: 520px;">${data.title}</div>
            </div>
            <div class="flex-column">
              <div class="mr-2"><img src="${basePath}/images/upvote.png" style="height: 20px; width: 20px; border-radius: 60%;"></div>
              <div>${data.outreach}</div>
            </div>
          </div>
        </div>
      `;
                            }
                        });
                    }
                </script>
            </div>


            <#--            <article class="panel is-info">-->
            <#--                <p class="panel-tabs is-boxed">-->
            <#--                    <a name="All" class=<#if active == "All">"d-flex d-flex is-active"<#else>"d-flex"</#if> onClick=(e => handleActiveClick("All"))>-->
            <#--                <div class="mr-2"><img src="/static/img/assets/browser.png" style="height: 20px; width: 20px;" /></div>-->
            <#--                All-->
            <#--                </a>-->
            <#--                <a name="Reddit" class=<#if active == "Reddit">"d-flex is-active"<#else>"d-flex"</#if> onClick=(e => handleActiveClick("Reddit"))>-->
            <#--                <div class="mr-2"><img src="/static/img/assets/reddit.png" style="height: 20px; width: 20px" /></div>-->
            <#--                Reddit-->
            <#--                </a>-->
            <#--                <a name="Youtube" class=<#if active == "Youtube">"d-flex is-active"<#else>"d-flex"</#if> onClick=(e => handleActiveClick("Youtube"))>-->
            <#--                <div class="mr-2"><img src="/static/img/assets/youtube.png" style="height: 20px; width: 20px" /></div>-->
            <#--                Youtube-->
            <#--                </a>-->
            <#--                <a name="Twitter" class=<#if active == "Twitter">"d-flex is-active"<#else>"d-flex"</#if> onClick=(e => handleActiveClick("Twitter"))>-->
            <#--                <div class="mr-2"><img src="/static/img/assets/twitter.png" style="height: 20px; width: 20px" /></div>-->
            <#--                Twitter-->
            <#--                </a>-->
            <#--                </p>-->

            <#--                <#if active == "All">-->
            <#--                    <#list Data as i>-->
            <#--                        <BuzzBody data=${i} />-->
            <#--                    </#list>-->
            <#--                <#elseif active == "Reddit">-->
            <#--                    <#list Data?filter(i => i.site == "Reddit") as i>-->
            <#--                        <BuzzBody data=${i} />-->
            <#--                    </#list>-->
            <#--                <#elseif active == "Youtube">-->
            <#--                    <#list Data?filter(i => i.site == "Youtube") as i>-->
            <#--                        <BuzzBody data=${i} />-->
            <#--                    </#list>-->
            <#--                <#elseif active == "Twitter">-->
            <#--                    <#list Data?filter(i => i.site == "Twitter") as i>-->
            <#--                        <BuzzBody data=${i} />-->
            <#--                    </#list>-->
            <#--                <#else>-->
            <#--                &lt;#&ndash; Do nothing if active is not "All", "Reddit", "Youtube", or "Twitter" &ndash;&gt;-->
            <#--                </#if>-->
            <#--            </article>-->


        </div>

    </section>

</@c.page>

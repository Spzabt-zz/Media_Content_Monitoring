<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">

<@c.page>
    <div class="homepage">
        <div class="home-nav">
            <div class="home-name ml-6 mt-3">Social Monitoring</div>
        </div>
        <hr class="container" style="outline: none; border: none; background-color: black; height: 0.5px; width: 80%;"/>

        <div class="home-text mt-5">
            <h5>Hello, ${username!"guest"}!</h5> <br>
            Analyse and Understand how the internet perceives your brand. <br/>Create the best possible persona for your
            brand on the internet with this visualization tool.
        </div>

        <#if user?? && currentUserId != -1>
        <#elseif currentUserId == -1>
            <div class="home-signup">
                <a href="/registration">
                    <button className="btn-signup">Create your dashboard</button>
                </a>
            </div>
        </#if>
    </div>
</@c.page>
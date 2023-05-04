<#import "login.ftl" as l>
<#include "security.ftl">

<nav class="navbar navbar-expand-lg bg-light">
    <div class="container-fluid">
        <a class="navbar-brand" href="/">Social Sprout</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent"
                aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
<#--                <li class="nav-item">-->
<#--                    <a class="nav-link" aria-current="page" href="/">Home</a>-->
<#--                </li>-->
<#--                <#if user?? && currentUserId != -1>-->
<#--                    <li class="nav-item">-->
<#--                        <a class="nav-link" aria-current="page" href="/dashboard">Dashboard</a>-->
<#--                    </li>-->
<#--                </#if>-->
                <#if isAdmin>
                    <li class="nav-item">
                        <a class="nav-link" aria-current="page" href="/user">User list</a>
                    </li>
                </#if>
                <#if user?? && currentUserId != -1>
                    <li class="nav-item">
                        <a class="nav-link" aria-current="page" href="/user/profile">Profile</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" aria-current="page" href="/panel">Projects</a>
                    </li>
                </#if>
            </ul>

            <div class="navbar-text me-3"><#if user?? && currentUserId != -1>${name}<#else>Please, login</#if></div>

            <@l.logout user?? currentUserId/>

        </div>
    </div>
</nav>
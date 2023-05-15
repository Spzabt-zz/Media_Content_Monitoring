<#import "login.ftl" as l>
<#include "security.ftl">

<div class="sidebar">
    <div class="logo-details">
        <i class='bx bxl-meta'></i>
        <span class="logo_name">Social Monitoring</span>
    </div>
    <ul class="nav-links">
        <li>
            <a href="/panel/results/${project.id}" class="active">
                <i class='bx bx-pie-chart-alt-2'></i>
                <span class="links_name">Mentions</span>
            </a>
        </li>
        <li>
            <a href="/panel/analysis/${project.id}">
                <i class='bx bx-analyse' ></i>
                <span class="links_name">Analysis</span>
            </a>
        </li>
        <li>
            <a href="/panel/sources/${project.id}">
                <i class='bx bx-link-external'></i>
                <span class="links_name">Sources</span>
            </a>
        </li>
        <li>
            <a href="/panel/compare/${project.id}">
                <i class='bx bx-git-compare' ></i>
                <span class="links_name">Comparison</span>
            </a>
        </li>
        <li>
            <a href="/panel/report/${project.id}">
                <i class='bx bxs-report' ></i>
                <span class="links_name">PDF report</span>
            </a>
        </li>
        <li>
            <a href="/edit-project/${project.id}">
                <i class='bx bx-cog'></i>
                <span class="links_name">Project settings</span>
            </a>
        </li>
        <li>
            <a href="/panel">
                <i class='bx bx-list-ul' ></i>
                <span class="links_name">Project list</span>
            </a>
        </li>
        <li class="log_out">
            <form action="/logout" method="post">
                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                <a onclick="this.closest('form').submit();return false;">
                    <i class='bx bx-log-out'></i>
                    <span class="links_name">Log out</span>
                </a>
            </form>
        </li>
    </ul>
</div>

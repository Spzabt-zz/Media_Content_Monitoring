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

            <#if message??>
                <div class="alert alert-${messageType}" role="alert">
                    ${message}
                </div>
            </#if>

            <table class="table">
                <thead>
                <tr>
                    <th scope="col">Project name</th>
                    <th scope="col">Created at</th>
                    <th scope="col">Project options</th>
                </tr>
                </thead>
                <tbody class="table-group-divider">
                <#list projects as project>
                    <tr>
                        <td>
                            <h3>${project.name}</h3>
                        </td>
                        <td>${project.createdAt}</td>
                        <td>
                            <form action="/panel/report/${project.id}" method="post">
                                <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                                <a href="/panel/report/${project.id}" onclick="this.closest('form').submit();return false;">Generate report</a>
                            </form>
                        </td>
                    </tr>
                </#list>
                </tbody>
            </table>

        </div>
    </section>

</@c.page>
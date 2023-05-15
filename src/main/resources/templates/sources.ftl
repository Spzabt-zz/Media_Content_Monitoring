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

            <div class="d-flex justify-content-between align-items-center">
                <h2>Sources</h2>
            </div>
            <br>
            <h5>Select social media from the list and analyse data by selected data source!</h5>

            <table class="table">
                <thead>
                <tr>
                    <th scope="col">Source name</th>
                </tr>
                </thead>
                <tbody class="table-group-divider">
                <#list sources as source>
                    <tr>
                        <td>
                            <a href="/panel/results/${projectId}?source=${source}">${source}</a>
                        </td>
                    </tr>
                </#list>
                </tbody>
            </table>
        </div>
    </section>

</@c.page>
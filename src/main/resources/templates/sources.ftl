<#import "parts/common.ftl" as c>

<@c.page>
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
</@c.page>
<#import "parts/common.ftl" as c>

<@c.page>
    <div class="d-flex justify-content-between align-items-center">
        <h2>Projects</h2>
        <a href="/create-project" class="btn btn-primary">+ Add new project</a>
    </div>
    <br>
    <h5>Select a project and browse mentions</h5>

    <table class="table">
        <thead>
        <tr>
            <th scope="col">#</th>
            <th scope="col">Project name</th>
            <th scope="col">Created at</th>
            <th scope="col">Project settings</th>
        </tr>
        </thead>
        <tbody class="table-group-divider">
        <#list projects as project>
            <tr>
                <th scope="row">${project.id}</th>
                <td>
                    <a href="/panel/results/${project.id}">${project.name}</a>
                </td>
                <td>${project.createdAt}</td>
                <td>
                    <form action="/delete-project/${project.id}" method="post">
                        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                        <a href="/delete-project/${project.id}" onclick="this.closest('form').submit();return false;">Delete</a>
                    </form>
                    <a href="/edit-project/${project.id}">Edit</a>

                    <form action="/panel/compare/${project.id}" method="post">
                        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                        <a href="/panel/compare/${project.id}"
                           onclick="this.closest('form').submit();return false;">Add to comparison</a>
                    </form>

                </td>
            </tr>
        </#list>
        </tbody>
    </table>
</@c.page>
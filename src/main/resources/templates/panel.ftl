<#import "parts/common.ftl" as c>

<@c.page>
    <div class="d-flex justify-content-between align-items-center">
        <h2>Projects</h2>
        <a href="/new-project" class="btn btn-primary">+ Add new project</a>
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
        <tr>
            <th scope="row">1</th>
            <td>Mark</td>
            <td>Otto</td>
            <td>
                <a href="/delete-project">Delete</a>
                <a href="/edit-project">Edit</a>
            </td>
        </tr>
        </tbody>
    </table>
</@c.page>
<#macro project path isCreateProjectForm>
    <form action="${path}" method="post" class="d-flex flex-column">
        <div class="form-input mt-5">
            <input class="form-in ml-4" type="text"
                   placeholder="Enter Brand Name" name="brand" value="<#if projName??>${projName}</#if>"/>
        </div>

        <div class="m-5 col-md-6">
            <input name='tags' class="form-control" autofocus>
        </div>

        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <button class="form-login mt-5 p-2" type="submit">Create project</button>
    </form>
</#macro>
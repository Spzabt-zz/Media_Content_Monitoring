<#macro projectSetup path isCreateProjectForm>
    <div class="container">
        <form action="${path}" method="post" class="d-flex flex-column">
            <div class="form-input mt-5">
                <input class="form-in ml-4" type="text"
                       placeholder="Enter Brand Name" name="brand"
                       value="<#if !isCreateProjectForm>${project.name}</#if>"/>
            </div>

            <#--            <div class="input-group mb-3">-->
            <#if !isCreateProjectForm>
                <input name='tags' class="form-control" value="<#list keywords as keyword>${keyword},</#list>"
                       autofocus>
            <#else>
                <div class="mb-3">
                    <label for="tagsId" class="form-label">Keywords</label>
                    <input id="tagsId" name='tags' class="form-control" autofocus>
                </div>
            </#if>

            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <button class="btn btn-primary" onclick="updateSpinner(true)" type="submit">
                <#if isCreateProjectForm>
                    Create project
                <#else>
                    Update project settings
                </#if>
            </button>
        </form>
    </div>
</#macro>
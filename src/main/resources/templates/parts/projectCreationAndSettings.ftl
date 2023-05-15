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

<#--                <div class="mb-3">-->
<#--                    <label for="redditClientId" class="form-label">Reddit client id</label>-->
<#--                    <input type="text" class="form-control" id="redditClientId" name="redditClient">-->
<#--                </div>-->
<#--                <div class="mb-3">-->
<#--                    <label for="redditClientSecretId" class="form-label">Reddit client secret</label>-->
<#--                    <input type="text" class="form-control" id="redditClientSecretId" name="redditClientSecret">-->
<#--                </div>-->

<#--                <div class="mb-3">-->
<#--                    <label for="twitterConsumerKeyId" class="form-label">Twitter consumer key</label>-->
<#--                    <input type="text" class="form-control" id="twitterConsumerKeyId" name="twitterConsumerKey">-->
<#--                </div>-->
<#--                <div class="mb-3">-->
<#--                    <label for="twitterConsumerSecretId" class="form-label">Twitter consumer secret</label>-->
<#--                    <input type="text" class="form-control" id="twitterConsumerSecretId" name="twitterConsumerSecret">-->
<#--                </div>-->
<#--                <div class="mb-3">-->
<#--                    <label for="twitterAccessTokenId" class="form-label">Twitter access token</label>-->
<#--                    <input type="text" class="form-control" id="twitterAccessTokenId" name="twitterAccessToken">-->
<#--                </div>-->
<#--                <div class="mb-3">-->
<#--                    <label for="twitterAccessTokenSecretId" class="form-label">Twitter access token secret</label>-->
<#--                    <input type="text" class="form-control" id="twitterAccessTokenSecretId"-->
<#--                           name="twitterAccessTokenSecret">-->
<#--                </div>-->

<#--                <div class="mb-3">-->
<#--                    <label for="ytApiKeyId" class="form-label">YouTube api key</label>-->
<#--                    <input type="text" class="form-control" id="ytApiKeyId" name="ytApiKey">-->
<#--                </div>-->
            </#if>
            <#--            </div>-->

            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <button class="btn btn-primary" type="submit">
                <#if isCreateProjectForm>
                    Create project
                <#else>
                    Update project settings
                </#if>
            </button>
        </form>
    </div>
</#macro>
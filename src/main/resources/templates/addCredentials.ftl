<#import "parts/common.ftl" as c>


<@c.page>
    <#if message??>
        <div class="alert alert-${messageType}" role="alert">
            ${message}
        </div>
    </#if>
    <form action="/credentials" method="post" class="d-flex flex-column">
        <div class="mb-3">
            <label for="redditClientId" class="form-label">Reddit client id</label>
            <input type="text" class="form-control" id="redditClientId" name="redditClient">
        </div>
        <div class="mb-3">
            <label for="redditClientSecretId" class="form-label">Reddit client secret</label>
            <input type="text" class="form-control" id="redditClientSecretId" name="redditClientSecret">
        </div>

        <div class="mb-3">
            <label for="twitterConsumerKeyId" class="form-label">Twitter consumer key</label>
            <input type="text" class="form-control" id="twitterConsumerKeyId" name="twitterConsumerKey">
        </div>
        <div class="mb-3">
            <label for="twitterConsumerSecretId" class="form-label">Twitter consumer secret</label>
            <input type="text" class="form-control" id="twitterConsumerSecretId" name="twitterConsumerSecret">
        </div>
        <div class="mb-3">
            <label for="twitterAccessTokenId" class="form-label">Twitter access token</label>
            <input type="text" class="form-control" id="twitterAccessTokenId" name="twitterAccessToken">
        </div>
        <div class="mb-3">
            <label for="twitterAccessTokenSecretId" class="form-label">Twitter access token secret</label>
            <input type="text" class="form-control" id="twitterAccessTokenSecretId" name="twitterAccessTokenSecret">
        </div>

        <div class="mb-3">
            <label for="ytApiKeyId" class="form-label">YouTube api key</label>
            <input type="text" class="form-control" id="ytApiKeyId" name="ytApiKey">
        </div>

        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <button type="submit" class="btn btn-primary">Add credentials</button>
    </form>

</@c.page>
<#import "parts/common.ftl" as c>
<#import "parts/projectCreationAndSettings.ftl" as projAsset>


<@c.page>
    <#if message??>
        <div class="alert alert-${messageType}" role="alert">
            ${message}
        </div>
    </#if>
    <@projAsset.projectSetup "/create-project" true/>
</@c.page>
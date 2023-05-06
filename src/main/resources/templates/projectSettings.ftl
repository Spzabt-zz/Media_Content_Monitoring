<#import "parts/common.ftl" as c>
<#import "parts/projectCreationAndSettings.ftl" as projAsset>

<@c.page>
    <@projAsset.projectSetup "/edit-project/${project.id}" false/>
</@c.page>
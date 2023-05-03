<#assign known = Session.SPRING_SECURITY_CONTEXT??>

<#if known>
    <#assign
        user = Session.SPRING_SECURITY_CONTEXT.authentication.principal
        name = user.getUsername()
        currentUserId = user.getId()
        isAdmin = user.admin
    >
<#else>
    <#assign
        name = "unknown"
        isAdmin = false
        currentUserId = -1
    >
</#if>
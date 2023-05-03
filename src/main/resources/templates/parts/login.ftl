<#macro login path isRegisterForm>
    <form action="${path}" method="post">
        <div class="form-group row">
            <label class="col-sm-2 col-form-label">User Name :</label>
            <div class="col-sm-6">
                <input class="form-control ${(usernameError??)?string('is-invalid', '')}" type="text" name="username"
                       value="<#if user??>${user.username}</#if>" placeholder="User name"/>
                <#if usernameError??>
                    <div class="invalid-feedback">
                        ${usernameError}
                    </div>
                </#if>
            </div>
        </div>
        <div class="form-group row mt-3">
            <label class="col-sm-2 col-form-label">Password: </label>
            <div class="col-sm-6">
                <input class="form-control ${(passwordError??)?string('is-invalid', '')}" type="password"
                       name="password" placeholder="Password"/>
                <#if passwordError??>
                    <div class="invalid-feedback">
                        ${passwordError}
                    </div>
                </#if>
            </div>
        </div>
        <#if isRegisterForm>
            <div class="form-group row mt-3">
                <label class="col-sm-2 col-form-label">Password confirmation: </label>
                <div class="col-sm-6">
                    <input class="form-control ${(password2Error??)?string('is-invalid', '')}" type="password"
                           name="password2" placeholder="Retype password"/>
                    <#if password2Error??>
                        <div class="invalid-feedback">
                            ${password2Error}
                        </div>
                    </#if>
                </div>
            </div>
            <div class="form-group row mt-3">
                <label class="col-sm-2 col-form-label">Email: </label>
                <div class="col-sm-6">
                    <input class="form-control ${(emailError??)?string('is-invalid', '')}" type="email" name="email"
                           value="<#if user??>${user.email}</#if>" placeholder="some@some.com"/>
                    <#if emailError??>
                        <div class="invalid-feedback">
                            ${emailError}
                        </div>
                    </#if>
                </div>
            </div>
        </#if>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <button class="btn btn-primary mt-3" type="submit">
            <#if isRegisterForm>
                Create
            <#else>
                Sign In
            </#if>
        </button>
    </form>
    <#if !isRegisterForm><a class="btn btn-outline-primary mt-3" role="button" aria-disabled="true"
                            href="/registration">Or sign up</a></#if>
</#macro>

<#macro logout user currentUserId>
    <#if user?? && currentUserId != -1>
        <form action="/logout" method="post">
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <button class="btn btn-primary" type="submit">Sign Out</button>
        </form>
    <#else>
        <a class="btn btn-primary" href="/login">Sign In</a>
    </#if>
</#macro>
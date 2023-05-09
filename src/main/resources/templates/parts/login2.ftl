<#macro login path isRegisterForm>
    <div class="form-div">
        <div class="wave"><img src="/static/img/assets/wave.png" style="width: 1550px; height: 710px;"></div>
        <div class="form-card">
            <div class="form-title">Social Monitoring</div>
            <hr class="container mt-2"
                style="outline: none; border: none; background-color: black; height: 0.5px; width: 50%;">
            <div class="d-flex">
                <div class="signup-img"><img src="/static/img/assets/signup.png"
                                             style="width: 400px; height: 400px;"></div>
                <div class="form-info" style="margin-left: 50px;">
                    <div class="form-header">
                        <#if isRegisterForm>
                            Sign up
                        <#else>
                            Sign In
                        </#if>
                    </div>
                    <div class="form-values">
                        <form action="${path}" method="post" class="d-flex flex-column">
                            <div class="form-input mt-5">
                                <input class="form-in ml-4 ${(usernameError??)?string('is-invalid', '')}" type="text"
                                       placeholder="User name" name="username"
                                       value="<#if user??>${user.username}</#if>"/>
                                <#if usernameError??>
                                    <div class="invalid-feedback">
                                        ${usernameError}
                                    </div>
                                </#if>
                            </div>

                            <div class="form-input mt-5">
                                <input class="form-in ml-4 ${(passwordError??)?string('is-invalid', '')}"
                                       type="password"
                                       placeholder="Password" name="password"/>
                                <br>
                                <#if passwordError??>
                                    <div class="invalid-feedback">
                                        ${passwordError}
                                    </div>
                                </#if>
                            </div>

                            <#if isRegisterForm>
                                <div class="form-input mt-5">
                                    <input class="form-in ml-4 ${(password2Error??)?string('is-invalid', '')}"
                                           type="password"
                                           placeholder="Retype password" name="password2"/>
                                    <br>
                                    <#if password2Error??>
                                        <div class="invalid-feedback">
                                            ${password2Error}
                                        </div>
                                    </#if>
                                </div>

                                <div class="form-input mt-5">
                                    <input class="form-in ml-4 ${(emailError??)?string('is-invalid', '')}" type="email"
                                           placeholder="some@some.com" name="email"
                                           value="<#if user??>${user.email}</#if>"/>
                                    <#if emailError??>
                                        <div class="invalid-feedback">
                                            ${emailError}
                                        </div>
                                    </#if>
                                </div>
                            </#if>
                            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
                            <div class="form-btn">
                                <button class="form-login mt-5 p-2" type="submit">
                                    <#if isRegisterForm>
                                        Sign up
                                    <#else>
                                        Sign In
                                    </#if>
                                </button>
                            </div>
                        </form>
                        <#if isRegisterForm>
                            <div class="mt-4 form-link">
                                <a href="/login" style="color: black; text-decoration: underline;">Already have an
                                    account?
                                    Click here to Login!</a>
                            </div>
                        </#if>
                    </div>
                </div>
            </div>
        </div>
    </div>
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
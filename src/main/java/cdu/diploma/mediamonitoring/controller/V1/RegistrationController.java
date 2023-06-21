package cdu.diploma.mediamonitoring.controller.V1;

import cdu.diploma.mediamonitoring.domain.model.ApiCredentials;
import cdu.diploma.mediamonitoring.domain.model.User;
import cdu.diploma.mediamonitoring.domain.repo.ApiCredentialsRepo;
import cdu.diploma.mediamonitoring.domain.repo.UserRepo;
import cdu.diploma.mediamonitoring.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Map;

@Controller
public class RegistrationController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final ApiCredentialsRepo apiCredentialsRepo;
    private final UserRepo userRepo;

    @Autowired
    public RegistrationController(UserService userService, AuthenticationManager authenticationManager, ApiCredentialsRepo apiCredentialsRepo, UserRepo userRepo) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.apiCredentialsRepo = apiCredentialsRepo;
        this.userRepo = userRepo;
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(
            @RequestParam("password2") String passwordConfirm,
            @Valid User user,
            BindingResult bindingResult,
            Model model) {
        boolean isConfirmEmpty = StringUtils.isEmpty(passwordConfirm);

        String passBeforeEncryption = user.getPassword();

        if (isConfirmEmpty) {
            model.addAttribute("password2Error", "Password confirmation cannot be empty");
        }

        if (user.getPassword() != null && !user.getPassword().equals(passwordConfirm)) {
            model.addAttribute("passwordError", "Passwords are different!");
            return "registration";
        }

        if (isConfirmEmpty || bindingResult.hasErrors()) {
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);
            return "registration";
        }

        if (!userService.addUser(user)) {
            model.addAttribute("usernameError", "User exists!");
            return "registration";
        }

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                user.getUsername(), passBeforeEncryption);

        try {
            Authentication authentication = authenticationManager.authenticate(authRequest);

            if (authentication.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("Auto login successfully!");
            }
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }

        return "redirect:/credentials";
    }

    @GetMapping("/credentials")
    public String apiKeys() {
        return "addCredentials";
    }

    @PostMapping("/credentials")
    @Transactional
    public String addApiKeys(
            @AuthenticationPrincipal User user,
            @RequestParam("redditClient") String redditClient,
            @RequestParam("redditClientSecret") String redditClientSecret,
            @RequestParam("twitterConsumerKey") String twitterConsumerKey,
            @RequestParam("twitterConsumerSecret") String twitterConsumerSecret,
            @RequestParam("twitterAccessToken") String twitterAccessToken,
            @RequestParam("twitterAccessTokenSecret") String twitterAccessTokenSecret,
            @RequestParam("ytApiKey") String ytApiKey,
            /*@RequestParam("redditUsername") String redditUsername,
            @RequestParam("redditPassword") String redditPassword,
            @RequestParam("redditUserAgent") String redditUserAgent,*/
            Model model) {
        if (redditClient.isEmpty() || redditClientSecret.isEmpty() || twitterConsumerKey.isEmpty()
        ||twitterConsumerSecret.isEmpty() || twitterAccessToken.isEmpty() || twitterAccessTokenSecret.isEmpty()
                || ytApiKey.isEmpty() /*|| redditUsername.isEmpty() || redditPassword.isEmpty() || redditUserAgent.isEmpty()*/) {
            model.addAttribute("messageType", "danger");
            model.addAttribute("message", "Fields can't be blank.");
            return "addCredentials";
        }

        ApiCredentials apiCredentials = new ApiCredentials();

        apiCredentials.setRedditClientId(redditClient);
        apiCredentials.setRedditClientSecret(redditClientSecret);
//        apiCredentials.setRedditUsername(redditUsername);
//        apiCredentials.setRedditPassword(redditPassword);
//        apiCredentials.setRedditUserAgent(redditUserAgent);
        apiCredentials.setRedditUsername("Spzabt_zz");
        apiCredentials.setRedditPassword("kLg84146ivW#?");
        apiCredentials.setRedditUserAgent("Praw1 by u/Spzabt_zz");

        apiCredentials.setTwitterConsumerKey(twitterConsumerKey);
        apiCredentials.setTwitterConsumerSecret(twitterConsumerSecret);
        apiCredentials.setTwitterAccessToken(twitterAccessToken);
        apiCredentials.setTwitterAccessTokenSecret(twitterAccessTokenSecret);

        apiCredentials.setYtApiKey(ytApiKey);

        apiCredentialsRepo.save(apiCredentials);

        user.setApiCredentials(apiCredentials);

        userRepo.save(user);

        return "redirect:/create-project";
    }

    private void authWithAuthManager(HttpServletRequest request, String username, String password) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
        authToken.setDetails(new WebAuthenticationDetails(request));

        Authentication authentication = authenticationManager.authenticate(authToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}

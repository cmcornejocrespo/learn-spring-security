package com.baeldung.lss.web.controller;

import com.baeldung.lss.model.User;
import com.baeldung.lss.model.VerificationToken;
import com.baeldung.lss.persistence.VerificationTokenRepository;
import com.baeldung.lss.service.EmailService;
import com.baeldung.lss.service.UserService;
import com.baeldung.lss.validation.EmailExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Calendar;

import static java.lang.String.format;
import static java.util.UUID.randomUUID;

@Controller
class RegistrationController {

    private final UserService userService;
    private final EmailService emailService;
    private final VerificationTokenRepository verificationTokenRepository;

    @Autowired
    public RegistrationController(UserService userService, EmailService emailService, VerificationTokenRepository verificationTokenRepository) {
        this.userService = userService;
        this.emailService = emailService;
        this.verificationTokenRepository = verificationTokenRepository;
    }

    @RequestMapping(value = "signup")
    public ModelAndView registrationForm() {
        return new ModelAndView("registrationPage", "user", new User());
    }

    @RequestMapping(value = "user/register")
    public ModelAndView registerUser(@Valid final User user, final BindingResult result, final HttpServletRequest request, final RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return new ModelAndView("registrationPage", "user", user);
        }

        try {
            //forcing the user to not being token-enabled
            user.setEnabled(false);
            userService.registerNewUser(user);

            //generate unique token
            final String token = randomUUID().toString();

            //create token for a user
            final VerificationToken myToken = new VerificationToken(token, user);

            //save token
            verificationTokenRepository.save(myToken);

            final String appUrl = new StringBuilder().append("http://").append(request.getServerName()).append(":").append(request.getServerPort()).append(request.getContextPath()).toString();

            emailService.sendVerificationEmail(user, token, appUrl);

        } catch (EmailExistsException e) {
            result.addError(new FieldError("user", "email", e.getMessage()));
            return new ModelAndView("registrationPage", "user", user);
        }

        redirectAttributes.addFlashAttribute("message", format("A confirmation email was sent to: %s", user.getEmail()));
        return new ModelAndView("redirect:/login");
    }

    @RequestMapping(value = "/registrationConfirm")
    public ModelAndView confirmRegistration(final Model model, @RequestParam("token") final String token, final RedirectAttributes redirectAttributes) {

        final VerificationToken verificationToken = verificationTokenRepository.findByToken(token);

        if (verificationToken == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid account confirmation token.");
            return new ModelAndView("redirect:/login");
        }

        final User user = verificationToken.getUser();
        final Calendar cal = Calendar.getInstance();

        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            redirectAttributes.addFlashAttribute("errorMessage", "Your registration token has expired. Please register again.");
            return new ModelAndView("redirect:/login");
        }

        user.setEnabled(true);
        userService.saveRegisteredUser(user);

        redirectAttributes.addFlashAttribute("message", "Your account verified successfully");
        return new ModelAndView("redirect:/login");
    }


}

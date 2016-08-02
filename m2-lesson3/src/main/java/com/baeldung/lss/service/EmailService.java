package com.baeldung.lss.service;

import com.baeldung.lss.model.User;

public interface EmailService {

    void sendVerificationEmail(final User user, final String token, final String appUrl);
}

package com.baeldung.lss.service;

import com.baeldung.lss.model.User;
import com.baeldung.lss.validation.EmailExistsException;

public interface UserService {

    User registerNewUser(User user) throws EmailExistsException;

    void saveRegisteredUser(User user);
}

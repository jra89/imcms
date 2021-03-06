package com.imcode.imcms.domain.service;

import com.imcode.imcms.domain.dto.UserFormData;
import com.imcode.imcms.domain.exception.UserValidationException;

/**
 * For users creation, obviously.
 *
 * @author Serhii Maksymchuk from Ubrainians for imCode
 * 27.06.18.
 */
public interface UserCreationService {

    void createUser(final UserFormData userData) throws UserValidationException;

}

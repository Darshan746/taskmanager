package com.encora.taskmanager.dto.request;

import com.encora.taskmanager.constant.ValidationConstants;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserSignUpRequest {
    @NotNull(message = ValidationConstants.NAME_IS_REQUIRED)
    @NotEmpty(message = ValidationConstants.NAME_SHOULD_NOT_BE_EMPTY)
    @Size(min = 2, max = 50, message = ValidationConstants.NAME_MUST_BE_BETWEEN_2_AND_50_CHARACTERS)
    private String name;

    @NotNull(message = ValidationConstants.EMAIL_IS_REQUIRED)
    @NotEmpty(message = ValidationConstants.EMAIL_SHOULD_NOT_BE_EMPTY)
    @Email(message = ValidationConstants.EMAIL_MUST_BE_VALID)
    private String email;

    @NotNull(message = ValidationConstants.PASSWORD_IS_REQUIRED)
    @NotEmpty(message = ValidationConstants.PASSWORD_SHOULD_NOT_BE_EMPTY)
    @Pattern(regexp = ValidationConstants.PASSWORD_REGEX, message = ValidationConstants.PASSWORD_CRITERIA)
    private String password;

    @NotEmpty(message = ValidationConstants.ROLE_SHOULD_NOT_BE_EMPTY)
    private String roles;
}

package com.nguyenvietloc.tutorials.validate;

import com.nguyenvietloc.tutorials.model.User;
import com.nguyenvietloc.tutorials.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {
    @Autowired
    private UserService userService;

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;

        // Validate username không được để trống
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "NotEmpty");

        // Check usernam là số điện thoại
        String regexUser = "^(0|\\+84)(\\s|\\.)?((3[2-9])|(5[689])|(7[06-9])|(8[1-689])|(9[0-46-9]))(\\d)(\\s|\\.)?(\\d{3})(\\s|\\.)?(\\d{3})$";
        boolean testUser = user.getUsername().matches(regexUser);
        if(!testUser) {
            errors.rejectValue("username", "Phone.userForm.username");
        }

        // Check user có tồn tại hay không
        if (userService.findByUsername(user.getUsername()) != null) {
            errors.rejectValue("username", "Duplicate.userForm.username");
        }

        // Validate password không được để trống
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty");

        // Check password có ít nhất 8 kí tự và không vượt quá 20 kí tự
        if (user.getPassword().length() < 7 || user.getPassword().length() > 21) {
            errors.rejectValue("password", "Size.userForm.password");
        }

        // Check password phải có nhất một chữ viết hoa , 1 chữ cái thường , 1 kí tự đặc biệt và 1 số
        String regexPass = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\S+$).{8,20}$";
        boolean testPass = user.getPassword().matches(regexPass);
        if(!testPass) {
            errors.rejectValue("password", "Check.userForm.password");
        }

        // Check mật khẩu xác nhận có giống mật khẩu đã tạo
        if (!user.getPasswordConfirm().equals(user.getPassword())) {
            errors.rejectValue("passwordConfirm", "Diff.userForm.passwordConfirm");
        }
    }
}

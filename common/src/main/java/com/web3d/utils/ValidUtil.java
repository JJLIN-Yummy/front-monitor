package com.web3d.utils;

import com.web3d.enums.ValidStatus;

public class ValidUtil {
    public static boolean isValid(Integer code) {
        ValidStatus status = ValidStatus.getByCode(code);
        return ValidStatus.VALID == status;
    }
    public static boolean isInvalid(Integer code) {
        return !isValid(code);
    }
}

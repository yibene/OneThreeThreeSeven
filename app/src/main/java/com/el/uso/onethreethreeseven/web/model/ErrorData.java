package com.el.uso.onethreethreeseven.web.model;

import com.el.uso.onethreethreeseven.helper.JsonUtils;
import com.google.gson.annotations.Expose;

public class ErrorData {

    public static final int CODE_OKAY = 0;
    public static final int CODE_FAIL = Integer.MAX_VALUE;
    public static final int CODE_TIME_OUT = Integer.MAX_VALUE - 1;

    public static final int CODE_UNKNOWN = -1;
    public static final int CODE_SERVER_ERROR = 1;
    public static final int CODE_NO_ACCOUNT = 2;
    public static final int CODE_PASSWORD_INCORRECT = 3;
    public static final int CODE_TOKEN_IS_INVALID = 4;
    public static final int CODE_NO_DATA = 5;
    public static final int CODE_INVALID_INPUT = 6;
    public static final int CODE_SCOOTER_NOT_MATCHED = 7;
    public static final int CODE_NO_AVAILABLE_SCOOTER = 8;
    public static final int CODE_FORBIDDEN_USER = 9;
    public static final int CODE_INVALIDAPPVERSION = 10;
    public static final int CODE_PAYMENT_DUE = 11;
    public static final int CODE_SCOOTER_OR_BATTERY_MISSED = 12;
    public static final int CODE_DEVICE_TOKEN_IS_INVALID = 13;

    @Expose
    private int Code;
    @Expose
    private String Message;

    public int getCode() {
        return Code;
    }

    public void setCode(final int code) {
        Code = code;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(final String message) {
        Message = message;
    }

    public ErrorData() {
        Code = CODE_UNKNOWN;
        Message = "";
    }

    public ErrorData(int code) {
        Code = code;
        Message = "";
    }

    public static ErrorData fromJson(String json) {
        return JsonUtils.deserialize(json, ErrorData.class);
    }

    public String toJson() {
        return JsonUtils.serialize(this);
    }

    public String toString() {
        switch (Code) {
            case CODE_SERVER_ERROR:
                return "Server error";
            case CODE_NO_ACCOUNT:
                return "No account";
            case CODE_PASSWORD_INCORRECT:
                return "Password incorrect";
            case CODE_TOKEN_IS_INVALID:
                return "Token is invalid";
            case CODE_NO_DATA:
                return "No data";
            case CODE_INVALID_INPUT:
                return "Invalid input";
            case CODE_SCOOTER_NOT_MATCHED:
                return "Scooter not matched";
            case CODE_NO_AVAILABLE_SCOOTER:
                return "No available scooter";
            case CODE_INVALIDAPPVERSION:
                return "InvalidAppVersion";
            default:
                return "Unknown";
        }
    }
}

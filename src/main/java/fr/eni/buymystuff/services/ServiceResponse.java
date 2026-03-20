package fr.eni.buymystuff.services;


public class ServiceResponse<T> {

    public String code;
    public String message;
    public T data;

    public ServiceResponse() {
    }

    public ServiceResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ServiceResponse(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ServiceResponse buildResponse(String code, String message, T data) {
        return new ServiceResponse<T>(code, message, data);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() { return code == null; }
    }



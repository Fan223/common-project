package fan.utils;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 统一结果返回类
 *
 * @author Fan
 * @since 2022/11/22 13:53
 */
@Builder
@Data
public class Result implements Serializable {

    private static final long serialVersionUID = -1L;

    private int code;

    private String msg;

    private Object data;

    public static Result success(int code, String msg, Object data) {
        return Result.builder().code(code).msg(msg).data(data).build();
    }

    public static Result success(String msg, Object data) {
        return Result.builder().code(200).msg(msg).data(data).build();
    }

    public static Result success(Object data) {
        return Result.builder().code(200).msg("操作成功").data(data).build();
    }

    public static Result fail(int code, String msg, Object data) {
        return Result.builder().code(code).msg(msg).data(data).build();
    }

    public static Result fail(String msg, Object data) {
        return Result.builder().code(500).msg(msg).data(data).build();
    }

    public static Result fail(Object data) {
        return Result.builder().code(500).msg("操作失败").data(data).build();
    }
}

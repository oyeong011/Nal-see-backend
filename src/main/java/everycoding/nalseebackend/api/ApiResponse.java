package everycoding.nalseebackend.api;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

@Getter
@JsonPropertyOrder({"success", "message", "results"})
public class ApiResponse<T> {
    private final boolean success;
    private final String message;
    private final T results;

    private ApiResponse(boolean success, String message, T results) {
        this.success = success;
        this.message = message;
        this.results = results;
    }

    public static <T> ApiResponse<T> ok() {
        return new ApiResponse<>(true,"요청에 성공했습니다.", null);
    }

    public static <T> ApiResponse<T> ok(T results) {
        return new ApiResponse<>(true,"요청에 성공했습니다.", results);
    }

    public static <T> ApiResponse<T> ok(T results, String message) {
        return new ApiResponse<>(true,message, results);
    }

    public static <T> ApiResponse<T> fail(String message) {
        return new ApiResponse<>(false, message, null);
    }
}

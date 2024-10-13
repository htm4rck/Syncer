package ndp.types;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Root<T> {
    private boolean successful;
    private String code;
    private String message;
    private Data<T> data;
}


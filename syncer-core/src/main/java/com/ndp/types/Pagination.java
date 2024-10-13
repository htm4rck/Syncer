package ndp.types;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Pagination<T> {
    private int countFilter;
    private int totalPages;
    private List<T> list;

    public Pagination(int countFilter, int totalPages, List<T> list) {
        this.countFilter = countFilter;
        this.totalPages = totalPages;
        this.list = list;
    }
}

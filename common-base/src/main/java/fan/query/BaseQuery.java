package fan.query;

import lombok.Data;

/**
 * 基础查询参数
 *
 * @author Fan
 * @since 2022/12/6 22:34
 */
@Data
public class BaseQuery {

    // 当前页面
    private int currentPage;

    // 页面大小
    private int pageSize;
}

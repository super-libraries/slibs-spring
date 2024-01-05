package cn.slibs.spring.page.v3;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用于 <b>swagger-v3</b> 的分页类
 *
 * @since 0.0.1
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "分页类")
public class Page<T> {
    /**
     * 每页记录数
     */
    @Schema(description = "【每页记录数】")
    protected Integer pageSize;
    /**
     * 当前页码
     */
    @Schema(description = "【当前页码】")
    protected Integer pageNum;
    /**
     * 总记录数
     */
    @Schema(description = "【总记录数】")
    protected Long totalCount;
    /**
     * 总页数
     */
    @Schema(description = "【总页数】")
    protected Integer totalPage;
    /**
     * 数据
     */
    @Schema(description = "【数据列表】")
    protected List<T> records;

}

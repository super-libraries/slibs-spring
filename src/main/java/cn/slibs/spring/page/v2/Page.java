package cn.slibs.spring.page.v2;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用于 <b>swagger-v2</b> 的分页类
 *
 * @since 0.0.1
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel(value = "分页类")
public class Page<T> {
    /**
     * 每页记录数
     */
    @ApiModelProperty(value = "【每页记录数】")
    protected Integer pageSize;
    /**
     * 当前页码
     */
    @ApiModelProperty(value = "【当前页码】")
    protected Integer pageNum;
    /**
     * 总记录数
     */
    @ApiModelProperty(value = "【总记录数】")
    protected Long totalCount;
    /**
     * 总页数
     */
    @ApiModelProperty(value = "【总页数】")
    protected Integer totalPage;
    /**
     * 数据
     */
    @ApiModelProperty(value = "【数据列表】")
    protected List<T> records;

}

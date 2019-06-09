package graprojection.entity.common;

import lombok.Data;

import java.util.List;
@Data
public class PageDataResult {
    /**
     * @Title: PageDataResult
     * @Description: 封装DTO分页数据（记录数和所有记录）
     * @author: shitong
     * @version: 1.0
     * @date: 2019/1/8 9:37
     */

        private Integer code=200;

        //总记录数量
        private Integer totals;

        private List<?> list;

        private Integer totalPages;

        private Integer currentPages;

        private Integer size;


}

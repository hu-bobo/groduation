package graprojection.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
@Slf4j
public class CopyBeanUtil {
    /**
     * 将2个hushMap 进行合并
     */
    public static Map<String, Map<String, Object>> mergeMapByKeyForLinks(Map map1, Map map2, String
            key) {
        Map<String, Map<String, Object>> map = new HashMap<>();
        if (CollectionUtils.isEmpty(map1)) {
            map.put(key, map2);
            return map;
        }
        if (CollectionUtils.isEmpty(map2)) {
            map.put(key, map1);
            return map;
        }
        map.merge(key, map1, (m1, m2) -> {
            m1.putAll(m2);
            return m1;
        });
        map.merge(key, map2, (m1, m2) -> {
            m1.putAll(m2);
            return m1;
        });
        return map;
    }


    // for lists
    public static <T, U> List<U> convertList(List<T> from, Function<T, U> func) {
        return from.stream().map(func).collect(Collectors.toList());
    }

    // for arrays
    public static <T, U> U[] convertArray(T[] from,
                                          Function<T, U> func,
                                          IntFunction<U[]> generator) {
        return Arrays.stream(from).map(func).toArray(generator);
    }

    /**
     * @param inputList
     * @param pageRequest
     * @param total
     * @param <T>
     * @return
     */
    public static <T> Page<T> convertListToPage(List<T> inputList, PageRequest pageRequest, long
            total) {
        final Page<T> page = new PageImpl<>(inputList, pageRequest, total);
        return page;
    }

    /**
     * List对象的转化
     *
     * @param sourceList 原List
     * @param targetClass 目标类
     */
    public static <A, B> List<B> convertList(List<A> sourceList, Class<B> targetClass)
            throws IllegalAccessException, InstantiationException {
        List<B> targetList = new ArrayList<>();
        for (A item : sourceList) {
            B targetItem = targetClass.newInstance();
            BeanUtils.copyProperties(item, targetItem);
            targetList.add(targetItem);
        }
        return targetList;
    }

    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    /**
     * 实体对象类间属性的拷贝
     *
     * java.lang.BigDecimal
     * java.lang.BigInteger
     * boolean and java.lang.Boolean
     * byte and java.lang.Byte
     * char and java.lang.Character
     * java.lang.Class
     * double and java.lang.Double
     * float and java.lang.Float
     * int and java.lang.Integer
     * long and java.lang.Long
     * short and java.lang.Short
     * java.lang.String
     * java.sql.Date
     * java.sql.Time
     * java.sql.Timestamp
     *
     */
    public static void copyProperties(Object src, Object target) {
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }
}

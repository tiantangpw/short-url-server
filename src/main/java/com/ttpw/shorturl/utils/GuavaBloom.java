package com.ttpw.shorturl.utils;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.nio.charset.Charset;

/**
 * Guava中布隆过滤器使用示例
 * @Author willlee
 * @Date 2022/5/22 20:54
 **/
public class GuavaBloom {
    public static void main(String[] args) {
        String value1 = "https://javaguide.cn/";
        String value2 = "https://github.com/Snailclimb";

        // 创建布隆过滤器对象
        BloomFilter<String> filter = BloomFilter.create(
                Funnels.stringFunnel(Charset.forName("utf-8")),
                1500, //预期容量
                0.01);//期望的误报概率
        // 判断指定元素是否存在
        System.out.println(filter.mightContain(value1));//false
        System.out.println(filter.mightContain(value2));//false
        // 将元素添加进布隆过滤器
        filter.put(value1);
        filter.put(value2);
        System.out.println(filter.mightContain(value1));//true
        System.out.println(filter.mightContain(value2));//true

    }
}

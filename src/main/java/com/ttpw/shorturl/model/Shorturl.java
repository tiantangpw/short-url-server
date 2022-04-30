package com.ttpw.shorturl.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * @author : leilei
 * @date : 10:23 2020/2/16
 * @desc :student 实体类
 */
@Data
@Document(collection = "shorturl")  //指定要对应的文档名(表名）
public class Shorturl {

    /*** 自定义mongo主键 加此注解可自定义主键类型以及自定义自增规则
     *  若不加 插入数据数会默认生成 ObjectId 类型的_id 字段
     *  org.springframework.data.annotation.Id 包下
     *  mongo库主键字段还是为_id 。不必细究(本文实体类中为id）
     */
    @Id
    private Long id;
    private String shortCode;
    private String url;
    private LocalDateTime createrTime;
}


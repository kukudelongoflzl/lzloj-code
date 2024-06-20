package com.yupi.lzloj.model.dto.questionsubmit;


import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

/**
 * 题目提交请求
 * 酷酷的龙
 */
@Data
public class QuestionSubmitAddRequest implements Serializable {

    /**
     * 使用语言
     */
    private String language;

    /**
     * 提交代码
     */
    private String code;


    /**
     * 题目id
     */
    private Long questionId;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}
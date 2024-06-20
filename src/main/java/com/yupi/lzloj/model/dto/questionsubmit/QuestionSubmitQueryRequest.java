package com.yupi.lzloj.model.dto.questionsubmit;


import com.baomidou.mybatisplus.annotation.TableField;
import com.yupi.lzloj.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 题目提交请求
 * 酷酷的龙
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QuestionSubmitQueryRequest extends PageRequest implements Serializable {

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

    /**
     * 提交用户id(做题者Id)
     */
    private Long userId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}
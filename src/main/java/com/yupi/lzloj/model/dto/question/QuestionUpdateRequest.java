package com.yupi.lzloj.model.dto.question;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 更新请求 用户也能发布题目，但是不能修改，这个设计很那评
 */
@Data
public class QuestionUpdateRequest implements Serializable {

    /**
     * 题目的主键id 不用简单的自增
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表（json 数组）
     */
    private List<String> tags;

    /**
     * 题目答案
     */
    private String answer;

    /**
     * 判题用例
     */
    private List<JudgeCase> judgeCase;

    /**
     * 判题配置
     */
    private JudgeConfig judgeConfig;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
package com.yupi.lzloj.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 题目提交
 *
 * @TableName question_submit
 */
@TableName(value = "question_submit")
@Data
public class QuestionSubmit implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 使用语言
     */
    private String language;

    /**
     * 提交代码
     */
    private String code;

    /**
     * 判题信息
     */
    private String judgeInfo;

    /**
     * 判题状态 0待判题 1判题中 2成功 3失败
     */
    private Integer status;

    /**
     * 题目id
     */
    private Long questionId;

    /**
     * 提交(做题)用户id
     */
    private Long userId;


    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
package com.yupi.lzloj.model.vo;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.yupi.lzloj.model.dto.question.JudgeConfig;
import com.yupi.lzloj.model.entity.Question;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.List;

/**
 * 题目视图 传给前端的数据
 */
@Data
public class QuestionVO {

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
     * 题目提交数
     */
    private Integer subNum;

    /**
     * 题目通过数量
     */
    private Integer acceptedNum;


    /**
     * 判题配置
     */
    private JudgeConfig judgeConfig;

    /**
     * 点赞数
     */
    private Integer thumbNum;

    /**
     * 收藏数
     */
    private Integer favourNum;

    /**
     * 创建题目用户 id
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
     * 创建题目者的信息
     */
    private UserVO userVO;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


    /**
     * 包装类转对象
     *
     * @param questionVO
     * @return Question
     */
    public static Question voToObj(QuestionVO questionVO) {
        if (questionVO == null) {
            return null;
        }
        Question question = new Question();
        BeanUtils.copyProperties(questionVO, question);
        //下面主要对一些特殊的进行处理 主要是题目标签 判题配置信息
        List<String> tagList = questionVO.getTags();
        question.setTags(JSONUtil.toJsonStr(tagList));
        JudgeConfig judgeConfig = questionVO.getJudgeConfig();
        question.setJudgeConfig(JSONUtil.toJsonStr(judgeConfig));
        return question;
    }

    /**
     * 对象转包装类
     *
     * @param question
     * @return QuestionVO
     */
    public static QuestionVO objToVo(Question question) {
        if (question == null) {
            return null;
        }
        QuestionVO questionVO = new QuestionVO();
        BeanUtils.copyProperties(question, questionVO);
        //下面要对于一些特殊的进行处理
        questionVO.setTags(JSONUtil.toList(question.getTags(), String.class));
        questionVO.setJudgeConfig(JSONUtil.toBean(question.getJudgeConfig(), JudgeConfig.class));
        return questionVO;
    }
}

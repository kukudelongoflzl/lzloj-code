package com.yupi.lzloj.model.vo;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.yupi.lzloj.judge.codesandbox.model.JudgeInfo;
import com.yupi.lzloj.model.entity.QuestionSubmit;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * 题目提交视图 传给前端的数据
 */
@Data
public class QuestionSubmitVO {

    /**
     * id
     */
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
    private JudgeInfo judgeInfo;

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
     * 提交(做题)用户VO
     */
    private UserVO userVO;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;


    /**
     * 包装类转对象
     *
     * @param questionSubmitVO
     * @return Question
     */
    public static QuestionSubmit voToObj(QuestionSubmitVO questionSubmitVO) {
        if (questionSubmitVO == null) {
            return null;
        }
        QuestionSubmit questionSubmit = new QuestionSubmit();
        BeanUtils.copyProperties(questionSubmitVO, questionSubmit);
        //下面主要对一些特殊的进行处理 主要是题目标签 判题配置  用户信息
        JudgeInfo judgeInfo = questionSubmitVO.getJudgeInfo();
        questionSubmit.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        return questionSubmit;
    }

    /**
     * 对象转包装类
     *
     * @param questionSubmit
     * @return QuestionSubmitVO
     */
    public static QuestionSubmitVO objToVo(QuestionSubmit questionSubmit) {
        if (questionSubmit == null) {
            return null;
        }
        QuestionSubmitVO questionSubmitVO = new QuestionSubmitVO();
        BeanUtils.copyProperties(questionSubmit, questionSubmitVO);
        //下面要对于一些特殊的进行处理 判题信息 用户
        JudgeInfo judgeInfo = JSONUtil.toBean(questionSubmit.getJudgeInfo(), JudgeInfo.class);
        questionSubmitVO.setJudgeInfo(judgeInfo);
        return questionSubmitVO;
    }

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}

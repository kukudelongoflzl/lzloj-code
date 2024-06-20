package com.yupi.lzloj.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.lzloj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.yupi.lzloj.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.yupi.lzloj.model.entity.QuestionSubmit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.lzloj.model.entity.User;
import com.yupi.lzloj.model.vo.QuestionSubmitVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 酷酷的龙
 * @description 针对表【question_submit(题目提交)】的数据库操作Service
 * @createDate 2024-06-06 19:42:31
 */
public interface QuestionSubmitService extends IService<QuestionSubmit> {

    /**
     * 提交
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    Long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);


    /**
     * 获取查询条件 主要是为了分页查询提交信息服务
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest);

    /**
     * 分页获取题目提交封装
     *
     * @param questionSubmitPage
     * @param request
     * @return
     */
    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, HttpServletRequest request);

}

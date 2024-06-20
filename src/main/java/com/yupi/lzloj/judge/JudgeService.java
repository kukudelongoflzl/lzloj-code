package com.yupi.lzloj.judge;

import com.yupi.lzloj.model.entity.QuestionSubmit;

/**
 * 判题服务接口
 */
public interface JudgeService {

    QuestionSubmit doJudge(Long questionSubmitId);
}

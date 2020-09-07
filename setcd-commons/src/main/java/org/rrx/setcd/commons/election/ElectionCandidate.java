package org.rrx.setcd.commons.election;

/**
 * @Auther: kevin3046@163.com
 * @Date: 2020/9/1 11:36
 * @Description:
 */
public interface ElectionCandidate {

    /**
     * 开始领导状态.
     * @throws Exception 抛出的异常
     */
    void startLeadership() throws Exception;

    void onError(Throwable t);

    void onCompleted();
}

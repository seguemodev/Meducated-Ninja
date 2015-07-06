
package com.seguetech.zippy.data.model.rximage;

import java.util.ArrayList;
import java.util.List;

public class RxImage {
    private ReplyStatus replyStatus;
    private List<NlmRxImage> nlmRxImages = new ArrayList<>();

    public ReplyStatus getReplyStatus() {
        return replyStatus;
    }
    public void setReplyStatus(ReplyStatus replyStatus) {
        this.replyStatus = replyStatus;
    }
    public List<NlmRxImage> getNlmRxImages() {
        return nlmRxImages;
    }
    public void setNlmRxImages(List<NlmRxImage> nlmRxImages) {
        this.nlmRxImages = nlmRxImages;
    }

}

package news.zxzq.com.videonews.entity;

import java.util.Date;

//{
//        "createdAt": 创建时间,
//        "objectId": 对象Id
//        }
public class CommentsResult {

    private Date createdAt;
    private String objectId;

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getObjectId() {
        return objectId;
    }
}

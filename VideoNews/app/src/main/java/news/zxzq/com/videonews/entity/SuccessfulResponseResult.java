package news.zxzq.com.videonews.entity;

/**
 * 用户注册的结果类
 * Created by Administrator on 2016/12/21.
 */

public class SuccessfulResponseResult {

    private String createdAt;
    private String objectId;
    private String sessionToken;
    private String username;
    private String updatedAt;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public SuccessfulResponseResult(String createdAt, String objectId, String sessionToken) {
        this.createdAt = createdAt;
        this.objectId = objectId;
        this.sessionToken = sessionToken;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    @Override
    public String toString() {
        return "SuccessfulResponseResult{" +
                "createdAt='" + createdAt + '\'' +
                ", objectId='" + objectId + '\'' +
                ", sessionToken='" + sessionToken + '\'' +
                '}';
    }
}

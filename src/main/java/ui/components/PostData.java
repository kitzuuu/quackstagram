package ui.components;

public class PostData {
    private final int postId;
    private final String userName;
    private final byte[] image;
    private final String postBio;
    private int likes;
    private final int ownerID;

    public PostData(int postId, String userName, byte[] image, String postBio, int likes, int ownerID) {
        this.postId = postId;
        this.userName = userName;
        this.image = image;
        this.postBio = postBio;
        this.likes = likes;
        this.ownerID = ownerID;
    }

    public int getOwnerID(){
        return ownerID;
    }
    public int getPostId() {
        return postId;
    }

    public String getUserName() {
        return userName;
    }

    public byte[] getImage() {
        return image;
    }

    public String getPostBio() {
        return postBio;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
}

package org.sopt.dto.request;

// 게시글 작성 요청 (클라이언트 → 서버)
// 변수 앞에 아무것도 안붙으면 default(해당 패키지 내에서만 접근 가능)
public class CreatePostRequest {
    public String title;
    public String content;
    public String author;

    public CreatePostRequest(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }
}

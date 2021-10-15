package com.allan.springBootBoard.security.oauth2;

import com.allan.springBootBoard.web.board.domain.Address;
import com.allan.springBootBoard.web.member.domain.Gender;
import com.allan.springBootBoard.web.member.domain.Member;
import com.allan.springBootBoard.web.member.domain.MemberRole;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

// CustomOAuth2UserService 와 AuthenticationConverter 에서 repository 단에서 조회를 하기 위해
// 사용 할 authId 는 attributes 필드에 저장후 꺼내서 사용하도록 업데이트 되었다.

@Getter
@ToString
public class OAuthAttributes {
    private String registerId;
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes,
                           String nameAttributeKey, String name,
                           String email, String picture, String registerId) {
        this.attributes = attributes;
        this.nameAttributeKey= nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.registerId = registerId;
    }

    public static OAuthAttributes of(String registrationId,
                                     String userNameAttributeName,
                                     Map<String, Object> attributes) {
        if("naver".equals(registrationId)) {
            return ofNaver("id", attributes, registrationId);
        }else if("kakao".equals(registrationId)){
            return ofKakao("id", attributes, registrationId);
        }
        return ofGoogle(userNameAttributeName, attributes, registrationId);
    }


    private static OAuthAttributes ofGoogle(String userNameAttributeName,
                                            Map<String, Object> attributes, String registerId) {

        // AuthenticatinoCoverter 에서 사용할 attributes 를 생성.
        Map<String, Object> googleAttributes = Map.ofEntries(Map.entry("sub", attributes.get("sub")),
                                                             Map.entry("name", attributes.get("name")),
                                                             Map.entry("given_name", attributes.get("given_name")),
                                                             Map.entry("family_name", attributes.get("family_name")),
                                                             Map.entry("picture", attributes.get("picture")),
                                                             Map.entry("email", attributes.get("email")),
                                                             Map.entry("email_verified", attributes.get("email_verified")),
                                                             Map.entry("authId", getAuthId((String) attributes.get("email"), registerId)),
                                                             Map.entry("locale", attributes.get("locale")));

        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(googleAttributes)
                .nameAttributeKey(userNameAttributeName)
                .registerId(registerId)
                .build();
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName,
                                           Map<String, Object> attributes, String registerId) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        response.put("authId", getAuthId((String) response.get("email"), registerId)); // authId 를 authenticationConverter 사용하기 위함.
        return OAuthAttributes.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .picture((String) response.get("profile_image"))
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .registerId(registerId)
                .build();
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes, String registerId) {
        // kakao는 kakao_account에 유저정보가 있다. (email)
        Map<String, Object> kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");
        // kakao_account안에 또 profile이라는 JSON객체가 있다. (nickname, profile_image)
        Map<String, Object> kakaoProfile = (Map<String, Object>)kakaoAccount.get("profile");

        // authId 를 구분하여 저장하기 위함.
        kakaoProfile.put("authId", getAuthId((String) kakaoAccount.get("email"), registerId)); // authId 를 authenticationConverter 사용하기 위함.
        kakaoProfile.put("id", attributes.get("id"));
        kakaoProfile.put("connected_at", attributes.get("connected_at"));
        kakaoProfile.put("name", (String) kakaoProfile.get("nickname"));

        return OAuthAttributes.builder()
                .name((String) kakaoProfile.get("nickname"))
                .email((String) kakaoAccount.get("email"))
                .picture((String) kakaoProfile.get("profile_image_url"))
                .attributes(kakaoProfile)
                .nameAttributeKey(userNameAttributeName)
                .registerId(registerId)
                .build();
    }

    private static String getAuthId(String email, String registerId){
        if(registerId.equals("google")){
            return email;
        }else if(registerId.equals("naver")){
            return email;
        }else if(registerId.equals("kakao")){
            return email + "/" + registerId;
        }else{
            throw new RuntimeException("존재하지 않는 oauth2 인증 방식입니다.");
        }
    }

    // member nullable 한 데이터 어떻게 저장 시킬 것인지 리팩토링 필요.
    // kakao, naver 이메일이 같은 값인 경우를 대비하기 위해서
    // 회원 가입 아이디를 email + registerId 로 구성.
    public Member toEntity() {
        return Member.builder()
                .name(name)
                .email(email)
                .authId((String) attributes.get("authId"))
                .picture(picture)
                .role(MemberRole.valueOfTitle(registerId))
                .address(new Address("", "", "", "", ""))
                .pwd("defalut")
                .dateOfBirth("")
                .age(3l)
                .gender(Gender.MAN)
                .phoneNumber("")
                .build();
    }
}
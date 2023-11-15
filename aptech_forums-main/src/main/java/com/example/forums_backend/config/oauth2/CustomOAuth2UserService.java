package com.example.forums_backend.config.oauth2;

import com.example.forums_backend.config.oauth2.user.OAuth2UserInfo;
import com.example.forums_backend.config.oauth2.user.OAuth2UserInfoFactory;
import com.example.forums_backend.entity.Account;
import com.example.forums_backend.entity.my_enum.AuthProvider;
import com.example.forums_backend.entity.my_enum.StatusEnum;
import com.example.forums_backend.exception.OAuth2AuthenticationProcessingException;
import com.example.forums_backend.repository.AccountRepository;
import com.example.forums_backend.utils.SlugGenerating;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private AccountRepository userRepository;
    public static final Pattern STUDENT_FPT_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Za-z0-9._%+-]+@fpt.edu.vn$", Pattern.CASE_INSENSITIVE);

    public static final Pattern STAFF_FPT_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Za-z0-9._%+-]+@fe.edu.vn$", Pattern.CASE_INSENSITIVE);

    private static final String[] IS_ADMIN = {"ngunvth2108037@fpt.edu.vn", "ducnvth2008042@fpt.edu.vn","quangnhth2008059@fpt.edu.vn"};

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        if (StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        Optional<Account> userOptional = userRepository.findByEmail(oAuth2UserInfo.getEmail());
        Account account;
        if (userOptional.isPresent()) {
            account = userOptional.get();
            if (!account.getProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
                throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
                        account.getProvider() + " account. Please use your " + account.getProvider() +
                        " account to login.");
            }
            account = updateExistingUser(account, oAuth2UserInfo);
        } else {
            account = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        }

        return UserPrincipal.create(account, oAuth2User.getAttributes());
    }

    private Account registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        Matcher studentMatcher = STUDENT_FPT_EMAIL_ADDRESS_REGEX.matcher(oAuth2UserInfo.getEmail());
        Matcher staffMatcher = STAFF_FPT_EMAIL_ADDRESS_REGEX.matcher(oAuth2UserInfo.getEmail());
        boolean isFptMember = false;
        boolean isAdmin = false;
        if (studentMatcher.find() || staffMatcher.find()) {
            isFptMember = true;
        }
        if (Arrays.asList(IS_ADMIN).contains(oAuth2UserInfo.getEmail())) {
            isAdmin = true;
        }
        Account user = new Account();
        String username = SlugGenerating.toUsername(oAuth2UserInfo.getName());
        if(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()) == AuthProvider.github){
            user.setGithub_username(oAuth2UserInfo.getUsername());
        }
        user.setProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
        user.setProviderId(oAuth2UserInfo.getId());
        user.setName(oAuth2UserInfo.getName());
        user.setEmail(oAuth2UserInfo.getEmail());
        user.setUsername(username);
        user.setStatus(StatusEnum.ACTIVE);
        user.setFpt_member(isFptMember);
        user.setEmail_verify(true);
        user.setImageUrl(oAuth2UserInfo.getImageUrl());
        user.setRole(isAdmin ? "ADMIN" : "USER");
        return userRepository.save(user);
    }

    private Account updateExistingUser(Account existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setName(oAuth2UserInfo.getName());
        existingUser.setImageUrl(oAuth2UserInfo.getImageUrl());
        return userRepository.save(existingUser);
    }

}

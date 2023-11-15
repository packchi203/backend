package com.example.forums_backend.service;

import com.example.forums_backend.dto.*;
import com.example.forums_backend.entity.*;
import com.example.forums_backend.entity.my_enum.AuthProvider;
import com.example.forums_backend.entity.my_enum.StatusEnum;
import com.example.forums_backend.exception.AccountException;
import com.example.forums_backend.exception.AppException;
import com.example.forums_backend.repository.AccountRepository;
import com.example.forums_backend.repository.UserBadgeRepository;
import com.example.forums_backend.repository.UserContactRepository;
import com.example.forums_backend.utils.GeneratingPassword;
import com.example.forums_backend.utils.JwtUtil;
import com.example.forums_backend.utils.SlugGenerating;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.transaction.Transactional;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {
    private int expireTime = 60 * 1000 * 5;

    public static final Pattern STUDENT_FPT_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Za-z0-9._%+-]+@fpt.edu.vn$", Pattern.CASE_INSENSITIVE);

    public static final Pattern STAFF_FPT_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Za-z0-9._%+-]+@fe.edu.vn$", Pattern.CASE_INSENSITIVE);

    private static final String[] IS_ADMIN = {"dduc7th.dec@gmail.com", "ngunvth2108037@fpt.edu.vn", "ahhaclong4@gmail.com"};

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    UserContactRepository userContactRepository;
    @Autowired
    UserBadgeRepository userBadgeRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    EmailService emailService;
    @Autowired
    private TemplateEngine templateEngine;

    public List<UserAllInfoDto> usersFamous(){
        List<Account> accountList = accountRepository.findAll(Sort.by(Sort.Direction.DESC,"reputation")).stream().filter(it-> it.getReputation() > 10).collect(Collectors.toList());
        return accountList.stream().limit(5).map(this::toDto).collect(Collectors.toList());
    }
    public CredentialDto loginWithOTP(LoginDto loginDto) throws AccountException {
        Optional<Account> account = Optional.ofNullable(accountRepository.findAccountByEmail(loginDto.getEmail()));
        if (!account.isPresent()) {

            throw new AccountException("Account not exist");
        }
        Account userInfo = account.get();
        boolean result = passwordEncoder.matches(loginDto.getPassword(), userInfo.getOne_time_password());
        if (!result) throw new AccountException("Password not match");
        boolean expireTime = true;
        if (new Date(System.currentTimeMillis()).before(userInfo.getExpire_time())) expireTime = false;
        if (expireTime) throw new AccountException("OTP IS EXPIRE");
        String accessToken = JwtUtil.generateToken(userInfo.getEmail(), userInfo.getRole(), "APTECH", JwtUtil.ONE_DAY * 7);
        // generate refresh token
        String refreshToken = JwtUtil.generateToken(userInfo.getEmail(), userInfo.getRole(), "APTECH", JwtUtil.ONE_DAY * 14);
        return CredentialDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(System.currentTimeMillis() + JwtUtil.ONE_DAY * 7)
                .tokenType("Bearer")
                .scope("basic_info")
                .build();
    }

    public CheckAccount getOtp(LoginEmailDto loginEmailDto) {
        try {
            Optional<Account> account = Optional.ofNullable(accountRepository.findAccountByEmail(loginEmailDto.getEmail()));
            if (!account.isPresent()) {
                return CheckAccount.builder()
                        .accountExist(false)
                        .build();
            }
            String password = String.valueOf(GeneratingPassword.generatePassword(12));
            Account accountUpdate = account.get();
            accountUpdate.setOne_time_password(passwordEncoder.encode(password));
            accountUpdate.setExpire_time(new Date(System.currentTimeMillis() + expireTime));
            if (!accountUpdate.isEmail_verify()) {
                accountUpdate.setEmail_verify(true);
            }
            accountRepository.save(accountUpdate);
            Context context = new Context();
            context.setVariable("password", password);
            String template = templateEngine.process("one_time_password", context);


            EmailDetails emailDetails = new EmailDetails();
            emailDetails.setRecipient(accountUpdate.getEmail());
            emailDetails.setMsgBody(template);
            emailDetails.setSubject("OTP LOGIN");
            emailService.sendSimpleMail(emailDetails);


            return CheckAccount.builder()
                    .accountExist(true)
                    .build();
        } catch (Exception exception) {
            log.error(String.valueOf(exception));
            throw new RuntimeException(exception);
        }

    }

    public RegisterDto register(RegisterDto accountRegisterDto) throws AccountException {
        Optional<Account> account = Optional.ofNullable(accountRepository.findAccountByEmail(accountRegisterDto.getEmail()));
        if (account.isPresent()) {
            throw new AccountException("Account is exist");
        }
        Matcher studentMatcher = STUDENT_FPT_EMAIL_ADDRESS_REGEX.matcher(accountRegisterDto.getEmail());
        Matcher staffMatcher = STAFF_FPT_EMAIL_ADDRESS_REGEX.matcher(accountRegisterDto.getEmail());
        boolean isFptMember = false;
        boolean isAdmin = false;
        if (studentMatcher.find() || staffMatcher.find()) {
            isFptMember = true;
        }
        if (studentMatcher.find() || staffMatcher.find()) {
            isFptMember = true;
        }
        if (Arrays.asList(IS_ADMIN).contains(accountRegisterDto.getEmail())) {
            isAdmin = true;
        }
        String username = SlugGenerating.toUsername(accountRegisterDto.getName().trim());
        Account newAccount = Account.builder()
                .name(accountRegisterDto.getName().trim())
                .email(accountRegisterDto.getEmail())
                .username(username)
                .email_verify(false)
                .status(StatusEnum.ACTIVE)
                .fpt_member(isFptMember)
                .provider(AuthProvider.local)
                .role(isAdmin ? "ADMIN" : "USER")
                .build();
        accountRepository.save(newAccount);
        accountRegisterDto.setEmail(newAccount.getEmail());
        return accountRegisterDto;

    }

    public List<UserBadge> getListBadge() {
        Account account = getUserInfoData();
        return getListUserBadge(account);
    }

    public List<UserBadge> getListBadgeByUsername(String username) {
        Optional<Account> optionalAccount = accountRepository.findFirstByUsername(username);
        if (!optionalAccount.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }
        Account account = optionalAccount.get();
        return getListUserBadge(account);
    }

    public List<UserBadge> getListUserBadge(Account account) {
        return userBadgeRepository.findByAccount_Id(account.getId());
    }

    public UserContact updateContact(Long contactId, UserContact userContact) {
        Account account = getUserInfoData();
        Optional<UserContact> userContactOptional = userContactRepository.findById(contactId);
        if (userContactOptional.isPresent()) {
            UserContact userContact1 = userContactOptional.get();
            if (userContact1.getAccount().equals(account)) {
                userContact1.setType(userContact.getType());
                userContact1.setName(userContact.getName());
                userContact1.setIcon(userContact.getIcon());
                userContactRepository.save(userContact1);
                return userContact;
            }
        }
        return null;
    }

    public boolean deleteContact(Long contactId) {
        Account account = getUserInfoData();
        Optional<UserContact> userContactOptional = userContactRepository.findById(contactId);
        if (userContactOptional.isPresent()) {
            UserContact userContact = userContactOptional.get();
            if (userContact.getAccount().equals(account)) {
                userContactRepository.deleteById(userContact.getId());
                return true;
            }
        }
        return false;
    }

    public List<UserContact> getUserContact() {
        Account account = getUserInfoData();
        return getListUserContact(account);
    }

    public List<UserContact> getUserContactByUsername(String username) {
        Optional<Account> optionalAccount = accountRepository.findFirstByUsername(username);
        if (!optionalAccount.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }
        Account account = optionalAccount.get();
        return getListUserContact(account);
    }

    public List<UserContact> getListUserContact(Account account) {
        return userContactRepository.findByAccount_Id(account.getId());
    }

    public Account getUserInfoData() {
        Object userInfo = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(userInfo);
        if (userInfo != "anonymousUser") {
            Optional<Account> optionalAccount = Optional.ofNullable(accountRepository.findAccountByEmail(userInfo.toString()));
            if (!optionalAccount.isPresent()) {
                throw new UsernameNotFoundException("User not found");
            }
            return optionalAccount.get();
        }
        return null;
    }

    public ProfileDto findUserInfoByUsername(String username) throws AppException {
        Optional<Account> optionalAccount = accountRepository.findFirstByUsername(username);
        if (!optionalAccount.isPresent()) {
            throw new AppException("User Not Found!");
        }
        Account account = optionalAccount.get();
        List<UserContact> userContacts = getUserContactByUsername(account.getUsername());
        List<UserBadge> userBadges = getListBadgeByUsername(account.getUsername());
        UserAllInfoDto userAllInfoDto = UserAllInfoDto.builder()
                .name(account.getName())
                .username(account.getUsername())
                .avatar(account.getImageUrl())
                .bio(account.getBio())
                .skill(account.getSkill())
                .reputation(account.getReputation())
                .github_username(account.getGithub_username())
                .post_count(account.getPosts().size())
                .comment_count(account.getComments().size())
                .tag_flowing_count(account.getTagFollowings().size())
                .badge_count(account.getUserBadge().size())
                .role(account.getRole())
                .web_url(account.getWeb_url())
                .education(account.getEducation())
                .email_display(account.getEmail_display())
                .createdAt(account.getCreatedAt())
                .build();
        return ProfileDto.builder()
                .info(userAllInfoDto)
                .contacts(userContacts)
                .badges(userBadges)
                .build();
    }

    public ProfileDto getMyProfile(){
        Account account = getUserInfoData();
        List<UserContact> userContacts = getUserContact();
        List<UserBadge> userBadges = getListBadge();
        UserAllInfoDto userAllInfoDto = UserAllInfoDto.builder()
                .name(account.getName())
                .username(account.getUsername())
                .bio(account.getBio())
                .avatar(account.getImageUrl())
                .skill(account.getSkill())
                .reputation(account.getReputation())
                .post_count(account.getPosts().size())
                .github_username(account.getGithub_username())
                .comment_count(account.getComments().size())
                .tag_flowing_count(account.getTagFollowings().size())
                .role(account.getRole())
                .web_url(account.getWeb_url())
                .education(account.getEducation())
                .email_display(account.getEmail_display())
                .badge_count(account.getUserBadge().size())
                .email(account.getEmail())
                .createdAt(account.getCreatedAt())
                .build();
        return ProfileDto.builder()
                .info(userAllInfoDto)
                .contacts(userContacts)
                .badges(userBadges)
                .build();
    }

    public UserAllInfoDto myInfo() {
        Account account = getUserInfoData();
        return UserAllInfoDto.builder()
                .name(account.getName())
                .username(account.getUsername())
                .bio(account.getBio())
                .avatar(account.getImageUrl())
                .skill(account.getSkill())
                .reputation(account.getReputation())
                .post_count(account.getPosts().size())
                .comment_count(account.getComments().size())
                .github_username(account.getGithub_username())
                .tag_flowing_count(account.getTagFollowings().size())
                .role(account.getRole())
                .web_url(account.getWeb_url())
                .education(account.getEducation())
                .email_display(account.getEmail_display())
                .badge_count(account.getUserBadge().size())
                .email(account.getEmail())
                .createdAt(account.getCreatedAt())
                .build();


    }
    public UserAllInfoDto toDto(Account account){
        return UserAllInfoDto.builder()
                .name(account.getName())
                .avatar(account.getImageUrl())
                .username(account.getUsername())
                .reputation(account.getReputation())
                .post_count(account.getPosts().size())
                .comment_count(account.getComments().size())
                .build();
    }

    public UpdateInfoDto updateInfoDto(UpdateInfoDto updateInfoDto) throws AppException {
        Account account = getUserInfoData();
        Optional<Account> optionalAccount = accountRepository.findFirstByUsername(updateInfoDto.getUsername());
        if(optionalAccount.isPresent()){
            Account accountExist = optionalAccount.get();
            if(!Objects.equals(accountExist.getEmail(), account.getEmail())){
                throw new AppException("USERNAME IS USED");
            }
        }
        account.setUsername(updateInfoDto.getUsername());
        account.setImageUrl(updateInfoDto.getImageUrl());
        account.setName(updateInfoDto.getName());
        account.setSkill(updateInfoDto.getSkill());
        account.setBio(updateInfoDto.getBio());
        account.setEducation(updateInfoDto.getEducation());
        account.setWeb_url(updateInfoDto.getWeb_url());
        account.setEmail_display(updateInfoDto.getEmail_display());
        account.setGithub_username(updateInfoDto.getGithub_username());
        accountRepository.save(account);
        return updateInfoDto;
    }

    public boolean deleteProfile() {
        Account account = getUserInfoData();
        accountRepository.deleteById(account.getId());
        return true;
    }

    public boolean adminCheck(){
        Account account = getUserInfoData();
        return Objects.equals(account.getRole(), "ADMIN");
    }

    public Account findByUsername(String username){
        return accountRepository.findFirstByUsername(username).get();
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = accountRepository.findAccountByEmail(email);
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(account.getRole()));
        return new User(account.getEmail(), account.getPassword(), authorities);
    }

}

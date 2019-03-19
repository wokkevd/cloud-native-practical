package com.ezgroceries.shoppinglist.service;

import com.ezgroceries.shoppinglist.contract.shoppinglist.ShopUserResource;
import com.ezgroceries.shoppinglist.domain.ShopUserEntity;
import com.ezgroceries.shoppinglist.domain.StubEmailEntity;
import com.ezgroceries.shoppinglist.domain.VerificationRequestEntity;
import com.ezgroceries.shoppinglist.exceptions.ConflictException;
import com.ezgroceries.shoppinglist.exceptions.UnauthorizedException;
import com.ezgroceries.shoppinglist.factory.ShopUserResourceFactory;
import com.ezgroceries.shoppinglist.repository.ShopUserRepository;
import com.ezgroceries.shoppinglist.repository.StubEmailRepository;
import com.ezgroceries.shoppinglist.repository.VerificationRequestRepository;
import com.ezgroceries.shoppinglist.security.SecurityManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopUserServiceTest {

    private static final String FIRST_NAME = "Bert";
    private static final String LAST_NAME = "Van Ernie";
    private static final String EMAIL = "bert@sesamstraat.nl";
    private static final String PASSWORD = "Banaan@002";
    private static final String ENCRYPTED_PASSWORD = "Encrypted password " + PASSWORD;
    private static final UUID USER_ID = UUID.randomUUID();
    private static final String VERIFICATION_CODE = "123456";
    private static final long VALIDITY = 30L;
    private static final Date CREATED_DATE = new Date();
    private static final String EMAIL_TEXT = "Hallo";
    private static final String SUBJECT = "Your personal verification code for the awesome ShoppingList app";
    private static final String SECURITY_TOKEN = "abc123";

    @Autowired
    private ShopUserService shopUserService;
    @MockBean
    private ShopUserRepository shopUserRepository;
    @MockBean
    private VerificationRequestRepository verificationRequestRepository;
    @MockBean
    private StubEmailRepository stubEmailRepository;
    @MockBean
    private SecurityManager securityManager;
    @SpyBean
    private ShopUserResourceFactory shopUserResourceFactory;
    @Captor
    ArgumentCaptor<VerificationRequestEntity> verificationRequestEntityArgumentCaptor;
    @Captor
    ArgumentCaptor<StubEmailEntity> stubEmailEntityArgumentCaptor;
    @Captor
    ArgumentCaptor<String> securityTokenStringArgumentCaptor;

    @Before
    public void init() {
        ShopUserEntity shopUserEntity = new ShopUserEntity(FIRST_NAME, LAST_NAME, EMAIL, ENCRYPTED_PASSWORD, false, new ArrayList<>());
        shopUserEntity.setId(USER_ID);
        VerificationRequestEntity verificationRequestEntity = new VerificationRequestEntity(USER_ID, VERIFICATION_CODE, VALIDITY, CREATED_DATE);
        StubEmailEntity stubEmailEntity = new StubEmailEntity(EMAIL_TEXT, SUBJECT, EMAIL, LocalDateTime.ofInstant(CREATED_DATE.toInstant(),
                TimeZone.getDefault().toZoneId()));
        BCryptPasswordEncoder passwordEncoder = mock(BCryptPasswordEncoder.class);
        when(securityManager.getPasswordEncoder()).thenReturn(passwordEncoder);
        when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCRYPTED_PASSWORD);
        when(passwordEncoder.matches(PASSWORD, ENCRYPTED_PASSWORD)).thenReturn(true);
        when(shopUserRepository.save(any())).thenReturn(shopUserEntity);
        when(verificationRequestRepository.save(verificationRequestEntityArgumentCaptor.capture())).thenReturn(verificationRequestEntity);
        when(stubEmailRepository.save(stubEmailEntityArgumentCaptor.capture())).thenReturn(stubEmailEntity);
        when(verificationRequestRepository.findTopByUserIdOrderByCreatedDateDesc(USER_ID)).thenReturn(Optional.of(verificationRequestEntity));
        when(shopUserRepository.findById(USER_ID)).thenReturn(Optional.of(shopUserEntity));
        when(securityManager.createJWT(USER_ID.toString())).thenReturn(SECURITY_TOKEN);
    }

    @Test
    public void registerUser() {
        ShopUserResource shopUserResource = shopUserService.registerUser(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD);
        assertThat(shopUserResource).isNotNull();
        assertThat(shopUserResource.getUserId()).isNotNull();
        assertThat(shopUserResource.getFirstName()).isEqualTo(FIRST_NAME);
        assertThat(shopUserResource.getLastName()).isEqualTo(LAST_NAME);
        assertThat(shopUserResource.getEmail()).isEqualTo(EMAIL);
        verify(shopUserRepository, times(1)).save(any());
        verify(verificationRequestRepository, times(1)).save(any());
        verify(stubEmailRepository, times(1)).save(any());
        VerificationRequestEntity capturedVerificationRequestEntity = verificationRequestEntityArgumentCaptor.getValue();
        assertThat(capturedVerificationRequestEntity).isNotNull();
        assertThat(capturedVerificationRequestEntity.getUserId()).isEqualTo(USER_ID);
        assertThat(capturedVerificationRequestEntity.getVerificationCode()).isBetween("100000", "999999");
        assertThat(capturedVerificationRequestEntity.getValidity()).isEqualTo(VALIDITY);
        StubEmailEntity capturedStubEmailEntity = stubEmailEntityArgumentCaptor.getValue();
        assertThat(capturedStubEmailEntity).isNotNull();
        assertThat(capturedStubEmailEntity.getToAddress()).isEqualTo(EMAIL);
        assertThat(capturedStubEmailEntity.getSubject()).isEqualTo(SUBJECT);
        assertThat(capturedStubEmailEntity.getEmailText()).isNotBlank();
    }

    @Test(expected = ConflictException.class)
    public void registerExistingUser() {
        when(shopUserRepository.findByEmail(EMAIL)).thenReturn(Optional.of(new ShopUserEntity()));
        shopUserService.registerUser(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD);
    }

    @Test
    public void verifyUser() {
        ShopUserResource shopUserResource = shopUserService.verifyUser(USER_ID, VERIFICATION_CODE);
        assertThat(shopUserResource).isNotNull();
        assertThat(shopUserResource.getUserId()).isNotNull();
        assertThat(shopUserResource.getFirstName()).isEqualTo(FIRST_NAME);
        assertThat(shopUserResource.getLastName()).isEqualTo(LAST_NAME);
        assertThat(shopUserResource.getEmail()).isEqualTo(EMAIL);
    }

    @Test(expected = UnauthorizedException.class)
    public void verifyUserWithoutVerificationRequest() {
        when(verificationRequestRepository.findTopByUserIdOrderByCreatedDateDesc(USER_ID)).thenReturn(Optional.empty());
        shopUserService.verifyUser(USER_ID, VERIFICATION_CODE);
    }

    @Test(expected = UnauthorizedException.class)
    public void verifyNonExistingUser() {
        when(shopUserRepository.findById(USER_ID)).thenReturn(Optional.empty());
        shopUserService.verifyUser(USER_ID, VERIFICATION_CODE);
    }

    @Test(expected = UnauthorizedException.class)
    public void verifyUserWithExpiredRequest() {
        VerificationRequestEntity verificationRequestEntity = new VerificationRequestEntity(USER_ID, VERIFICATION_CODE, VALIDITY,
                java.sql.Timestamp.valueOf(LocalDateTime.now().minusHours(1)));
        when(verificationRequestRepository.findTopByUserIdOrderByCreatedDateDesc(USER_ID)).thenReturn(Optional.of(verificationRequestEntity));
        shopUserService.verifyUser(USER_ID, VERIFICATION_CODE);
    }

    @Test(expected = UnauthorizedException.class)
    public void verifyUseWithIncorrectVerificationCode() {
        shopUserService.verifyUser(USER_ID, VERIFICATION_CODE + "X");

    }

    @Test
    public void loginUser() {
        ShopUserEntity shopUserEntity = new ShopUserEntity(FIRST_NAME, LAST_NAME, EMAIL, ENCRYPTED_PASSWORD, true, new ArrayList<>());
        shopUserEntity.setId(USER_ID);
        when(shopUserRepository.findByEmail(EMAIL)).thenReturn(Optional.of(shopUserEntity));
        String securityToken = shopUserService.loginUser(EMAIL, PASSWORD);
        assertThat(securityToken).isNotNull();
        assertThat(securityToken).isEqualTo(SECURITY_TOKEN);

    }

    @Test(expected = UnauthorizedException.class)
    public void loginNonExistingUser() {
        when(shopUserRepository.findById(USER_ID)).thenReturn(Optional.empty());
        shopUserService.loginUser(EMAIL, PASSWORD);
    }

    @Test(expected = UnauthorizedException.class)
    public void loginUserWrongPass() {
        ShopUserEntity shopUserEntity = new ShopUserEntity(FIRST_NAME, LAST_NAME, EMAIL, ENCRYPTED_PASSWORD, true, new ArrayList<>());
        shopUserEntity.setId(USER_ID);
        when(shopUserRepository.findByEmail(EMAIL)).thenReturn(Optional.of(shopUserEntity));
        shopUserService.loginUser(EMAIL, PASSWORD + "X");
    }
}
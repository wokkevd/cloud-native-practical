package com.ezgroceries.shoppinglist.service;

import com.ezgroceries.shoppinglist.contract.shoppinglist.ShopUserResource;
import com.ezgroceries.shoppinglist.domain.ShopUserEntity;
import com.ezgroceries.shoppinglist.domain.StubEmailEntity;
import com.ezgroceries.shoppinglist.domain.VerificationRequestEntity;
import com.ezgroceries.shoppinglist.exceptions.BadRequestException;
import com.ezgroceries.shoppinglist.exceptions.ConflictException;
import com.ezgroceries.shoppinglist.exceptions.UnauthorizedException;
import com.ezgroceries.shoppinglist.factory.ShopUserResourceFactory;
import com.ezgroceries.shoppinglist.repository.ShopUserRepository;
import com.ezgroceries.shoppinglist.repository.StubEmailRepository;
import com.ezgroceries.shoppinglist.repository.VerificationRequestRepository;
import com.ezgroceries.shoppinglist.security.SecurityManager;
import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;
import java.util.UUID;

@Service
@Slf4j
public class ShopUserService {

    private static final Long VALIDITY_MINUTES = 30L;
    private static final String EMAIL_CONTENT_PATTERN = "Dear %s,\n\nYour verification code is %s\nPlease enter it in your app when asked to verify.";
    private static final UnauthorizedException INVALID_VERIFICATION_ATTEMPT = new UnauthorizedException("Invalid verification attempt");

    private final ShopUserRepository shopUserRepository;
    private final ShopUserResourceFactory shopUserResourceFactory;
    private final VerificationRequestRepository verificationRequestRepository;
    private final StubEmailRepository stubEmailRepository;
    private final SecurityManager securityManager;

    public ShopUserService(ShopUserRepository shopUserRepository, ShopUserResourceFactory shopUserResourceFactory,
                           VerificationRequestRepository verificationRequestRepository, StubEmailRepository stubEmailRepository,
                           SecurityManager securityManager) {
        this.shopUserRepository = shopUserRepository;
        this.shopUserResourceFactory = shopUserResourceFactory;
        this.verificationRequestRepository = verificationRequestRepository;
        this.stubEmailRepository = stubEmailRepository;
        this.securityManager = securityManager;
    }

    public ShopUserResource registerUser(String firstName, String lastName, String email, String password) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        if (shopUserRepository.findByEmail(email).isPresent()) {
            throw new ConflictException("User already exists");
        }
        log.info("Find existing user took " + stopwatch.stop());
        stopwatch = Stopwatch.createStarted();
        ShopUserEntity newShopUserEntity = ShopUserEntity.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password(securityManager.getPasswordEncoder().encode(password))
                .build();
        ShopUserEntity savedShopUserEntity = shopUserRepository.save(newShopUserEntity);
        log.info("Saving user took " + stopwatch.stop());
        startUserVerification(savedShopUserEntity);
        return shopUserResourceFactory.create(savedShopUserEntity);
    }

    private void startUserVerification(ShopUserEntity shopUserEntity) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Random rnd = new Random();
        int verificationCode = 100000 + rnd.nextInt(900000);
        verificationRequestRepository.save(VerificationRequestEntity.builder()
                .userId(shopUserEntity.getId())
                .validity(VALIDITY_MINUTES)
                .verificationCode(Integer.toString(verificationCode))
                .build());
        log.info("Verification request took " + stopwatch.stop());
        stopwatch = Stopwatch.createStarted();
        String subject = "Your personal verification code for the awesome ShoppingList app";
        String content = String.format(EMAIL_CONTENT_PATTERN, shopUserEntity.getFirstName(), verificationCode);
        sendEmail(shopUserEntity.getEmail(), subject, content);
        log.info("Send mail took " + stopwatch.stop());
    }

    private void sendEmail(String email, String subject, String content) {
        //TODO add actual Mail service to send verification codes
        StubEmailEntity stubEmailEntity = StubEmailEntity.builder()
                .toAddress(email)
                .subject(subject)
                .emailText(content)
                .build();
        stubEmailRepository.save(stubEmailEntity);
    }

    public ShopUserResource verifyUser(UUID userId, String verificationCode) {
        VerificationRequestEntity verificationRequestEntity = verificationRequestRepository.findTopByUserIdOrderByCreatedDateDesc(userId)
                .orElseThrow(() -> INVALID_VERIFICATION_ATTEMPT);
        if (isValid(verificationRequestEntity, verificationCode)) {
            ShopUserEntity shopUserEntity = shopUserRepository.findById(userId).orElseThrow(() -> INVALID_VERIFICATION_ATTEMPT);
            shopUserEntity.setVerified(true);
            return shopUserResourceFactory.create(shopUserRepository.save(shopUserEntity));
        }
        throw INVALID_VERIFICATION_ATTEMPT;
    }

    public String loginUser(String email, String password) {
        ShopUserEntity shopUserEntity = shopUserRepository.findByEmail(email).orElseThrow(() -> INVALID_VERIFICATION_ATTEMPT);
        if (!shopUserEntity.isVerified()) {
            throw new UnauthorizedException("Please verify your account first...");
        }
        if (!securityManager.getPasswordEncoder().matches(password, shopUserEntity.getPassword())) {
            throw INVALID_VERIFICATION_ATTEMPT;
        }
        return securityManager.createJWT(shopUserEntity.getId().toString());
    }

    private boolean isValid(VerificationRequestEntity verificationRequestEntity, String verificationCode) {
        Date createdDate = verificationRequestEntity.getCreatedDate();
        long timeExpired = ChronoUnit.MINUTES.between(LocalDateTime.ofInstant(createdDate.toInstant(), TimeZone.getDefault().toZoneId()), LocalDateTime.now());
        return verificationRequestEntity.getValidity() >= timeExpired && verificationRequestEntity.getVerificationCode().equals(verificationCode);
    }
}

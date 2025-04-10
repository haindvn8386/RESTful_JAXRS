package restful.jaxrs.service;

import com.github.javafaker.Faker;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import restful.jaxrs.entity.Profile;
import restful.jaxrs.entity.User;
import restful.jaxrs.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class SeedDataService {

    @Autowired
    private UserRepository userRepository;

    private static final int BATCH_SIZE = 100; // Tăng batch size để cải thiện hiệu năng
    private static final int TOTAL_USERS = 1000;
    private static final String DOMAIN = "example.com";
    private static final int MAX_RETRIES = 3;

    private final AtomicInteger successCount = new AtomicInteger(0);
    private final AtomicInteger duplicateCount = new AtomicInteger(0);
    private final AtomicInteger errorCount = new AtomicInteger(0);

    @Transactional
    public String addUserFake() {
        Faker faker = new Faker();
        List<User> usersToSave = new ArrayList<>(BATCH_SIZE);

        for (int i = 1; i <= TOTAL_USERS; i++) {
            try {
                Profile profile = createFakeProfile(faker);
                User user = createFakeUser(faker, profile, i);
                usersToSave.add(user);

                if (usersToSave.size() >= BATCH_SIZE || i == TOTAL_USERS) {
                    saveBatchWithRetry(usersToSave);
                    usersToSave.clear();
                }

                successCount.incrementAndGet();
            } catch (Exception e) {
                errorCount.incrementAndGet();
                System.err.println("Error creating user " + i + ": " + e.getMessage());
            }
        }

        return generateReport();
    }

    private void saveBatchWithRetry(List<User> users) {
        int retry = 0;
        while (retry < MAX_RETRIES) {
            try {
                userRepository.saveAll(users);
                return;
            } catch (DataIntegrityViolationException e) {
                retry++;
                duplicateCount.incrementAndGet();
                if (retry >= MAX_RETRIES) {
                    System.err.println("Failed to save batch after " + MAX_RETRIES + " attempts: " + e.getMessage());
                    return;
                }
                handleDuplicateBatch(users, e);
            }
        }
    }

    private void handleDuplicateBatch(List<User> users, DataIntegrityViolationException e) {
        System.err.println("Duplicate data detected in batch: " + e.getMessage());
        // Có thể thêm logic phức tạp hơn để xử lý duplicate, hiện tại chỉ log
    }

    private Profile createFakeProfile(Faker faker) {
        Profile profile = new Profile();
        profile.setFullName(faker.name().fullName());
        profile.setDateOfBirth(faker.date().birthday(18, 60).toInstant()
                .atZone(ZoneOffset.UTC).toLocalDate().toString());
        profile.setGender(faker.options().option("Male", "Female", "Other"));
        profile.setPhoneNumber(faker.phoneNumber().phoneNumber());
        profile.setAddress(faker.address().fullAddress());
        profile.setAvatarUrl(faker.internet().image());
        profile.setCreatedBy("system");
        profile.setStartDate(LocalDateTime.now());
        return profile;
    }

    private User createFakeUser(Faker faker, Profile profile, int index) {
        User user = new User();
        String username = generateUniqueUsername(faker, index);
        String email = generateUniqueEmail(faker, index);

        user.setUserName(username);
        user.setPasswordHash(faker.internet().password());
        user.setNormalizedUserName(username.toUpperCase());
        user.setEmail(email);
        user.setNormalizedEmail(email.toUpperCase());
        user.setEmailConfirmed(faker.bool().bool());
        user.setSecurityStamp(faker.random().hex(32));
        user.setConcurrencyStamp(faker.random().hex(32));
        user.setPhoneNumberConfirmed(faker.bool().bool());
        user.setTwoFactorEnabled(false);
        user.setLockoutEnabled(true);
        user.setLockoutEnd(null);
        user.setAccessFailedCount(0);
        user.setCreatedBy("system");
        user.setStartDate(LocalDateTime.now());
        user.setProfile(profile);
        // Không cần set tasks vì không tạo Task trong seed data này

        return user;
    }

    private String generateUniqueUsername(Faker faker, int index) {
        return faker.name().username().replace(".", "") + "_" + index;
    }

    private String generateUniqueEmail(Faker faker, int index) {
        String base = faker.internet().emailAddress().split("@")[0].replace(".", "");
        return base + "_" + index + "@" + DOMAIN;
    }

    private String generateReport() {
        return String.format("""
                Data generation completed!
                Total users processed: %d
                Successfully created: %d
                Duplicates skipped: %d
                Errors encountered: %d
                """, TOTAL_USERS, successCount.get(), duplicateCount.get(), errorCount.get());
    }
}
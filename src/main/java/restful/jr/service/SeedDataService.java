package restful.jr.service;

import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import restful.jr.entity.Profile;
import restful.jr.entity.User;
import restful.jr.enums.Gender;
import restful.jr.repository.UserRepository;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class SeedDataService {

    @Autowired
    private UserRepository userRepository;

    private static final int BATCH_SIZE = 1000;
    private static final int TOTAL_USERS = 1000000;
    private static final String DOMAIN = "example.com";
    private static final int MAX_RETRIES = 3;

    private final AtomicInteger successCount = new AtomicInteger(0);
    private final AtomicInteger duplicateCount = new AtomicInteger(0);
    private final AtomicInteger errorCount = new AtomicInteger(0);



    @Transactional
    public String addUserFake() {
        Faker faker = new Faker();
        List<User> usersToSave = new ArrayList<>(BATCH_SIZE);

        // Lấy ID lớn nhất hiện tại từ bảng users
        Long maxId = userRepository.findMaxId();

        for (int i = 1; i <= TOTAL_USERS; i++) {
            try {
                // Tăng ID dựa trên maxId
                long currentId = maxId + i;
                Profile profile = createFakeProfile(faker);
                User user = createFakeUser(faker, profile, currentId);
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
        // Có thể thử tạo lại username và email mới cho user trong batch
        Faker faker = new Faker();
        for (User user : users) {
            try {
                // Tạo lại username và email duy nhất
                long newId = userRepository.findMaxId()+ 1;
                user.setUsername(generateUniqueUsername(faker, newId));
                user.setNormalizedUsername(user.getNormalizedUsername().toUpperCase());
                user.setEmail(generateUniqueEmail(faker, newId));
                user.setNormalizedEmail(user.getEmail().toUpperCase());
            } catch (Exception ex) {
                System.err.println("Error regenerating data for user: " + ex.getMessage());
            }
        }
    }

    private Profile createFakeProfile(Faker faker) {
        Profile profile = new Profile();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        profile.setFirstName(firstName);
        profile.setLastName(lastName);
        profile.setFullName(firstName + " " + lastName);
        profile.setGender(faker.options().option(Gender.MALE, Gender.FEMALE));
        profile.setDateOfBirth(Date.from(
                faker.date().birthday(18, 60).toInstant().atZone(ZoneOffset.UTC).toInstant()
        ));
        profile.setPhoneNumber(faker.phoneNumber().cellPhone());
        //profile.setAddress(faker.address().fullAddress());
        profile.setAvatarUrl(faker.internet().avatar());
        profile.setStatus("Active");
        profile.setCreatedBy(0L); // Giả định user mặc định
        profile.setUpdatedBy(0L);
        return profile;
    }

    private User createFakeUser(Faker faker, Profile profile, long index) {
        User user = new User();
        user.setUsername(generateUniqueUsername(faker, index));
        user.setNormalizedUsername(user.getNormalizedUsername().toUpperCase());
        user.setEmail(generateUniqueEmail(faker, index));
        user.setNormalizedEmail(user.getEmail().toUpperCase());
        user.setPasswordHash(faker.internet().password(8, 20, true, true, true));
        user.setEmailConfirmed(faker.random().nextBoolean());
        user.setPhoneNumberConfirmed(faker.random().nextBoolean());
        user.setTwoFactorEnabled(faker.random().nextBoolean());
        user.setLockoutEnabled(faker.random().nextBoolean());
        user.setAccessFailedCount(faker.random().nextInt(0, 3));
        user.setSecurityStamp(UUID.randomUUID().toString());
        user.setConcurrencyStamp(UUID.randomUUID().toString());
        user.setProfile(profile);
        user.setStatus("Active");
        user.setCreatedBy(0L); // Giả định user mặc định
        user.setUpdatedBy(0L);
        return user;
    }

    private String generateUniqueUsername(Faker faker, long index) {
        String baseUsername = faker.name().username().replace(".", "").replace(" ", "");
        return baseUsername + "_" + index;
    }

    private String generateUniqueEmail(Faker faker, long index) {
        String baseEmail = faker.internet().emailAddress().split("@")[0].replace(".", "");
        return baseEmail + "_" + index + "@" + DOMAIN;
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
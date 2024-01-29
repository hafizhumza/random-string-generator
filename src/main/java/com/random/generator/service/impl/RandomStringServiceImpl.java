package com.random.generator.service.impl;

import com.random.generator.entity.RandomString;
import com.random.generator.repository.RandomStringRepository;
import com.random.generator.service.RandomStringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RandomStringServiceImpl implements RandomStringService {

    private final RandomStringRepository randomStringRepository;

    @Autowired
    public RandomStringServiceImpl(RandomStringRepository randomStringRepository) {
        this.randomStringRepository = randomStringRepository;
    }

    @Override
    public List<RandomString> getHistory() {
        return randomStringRepository.findAll();
    }

    @Override
    public List<RandomString> generateRandomString(int numStrings, int numCharacters, boolean includeDigits,
                                                   boolean includeUppercase, boolean includeLowercase, boolean uniqueStrings) {

        List<RandomString> generatedStrings = new ArrayList<>();
        Set<String> existingStrings = new HashSet<>();
        String allowedCharacters = buildAllowedCharacters(includeDigits, includeUppercase, includeLowercase);

        for (int i = 0; i < numStrings; i++) {
            String randomString = generateSingleRandomString(numCharacters, allowedCharacters);

            if (uniqueStrings) {
                while (!existingStrings.add(randomString)) {
                    randomString = generateSingleRandomString(numCharacters, allowedCharacters);
                }
            }

            RandomString entity = new RandomString();
            entity.setGeneratedString(randomString);
            entity.setIncludeDigits(includeDigits);
            entity.setIncludeUppercase(includeUppercase);
            entity.setIncludeLowercase(includeLowercase);
            entity.setUniqueString(uniqueStrings);
            randomStringRepository.save(entity);

            generatedStrings.add(entity);
        }

        return generatedStrings;
    }

    private String buildAllowedCharacters(boolean includeDigits, boolean includeUppercase, boolean includeLowercase) {
        StringBuilder allowedCharacters = new StringBuilder();
        if (includeDigits) {
            allowedCharacters.append("0123456789");
        }
        if (includeUppercase) {
            allowedCharacters.append("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        }
        if (includeLowercase) {
            allowedCharacters.append("abcdefghijklmnopqrstuvwxyz");
        }
        return allowedCharacters.toString();
    }

    private String generateSingleRandomString(int length, String allowedCharacters) {
        SecureRandom random = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(allowedCharacters.length());
            stringBuilder.append(allowedCharacters.charAt(randomIndex));
        }

        return stringBuilder.toString();
    }
}
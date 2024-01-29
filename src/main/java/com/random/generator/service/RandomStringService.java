package com.random.generator.service;


import com.random.generator.entity.RandomString;

import java.util.List;

public interface RandomStringService {

    List<RandomString> getHistory();

    List<RandomString> generateRandomString(int numStrings, int numCharacters, boolean includeDigits,
                                            boolean includeUppercase, boolean includeLowercase, boolean uniqueStrings);

}

package com.random.generator.controller;

import com.random.generator.entity.RandomString;
import com.random.generator.service.RandomStringService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class RandomStringController {

    private final RandomStringService randomStringService;

    public RandomStringController(RandomStringService randomStringService) {
        this.randomStringService = randomStringService;
    }

    @GetMapping("/input-form")
    public String showInputForm(Model model) {
        model.addAttribute("generatedStrings", randomStringService.getHistory());
        return "inputForm";
    }

    @PostMapping("/generate-strings")
    public String generateStrings(
            @RequestParam Integer numStrings,
            @RequestParam Integer numCharacters,
            @RequestParam(required = false) Boolean includeDigits,
            @RequestParam(required = false) Boolean includeUppercase,
            @RequestParam(required = false) Boolean includeLowercase,
            @RequestParam(required = false) Boolean uniqueStrings,
            Model model
    ) {

        boolean allowDigits = includeDigits != null ? includeDigits : false;
        boolean allowUppercase = includeUppercase != null ? includeUppercase : false;
        boolean allowLowercase = includeLowercase != null ? includeLowercase : false;
        boolean allowUniqueString = uniqueStrings != null ? uniqueStrings : false;

        if (!allowDigits && !allowUppercase && !allowLowercase) {
            System.out.println("Check at least one checkbox. Digits, Uppercase, Lowercase");
            return "inputForm";
        }

        if (Objects.isNull(numStrings) || Objects.isNull(numCharacters)) {
            System.out.println("Please fill the information. Number of String. Number of Characters");
            return "inputForm";
        }

        List<RandomString> generatedStrings = randomStringService.generateRandomString(numStrings, numCharacters,
                allowDigits, allowUppercase, allowLowercase, allowUniqueString);

        model.addAttribute("numStrings", numStrings);
        model.addAttribute("numCharacters", numCharacters);
        model.addAttribute("includeDigits", includeDigits);
        model.addAttribute("includeUppercase", includeUppercase);
        model.addAttribute("includeLowercase", includeLowercase);
        model.addAttribute("uniqueStrings", uniqueStrings);
        model.addAttribute("generatedStrings", generatedStrings);

        return "outputPage";
    }

    @ResponseBody
    @GetMapping("/history")
    public List<String> getHistory() {
        List<RandomString> history = randomStringService.getHistory();
        return history.stream().map(RandomString::getGeneratedString).collect(Collectors.toList());
    }
}

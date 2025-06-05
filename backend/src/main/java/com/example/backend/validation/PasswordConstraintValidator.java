package com.example.backend.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator pentru adnotarea @ValidPassword.
 * În această implementare, parola trebuie:
 *   • să aibă cel puțin 8 caractere,
 *   • să conțină cel puțin o literă mare,
 *   • să conțină cel puțin o literă mică,
 *   • să conțină cel puțin o cifră.
 */
public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
        // Nu avem nevoie de inițializare suplimentară în acest caz.
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        // Dacă valoarea este null sau goală, considerăm invalid (dacă vrei să permiți null,
        // atunci returnează true aici și adaugă @NotNull separat pe câmp).
        if (password == null || password.isEmpty()) {
            return false;
        }

        // 1) Verifică lungimea minimă: cel puțin 8 caractere
        if (password.length() < 8) {
            return false;
        }

        // 2) Verifică că există cel puțin o literă mare
        boolean hasUppercase = password.chars().anyMatch(ch -> Character.isUpperCase(ch));
        if (!hasUppercase) {
            return false;
        }

        // 3) Verifică că există cel puțin o literă mică
        boolean hasLowercase = password.chars().anyMatch(ch -> Character.isLowerCase(ch));
        if (!hasLowercase) {
            return false;
        }

        // 4) Verifică că există cel puțin o cifră
        boolean hasDigit = password.chars().anyMatch(ch -> Character.isDigit(ch));
        if (!hasDigit) {
            return false;
        }

        // Dacă toate condițiile au fost îndeplinite, considerăm parola validă
        return true;
    }
}

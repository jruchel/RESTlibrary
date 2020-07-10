package org.whatever.library.validation;

public class NameValidator extends Validator<NameConstraint, String> {

    private int upperLimit = 20;
    private int lowerLimit = 2;

    public boolean Constraint_isCorrectFormat(String name) {
        if (!name.matches("[A-Z][a-z]+")) {
            addMessage("Incorrect name format, name must start with an uppercase letter and be lowercase letters from then on\n");
            return false;
        }
        return true;
    }

    public boolean Constraint_shorterThanLimit(String name) {
        if (name.length() > upperLimit) {
            addMessage(String.format("Name must be shorter than or exactly %d\n", upperLimit));
            return false;
        }
        if (name.length() < lowerLimit) {
            addMessage(String.format("Name must be at least %d characters long\n", lowerLimit));
            return false;
        }
        return true;
    }

}

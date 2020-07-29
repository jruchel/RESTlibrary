package org.whatever.library.validation;

public class NameValidator extends Validator<NameConstraint, String> {

    private final int upperLimit = 50;
    private final int lowerLimit = 4;

    public boolean Constraint_notNull(String name) {
        if (name == null) {
            addMessage("Name must not be null");
            return false;
        }
        return true;
    }

    public boolean Constraint_isCorrectFormat(String name) {
        if (!name.matches("[A-Z][A-Za-z.\\- ]+")) {
            addMessage("Incorrect name format, name must start with an uppercase letter and be lowercase letters from then on");
            return false;
        }
        return true;
    }

    public boolean Constraint_shorterThanLimit(String name) {
        if (name.length() > upperLimit) {
            addMessage(String.format("Name must be shorter than or exactly %d", upperLimit));
            return false;
        }
        if (name.length() < lowerLimit) {
            addMessage(String.format("Name must be at least %d characters long", lowerLimit));
            return false;
        }
        return true;
    }

}

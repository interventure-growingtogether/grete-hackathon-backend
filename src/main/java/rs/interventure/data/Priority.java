package rs.interventure.data;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Priority {
    LOW("low"), MEDIUM("medium"), HIGH("high"), URGENT("urgent"), SUPER_URGENT("super urgent");

    private final String name;

    public static Priority make(int priorityCode) {

        if (priorityCode <1 || priorityCode > 5)
            throw new IllegalArgumentException(String.format("Priority: %d isn't supported!", priorityCode));

        return values()[priorityCode - 1];
    }

    @Override public String toString() {
        return name;
    }
}

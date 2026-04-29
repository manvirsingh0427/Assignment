package com.module3.model;

/**
 * Enum representing filter options for product search in the application.
 * Each option has a menu number
 */
public enum FilterOption {
    COLOR(1),
    SIZE(2),
    COLOR_AND_SIZE(3),
    BRAND_AND_COLOR(4),
    BRAND_AND_SIZE(5);

    private final int menuOption;

    /**
     * Constructs a FilterOption enum value.
     * @param menuOption Menu number for selection
     */
    FilterOption(int menuOption) {
        this.menuOption = menuOption;
    }

    /**
     * Returns the FilterOption corresponding to the given menu value.
     * @param value Menu value as string
     * @return FilterOption enum value
     * @throws IllegalArgumentException if value is invalid
     */
    public static FilterOption fromMenuValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Please choose a valid menu option.");
        }

        try {
            int selected = Integer.parseInt(value.trim());
            for (FilterOption option : values()) {
                if (option.menuOption == selected) {
                    return option;
                }
            }
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Menu option must be a number between 1 and 5.");
        }

        throw new IllegalArgumentException("Please choose a valid menu option between 1 and 5.");
    }
}

/*package com.example.cincuentazo.models;
public class Card {
    private String name;          // e.g. "7H"
    private String suit;          // e.g. "H" (Hearts)
    private String valueText;     // e.g. "7"
    private int valueNumeric;     // e.g. 7
    private String imagePath;

    public Card(String valueText, String suit, String imagePath) {
        this.valueText = valueText;
        this.suit = suit;
        this.name = valueText + suit;
        this.imagePath = imagePath;
        this.valueNumeric = assignValue(valueText);
    }

    private int assignValue(String valueText) {
        switch (valueText) {
            case "A": return 1;
            case "J": return 11;
            case "Q": return 12;
            case "K": return 13;
            default: return Integer.parseInt(valueText);
        }
    }

    public int getValueNumeric() {
        return valueNumeric;
    }

    public String getSuit() {
        return suit;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getName() {
        return name;
    }
}
*/
package com.example.cincuentazo.models;

public class Card {
    private final String name;
    private final String suit;
    private final int valueNumeric;
    private final String imagePath;

    public Card(String name, String suit, int valueNumeric, String imagePath) {
        this.name = name;
        this.suit = suit;
        this.valueNumeric = valueNumeric;
        this.imagePath = imagePath;
    }

    public String getName() { return name; }
    public String getSuit() { return suit; }
    public int getValueNumeric() { return valueNumeric; }
    public String getImagePath() { return imagePath; }

    @Override
    public String toString() {
        return name + " of " + suit;
    }
}

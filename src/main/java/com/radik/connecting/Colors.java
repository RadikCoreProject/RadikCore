package com.radik.connecting;

public enum Colors {
    DARK_RED ('4'),
    RED ('c'),
    GOLD ('6'),
    YELLOW ('e'),
    DARK_GREEN ('2'),
    GREEN ('a'),
    AQUA ('b'),
    DARK_AQUA ('3'),
    DARK_BLUE ('1'),
    BLUE ('9'),
    LIGHT_PURPLE ('d'),
    DARK_PURPLE ('5'),
    WHITE ('f'),
    GRAY ('7'),
    DARK_GRAY ('8'),
    BLACK ('0');

    private char color;

    Colors(char color) {
        this.color = color;
    }

    public char getColor() {
        return color;
    }
}

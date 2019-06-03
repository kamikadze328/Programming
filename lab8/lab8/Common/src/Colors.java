import java.awt.*;

public enum Colors {
    FernGreen(new Color(26, 133, 56)),
    Black(new Color(0, 0, 0)),
    White(new Color(255, 255, 255)),
    PareGold(new Color(161, 142, 24)),
    DeepRed(new Color(102, 29, 29)),
    Purple(new Color(111, 90, 163));



    private final Color rgbColor;

    Colors(Color rgbColor) {
        this.rgbColor = rgbColor;
    }

    public Color getRgbColor() {
        return this.rgbColor;
    }
}
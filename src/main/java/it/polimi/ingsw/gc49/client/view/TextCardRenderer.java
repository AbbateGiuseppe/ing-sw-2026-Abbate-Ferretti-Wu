package it.polimi.ingsw.gc49.client.view;

import it.polimi.ingsw.gc49.server.model.Era;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;

public final class TextCardRenderer {
    private TextCardRenderer() {
    }

    public static RectangleAttributedString render(String label, Era era) {
        String safeLabel = label == null || label.isBlank() ? "CARD" : label;
        if (safeLabel.length() > 12) {
            safeLabel = safeLabel.substring(0, 12);
        }
        String eraText = era == null ? "" : era.name();
        AttributedString text = new AttributedStringBuilder()
                .append("+--------------+\n")
                .append("| ")
                .append(padRight(safeLabel, 12))
                .append(" |\n")
                .append("| ")
                .append(padRight(eraText, 12))
                .append(" |\n")
                .append("+--------------+")
                .toAttributedString();
        return new RectangleAttributedString(4, 16, text);
    }

    public static RectangleAttributedString renderEra(Era era) {
        String eraText = era == null ? "ERA" : era.name();
        AttributedString text = new AttributedStringBuilder()
                .append("+-----+\n")
                .append("|ERA  |\n")
                .append("|")
                .append(padRight(eraText.length() > 5 ? eraText.substring(0, 5) : eraText, 5))
                .append("|\n")
                .append("+-----+")
                .toAttributedString();
        return new RectangleAttributedString(4, 7, text);
    }

    private static String padRight(String text, int width) {
        if (text.length() >= width) {
            return text;
        }
        return text + " ".repeat(width - text.length());
    }
}

package nl.reneax.sumostom.utils;

public class ChatUtils {
    /**
     * Makes chat color codes properly formatted for Minecraft.
     * @param unformattedText The text to be formatted
     * @return String
     */
    public static String format(String unformattedText) {
        char[] b = unformattedText.toCharArray();

        for(int i = 0; i < b.length - 1; ++i) {
            if (b[i] == '&' && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i + 1]) > -1) {
                b[i] = 167;
                b[i + 1] = Character.toLowerCase(b[i + 1]);
            }
        }

        return new String(b);
    }
}

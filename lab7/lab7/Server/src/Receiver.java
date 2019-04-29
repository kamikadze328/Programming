class Receiver {
    private static String message = "";

    static String get() {
        String str = message;
        message = "";
        return str;
    }

    static void add(String additive) {
        if (message.equals("")) message += additive;
        else message = message + "\n" + additive;
    }
}

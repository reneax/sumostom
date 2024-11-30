package nl.reneax.sumostom;

import nl.reneax.sumostom.utils.Constants;

public class Main {
    public static void main(String[] args) {
        Server server = new Server();
        server.start(Constants.SERVER_HOST, Constants.SERVER_PORT);
        System.out.printf("Sumostom is up and running on %s:%s%n",
                Constants.SERVER_HOST,
                Constants.SERVER_PORT);
    }
}
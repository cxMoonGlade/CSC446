import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {
    private final String name;
    public Server(String name){
        this.name = name;
        ExecutorService es = Executors.newFixedThreadPool(8);
        for (int i = 0; i < 10; i++){

        }
    }

    @Override
    public void run() {

    }
}

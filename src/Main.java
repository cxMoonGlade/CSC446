import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

public class Main {
    private AtomicDouble max_workload;
    private AtomicDouble total_waiting_time;
    private AtomicLong concurrent_users;
    private AtomicFloat server_load;
    private AtomicLong waitings;
    public static void main(String[] args) {
        // 4 servers
        ExecutorService es = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 4; i++){
            es.submit(new Server("" + i));
        }

        es.shutdown();
    }
    public void serTotal_waiting_time(AtomicDouble new_wait){

    }

}
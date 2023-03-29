import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/* Main is the engineer and initializer for everything
 * Will need you to add some methods to work the final answer out
 * I only have the total waiting time yet
 */
public class Main {
    // util fields
    private static final long size = 10; // Please modify manually
    private static final float lambda_iat = 1000f; // Please modify manually
    private static final float lambda_st = 2000f; // Please modify manually
    public static long[] inter_arriving_time_array;
    public static long[] service_time_array;
    public static long[] arriving_time_array;
    public static BlockingQueue<Client> clients;
    public static long start_time;
    private static AtomicLong concurrent_users = new AtomicLong(0l);


    // final calculation fields
    private static AtomicLong total_waiting_time = new AtomicLong(0l);
    private float server_load = 0f;
    private double max_workload = 0d;




    public static void main(String[] args) throws Exception {
        // get the current time, which can be the time that everything starts
        start_time = System.currentTimeMillis();
        TimeTable tasks = new TimeTable(lambda_iat, lambda_st, size);

        setIAT(tasks.getInter_arriving_time());
        setST(tasks.getService_time_array());
        setAT(start_time);



        clients = new LinkedBlockingQueue<>();
        Pusher pusher = new Pusher(clients, size);
        Server server1 = new Server(clients, size);

        new Thread(pusher).start();
        new Thread(server1).start();
        System.out.println("START!");

    }


    static boolean addClient(BlockingQueue<Client> clients, int idx){
        try{
            clients.add(new Client(service_time_array[idx], inter_arriving_time_array[idx],
                    arriving_time_array[idx]) );
            return true;
        }catch (Exception e){
            e.getStackTrace();
            return false;
        }
    }

    private static void setAT(long currentTime){
        arriving_time_array = new long[inter_arriving_time_array.length];
        arriving_time_array[0] = 0;
        for (int i = 0; i < size-1 ; i++){
            long last_arrival = arriving_time_array[i] + currentTime;
            arriving_time_array[i+1] = inter_arriving_time_array[i] + last_arrival;
        }
    }


    public static void setIAT(long[] src) {
        List<Long> res = new ArrayList<>();
        for (long d : src) {
            res.add(d+0);
        }
        inter_arriving_time_array = res.stream().mapToLong(i -> i).toArray();
    }

    public static void setST(long[] src) {
        List<Long> res = new ArrayList<>();
        for (long d : src) {
            res.add(d);
        }
        service_time_array = res.stream().mapToLong(i -> i).toArray();
    }


    public static void addWaiting(long waiting){
        total_waiting_time.set(waiting + total_waiting_time.get());
    }
    public static void addConcurrentUser(){
        concurrent_users.incrementAndGet();
    }

    public static long getConcurrent_users(){
        return  concurrent_users.get();
    }
}

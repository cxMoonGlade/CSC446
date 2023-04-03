import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/* Main is the engineer and initializer for everything
 * Will need you to add some methods to work the final answer out
 * I only have the total waiting time yet
 */
public class Main {
    // util fields
    // private static final int size = 5000;
    private static int size;
    private static final float lambda_iat = 2f; // Please modify manually
    private static final float lambda_st = 3f; // Please modify manually
    public static long[] inter_arriving_time_array;
    public static long[] service_time_array;
    public static long[] arriving_time_array;
    public static BlockingQueue<Client> clients;
    public static long start_time;
    private static final AtomicLong concurrent_users = new AtomicLong(0L);


    // final calculation fields
    private static final AtomicLong total_waiting_time = new AtomicLong(0L);
    private static final AtomicLong total_service_time = new AtomicLong(0L);
    private static final AtomicLong max_workload = new AtomicLong(0L);


    public static void main(String[] args) {
        // get the size of the simulation
        // Generate a random number between 1 and 5000 for the current simulation
        size = new Random().nextInt(5000) + 1;

        // get the current time, which can be the time that everything starts
        start_time = System.currentTimeMillis();
        TimeTable tasks = new TimeTable(lambda_iat, lambda_st, size);

        setIAT(tasks.getInter_arriving_time());
        setST(tasks.getService_time_array());
        setAT(start_time);



        clients = new ArrayBlockingQueue<>(size);
        Pusher pusher = new Pusher(clients, size);
        Server server1 = new Server(clients, size);

        new Thread(pusher).start();
        new Thread(server1).start();
        System.out.println("Simulation started with " + size + " concurrent users.");
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
            res.add(d);
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


    public static void addServiceTime(long serviceTime){
        total_service_time.addAndGet(serviceTime);
    }
    public static void updateMaxWorkload(long workload) {
        long currentMax;
        do {
            currentMax = max_workload.get();
            if (workload <= currentMax) {
                break;
            }
        } while (!max_workload.compareAndSet(currentMax, workload));
    }
    // total requests processed
    public static long getConcurrent_users(){
        return concurrent_users.get();
    }
    public static long getTotal_Waiting_Time(){
        return total_waiting_time.get();
    }
    public static long getMaxWorkload() {
        return max_workload.get();
    }
    public static long getTotal_Service_Time(){
        return total_service_time.get();
    }
}

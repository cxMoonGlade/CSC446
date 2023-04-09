import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/* Main is the engineer and initializer for everything
 * Will need you to add some methods to work the final answer out
 * I only have the total waiting time yet
 */
public class Main {
    // util fields
    private static int size;
    private static final float lambda_iat = 2f; // Please modify manually
    private static final float lambda_st = 3f; // Please modify manually
    public static long[] inter_arriving_time_array;
    public static long[] service_time_array;
    public static long[] arriving_time_array;
    public static BlockingQueue<Client> clients;
    public static long start_time;
    public static boolean step1 = true;
    private static boolean step_2 = false;



    // final calculation fields
    private static final AtomicLong concurrent_users_1 = new AtomicLong(0L);
    private static final AtomicLong total_waiting_time_1 = new AtomicLong(0L);
    private static final AtomicLong total_service_time_1 = new AtomicLong(0L);
    private static final AtomicLong max_workload_1 = new AtomicLong(0L);

    private static final AtomicLong concurrent_users_2 = new AtomicLong(0L);
    private static final AtomicLong total_waiting_time_2 = new AtomicLong(0L);
    private static final AtomicLong total_service_time_2 = new AtomicLong(0L);
    private static final AtomicLong max_workload_2 = new AtomicLong(0L);


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


        while(!step_2){if (step1){
            Thread.yield();
        }else {
            clients = new ArrayBlockingQueue<>(size);
            // get the current time, which can be the time that everything starts
            System.out.println("\n\nStep-2 Starts");
            start_time = System.currentTimeMillis();
            pusher = new Pusher(clients, size);
            Server2 server2 = new Server2(clients, size);

            new Thread(pusher).start();
            new Thread(server2).start();
            System.out.println("Simulation started with " + size + " concurrent users.");
            break;
        }}
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

    public static void addWaiting_1(long waiting){
        total_waiting_time_1.set(waiting + total_waiting_time_1.get());
    }
    
    public static void addConcurrentUser_1(){
        concurrent_users_1.incrementAndGet();
    }


    public static void addServiceTime_1(long serviceTime){
        total_service_time_1.addAndGet(serviceTime);
    }
    public static void updateMaxWorkload_1(long workload) {
        long currentMax;
        do {
            currentMax = max_workload_1.get();
            if (workload <= currentMax) {
                break;
            }
        } while (!max_workload_1.compareAndSet(currentMax, workload));
    }
    // total requests processed
    public static long getConcurrent_users_1(){
        return concurrent_users_1.get();
    }
    public static long getTotal_Waiting_Time_1(){
        return total_waiting_time_1.get();
    }
    public static long getMaxWorkload_1() {
        return max_workload_1.get();
    }
    public static long getTotal_Service_Time_1(){
        return total_service_time_1.get();
    }
    public static void addWaiting_2(long waiting){
        total_waiting_time_2.set(waiting + total_waiting_time_1.get());
    }

    public static void addConcurrentUser_2(){
        concurrent_users_2.incrementAndGet();
    }


    public static void addServiceTime_2(long serviceTime){
        total_service_time_2.addAndGet(serviceTime);
    }
    public static void updateMaxWorkload_2(long workload) {
        long currentMax;
        do {
            currentMax = max_workload_2.get();
            if (workload <= currentMax) {
                break;
            }
        } while (!max_workload_2.compareAndSet(currentMax, workload));
    }
    // total requests processed
    public static long getConcurrent_users_2(){
        return concurrent_users_2.get();
    }
    public static long getTotal_Waiting_Time_2(){
        return total_waiting_time_2.get();
    }
    public static long getMaxWorkload_2() {
        return max_workload_2.get();
    }
    public static long getTotal_Service_Time_2(){
        return total_service_time_2.get();
    }
}

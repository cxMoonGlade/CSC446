import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class Main {
    private static final long size = 10;
    private static final float lambda1 = 1000f;
    private static final float lambda2 = 2000f;
    public static long[] inter_arriving_time_array;
    public static long[] service_time_array;
    public static long[] arriving_time_array;
    public static BlockingQueue<Client> clients;
    static BlockingQueue<Runnable> bq;
    private static double max_workload = 0d;
    private static AtomicLong total_waiting_time = new AtomicLong(0l);
    private static AtomicLong concurrent_users = new AtomicLong(0l);
    private static float server_load = 0f;


    public static void main(String[] args) throws Exception {
        long currentTime = System.currentTimeMillis();
        TimeTable tasks = new TimeTable(lambda1, lambda2, size);

        setIAT(currentTime, tasks.getInter_arriving_time());
        setST(currentTime, tasks.getService_time_array());
        setAT(currentTime);



        clients = new LinkedBlockingQueue<>();
        Pusher pusher = new Pusher(clients, size);
        Server server1 = new Server(clients, size);

        new Thread(pusher).start();
        new Thread(server1).start();
        System.out.println("START!");

    }




        /*
        CompletableFuture<Boolean> cf2 = CompletableFuture.supplyAsync(()->{

        });
        */


        // 4 servers


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


    public static void setIAT(long currentTime, long[] src) {
        List<Long> res = new ArrayList<>();
        for (long d : src) {
            res.add(d+0);
        }
        inter_arriving_time_array = res.stream().mapToLong(i -> i).toArray();
    }

    public static void setST(long currentTime, long[] src) {
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
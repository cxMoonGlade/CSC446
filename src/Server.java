import java.util.concurrent.*;

public class Server implements Runnable{
    private final BlockingQueue<Client> queue;
    private final int size;

    public Server(BlockingQueue<Client> q, int size){
        this.queue = q;
        this.size = size;
    }
    // hide this part for non-priority run()
    /*
    @Override
    public void run() {
        Thread t1 = new Single(queue, size);
        t1.setPriority(10);
        Thread t2 = new Single(queue, size);
        t2.setPriority(5);
        Thread t3 = new Single(queue, size);
        t3.setPriority(1);
        t1.start();
        t2.start();
        t3.start();
    }
    */

    // hide this part for priority run.
    // use 12 threads = 3(serves) * 4(threads per server) 
    //*
    @Override
    public void run(){
        System.out.println("Server is starting...");
        System.out.println("----------------------------------------");
        ThreadPoolExecutor es = new ThreadPoolExecutor(12,12, 0L,
                TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>());
        System.out.println("Thread pool is created.");
        while (Main.getConcurrent_users() < size){

            if (Main.clients.size() > 0){
                es.execute(()-> new Handler(queue, size).start());
            }else {
                Thread.yield();
            }
        }
        System.out.println("----------------------------------------");
        System.out.println("Total Server load(Total requests): " + Main.getConcurrent_users());
        System.out.println("Total_waiting_time: " + Main.getTotal_Waiting_Time());
        System.out.println("Total service time: " + Main.getTotal_Service_Time());
        System.out.println("Maximum workload: " + Main.getMaxWorkload());
        System.out.println("Average waiting time: " + (Main.getTotal_Waiting_Time() / (double) Main.getConcurrent_users()));
        System.out.println("Average service time: " + (Main.getTotal_Service_Time() / (double) Main.getConcurrent_users()));
        System.out.println("----------------------------------------");
        System.out.println("Server is shutting down...");
        es.shutdown();
        // you can add any operations in any needed case
        System.out.println("Server is closed.");
        System.out.println("End of the simulation.");
        //
        System.exit(0);
    }
     //*/
}

/* Single stands for single server.
 * creates a threadPool with fixed 4 core threads.
 */
class Single extends Thread{
    private final BlockingQueue<Client> queue;
    private final long size;
    public Single(BlockingQueue<Client> queue, long size){
        this.size = size;
        this.queue = queue;
    }

    public void run(){
        ThreadPoolExecutor es = new ThreadPoolExecutor(4,4, 0L,
                TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>());
        while (Main.getConcurrent_users() < size){
            if (Main.clients.size() > 0){
                es.execute(()-> new Handler(queue, size).start());
            }else {
                Thread.yield();
            }
        }
        es.shutdown();
        System.out.println(Main.getTotal_Waiting_Time());
    }
}

/* Handler stands for client handler
 *
 */
class Handler extends Thread{
    private final BlockingQueue<Client> queue;
    private final long size;
    public Handler(BlockingQueue<Client> queue, long size){
        this.size = size;
        this.queue = queue;
    }

    // **************************** IMPORTANT ****** PAY ATTENTION HERE ************************************
    // Daming, please modify the code to call back some value to the Main object.
    public void run(){
        try {
            Client client;
            if(queue.size() > 0){
                client = queue.poll();

                // set the field of that client.
                // You can also create a queue of served client if you need
                // otherwise, you can delete this part
                long current = System.currentTimeMillis();
                client.setWaiting_time(current - client.getArrival_time());
                client.setServiced(true);
                client.setService_start_time(current);
                client.setTime_in_system(client.getWaiting_time()+client.getService_time());

                Thread.sleep(client.getService_time());
                // System.out.println("Client Succeed ---> " + Main.getConcurrent_users()); // You can always change or delete this line

                // here is a call back method example.
                // because we only do the increment, so it does not matter obout the order, so it is thread safe.
                Main.addConcurrentUser();
                Main.addWaiting(client.getWaiting_time());
                Main.updateMaxWorkload(queue.size());
                Main.addServiceTime(client.getService_time());
            }
            else if (Main.getConcurrent_users() < size) {
                Thread.yield();
            }
        }catch (Exception e){}
    }
}

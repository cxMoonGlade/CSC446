import java.util.concurrent.*;
import java.io.*;

public class Server implements Runnable{
    private final BlockingQueue<Client> queue;
    private final int size;

    public Server(BlockingQueue<Client> q, int size){
        this.queue = q;
        this.size = size;
    }
    // hide this part for non-priority run()
    ///*
    @Override
    public void run() {
        System.out.println("Servers are starting...");
        Thread t1 = new Single(queue, size);
        t1.setPriority(10);
        Thread t2 = new Single(queue, size);
        t2.setPriority(5);
        Thread t3 = new Single(queue, size);
        t3.setPriority(1);
        t1.start();
        System.out.println("Highest Priority Server is starting...");
        t2.start();
        System.out.println("Medium Priority Server is starting...");
        t3.start();
        System.out.println("Lowest Priority Server is starting...");

    }
    //*/

    // hide this part for priority run.
    // use 12 threads = 3(serves) * 4(threads per server) 
    /*
    @Override
    public void run(){
        System.out.println("Servers are starting...");
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
        es.shutdown();
        System.out.println("----------------------------------------");
        System.out.println("Total Server load(Total requests): " + Main.getConcurrent_users());
        System.out.println("Total_waiting_time: " + Main.getTotal_Waiting_Time());
        System.out.println("Total service time: " + Main.getTotal_Service_Time());
        System.out.println("Maximum workload: " + Main.getMaxWorkload());
        System.out.println("Average waiting time: " + (Main.getTotal_Waiting_Time() / (double) Main.getConcurrent_users()));
        System.out.println("Average service time: " + (Main.getTotal_Service_Time() / (double) Main.getConcurrent_users()));
        System.out.println("----------------------------------------");
        System.out.println("Servers are shutting down...");

        // you can add any operations in any needed case
        System.out.println("Servers are closed.");
        System.out.println("End of the simulation.");
        writeToCSV(Main.getConcurrent_users(), Main.getTotal_Waiting_Time(), Main.getTotal_Service_Time(), Main.getMaxWorkload(), (Main.getTotal_Waiting_Time() / (double) Main.getConcurrent_users()), (Main.getTotal_Service_Time() / (double) Main.getConcurrent_users()));
        
        System.exit(0);
    }
<<<<<<< HEAD
    public static void writeToCSV(long concurrentUsers, long totalWaitingTime, 
        long totalServiceTime, long maxWorkload, double avgWaitingTime, double avgServiceTime) {
        String fileName = "results.csv";
        File file = new File(fileName);
        try (FileWriter fileWriter = new FileWriter(file, true);
            PrintWriter printWriter = new PrintWriter(fileWriter)) {
            // If the file is empty, write the header row
            if (file.length() == 0) {
                printWriter.println("Concurrent Users,Total Waiting Time,Total Service Time,
                    Maximum Workload,Average Waiting Time,Average Service Time");
            }
    
            // Write the data row
            printWriter.printf("%d,%d,%d,%d,%.2f,%.2f%n", concurrentUsers, totalWaitingTime, 
                totalServiceTime, maxWorkload, avgWaitingTime, avgServiceTime);
    
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
        }
    }
    
    
=======
     */
>>>>>>> 80be3f7d25962ec0fbea6417590950eb8ae2a4f1
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
        if (Thread.currentThread().getPriority() == 1){
        System.out.println("----------------------------------------");
        System.out.println("Total Server load(Total requests): " + Main.getConcurrent_users());
        System.out.println("Total_waiting_time: " + Main.getTotal_Waiting_Time());
        System.out.println("Total service time: " + Main.getTotal_Service_Time());
        System.out.println("Maximum workload: " + Main.getMaxWorkload());
        System.out.println("Average waiting time: " + (Main.getTotal_Waiting_Time() / (double) Main.getConcurrent_users()));
        System.out.println("Average service time: " + (Main.getTotal_Service_Time() / (double) Main.getConcurrent_users()));
        System.out.println("----------------------------------------");
        System.out.println("Servers are shutting down...");
        // you can add any operations in any needed case
        System.out.println("Servers are closed.");
        System.out.println("End of the simulation.");
        System.exit(0);
        //
        }


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

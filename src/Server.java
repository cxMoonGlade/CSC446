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
        Thread t3 = new Single(queue, size);
        t3.setPriority(1);
        t1.start();
        System.out.println("Highest Priority Server is starting...");
        t2.start();
        System.out.println("Medium Priority Server is starting...");
        t3.start();
        System.out.println("Lowest Priority Server is starting...");

    }
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
        while (Main.getConcurrent_users_1() < size){
            if (Main.clients.size() > 0){
                es.execute(()-> new Handler(queue, size).start());
            }else {
                Thread.yield();
            }
        }
        es.shutdown();
        if (Thread.currentThread().getPriority() == 1){
            System.out.println("----------------------------------------");
            System.out.println("Total Server load(Total requests): " + Main.getConcurrent_users_1());
            System.out.println("Total_waiting_time: " + Main.getTotal_Waiting_Time_1());
            System.out.println("Total service time: " + Main.getTotal_Service_Time_1());
            System.out.println("Maximum workload: " + Main.getMaxWorkload_1());
            System.out.println("Average waiting time: " + (Main.getTotal_Waiting_Time_1() / (double) Main.getConcurrent_users_1()));
            System.out.println("Average service time: " + (Main.getTotal_Service_Time_1() / (double) Main.getConcurrent_users_1()));
            System.out.println("----------------------------------------");
            System.out.println("Servers-1 are shutting down...");
            // you can add any operations in any needed case
            System.out.println("Servers-1 are closed.");
            System.out.println("End of the Servers-1 simulation.");

            writeToCSV(Main.getConcurrent_users_1(), Main.getTotal_Waiting_Time_1(), Main.getTotal_Service_Time_1(), Main.getMaxWorkload_1(), (Main.getTotal_Waiting_Time_1() / (double) Main.getConcurrent_users_1()), (Main.getTotal_Service_Time_1() / (double) Main.getConcurrent_users_1()));
            //System.exit(0);
            System.out.println("Step-1 finished");
            Main.step1 = false;
        }
    }
    private static void writeToCSV(long concurrentUsers, long totalWaitingTime,
                                  long totalServiceTime, long maxWorkload, double avgWaitingTime, double avgServiceTime) {
        String fileName = "results_1.csv";
        File file1 = new File(fileName);
        try (FileWriter fileWriter = new FileWriter(file1, true);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
            // If the file is empty, write the header row
            if (file1.length() == 0) {
                printWriter.println("Concurrent Users,Total Waiting Time,Total Service Time, Maximum Workload,Average Waiting Time,Average Service Time");
            }

            // Write the data row
            printWriter.printf("%d,%d,%d,%d,%.2f,%.2f%n", concurrentUsers, totalWaitingTime,
                    totalServiceTime, maxWorkload, avgWaitingTime, avgServiceTime);

        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
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
                Main.addConcurrentUser_1();
                Main.addWaiting_1(client.getWaiting_time());
                Main.updateMaxWorkload_1(queue.size());
                Main.addServiceTime_1(client.getService_time());
            }
            else if (Main.getConcurrent_users_1() < size) {
                Thread.yield();
            }
        }catch (Exception e){}
    }
}

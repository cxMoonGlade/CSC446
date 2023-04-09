import java.util.concurrent.*;
import java.io.*;

public class Server2 implements Runnable{
    private final BlockingQueue<Client> queue;
    private final int size;

    public Server2(BlockingQueue<Client> q, int size){
        this.queue = q;
        this.size = size;
    }

    @Override
    public void run(){
        System.out.println("Servers-2 are starting...");
        System.out.println("----------------------------------------");
        ThreadPoolExecutor es = new ThreadPoolExecutor(12,12, 0L,
                TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>());
        System.out.println("Thread pool for Servers-2 is created.");
        while (Main.getConcurrent_users_2() < size){
            if (Main.clients.size() > 0){
                es.execute(()-> new Handler2(queue, size).start());
            }else {
                Thread.yield();
            }
        }
        es.shutdown();
        System.out.println("----------------------------------------");
        System.out.println("Total Server load(Total requests): " + Main.getConcurrent_users_2());
        System.out.println("Total_waiting_time: " + Main.getTotal_Waiting_Time_2());
        System.out.println("Total service time: " + Main.getTotal_Service_Time_2());
        System.out.println("Maximum workload: " + Main.getMaxWorkload_2());
        System.out.println("Average waiting time: " + (Main.getTotal_Waiting_Time_2() / (double) Main.getConcurrent_users_2()));
        System.out.println("Average service time: " + (Main.getTotal_Service_Time_2() / (double) Main.getConcurrent_users_2()));
        System.out.println("----------------------------------------");
        System.out.println("Servers-2 are shutting down...");

        // you can add any operations in any needed case
        System.out.println("Servers-2 are closed.");
        System.out.println("End of the Servers-2 simulation.");
        writeToCSV(Main.getConcurrent_users_2(), Main.getTotal_Waiting_Time_2(), Main.getTotal_Service_Time_2(), Main.getMaxWorkload_2(), (Main.getTotal_Waiting_Time_2() / (double) Main.getConcurrent_users_2()), (Main.getTotal_Service_Time_2() / (double) Main.getConcurrent_users_2()));

        System.exit(0);
    }
    public static void writeToCSV(long concurrentUsers, long totalWaitingTime,
        long totalServiceTime, long maxWorkload, double avgWaitingTime, double avgServiceTime) {
        String fileName = "results_2.csv";
        File file2 = new File(fileName);
        try (FileWriter fileWriter = new FileWriter(file2, true);
            PrintWriter printWriter = new PrintWriter(fileWriter)) {
            // If the file is empty, write the header row
            if (file2.length() == 0) {
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
class Handler2 extends Thread{
    private final BlockingQueue<Client> queue;
    private final long size;
    public Handler2(BlockingQueue<Client> queue, long size){
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
                Main.addConcurrentUser_2();
                Main.addWaiting_2(client.getWaiting_time());
                Main.updateMaxWorkload_2(queue.size());
                Main.addServiceTime_2(client.getService_time());
            }
            else if (Main.getConcurrent_users_2() < size) {
                Thread.yield();
            }
        }catch (Exception e){}
    }
}

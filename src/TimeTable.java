import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/* To create a timetable to mark the clients
 * Generate the inter-arriving time and service time
 */
public class TimeTable {
    private long[] inter_arriving_time_array;
    private long[] service_time_array;
    private long seed = 10;

    public TimeTable(float lambda_iat, float lambda_st, long size){
        setInterArrivingTime(lambda_iat, size);
        setService_time_array(lambda_st, size);
    }


    public long[] getInter_arriving_time(){
        return  this.inter_arriving_time_array;
    }

    public long[] getService_time_array(){
        return  this.service_time_array;
    }
    private void setInterArrivingTime(float lambda, long size){
        List<Long> pre_IAT = new ArrayList<>();
        Random random = new Random(seed);
        for(int i = 0; i < size; i++){
            double y = random.nextDouble();
            pre_IAT.add(possionNumberGen(lambda/10, y)*10L);
        }
        this.inter_arriving_time_array =  pre_IAT.stream().mapToLong(i->i).toArray();
    }

    private void setService_time_array(float lambda, long size){
        List<Long> pre_ST = new ArrayList<>();
        Random random = new Random(seed);
        for (int i = 0; i < size; i++){
            double y = random.nextDouble();
            pre_ST.add(possionNumberGen(lambda/10, y)* 10L);
        }
        this.service_time_array = pre_ST.stream().mapToLong(i->i).toArray();
    }


    private static int possionNumberGen(double lambda, double y){
        int x = 0;
        double cdf = PDF(x, lambda);
        while (cdf < y){
            x++;
            cdf += PDF(x, lambda);
        }
        return x;
    }
    private  static double PDF(double k, double lambda){
        double c = Math.exp(-lambda), sum = 1;
        for(int i = 1; i <= k; i++){
            sum *= lambda/i;
        }
        return sum * c;
    }

}

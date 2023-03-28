import java.util.ArrayList;
import java.util.List;

public class TimeTable {
    private long[] inter_arriving_time_array;
    private long[] service_time_array;

    public TimeTable(float lambda1, float lambda2, long size){
        setInterArrivingTime(lambda1, size);
        setService_time_array(lambda2, size);
    }


    public long[] getInter_arriving_time(){
        return  this.inter_arriving_time_array;
    }

    public long[] getService_time_array(){
        return  this.service_time_array;
    }
    private void setInterArrivingTime(float lambda, long size){
        List<Long> pre_IAT = new ArrayList<>();
        for(int i = 0; i < size; i++){
            pre_IAT.add(possionNumberGen(lambda/10)*10L);
        }
        this.inter_arriving_time_array =  pre_IAT.stream().mapToLong(i->i).toArray();
    }

    private void setService_time_array(float lambda, long size){
        List<Long> pre_ST = new ArrayList<>();
        for (int i = 0; i < size; i++){
            pre_ST.add(possionNumberGen(lambda/10)* 10L);
        }
        this.service_time_array = pre_ST.stream().mapToLong(i->i).toArray();
    }


    private static int possionNumberGen(double lambda){
        int x = 0;
        double y = Math.random(), cdf = PDF(x, lambda);
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

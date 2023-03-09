import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static java.lang.Float.*;

class AtomicDouble extends Number {

    private AtomicLong bits;

    public AtomicDouble() {
        this(0L);
    }

    public AtomicDouble(double initialValue) {
        bits = new AtomicLong(Double.doubleToRawLongBits(initialValue));
    }

    public final boolean compareAndSet(double expect, double update) {
        return bits.compareAndSet(Double.doubleToRawLongBits(expect),
                Double.doubleToRawLongBits(update));
    }

    public final void set(double newValue) {
        bits.set(Double.doubleToRawLongBits(newValue));
    }

    public double addAndGet(double adder){
        double d = get()+adder;
        set(d);
        return d;
    }

    public final double get() {
        return (double)bits.get();
    }


    public final double getAndSet(double newValue) {
        return (bits.getAndSet((long)newValue));
    }

    public final boolean weakCompareAndSet(float expect, float update) {
        return bits.weakCompareAndSet(floatToIntBits(expect),
                floatToIntBits(update));
    }

    public double doubleValue() { return (double) floatValue(); }
    public int intValue()       { return (int) get();           }
    public long longValue()     { return (long) get();          }

    @Override
    public float floatValue() {
        return intBitsToFloat((int)bits.get());
    }
}

package primes.concurrency;

import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    static ConcurrentSkipListSet<Integer> primes = new ConcurrentSkipListSet<>();

    public static void main(String args[]) {
        int minPrime = 1, maxPrime = 1234, threadCount = 4;
        try {
            minPrime = Integer.parseInt(args[0]);
            maxPrime = Integer.parseInt(args[1]);
            threadCount = Integer.parseInt(args[2]);
        } catch (Exception e) {
            System.out.println("Usage: primeSearcher minPrime maxPrime threadCount");
            System.out.println("\nDue to an Error we are using default values `primesearcher "+minPrime+" "+maxPrime+" "+threadCount+"`\n");
        }
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        for (int i = 0, min=minPrime, max=maxPrime/threadCount; i < threadCount; i++) {
            executorService.submit(new Primesearcher(min, max));
            min = max - 1;
            max += maxPrime / threadCount;
            max = max + threadCount >= maxPrime ? maxPrime : max;
        }
        try {
            executorService.shutdown();
            executorService.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.err.println("Due to time restrictions, programm was terminated! Please consider to use more threads or another range of primes!");
        } finally {
            System.out.println(primes.toString());
        }
    }

    public static boolean isPrime(int n) {
        if (primes.contains(n)) return true;
        if ((n % 2 == 0 && n > 2) || (n < 2)) return false;
        for (int i = 3; i <= Math.sqrt(n) + 1; i += 2)
            if (n % i == 0) return false;
        return true;
    }
}

class Primesearcher extends Thread {
    int start = 0;
    int end = 0;

    public Primesearcher(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public void run() {
        if (start == 0 || end == 0) return;
        for (; start <= end; start++) {
            if (Main.isPrime(start)) Main.primes.add(start);
        }
    }
}

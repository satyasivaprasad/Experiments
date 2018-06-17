package com.newgame.network;

import java.util.Comparator;
import java.util.concurrent.*;
/**
 * Class for allocating priorities for different runnables
 * @author jonnalagadda.siva
 *
 */
public class PriorityExecutor extends ThreadPoolExecutor {
    public PriorityExecutor(final int corePoolSize, final int maximumPoolSize, final long keepAliveTime, final TimeUnit unit) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, new PriorityBlockingQueue<Runnable>(11, new PriorityTaskComparator<Important>()));
    }

    protected <T> RunnableFuture<T> newTaskFor(final Runnable runnable, final T value) {
        if (runnable instanceof Important) return new PriorityTask<T>(((Important) runnable).getPriority(), runnable, value);
        else return new PriorityTask<T>(0, runnable, value);
    }

    public static abstract class Important implements Runnable {
        int getPriority() { return 0; }
        public abstract void run();
    }

    private static final class PriorityTask<T> extends FutureTask<T> implements Comparable<PriorityTask<T>> {
        private final int priority;

        public PriorityTask(final int priority, final Callable<T> tCallable) {
            super(tCallable);
            this.priority = priority;
        }

        public PriorityTask(final int priority, final Runnable runnable, final T result) {
            super(runnable, result);
            this.priority = priority;
        }

        public int compareTo(final PriorityTask<T> o) {
            final long diff = o.priority - priority;
            return 0 == diff ? 0 : 0 > diff ? -1 : 1;
        }
    }

    private static class PriorityTaskComparator<T> implements Comparator<Runnable> {
        @SuppressWarnings("unchecked")
        public int compare(final Runnable left, final Runnable right) {
            return ((PriorityTask<T>) left).compareTo((PriorityTask<T>) right);
        }
    }
}

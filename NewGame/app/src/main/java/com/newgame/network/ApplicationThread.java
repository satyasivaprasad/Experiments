package com.newgame.network;

import java.util.concurrent.Future;

import android.os.Looper;


/**
 * Class for performing works in the background
 * 
 */
public final class ApplicationThread {
	
    private final static int THREAD_DELAY   = 0;

    // values from AsyncTask.java
    private final static int BGND_POOL_SIZE = 5;
    private final static int BGND_POOL_MAX = 128;

    private static PriorityExecutor bgnd;
    private static android.os.Handler nui;
    private static android.os.Handler ui;
    private static android.os.Handler db;

    private static boolean started = false;

    /**
     * Checking all the Handlers are initialized or not if not initialized then initialize all Handlers. 
     */
    public static void start() {
        if (started) return;
        started = true;

        final android.os.HandlerThread nuiThread = new android.os.HandlerThread("non-ui");
        nuiThread.start();

        final android.os.HandlerThread dbThread = new android.os.HandlerThread("db", android.os.Process.THREAD_PRIORITY_BACKGROUND);
        dbThread.start();

        bgnd = new PriorityExecutor(BGND_POOL_SIZE, BGND_POOL_MAX, 1, java.util.concurrent.TimeUnit.MILLISECONDS);
        nui = new android.os.Handler(nuiThread.getLooper());
        ui  = new android.os.Handler(Looper.getMainLooper());
        db  = new android.os.Handler(dbThread.getLooper());
    }

    /**
     * Method for quitting all the handlers
     */
    public static void stop() {
        if (null != nui) nui.getLooper().quit();
        if (null != db) db.getLooper().quit();
        if (null != bgnd) bgnd.shutdown();
//        if (null != ir) ir.getLooper().quit();

        bgnd = null;
        nui = null;
        ui  = null;
        db  = null;
//        ir  = null;
        started = false;
    }

    public static boolean uiThreadCheck() { return Looper.myLooper().equals(ui.getLooper()); }
    public static boolean nuiThreadCheck() { return Looper.myLooper().equals(nui.getLooper()); }

    public final static void uiRemove(final Runnable runner) { ui.removeCallbacks(runner); }
    public final static void nuiRemove(final Runnable runner) { nui.removeCallbacks(runner); }
    public final static void bgndRemove(final Future<?> runner, final boolean force) { if (null != runner) runner.cancel(force); }

    /** Note: do not use bgndPost for thread-unsafe operations */
    public final static Future<?> bgndPost(final String clazz, final String msg, final int priority, final Runnable run) {
        return bgnd.submit(new PriorityExecutor.Important() {
            public int getPriority() { return priority; }
            public void run() {
                final long startTime = System.nanoTime();
                try { run.run(); }
                catch (Throwable ex) { com.newgame.network.Log.e(clazz, ex); }
                finally { com.newgame.network.Log.d(clazz, "BGND THREAD "+msg+" ("+(System.nanoTime()-startTime)/1000000+"ms)"); }
            }
        });
    }
    public final static Future<?> bgndPost(final String clazz, final String msg, final Runnable run) { return bgndPost(clazz, msg, 10, run); }

    public final static Runnable nuiPost(final String clazz, final String msg, final Runnable run, final long delay) {
        final Runnable runner = new Runnable() {
            public void run() {
                final long startTime = System.nanoTime();
                try { run.run(); }
                catch (Throwable ex) { com.newgame.network.Log.e(clazz, ex); }
                finally { com.newgame.network.Log.d(clazz, "NON-UI THREAD "+(null==msg?"":msg)+" ("+(System.nanoTime()-startTime)/1000000+"ms)"); }
            }
        };
        nui.postDelayed(runner ,delay);
        return runner;
    }

    public final static Runnable dbPost(final String clazz, final String msg, final Runnable run, final long delay) {
        final Runnable runner = new Runnable() {
            public void run() {
                final long startTime = System.nanoTime();
                try { run.run(); }
                catch (Throwable ex) { com.newgame.network.Log.e(clazz, ex); }
                finally { com.newgame.network.Log.d(clazz, "DB THREAD "+(null==msg?"":msg)+" ("+(System.nanoTime()-startTime)/1000000+"ms)"); }
            }
        };
        db.postDelayed(runner ,delay);
        return runner;
    }

    public final static Runnable uiPost(final String clazz, final String msg, final Runnable run, final long delay) {
        final Runnable runner = new Runnable() {
            public void run() {
                final long startTime = System.nanoTime();
                try { run.run(); }
                catch (Throwable ex) { com.newgame.network.Log.e(clazz, ex); }
                finally { com.newgame.network.Log.d(clazz, "UI THREAD "+(null==msg?"":msg)+" ("+(System.nanoTime()-startTime)/1000000+"ms)"); }
            }
        };
        ui.postDelayed(runner, delay);
        return runner;
    }

    public final static Runnable nuiPost(final String clazz, final String msg, final Runnable run) { return nuiPost(clazz, msg, run, THREAD_DELAY); }
    public final static Runnable dbPost(final String clazz, final String msg, final Runnable run)  { return dbPost(clazz, msg, run, THREAD_DELAY); }
    public final static Runnable uiPost(final String clazz, final String msg, final Runnable run)  { return uiPost(clazz, msg, run, THREAD_DELAY); }

    /**
     * Class for executing the different types of background and UI tasks and posting to proper handlers
     *
     */
    public static abstract class OnComplete<T> implements Runnable {
        public static final int NUI     = 0;
        public static final int UI      = 1;
        public static final int BGND    = 2;
        public int mode;

        public boolean success;
        public Object result;
        public String msg;

        public OnComplete() { this.mode = OnComplete.NUI; }
        public OnComplete(final int mode) { this.mode = mode; }

        public void run() { com.newgame.network.Log.e(OnComplete.class.getName(), "run not implemented!"); }

        private void execute() {
            final OnComplete thread = this;
            final Runnable onComplete = new Runnable() {
                public void run() {
                    try { thread.run(); }
                    catch (Throwable ex) { com.newgame.network.Log.e("OnComplete", ex); }
                }
            };

            switch(mode) {
                default:
                    com.newgame.network.Log.e(ApplicationThread.class.getName(), new RuntimeException("UNRECOGNIZED OnComplete mode "+mode+" posting to NUI"));

                case OnComplete.NUI:
                	 com.newgame.network.Log.e("OnComplete NUI","NUI");
                    ApplicationThread.nuiPost(OnComplete.class.getName(), "OnComplete", onComplete);
                    break;

                case OnComplete.UI:
               	 com.newgame.network.Log.e("OnComplete UI","UI");

                    ApplicationThread.uiPost(OnComplete.class.getName(), "OnComplete", onComplete);
                    break;

                case OnComplete.BGND:
               	 com.newgame.network.Log.e("OnComplete BG","BG");

                    ApplicationThread.bgndPost(OnComplete.class.getName(), "OnComplete", onComplete);
                    break;
            }
        }

        /**
         * Once the task is finished this method will post result and status to the handler
         * @param success
         * @param result
         * @param msg
         */
        public void execute(final boolean success, final Object result, final String msg) {
            this.success = success;
            this.result = result;
            this.msg = msg;
            execute();
        }
        
        public void executeJson(final boolean success, final T result, final String msg) {
            this.success = success;
            this.result = result;
            this.msg = msg;
            execute();
        }
    }

    public static abstract class Observable {
        private final java.util.List<Object> observers = new java.util.ArrayList<Object>();

        public final void clear() { synchronized(observers) { observers.clear(); } }
        public final void add(final Object observer) {
            synchronized(observers) {
                if (null==observer) throw new IllegalArgumentException("observer is null");
                if (observers.contains(observer)) { com.newgame.network.Log.e(this.getClass().getName(),"ERROR already contains class:"+observer.getClass().getName()+android.util.Log.getStackTraceString(new Throwable())); return; }
                observers.add(0, observer);
            }
        }

        public final void remove(final Object observer) {
            synchronized(observers) {
                if (!observers.contains(observer)) { com.newgame.network.Log.e(this.getClass().getName(),"ERROR does not contain class:"+observer.getClass().getName()+android.util.Log.getStackTraceString(new Throwable())); return; }
                observers.remove(observer);
            }
        }

        public final Object[] get() {
            Object[] observerArray = null;
            synchronized(observers) { observerArray = observers.toArray(new Object[observers.size()]); }
            return observerArray;
        }


        public static abstract class Message {
            public void event(final int id, final Object obj, final Object... param) { com.newgame.network.Log.v(getClass().getName(),"event(int,object,object) not implemented"); }
        }

        public final void notify(final int id,final Object obj,final Object... param) {
            com.newgame.network.ApplicationThread.nuiPost(getClass().getName(), "notify observers", new Runnable() {
                public void run() {
                    final Object[] observers = get();
                    for (int i=observers.length-1; i>=0; i--) {
                        ((Message)observers[i]).event(id,obj,param);
                    }
                }
            });
        }
    }
}

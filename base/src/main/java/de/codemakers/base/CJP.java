/*
 *    Copyright 2018 Paul Hagedorn (Panzer1119)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package de.codemakers.base;

import de.codemakers.base.action.Action;
import de.codemakers.base.action.RunningAction;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CJP {

    private static final CJP CJP = new CJP(Runtime.getRuntime().availableProcessors());

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> shutdownInstance()));
    }

    private ExecutorService executorService;
    private ScheduledExecutorService scheduledExecutorService;

    public CJP(ExecutorService executorService, ScheduledExecutorService scheduledExecutorService) {
        this.executorService = executorService;
        this.scheduledExecutorService = scheduledExecutorService;
    }

    public CJP(int fixedThreadPoolSize) {
        this(Executors.newScheduledThreadPool(fixedThreadPoolSize), Executors.newSingleThreadScheduledExecutor());
    }

    public static final void shutdownInstance() {
        CJP.stopExecutorServiceNow();
    }

    public static final RunningAction shutdown() {
        return shutdown(0);
    }

    public static final RunningAction shutdown(int status) {
        return Action.ofToughRunnable(() -> System.exit(status));
    }

    public static final CJP getInstance() {
        return CJP;
    }

    public final ExecutorService getExecutorService() {
        return executorService;
    }

    public final ScheduledExecutorService getScheduledExecutorService() {
        return scheduledExecutorService;
    }

    public final CJP stopExecutorServiceNow() {
        if (executorService != null) {
            executorService.shutdownNow();
            executorService = null;
        }
        return this;
    }

    public final CJP stopExecutorService(long timeout, TimeUnit unit) {
        if (executorService == null) {
            return this;
        }
        try {
            executorService.shutdown();
            executorService.awaitTermination(timeout, unit);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return stopExecutorServiceNow();
    }

}

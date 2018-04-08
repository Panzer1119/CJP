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

package de.codemakers.base.rest;

import de.codemakers.base.CJP;
import de.codemakers.base.util.ToughSupplier;

import java.util.Objects;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class RestAction<T> {

    private final CJP cjp;
    private final ToughSupplier<T> supplier;

    public RestAction(ToughSupplier<T> supplier) {
        this(CJP.getInstance(), supplier);
    }

    public RestAction(CJP cjp, ToughSupplier<T> supplier) {
        Objects.requireNonNull(cjp, "CJP may not be null!");
        this.cjp = cjp;
        this.supplier = supplier;
    }


    public static final <T> RestAction<T> ofSupplier(Supplier<T> supplier) {
        return ofToughSupplier(() -> supplier.get());
    }

    public static final <T> RestAction<T> ofToughSupplier(ToughSupplier<T> supplier) {
        return new RestAction<>(supplier);
    }

    public final CJP getCJP() {
        return cjp;
    }

    public final T get() {
        return supplier.getWithoutException();
    }

    public final void queue() {
        queue(null, null);
    }

    public final void queue(Consumer<T> success) {
        queue(success, null);
    }

    public final void queue(Consumer<T> success, Consumer<Throwable> failure) {
        cjp.getExecutorService().submit(() -> {
            try {
                final T t = supplier.get();
                if (success != null) {
                    success.accept(t);
                }
            } catch (Exception ex) {
                if (failure != null) {
                    failure.accept(ex);
                } else {
                    ex.printStackTrace();
                }
            }
        });
    }

    public final void queueAfter(long delay, TimeUnit unit) {
        queueAfter(delay, unit, null, null);
    }

    public final void queueAfter(long delay, TimeUnit unit, Consumer<T> success) {
        queueAfter(delay, unit, success, null);
    }

    public final void queueAfter(long delay, TimeUnit unit, Consumer<T> success, Consumer<Throwable> failure) {
        cjp.getScheduledExecutorService().schedule(() -> queue(success, failure), delay, unit);
    }

    public final Future<T> submit() {
        return cjp.getScheduledExecutorService().submit(() -> {
            try {
                return supplier.get();
            } catch (Exception ex) {
                return null;
            }
        });
    }

    public final T complete() {
        try {
            return submit().get();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}

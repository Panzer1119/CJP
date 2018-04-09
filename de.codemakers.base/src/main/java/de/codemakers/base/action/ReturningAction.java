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

package de.codemakers.base.action;

import de.codemakers.base.CJP;
import de.codemakers.base.util.tough.ToughConsumer;
import de.codemakers.base.util.tough.ToughSupplier;

import java.util.concurrent.Future;

/**
 * Inspired by the RestAction from Austin Keener & Michael Ritter & Florian Spieß
 *
 * @param <T> Type input
 */
public class ReturningAction<T> extends Action<ToughConsumer<T>, T> {

    private final ToughSupplier<T> supplier;

    public ReturningAction(ToughSupplier<T> supplier) {
        super();
        this.supplier = supplier;
    }

    public ReturningAction(CJP cjp, ToughSupplier<T> supplier) {
        super(cjp);
        this.supplier = supplier;
    }

    public final ToughSupplier<T> getSupplier() {
        return supplier;
    }

    @Override
    public final void queue(ToughConsumer<T> success, ToughConsumer<Throwable> failure) {
        cjp.getExecutorService().submit(() -> {
            try {
                final T t = supplier.get();
                if (success != null) {
                    success.acceptWithoutException(t);
                }
            } catch (Exception ex) {
                if (failure != null) {
                    failure.acceptWithoutException(ex);
                } else {
                    ex.printStackTrace();
                }
            }
        });
    }

    @Override
    public final Future<T> submit() {
        return cjp.getExecutorService().submit(() -> {
            try {
                return supplier.get();
            } catch (Exception ex) {
                return null;
            }
        });
    }

}

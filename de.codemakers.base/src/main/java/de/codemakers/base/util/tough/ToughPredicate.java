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

package de.codemakers.base.util.tough;

public interface ToughPredicate<T> extends Tough<T, Boolean> {

    Boolean test(T t) throws Exception;

    default ToughPredicate<T> negate() {
        return (t) -> !test(t);
    }

    default Boolean testWithoutException(T t) {
        return testWithoutException(t, false);
    }

    default Boolean testWithoutException(T t, boolean onError) {
        try {
            return test(t);
        } catch (Exception ex) {
            ex.printStackTrace();
            return onError;
        }
    }

    @Override
    default Boolean action(T t) throws Exception {
        return test(t);
    }

    @Override
    default boolean canConsume() {
        return true;
    }

    @Override
    default boolean canSupply() {
        return true;
    }

}

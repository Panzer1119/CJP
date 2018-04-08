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

import de.codemakers.base.CJP;
import de.codemakers.base.rest.RestAction;

import java.util.concurrent.TimeUnit;

public class RestActionTest {

    public static final void main(String[] args) {
        final RestAction<String> test = RestAction.ofToughSupplier(() -> {
            Thread.sleep(1000);
            return "Test 2";
        });
        //System.out.println(test.complete());
        test.queue(System.out::println);
        for (int i = 0; i < 10000; i++) {
            System.out.println(i);
        }
        CJP.shutdown().queueAfter(5, TimeUnit.SECONDS);
    }
}

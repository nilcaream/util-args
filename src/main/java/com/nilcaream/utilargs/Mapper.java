/*
 * Copyright 2020 Krzysztof Smigielski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nilcaream.utilargs;

/**
 * String value to specific type mapper.
 */
@FunctionalInterface
public interface Mapper {

    /**
     * Maps provided value to a specific class instance.
     *
     * @param value String value.
     * @param cls   Target class.
     * @param <T>   Target type.
     * @return Target class instance or null.
     */
    <T> T map(String value, Class<T> cls);
}

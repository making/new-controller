/*
 * Copyright (C) 2015 Toshiaki Maki <makingx@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package newcontroller.handler;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface Request {
    Optional<String> param(String name);

    List<String> params(String name);

    Map<String, List<String>> params();

    <T> T params(Class<T> clazz);

    <T> T body(Class<T> clazz);

    Request put(String key, Object value);

    <T> T get(String key, Class<T> clazz);

    Map<String, ?> model();

    <T> T unwrap(Class<T> clazz);
}

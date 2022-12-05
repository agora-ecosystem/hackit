/*
 * Copyright 2022 DIMA/TU-Berlin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.tuberlin.dima.hackit.util;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * HackitUtil for the {@Link Stream}'s
 */
public class HackitStreamUtil {

  /**
   * Create a {@link Stream} from an {@link Iterator}
   *
   * @param iterator that will be wrapped to be {@link Stream}
   * @return instance of {@link Stream}
   * @param <T> type of the data
   */
  public static <T> Stream<T> fromIterator(Iterator<T> iterator){
    return StreamSupport.stream(
        Spliterators.spliteratorUnknownSize(
            iterator,
            Spliterator.ORDERED
        ),
        false
    );
  }

  /**
   * Convert the {@link Stream} into a {@link Set}
   *
   * @param stream that will be converted to {@link Set}
   * @return instance of a {@link Set} that contain the data of the {@link Stream}
   * @param <T> type of the data
   */
  public static <T> Set<T> toSet(Stream<T> stream){
    return stream.collect(Collectors.toSet());
  }

  /**
   * Convert the {@link Stream} into a {@link List}
   *
   * @param stream that will be converted to {@link List}
   * @return instance of a {@link List} that contain the data of the {@link Stream}
   * @param <T> type of the data
   */
  public static <T> List<T> toList(Stream<T> stream){
    return stream.collect(Collectors.toList());
  }
}

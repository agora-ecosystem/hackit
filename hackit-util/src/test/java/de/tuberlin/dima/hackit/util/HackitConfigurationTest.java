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

import static org.junit.jupiter.api.Assertions.*;

import de.tuberlin.dima.hackit.util.conf.UtilTestConfiguration;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HackitConfigurationTest {

  HackitConfiguration configuration;
  HackitConfiguration empty_configuration;
  @BeforeEach
  void setUp() {
    this.configuration = new UtilTestConfiguration();

    this.empty_configuration = new HackitConfiguration() {
      @Override
      public String name() {
        return "empty-test";
      }
    };
  }

  @Test
  void name() {
    assertEquals(
        this.configuration.name(),
        "hackit-util-test"
    );
  }

  @Test
  void extension() {
    assertEquals(
        this.configuration.extension(),
        "properties"
    );
  }

  @Test
  void containsKey() {

    Set<String> keys = HackitStreamUtil.toSet(
        HackitStreamUtil.fromIterator(this.configuration.getKeys())
    );

    assertTrue( keys.contains("var") );
    assertTrue( keys.contains("var.test") );

  }

  @Test
  void isEmpty() {
    assertFalse( this.configuration.isEmpty() );

    assertTrue( this.empty_configuration.isEmpty());

  }

  @Test
  void size() {
    //greater or equal than
    assertTrue(this.configuration.size() >= 2 );

    assertEquals(this.empty_configuration.size(), 0);
  }
}
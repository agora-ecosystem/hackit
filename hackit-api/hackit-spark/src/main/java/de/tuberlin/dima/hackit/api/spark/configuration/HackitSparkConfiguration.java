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

package de.tuberlin.dima.hackit.api.spark.configuration;

import de.tuberlin.dima.hackit.util.HackitConfiguration;

/**
 * HackitSparkConfiguration is an instance of {@link HackitConfiguration}, this
 * configuration is static
 */
public class HackitSparkConfiguration extends HackitConfiguration {

  /**
   * Instance of HackitSparkConfiguration
   */
  static private HackitSparkConfiguration CONF;

  /**
   * Default Construct is private to be able of getting an instance is required
   * to use the {@link HackitSparkConfiguration#getInstance()}
   */
  private HackitSparkConfiguration() {
    super();
  }

  @Override
  public String name() {
    return "hackit-api-spark";
  }

  /**
   * Get the instance of the HackitSparkConfiguration
   *
   * @return
   */
  public static HackitSparkConfiguration getInstance() {
    if (CONF == null) {
      CONF = new HackitSparkConfiguration();
    }
    return CONF;
  }
}
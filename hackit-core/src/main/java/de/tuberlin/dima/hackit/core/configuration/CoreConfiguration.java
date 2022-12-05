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

package de.tuberlin.dima.hackit.core.configuration;

import de.tuberlin.dima.hackit.util.HackitConfiguration;

/**
 * CoreConfiguration is an instance of {@link HackitConfiguration}, this
 * configuration is static
 */
public class CoreConfiguration extends HackitConfiguration {

  /**
   * Instance of CoreConfiguration
   */
  static private CoreConfiguration CONF;

  /**
   * Default Construct is private to be able of getting an instance is
   * required to use the {@link CoreConfiguration#getInstance()}
   */
  private CoreConfiguration() {
    super();
  }
  @Override
  public String name() {
    return "hackit-core";
  }

  /**
   * Get the instance of the CoreConfiguration
   * @return
   */
  public static CoreConfiguration getInstance(){
    if(CONF == null){
      CONF = new CoreConfiguration();
    }
    return CONF;
  }

}

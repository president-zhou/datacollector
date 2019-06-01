/*
 * Copyright 2019 StreamSets Inc.
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
package com.streamsets.pipeline.lib.remote;

import com.streamsets.pipeline.api.GenerateResourceBundle;
import com.streamsets.pipeline.api.Label;
import org.apache.commons.vfs2.provider.ftps.FtpsMode;

@GenerateResourceBundle
public enum FTPSMode implements Label {
  EXPLICIT("Explicit", FtpsMode.EXPLICIT),
  IMPLICIT("Implicit", FtpsMode.IMPLICIT),
  ;

  private final String label;
  private final FtpsMode mode;

  FTPSMode(String label, FtpsMode mode) {
    this.label = label;
    this.mode = mode;
  }

  @Override
  public String getLabel() {
    return label;
  }

  public FtpsMode getMode() {
    return mode;
  }
}

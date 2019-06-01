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
package com.streamsets.pipeline.stage.metadata.gen2;

import com.streamsets.pipeline.stage.destination.hdfs.metadataexecutor.HdfsActionsConfig;
import com.streamsets.pipeline.stage.destination.hdfs.metadataexecutor.HdfsMetadataExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataLakeGen2MetadataExecutor extends HdfsMetadataExecutor {

  private static final Logger LOG = LoggerFactory.getLogger(DataLakeGen2MetadataExecutor.class);

  public DataLakeGen2ConnectionConfig dataLakeConnectionConfig;

  public DataLakeGen2MetadataExecutor(DataLakeGen2ConnectionConfig dataLakeConnectionConfig, HdfsActionsConfig actions) {
    super(dataLakeConnectionConfig, actions);
    this.dataLakeConnectionConfig = dataLakeConnectionConfig;
  }
}

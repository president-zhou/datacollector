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

import com.google.common.base.Joiner;
import com.streamsets.datacollector.security.HadoopSecurityUtil;
import com.streamsets.pipeline.api.ConfigDefBean;
import com.streamsets.pipeline.api.Stage;
import com.streamsets.pipeline.stage.destination.hdfs.metadataexecutor.Groups;
import com.streamsets.pipeline.stage.destination.hdfs.metadataexecutor.HdfsConnectionConfig;
import com.streamsets.pipeline.stage.destination.hdfs.metadataexecutor.HdfsMetadataErrors;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.RawLocalFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.security.PrivilegedExceptionAction;
import java.util.List;
import java.util.Map;

public class DataLakeGen2ConnectionConfig extends HdfsConnectionConfig {

  private static final Logger LOG = LoggerFactory.getLogger(DataLakeGen2ConnectionConfig.class);
  private static Joiner JOIN = Joiner.on(".");

  private static final String ADLS_CONFIG_FS_IMPL = "fs.file.impl";

  @ConfigDefBean
  public DataLakeGen2MetadataConfig dataLakeConfig;

  @Override
  public void init(Stage.Context context, String prefix, List<Stage.ConfigIssue> issues) {
    conf = new Configuration();
    conf.setClass(ADLS_CONFIG_FS_IMPL, RawLocalFileSystem.class, FileSystem.class);

    hdfsUri = dataLakeConfig.getAbfsUri(context, issues);
    Map<String, String> hadoopConfigs = dataLakeConfig.getHdfsConfigBeans(context, issues);
    for (String hadoopConfig : hadoopConfigs.keySet()) {
      conf.set(hadoopConfig, hadoopConfigs.get(hadoopConfig));
    }

    try {
      loginUgi = HadoopSecurityUtil.getLoginUser(conf);
      userUgi = HadoopSecurityUtil.getProxyUser(
          hdfsUser,
          context,
          loginUgi,
          issues,
          Groups.HDFS.name(),
          JOIN.join(prefix, "hdfsUser")
      );
    } catch (IOException e) {
      LOG.error("Can't create UGI", e);
      issues.add(context.createConfigIssue(Groups.HDFS.name(), null, HdfsMetadataErrors.HDFS_METADATA_005, e.getMessage(), e));
    }

    if(!issues.isEmpty()) {
      return;
    }

    try {
      fs = getUGI().doAs((PrivilegedExceptionAction<FileSystem>) () -> FileSystem.newInstance(new URI(hdfsUri), conf));
    } catch (Exception ex) {
      LOG.error("Can't retrieve FileSystem instance", ex);
      issues.add(context.createConfigIssue(Groups.HDFS.name(), null, HdfsMetadataErrors.HDFS_METADATA_005, ex.getMessage(), ex));
    }
  }
}

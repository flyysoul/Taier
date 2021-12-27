/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dtstack.batch.service.task.impl;

import com.dtstack.engine.common.exception.RdosDefineException;
import com.dtstack.batch.dao.BatchTaskResourceShadeDao;
import com.dtstack.batch.domain.BatchResource;
import com.dtstack.batch.domain.BatchTaskResource;
import com.dtstack.batch.domain.BatchTaskResourceShade;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Reason:
 * Date: 2017/8/23
 * Company: www.dtstack.com
 * @author xuchao
 */

@Service
public class BatchTaskResourceShadeService {

    private static Logger logger = LoggerFactory.getLogger(BatchTaskResourceShadeService.class);

    @Autowired
    private BatchTaskResourceShadeDao batchTaskResourceShadeDao;

    public void clearDataByTaskId(Long taskId) {
        batchTaskResourceShadeDao.deleteByTaskId(taskId);
        logger.info(String.format("clear taskResource success  taskId = %s",taskId));

    }

    public void saveTaskResource(List<BatchTaskResource> taskResourceList) {
        for (BatchTaskResource resource : taskResourceList) {
            BatchTaskResourceShade shade = new BatchTaskResourceShade();
            BeanUtils.copyProperties(resource, shade);
            //把taskResourceShade的id置为null 防止误更新
            shade.setId(null);
            addOrUpdate(shade);
        }
    }

    public void addOrUpdate(BatchTaskResourceShade batchTaskResourceShade) {
        if (batchTaskResourceShade.getId()!= null && batchTaskResourceShade.getId()>0) {
            //查询是否传入参数有问题
            BatchTaskResourceShade one = batchTaskResourceShadeDao.getOne(batchTaskResourceShade.getId());
            if (one == null){
                throw new RdosDefineException(String.format("未查询到id = %s对应的记录", batchTaskResourceShade.getId()));
            }
            batchTaskResourceShadeDao.update(batchTaskResourceShade);
        } else {
            batchTaskResourceShadeDao.insert(batchTaskResourceShade);
        }
    }

    public List<Long> getResourceIdList(long taskId, long projectId, int type) {
        List<BatchTaskResourceShade> resourceList = batchTaskResourceShadeDao.listByTaskId(taskId, type, projectId);
        List<Long> resultIdList = Lists.newArrayList();
        if (resourceList == null) {
            return resultIdList;
        }

        resourceList.forEach(record -> resultIdList.add(record.getResourceId()));
        return resultIdList;
    }

    public List<BatchResource> listResourceByTaskId (Long taskId, Integer resourceType, Long projectId) {
        List<BatchResource> resourceList = batchTaskResourceShadeDao.listResourceByTaskId(taskId, resourceType, projectId);
        if (CollectionUtils.isEmpty(resourceList)) {
            return new ArrayList<>();
        } else {
            return resourceList;
        }
    }

    public void deleteByProjectId(Long projectId) {
        batchTaskResourceShadeDao.deleteByProjectId(projectId);
    }

    /**
     * 根据taskId 删除任务
     *
     * @param taskId
     * @return
     */
    public Integer deleteByTaskId(Long taskId) {
        return batchTaskResourceShadeDao.deleteByTaskId(taskId);
    }
}

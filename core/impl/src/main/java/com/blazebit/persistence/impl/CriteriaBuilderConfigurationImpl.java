/*
 * Copyright 2014 Blazebit.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.blazebit.persistence.impl;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.spi.CriteriaBuilderConfiguration;
import com.blazebit.persistence.spi.QueryTransformer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

/**
 *
 * @author Christian Beikov
 * @since 1.0
 */
public class CriteriaBuilderConfigurationImpl implements CriteriaBuilderConfiguration {
    
    private final List<QueryTransformer> queryTransformers = new ArrayList<QueryTransformer>();
    
    public CriteriaBuilderConfigurationImpl() {
        loadQueryTransformers();
    }

    private void loadQueryTransformers() {
        ServiceLoader<QueryTransformer> serviceLoader = ServiceLoader.load(QueryTransformer.class);
        Iterator<QueryTransformer> iterator = serviceLoader.iterator();

        if (iterator.hasNext()) {
            QueryTransformer transformer = iterator.next();
            queryTransformers.add(transformer);
        }
    }

    @Override
    public void registerQueryTransformer(QueryTransformer transformer) {
        queryTransformers.add(transformer);
    }

    @Override
    public List<QueryTransformer> getQueryTransformers() {
        return queryTransformers;
    }

    @Override
    public CriteriaBuilderFactory createCriteriaBuilderFactory() {
        return new CriteriaBuilderFactoryImpl(this);
    }
    
}
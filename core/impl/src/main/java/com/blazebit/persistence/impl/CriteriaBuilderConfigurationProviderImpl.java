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

import com.blazebit.apt.service.ServiceProvider;
import com.blazebit.persistence.spi.CriteriaBuilderConfiguration;
import com.blazebit.persistence.spi.CriteriaBuilderConfigurationProvider;

/**
 *
 * @author Christian
 */
@ServiceProvider(CriteriaBuilderConfigurationProvider.class)
public class CriteriaBuilderConfigurationProviderImpl implements CriteriaBuilderConfigurationProvider {

    @Override
    public CriteriaBuilderConfiguration createConfiguration() {
        return new CriteriaBuilderConfigurationImpl();
    }
    
}

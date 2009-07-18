/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.entity;

import org.seasar.doma.DomaIllegalArgumentException;
import org.seasar.doma.domain.Domain;

/**
 * @author taedium
 * 
 */
public class BuiltinEntityUtilDelegate implements EntityUtilDelegate {

    @Override
    public <D extends Domain<?, ?>> D getDomain(Object entity,
            Class<D> domainClass, String propertyName) {
        if (entity == null) {
            throw new DomaIllegalArgumentException("entity", entity);
        }
        if (!Entity.class.isInstance(entity)) {
            throw new NonEntityArgumentException("entity", entity);
        }
        if (domainClass == null) {
            throw new DomaIllegalArgumentException("domainClass", domainClass);
        }
        if (propertyName == null) {
            throw new DomaIllegalArgumentException("propertyName", propertyName);
        }
        Entity<?> e = Entity.class.cast(entity);
        EntityProperty<?> property = e.__getEntityProperty(propertyName);
        if (property == null) {
            return null;
        }
        if (!domainClass.isInstance(property.getDomain())) {
            return null;
        }
        return domainClass.cast(property.getDomain());
    }
}

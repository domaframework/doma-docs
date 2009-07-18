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
package org.seasar.doma.it.entity;

import java.util.List;

import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Transient;
import org.seasar.doma.Version;
import org.seasar.doma.domain.BigDecimalDomain;
import org.seasar.doma.domain.IntegerDomain;
import org.seasar.doma.domain.StringDomain;
import org.seasar.doma.domain.TimestampDomain;

/**
 * 
 * @author taedium
 * 
 */
@Entity(listener = EmpListener.class)
public interface Emp {

    @Id
    IntegerDomain id();

    StringDomain name();

    BigDecimalDomain salary();

    @Version
    IntegerDomain version();

    TimestampDomain insertTimestamp();

    TimestampDomain updateTimestamp();

    @Transient
    StringDomain temp();

    @Transient
    List<StringDomain> tempList();

}

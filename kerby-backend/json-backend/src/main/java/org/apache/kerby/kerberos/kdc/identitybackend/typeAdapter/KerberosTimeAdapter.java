/**
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package org.apache.kerby.kerberos.kdc.identitybackend.typeAdapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.apache.kerby.kerberos.kerb.spec.KerberosTime;

import java.lang.reflect.Type;

public class KerberosTimeAdapter implements JsonSerializer<KerberosTime>,
        JsonDeserializer<KerberosTime> {

    @Override
    public KerberosTime deserialize(JsonElement jsonElement, Type type,
                                    JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {
        String timeString = jsonElement.getAsString();
        long time = Long.valueOf(timeString);
        return new KerberosTime(time);
    }

    @Override
    public JsonElement serialize(KerberosTime kerberosTime, Type type,
                                 JsonSerializationContext jsonSerializationContext) {
        String timeString = String.valueOf(kerberosTime.getTime());
        return new JsonPrimitive(timeString);
    }
}

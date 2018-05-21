/*
 * Copyright 2017-2018, Société Générale All rights reserved.
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

package com.zhihui.sbs.commons.amqp.core.processor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * Created by anand on 02/07/17.
 */
public class DefaultCorrelationPostProcessorTest {

  private DefaultCorrelationPostProcessor correlationPostProcessor;

  private Message message;

  private Tracer tracer;

  @Before
  public void setUp() {
    tracer = Mockito.mock(Tracer.class);
    correlationPostProcessor = new DefaultCorrelationPostProcessor(tracer);
    message = MessageBuilder.withBody("DummyMessage".getBytes()).build();
  }

  @Test
  public void addNewCorrelationIdToHeaderIfMissingTest() {
    correlationPostProcessor.postProcessMessage(message);
    assertNotNull(message.getMessageProperties().getHeaders().get("correlation-id"));
  }

  @Test
  public void addNewCorrelationIdFromTracerToHeaderIfMissingTest() {
    Mockito.when(tracer.getCurrentSpan()).thenReturn(Span.builder().traceId(10L).build());
    correlationPostProcessor.postProcessMessage(message);
    assertNotNull(message.getMessageProperties().getHeaders().get("correlation-id"));
    assertThat(message.getMessageProperties().getHeaders().get("correlation-id"), equalTo("000000000000000a"));
  }

  @Test
  public void addExistingCorrelationIdToHeaderIfPresentTest() {
    message.getMessageProperties().setCorrelationIdString("ExistingCorrelationId");
    correlationPostProcessor.postProcessMessage(message);
    assertNotNull(message.getMessageProperties().getHeaders().get("correlation-id"));
    assertThat(message.getMessageProperties().getHeaders().get("correlation-id"), is(equalTo("ExistingCorrelationId")));
  }

}

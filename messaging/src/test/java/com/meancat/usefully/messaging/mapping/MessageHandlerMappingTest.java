package com.meancat.usefully.messaging.mapping;

import static junit.framework.Assert.assertSame;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import com.meancat.usefully.messaging.messages.custom.Ping;


@RunWith(MockitoJUnitRunner.class)
public class MessageHandlerMappingTest {

    @Mock
    private DefaultListableBeanFactory mockBeanFactory;

    private MessageHandlerMapping messageHandlerMapping;

    @Before
    public void setUp() {
        messageHandlerMapping = new MessageHandlerMapping();
        messageHandlerMapping.beanFactory = mockBeanFactory;

        // scans the bean definitions to find handlers
        when(mockBeanFactory.getBeanDefinitionNames()).thenReturn(new String[]{});
    }

    @Test
    public void useStaticAttachment() throws NoSuchMethodException {
        Method thisMethod = this.getClass().getMethod("useStaticAttachment");
        HandlerMapping handlerMapping = new HandlerMapping(thisMethod, this);
        messageHandlerMapping.staticHandlerMappings.put(String.class, handlerMapping);

//      this message no player, calls the static handler defined ab
        TestMessageImpl m = new TestMessageImpl("hi");

        HandlerMapping result = messageHandlerMapping.getStaticMapping(m);
        assertSame(handlerMapping, result);
    }

    @Test(expected = MappingException.class)
    public void determineHandlerMappingKey_NoArgs() throws NoSuchMethodException, MappingException {
        messageHandlerMapping.determineHandlerMappingKey(this.getClass(), this.getClass().getMethod("noArgs"));
    }


    @Test
    public void determineHandlerMappingKey_SimplerCustomMessage() throws NoSuchMethodException, MappingException {
        Class<?> key = messageHandlerMapping.determineHandlerMappingKey(this.getClass(), this.getClass().getMethod("simplerCustomHandlerMethod",
                TestMessageHeaderImpl.class, Ping.class));
        assertSame(Ping.class, key);
    }


    public void noArgs() {}

    @SuppressWarnings("UnusedParameters")
    public void simplerCustomHandlerMethod(TestMessageHeaderImpl header, Ping ping) {}
}

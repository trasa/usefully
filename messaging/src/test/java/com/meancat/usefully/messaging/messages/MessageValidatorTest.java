package com.meancat.usefully.messaging.messages;

import static com.google.common.collect.Lists.newArrayList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;

import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageValidatorTest {

    private static final Logger logger = LoggerFactory.getLogger(MessageValidatorTest.class);

    private PlasmaMessageValidator messageValidator;

    @Before
    public void setUp() {
        messageValidator = new PlasmaMessageValidator();
        messageValidator.validator = Validation.byProvider(HibernateValidator.class)
                .configure()
                .buildValidatorFactory()
                .getValidator();
    }

    @Test
    public void badMessage() {
        // breaks all the rules!
        SomeMessage msg = new SomeMessage(null, "");
        msg.details.add(new Details(-1));

        Set<ConstraintViolation<Message>> errors =  messageValidator.validate(msg);
        assertEquals(3, errors.size());
        logger.debug(messageValidator.getDescription(errors));
    }

    @Test
    public void goodMessage() {
        SomeMessage msg = new SomeMessage("required!", "bluey");
        msg.details.add(new Details(8));

        Set<ConstraintViolation<Message>> errors =  messageValidator.validate(msg);
        assertTrue(errors.isEmpty());
    }

    private class SomeMessage implements Message {
        @NotEmpty
        public String someRequiredField;

        @Length(min=5, max=10)
        public String favoriteColor;

        @Valid
        @NotEmpty
        public List<Details> details;

        private SomeMessage(String someRequiredField, String favoriteColor) {
            this.someRequiredField = someRequiredField;
            this.favoriteColor = favoriteColor;
            details = newArrayList();
        }

        @Override
        public Class<?> getPayloadClass() {
            return Details.class;
        }

        @Override
        public Object getPayload() {
            return details;
        }
    }

    private class Details {
        @Range(min=0, max=10)
        public int volume;

        private Details(int volume) {
            this.volume = volume;
        }
    }
}

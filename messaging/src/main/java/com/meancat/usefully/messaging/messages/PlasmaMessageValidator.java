package com.meancat.usefully.messaging.messages;

import java.io.StringWriter;
import java.util.Iterator;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Joiner;

@Component
public class PlasmaMessageValidator {
    @Autowired
    protected Validator validator;

    public Set<ConstraintViolation<Message>> validate(Message message) {
        return validator.validate(message);
    }

    public String getDescription(Set<ConstraintViolation<Message>> violations) {
        StringWriter writer = new StringWriter();
        Iterator<ConstraintViolation<Message>> iter = violations.iterator();

        // the null escape here makes a test of this slightly easier to write.
        while (iter != null && iter.hasNext()) {
            ConstraintViolation<Message> violation = iter.next();
            writer.append(Joiner.on(".").join(violation.getPropertyPath()))
                    .append(" - ")
                    .append(violation.getMessage())
                    .append(iter.hasNext() ? ", " : "");
        }
        return writer.toString();
    }
}

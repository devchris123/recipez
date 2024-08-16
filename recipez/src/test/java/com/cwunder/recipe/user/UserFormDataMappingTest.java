package com.cwunder.recipe.user;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserFormDataMappingTest {
    @Test
    public void testSerializeObjectIntoJson() throws StreamWriteException, DatabindException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        var data = new UserFormData("user", "pass", "key");

        // execute
        var writer = new StringWriter();
        mapper.writeValue(writer, data);

        // assert
        assertTrue(
                "{\"username\":\"user\",\"password\":\"pass\",\"enabled\":true,\"g-recaptcha-response\":\"key\"}"
                        .equals(writer.toString()));
    }

    @Test
    public void testDeserializeObjectIntoJson() throws StreamWriteException, DatabindException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        var expected = new UserFormData("user", "pass", "key");
        var json = "{\"username\":\"user\",\"password\":\"pass\",\"enabled\":true,\"g-recaptcha-response\":\"key\"}";

        // execute
        var formData = mapper.readValue(new StringReader(json), UserFormData.class);

        // assert
        assertEquals(expected.getUsername(), formData.getUsername());
        assertEquals(expected.getPassword(), formData.getPassword());
        assertEquals(expected.getEnabled(), formData.getEnabled());
        assertEquals(expected.getGrecaptchaResponse(), formData.getGrecaptchaResponse());
    }
}

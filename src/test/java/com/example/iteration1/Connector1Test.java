package com.example.iteration1;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class Connector1Test {

    @Test
    public void placeRequest() {
        Connector1 underTest = new Connector1();

        // When we try to run this, this test will actually try to contact an external service because the
        // connector is highly coupled with a real HTTP Client implementation we don't have control over!
        // Plus, should we forgotten to call AbstractConnector's init() method from Connector1(), this test would have failed with NPE!! :(
        ResponseDTO responseDTO = underTest.placeRequest("payload ***", "s3209");

        assertNotNull(responseDTO);
    }

    @Test
    public void placeRequest_invalidInput() {
        Connector1 underTest = new Connector1();

        // This will fail because of invalid input (we are not validating/restricting input)
        underTest.placeRequest(null, null);
    }
}

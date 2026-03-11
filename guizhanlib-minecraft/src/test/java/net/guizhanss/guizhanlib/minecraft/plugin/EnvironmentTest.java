package net.guizhanss.guizhanlib.minecraft.plugin;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EnvironmentTest {

    @Test
    void detectsLegacyMockBukkitServerClass() {
        Environment environment = Environment.detect(
            "be.seeseemelk.mockbukkit.ServerMock",
            new StackTraceElement[0]
        );

        Assertions.assertEquals(Environment.TEST, environment);
    }

    @Test
    void detectsModernMockBukkitServerClass() {
        Environment environment = Environment.detect(
            "org.mockbukkit.mockbukkit.ServerMock",
            new StackTraceElement[0]
        );

        Assertions.assertEquals(Environment.TEST, environment);
    }

    @Test
    void doesNotTreatJUnitAloneAsTestEnvironment() {
        Environment environment = Environment.detect(
            null,
            new StackTraceElement[]{
                new StackTraceElement("org.junit.jupiter.engine.execution.InvocationInterceptorChain", "proceed", "InvocationInterceptorChain.java", 12)
            }
        );

        Assertions.assertEquals(Environment.LIVE, environment);
    }

    @Test
    void detectsCombinedJUnitAndMockBukkitStackTrace() {
        Environment environment = Environment.detect(
            null,
            new StackTraceElement[]{
                new StackTraceElement("org.junit.jupiter.engine.execution.InvocationInterceptorChain", "proceed", "InvocationInterceptorChain.java", 12),
                new StackTraceElement("org.mockbukkit.mockbukkit.MockBukkit", "mock", "MockBukkit.java", 42)
            }
        );

        Assertions.assertEquals(Environment.TEST, environment);
    }

    @Test
    void fallsBackToLiveWhenNoTestSignalsExist() {
        Environment environment = Environment.detect(
            null,
            new StackTraceElement[0]
        );

        Assertions.assertEquals(Environment.LIVE, environment);
    }
}

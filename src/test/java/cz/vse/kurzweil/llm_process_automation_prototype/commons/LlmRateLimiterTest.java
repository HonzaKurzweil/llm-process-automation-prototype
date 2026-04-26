package cz.vse.kurzweil.llm_process_automation_prototype.commons;

import cz.vse.kurzweil.llm_process_automation_prototype.dto.ModelType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LlmRateLimiterTest {

    private LlmRateLimiter limiter;

    @BeforeEach
    void setUp() {
        limiter = new LlmRateLimiter();
        enableLimiter(0, 3, 0, 2.0);
    }

    @Test
    void execute_whenDisabled_callsSupplierDirectly() {
        ReflectionTestUtils.setField(limiter, "enabled", false);

        String result = limiter.execute(() -> "ok");

        assertThat(result).isEqualTo("ok");
    }

    @Test
    void execute_whenEnabled_returnsSupplierResult() {
        String result = limiter.execute(() -> "result");

        assertThat(result).isEqualTo("result");
    }

    @Test
    void execute_retriesOnFailureAndEventuallySucceeds() {
        AtomicInteger attempts = new AtomicInteger(0);
        Supplier<String> flakySupplier = () -> {
            if (attempts.incrementAndGet() < 3) {
                throw new RuntimeException("transient error");
            }
            return "success";
        };

        String result = limiter.execute(flakySupplier);

        assertThat(result).isEqualTo("success");
        assertThat(attempts.get()).isEqualTo(3);
    }

    @Test
    void execute_exhaustsMaxAttempts_throwsLastException() {
        AtomicInteger attempts = new AtomicInteger(0);
        Supplier<String> alwaysFails = () -> {
            attempts.incrementAndGet();
            throw new RuntimeException("permanent error");
        };

        assertThatThrownBy(() -> limiter.execute(alwaysFails))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("permanent error");

        assertThat(attempts.get()).isEqualTo(3);
    }

    @Test
    void execute_withOllamaModel_bypassesRateLimiter() {
        AtomicInteger attempts = new AtomicInteger(0);
        Supplier<String> alwaysFails = () -> {
            attempts.incrementAndGet();
            throw new RuntimeException("fail");
        };

        // OLLAMA calls skip the limiter and do not retry
        assertThatThrownBy(() -> limiter.execute(ModelType.GEMMA3_12B, alwaysFails))
                .isInstanceOf(RuntimeException.class);

        assertThat(attempts.get()).isEqualTo(1);
    }

    @Test
    void execute_withOpenAiModel_goesThoughRateLimiter() {
        AtomicInteger attempts = new AtomicInteger(0);
        Supplier<String> flakySupplier = () -> {
            if (attempts.incrementAndGet() < 3) {
                throw new RuntimeException("transient");
            }
            return "ok";
        };

        String result = limiter.execute(ModelType.GPT_4O, flakySupplier);

        assertThat(result).isEqualTo("ok");
        assertThat(attempts.get()).isEqualTo(3);
    }

    @Test
    void execute_withMaxAttemptsOne_noRetryOnFailure() {
        enableLimiter(0, 1, 0, 1.0);

        AtomicInteger attempts = new AtomicInteger(0);
        Supplier<String> alwaysFails = () -> {
            attempts.incrementAndGet();
            throw new RuntimeException("fail");
        };

        assertThatThrownBy(() -> limiter.execute(alwaysFails)).isInstanceOf(RuntimeException.class);
        assertThat(attempts.get()).isEqualTo(1);
    }

    // --- helper ---

    private void enableLimiter(long minDelayMs, int maxAttempts, long retryInitialDelayMs, double retryMultiplier) {
        ReflectionTestUtils.setField(limiter, "enabled", true);
        ReflectionTestUtils.setField(limiter, "minDelayMs", minDelayMs);
        ReflectionTestUtils.setField(limiter, "maxAttempts", maxAttempts);
        ReflectionTestUtils.setField(limiter, "retryInitialDelayMs", retryInitialDelayMs);
        ReflectionTestUtils.setField(limiter, "retryMultiplier", retryMultiplier);
    }
}

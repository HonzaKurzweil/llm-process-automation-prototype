package cz.vse.kurzweil.llm_process_automation_prototype.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

@Slf4j
@Component
public class LlmRateLimiter {

    @Value("${app.llm.rate-limiter.enabled}")
    private boolean enabled;

    @Value("${app.llm.rate-limiter.min-delay-ms}")
    private long minDelayMs;

    @Value("${app.llm.rate-limiter.retry-max-attempts:3}")
    private int maxAttempts;

    @Value("${app.llm.rate-limiter.retry-initial-delay-ms}")
    private long retryInitialDelayMs;

    @Value("${app.llm.rate-limiter.retry-multiplier}")
    private double retryMultiplier;

    private final AtomicLong lastCallTime = new AtomicLong(0);

    public <T> T execute(Supplier<T> call) {
        if (!enabled) {
            return call.get();
        }
        applyMinDelay();
        return executeWithRetry(call);
    }

    private void applyMinDelay() {
        if (minDelayMs <= 0) {
            return;
        }
        long wait = minDelayMs - (System.currentTimeMillis() - lastCallTime.get());
        if (wait > 0) {
            log.debug("Rate limiter throttling for {}ms", wait);
            sleep(wait);
        }
        lastCallTime.set(System.currentTimeMillis());
    }

    private <T> T executeWithRetry(Supplier<T> call) {
        long delay = retryInitialDelayMs;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                return call.get();
            } catch (Exception e) {
                if (attempt == maxAttempts) {
                    throw e;
                }
                log.warn("Rate limit hit (attempt {}/{}), retrying after {}ms", attempt, maxAttempts, delay);
                sleep(delay);
                delay = (long) (delay * retryMultiplier);
            }
        }
        throw new IllegalStateException("unreachable");
    }

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted during rate limiter wait", e);
        }
    }
}

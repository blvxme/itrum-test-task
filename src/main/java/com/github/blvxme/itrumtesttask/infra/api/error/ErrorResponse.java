package com.github.blvxme.itrumtesttask.infra.api.error;

import java.time.Instant;

public record ErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String path
) { }

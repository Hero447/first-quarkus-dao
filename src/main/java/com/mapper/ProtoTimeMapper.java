package com.mapper;

import com.google.protobuf.Timestamp;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@ApplicationScoped
public class ProtoTimeMapper {
    public Timestamp toTimestamp(LocalDateTime ldt) {
        if (ldt == null) return Timestamp.getDefaultInstance();
        Instant instant = ldt.toInstant(ZoneOffset.UTC);
        return Timestamp.newBuilder()
                .setSeconds(instant.getEpochSecond())
                .setNanos(instant.getNano())
                .build();
    }

    public LocalDateTime toLocalDateTime(Timestamp ts) {
        if (ts == null || ts.equals(Timestamp.getDefaultInstance())) return null;
        return LocalDateTime.ofInstant(
                Instant.ofEpochSecond(ts.getSeconds(), ts.getNanos()),
                ZoneOffset.UTC
        );
    }
}

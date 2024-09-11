package com.grepp.clone_project;

import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

public class Util {

    public static UUID toUUID(byte[] bytes) {
        var byteBuffer = ByteBuffer.wrap(bytes);
        return new UUID(byteBuffer.getLong(), byteBuffer.getLong());
    }

    public static LocalDateTime toLocalDateTime(Timestamp timestamp){
        return timestamp != null ? timestamp.toLocalDateTime() : null;
    }
}

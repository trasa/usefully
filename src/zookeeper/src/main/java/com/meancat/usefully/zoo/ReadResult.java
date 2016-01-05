package com.meancat.usefully.zoo;

import org.apache.zookeeper.data.Stat;

import java.io.ByteArrayInputStream;

public class ReadResult {
    private final Stat stat;
    private final ByteArrayInputStream data;

    public ReadResult(Stat stat, ByteArrayInputStream data) {
        this.stat = stat;
        this.data = data;
    }

    public Stat getStat() {
        return stat;
    }

    public ByteArrayInputStream getData() {
        return data;
    }
}

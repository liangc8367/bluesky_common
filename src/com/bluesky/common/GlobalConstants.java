package com.bluesky.common;

/**
 * Created by liangc on 31/12/14.
 */
public class GlobalConstants {
    final static String TAG = "TrunkManager";
    public static final String TRUNK_CENTER_ADDR = "127.0.0.1"; // on same machine
    public static final int TRUNK_CENTER_PORT   = 32000;
    public static final int INIT_SEQ_NUMBER     = 12345;

    public static final long REGISTRATION_RETRY_TIME = 1000; // 1 second
    public static final long REGISTRATION_RETRY_MAX_TIME = 60 * 5 * 1000; // 5 minutes

    /** call parameters */
    public static final long MAX_ROUND_TRIP_TIME    = 100; //amazon ec2 free-tier has 80ms RTT time
    public static final long CALL_FLYWHEEL_PERIOD    = MAX_ROUND_TRIP_TIME * 20;  // return to idle if no rxed packet
    public static final long CALL_HANG_PERIOD_MS        = 5000; // 5second call hang
    public static final long CALL_PACKET_INTERVAL    = 20;    // 20ms
    public static final short CALL_HANG_COUNTDOWN   = (short)(CALL_HANG_PERIOD_MS / CALL_PACKET_INTERVAL);
    public static final int CALL_PREAMBLE_NUMBER    = 3;
    public static final int CALL_TERM_NUMBER        = -3;

    public static final int COMPRESSED_20MS_AUDIO_SIZE  = 20;

    /** call info for faked echo */
    public static final long    SUID_TRUNK_MANAGER  = 1;
    public static final long    SUID_INVALID = 0;

}

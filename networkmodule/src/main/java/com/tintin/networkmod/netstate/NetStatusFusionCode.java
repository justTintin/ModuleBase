package com.tintin.networkmod.netstate;

/**
 *
 */
public class NetStatusFusionCode
{
    /** Network type is unknown */
    public static final int NETWORK_TYPE_UNKNOWN = 0;

    /** Current network is GPRS */
    public static final int NETWORK_TYPE_GPRS = 1;

    /** Current network is EDGE */
    public static final int NETWORK_TYPE_EDGE = 2;

    /** Current network is UMTS */
    public static final int NETWORK_TYPE_UMTS = 3;

    /** Current network is CDMA: Either IS95A or IS95B*/
    public static final int NETWORK_TYPE_CDMA = 4;

    /** Current network is EVDO revision 0*/
    public static final int NETWORK_TYPE_EVDO_0 = 5;

    /** Current network is EVDO revision A*/
    public static final int NETWORK_TYPE_EVDO_A = 6;

    /** Current network is 1xRTT*/
    public static final int NETWORK_TYPE_1xRTT = 7;

    /** Current network is HSDPA */
    public static final int NETWORK_TYPE_HSDPA = 8;

    /** Current network is HSUPA */
    public static final int NETWORK_TYPE_HSUPA = 9;

    /** Current network is HSPA */
    public static final int NETWORK_TYPE_HSPA = 10;

    /** Current network is iDen */
    public static final int NETWORK_TYPE_IDEN = 11;

    /** Current network is EVDO revision B*/
    public static final int NETWORK_TYPE_EVDO_B = 12;

    /** Current network is LTE */
    public static final int NETWORK_TYPE_LTE = 13;

    /** Current network is eHRPD */
    public static final int NETWORK_TYPE_EHRPD = 14;

    /** Current network is HSPA+ */
    public static final int NETWORK_TYPE_HSPAP = 15;

    public static final int NET_DISCONNECTED = 51;

    public static final int NET_CONNECTED = 100;

    public static final int NETWORK_CLASS_UNKNOWN = 52;

    public static final int NETWORK_CLASS_2_G = 53;

    public static final int NETWORK_CLASS_3_G = 54;

    public static final int NETWORK_CLASS_4_G = 55;

    public static final int WIFI_CONNECTED = 101;

    public static final int WIFI_DISCONNECTED = 102;

    public static final int WIFI_OPEN = 103;

    public static final int WIFI_CLOSE = 104;

}

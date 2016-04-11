package com.example.syrok.myfiinder;

/**
 * Created by Syrok on 04/04/2016.
 */
public final class Constants {
    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;
    public static final String PACKAGE_NAME = "com.example.syrok.myfiinder";
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";
    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_DATA_EXTRA";

    protected static final String ADDRESS_REQUESTED_KEY = "address-request-pending";
    protected static final String LOCATION_ADDRESS_KEY = "location-address";

    // Keys for storing activity state in the Bundle.
    protected final static String LOCATION_KEY = "location-key";

    public static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 20;
    public static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 21;
}

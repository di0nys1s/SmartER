package com.di0nys1s.smarter.JavaClass;

public class URLs {
    public static String WEATHER_WS_URL = "http://api.openweathermap.org/data/2.5/weather?zip=";
    public static String SAVE_RESIDENT_URL = "http://10.0.2.2:8080/SmartERDB/webresources/smarter.resident"; // ok
    public static String SAVE_RESIDENT_URL_2 = "http://10.0.2.2:8080/SmartERDB/webresources/smarter.resident/findByAll/";
    public static String SAVE_RESIDENT_CREDENTIAL_URL ="http://10.0.2.2:8080/SmartERDB/webresources/smarter.residentcredential"; // ok
    public static String SMARTER_WS_electricityusage_URL = "http://10.0.2.2:8080/SmartERDB/webresources/smarter.electricityusage"; // ok
    public static String FIND_USER_BY_EMAIL_WS = "http://10.0.2.2:8080/SmartERDB/webresources/smarter.resident/findByEmail/"; // ok
    public static String FIND_CREDENTIAL_BY_USERNAME_WS = "http://10.0.2.2:8080/SmartERDB/webresources/smarter.residentcredential/findByUsername/"; // ok
    public static String FIND_APP_USAGE_BY_RESID_DATE_URL = "http://10.0.2.2:8080/SmartERDB/webresources/smarter.electricityusage/showDailyUsageOfAppliances/"; // ok
    public static String FIND_USAGE_BY_RESID = "http://10.0.2.2:8080/SmartERDB/webresources/smarter.electricityusage/findByResId/"; // ok
    public static String GET_ALL_USERS = "http://10.0.2.2:8080/SmartERDB/webresources/smarter.resident"; // ok
    public static String MAP_WS_URL = "http://www.mapquestapi.com/geocoding/v1/address?key=9dlH0Lh3fa4tZ8sWVsMn0wac3vsyfs09&thumbMaps=false&location="; // ok
    public static String get_username_password = "http://10.0.2.2:8080/SmartERDB/webresources/smarter.residentcredential/findByUsernameANDpassword/";     // ok
    public static String FIND_HOURLY_USAGE_BY_RESID_DATE = "http://10.0.2.2:8080/SmartERDB/webresources/smarter.electricityusage/showHourlyDailyUsage/"; // ok
    public static String WS_KEY_EXCEPTION = "Exception";
    public static String MSG_400 = "Bad Request. Please check your connection";
    public static String MSG_401 = "401 Unauthorized. Please check your identity";
    public static String MSG_404 = "404 Resource not found";
    public static String MSG_500 = "500 Internal server error";
    public static String DATE_FORMAT = "yyyy-MM-dd";
    public static String SERVER_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static String SUCCESS_MSG = "Success";

    public static String ADDRESSKEY = "address";
    public static String DOBKEY ="dob";
    public static String EMAILKEY ="email";
    public static String PROVIDERKEY ="energyprovider";
    public static String NAMEKEY ="firstname";
    public static String MOBILEKEY ="mobile";
    public static String RESIDENTNUMBERKEY ="numberofresidents";
    public static String POSTCODEKEY ="postcode";
    public static String RESIDKEY ="resid";
    public static String LASTNAMEKEY ="surname";
    public static String MAP_VIEW_HOURLY = "hourly";
    public static String MAP_VIEW_DAILY = "daily";
    public static String WS_KEY_MAP_LOCATION ="locations";
    public static String WS_KEY_MAP_LATLNG = "latLng";
    public static String WS_KEY_CREDENTIAL_PASSWORD = "password";
    public static String WS_KEY_CREDENTIAL_REGISTER_DATE = "registrationdate";
    public static String WS_KEY_CREDENTIAL_USERNAME = "username";
    public static String WS_KEY_WEATHER_APPID = "&appid=a53cec11292d5d6b58dd6dde1c200e94";
}

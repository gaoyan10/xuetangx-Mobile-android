package com.xuetangx.util;

public class ConstantUtils {
	public static int SCREENWIDTH;
	public static int SCREENHEIGHT;
	public static final String URL = "http://mobile-api.tsinghuax.org/";//"https://yangao.apiary.io";
	public static final String KEY = "X-edx-api-key"; //注意X 大小写
	public static final String ACCESS = "Authorization";
	public static final String REGISTER = "edx-api/signup/v1/register";
	public static final String LOGIN = "edx-api/auth/v1/oauth2/access_token";//"/auth/login";
	public static final String ENROLL = "edx-api/enrollments/v1/";
	public static final String UNENROLL = "edx-api/enrollments/v1/";
	public static final String GET_ENROLL_COURSES = "edx-api/enrollments/v1/";
	public static final String GET_ALL_COURSES = "edx-api/courseware/v1";
	public static final String COURSE_NAVIGATION = "edx-api/courseware/v1/";
	public static final String COURSE_VEDIO = "edx-api/courseware/v1/";
	public static final String COURSE_NUM = "course_num";
	public static final String DEFAULT_PRE = "prefrence";
	public static final String SD_PATH = "save_path";
	public static final String KEY_PATH = "secret_preference";
}
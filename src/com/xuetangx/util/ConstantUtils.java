package com.xuetangx.util;

public class ConstantUtils {
	public static int SCREENWIDTH;
	public static int SCREENHEIGHT;
	public static String URL = "https://10.9.10.253:3000/";//"https://yangao.apiary.io";
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
	public static final String IMAGE_CACHE_PATH = "/.xuetangx/images";
	public static final String VIDEO_CACHE_PATH = "/.xuetangx/video";
	public static final String VIDEO_SRT_PATH = "/.xuetangx/srt";
	public static final String SD_PATH = "save_path";
	public static final String KEY_PATH = "secret_preference";
	public static final String USER_PRE = "userdata";
	public static final String BOOT_IMAGE = "bootimage";
	public static final String BOOT_IMAGE_DISPLAY = "boot_image_display";
	public static final String COURSE_DATA = "coursedata.db";
	public static final String USER_DATA = "userdata.db";
	public static final String COURSE_DATA_DETAIL = "course_data_detail.db";
	public static final String T_ENROLLMENT = "enrollment";
	public static final String T_COURSE = "course";
	public static final String T_COURSE_DATA = "course_data";
	public static final String T_USER = "current_user";
	public static final String T_HISTORY = "history";
	public static final String T_DOWNLOAD = "download";
}

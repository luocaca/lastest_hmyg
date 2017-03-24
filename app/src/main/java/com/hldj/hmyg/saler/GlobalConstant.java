package com.hldj.hmyg.saler;

public class GlobalConstant {

	/** 通知栏标题 */
	public final static String Notifacation_Title = "花木易购";
	/** 本机SD卡中文件的存储目录 */
	public final static String path = "/Flowers";
	/** SDCARD首级目录 */
	public final static String DIR_HEAD = "Flowers";

	/** 图片文件目录 */
	public static final String IMAGE_DIR = "Flowers/image";
	/** 网络图片缓存路径 写死在包中，不能只修改这里 */
	public static final String IMAGE_CACHE_DIR = "Flowers/urlimage";

	/** 更新文件名 */
	public final static String APK_NAME = "Flowers.apk";

	/** 选择收货/苗源地址返回结果 */
	public static final String RESULT_NEW_ADDRESS_INFO_KEY = "result_new_address_info_key";
	/** 图片预览加载失败(大图） */
	public static final String IMAGE_PREVIEW_LOAD_FAILURE = "image_preview_load_failure";

	// 存储空间
	/** 未发现SD卡 */
	public static final int SDCARD_IS_UNMOUNT = 1001;
	/** 存储空间不足 */
	public static final int SDCARD_IS_FULL = 1002;

	/** 显示的通知ID */
	public final static int ONGOING_NOTIFICATION_ID = 2000;// 显示的通知ID
	public final static int UPGRADE_NOTIFICATION_ID = 2001;// 下载更新通知栏ID

	// *********************** SharedPreferences 字符串定义 ***********************

	// *********************** 帐号配置文件 **********************************
	public static final String SP_ACCOUNT = "account";
	/** 帐号 */
	public static final String SP_ACCOUNT_NAME = "accountName";
	/** 密码 */
	public static final String SP_ACCOUNT_PWD = "accountPwd";
	/** 联系人id */
	public static final String SP_ACCOUNT_PERSON_ID = "accountPersonId";
	// *********************** 和帐号无关的设置 **********************************
	public static final String SP_SETTING = "setting";
	/** 程序版本 */
	public static final String SP_VERSION = "version";
	/** 标准名库最后更新时间 */
	public static final String SP_STANDAR_LIB_UPDATE_TIME = "standar_lib_update_time";
	/** 是否需要升级 */
	public static final String SP_IS_UPGRADE = "isupgrade";
	/** 手机屏幕高 */
	public static final String SP_GLOBAL_SCREEN_HEIGHT = "globalscreenheight";
	/** 手机屏幕宽 */
	public static final String SP_GLOBAL_SCREEN_WIDTH = "globalscreenwidth";
	/** 手机可见屏幕高，包括状态栏 */
	public static final String SP_GLOBAL_SCREEN_VISIABLE_HEIGHT = "globalscreenvisiableheight";
	/** 上次选择照片的路径 */
	public static final String SP_CHOOSE_PHOTO_DIR_ID = "choose_photo_dir_id";
	/** 与服务器的时间差 */
	public static final String SP_SERVER_TIME_OFFSET = "server_time_offset";
	/** 省市版本 */
	public static final String SP_PROVINCE_VERSION = "province_version";
	/** 省市数据 */
	public static final String SP_PROVINCE_DATA_STR = "province_data_str";
	/** push地址 */
	public static final String SP_PUSH_SERVER_STR = "push_server_str";
	/** push端口 */
	public static final String SP_PUSH_POST_STR = "push_post_str";
	/** banner数据jsonarray的字符串 */
	public static final String SP_BANNER_JSON_LIST_STR = "banner_json_list_str";
	public static final String SP_CURRENT_PROVINCE = "currentprovince";
	public static final String SP_CURRENT_CITY = "currentcity";
	// ************************ 和帐号相关的设置 ************************************
	public static final String SP_ACCOUNT_SERVER_SETTING = "accountserversetting";
	/** 个人资料 */
	public static final String SP_ACCOUNT_PERSON_INFO = "accountPersonInfo";
	/** 主账户id */
	public static final String SP_ACCOUNT_PARENTID = "accountParentId";
	/** 个人认证资料 */
	public static final String SP_ACCOUNT_CERTIFY_SELF = "accountCertifySelf";
	/** 单位认证资料 */
	public static final String SP_ACCOUNT_CERTIFY_COMPANY = "accountCertifyCompany";
	/** 发票设置 */
	public static final String SP_ACCOUNT_INVOICE_SETTING = "accountInvoiceSetting";
	/** 上次选择的银行帐号 */
	public static final String SP_ACCOUNT_LAST_CHOOSE_BANK_ID = "accountLastChooseBankId";
	/** 订阅通知 */
	public static final String SP_IS_SUBSCRIBE_NOTICE = "issbuscribenotice";
	/** push的用户名 */
	public static final String SP_ACCOUNT_PUSH_ACCOUNT = "accountPushAccount";
	/** 消息通知 */
	public static final String SP_IS_NOTICE_REMIND = "isNoticeRemind";
	public static final String SP_NOTICE_BEGIN_TIME_HOUR = "noticebegintimehour";
	public static final String SP_NOTICE_END_TIME_HOUR = "noticeendtimehour";

	// ************************ handler处理相关 ********************************

	/** 新消息到达 */
	public static final int NEW_MSG_NOTIFY = 1001;
	/** 未读数量增加 */
	public static final int NEW_MSG_UNREAD = 1002;
	/** 获取到新push消息 */
	public static final int GET_NEW_PUSH_MSG = 1003;

	// ***************************登录相关*******************************
	/** 注册成功 */
	public static final int HANDLER_REGEIST_SUCCESS = 10001;
	/** 注册失败 */
	public static final int HANDLER_REGEIST_FAILURE = 10002;
	/** 获取验证码成功 */
	public static final int HANDLER_GET_SMS_SUCCESS = 10010;
	/** 获取验证码失败 */
	public static final int HANDLER_GET_SMS_FAILURE = 10011;
	/** 密码重置成功 */
	public static final int HANDLER_RESET_PWD_SUCCESS = 10020;
	/** 密码重置失败 */
	public static final int HANDLER_RESET_PWD_FAILURE = 10021;
	/** 重新绑定手机成功 */
	public static final int HANDLER_REBIND_MOBILE_SUCCESS = 10025;
	/** 重新绑定手机失败 */
	public static final int HANDLER_REBIND_MOBILE_FAILURE = 10026;
	/** 登录成功 */
	public static final int HANDLER_LOGIN_SUCCESS = 10030;
	/** 登录失败 */
	public static final int HANDLER_LOGIN_FAILURE = 10031;
	/** 退出登录成功 */
	public static final int HANDLER_LOGOUT_SUCCESS = 10032;

	// *************************** 更新个人资料*******************
	/** 资料获取成功 */
	public static final int REFRESH_ACCOUNT_INFO_SUCCESS = 10040;
	/** 修改资料成功 */
	public static final int MODIFY_ACCOUNT_INFO_SUCCESS = 10045;
	/** 修改资料失败 */
	public static final int MODIFY_ACCOUNT_INFO_FAILURE = 10046;
	/** 获取发票设置成功 */
	public static final int REFRESH_INVOICE_SUCCESS = 10050;
	/** 发票设置修改成功 */
	public static final int MODIFY_INVOICE_SUCCESS = 10055;
	/** 发票设置修改失败 */
	public static final int MODIFY_INVOICE_FAILURE = 10056;
	/** 会员信息获取成功 */
	public static final int VIP_INFO_REFRESH_SUCCESS = 10060;
	// **************************修改认证信息************************
	/** 修改个人认证成功 */
	public static final int CERTIFY_SELF_MODIFY_SUCCESS = 10065;
	/** 修改个人认证失败 */
	public static final int CERTIFY_SELF_MODIFY_FAILURE = 10066;
	/** 修改单位认证成功 */
	public static final int CERTIFY_COMPANY_MODIFY_SUCCESS = 10070;
	/** 修改单位认证失败 */
	public static final int CERTIFY_COMPANY_MODIFY_FAILURE = 10071;
	/** 图片上传成功 */
	public static final int IMAGE_UPLOAD_SUCCESS = 10073;
	/** 图片上传失败 */
	public static final int IMAGE_UPLOAD_FAILURE = 10074;

	// *************************** 银行账户*******************
	/** 添加银行账户成功 */
	public static final int HANDLER_ADD_BANK_INFO_SUCCESS = 10075;
	/** 添加银行账户失败 */
	public static final int HANDLER_ADD_BANK_INFO_FAILURE = 10076;
	/** 删除银行账户成功 */
	public static final int HANDLER_DEL_BANK_INFO_SUCCESS = 10080;
	/** 删除银行账户失败 */
	public static final int HANDLER_DEL_BANK_INFO_FAILURE = 10081;
	/** 修改银行账户成功 */
	public static final int HANDLER_MODIFY_BANK_INFO_SUCCESS = 10095;
	/** 修改银行账户失败 */
	public static final int HANDLER_MODIFY_BANK_INFO_FAILURE = 10096;

	// ***************************地址管理*******************
	/** 添加地址账户成功 */
	public static final int HANDLER_ADD_ADDR_INFO_SUCCESS = 10100;
	/** 添加银行账户失败 */
	public static final int HANDLER_ADD_ADDR_INFO_FAILURE = 10101;
	/** 删除银行账户成功 */
	public static final int HANDLER_DEL_ADDR_INFO_SUCCESS = 10105;
	/** 删除银行账户失败 */
	public static final int HANDLER_DEL_ADDR_INFO_FAILURE = 10106;
	/** 修改银行账户成功 */
	public static final int HANDLER_MODIFY_ADDR_INFO_SUCCESS = 10110;
	/** 修改银行账户失败 */
	public static final int HANDLER_MODIFY_ADDR_INFO_FAILURE = 10111;

	// **************************苗木资源********************************
	/** 刷新苗木资源列表成功 */
	public static final int REFRESH_SEEDLING_LIST_SYNC_SUCCESS = 10115;
	/** 刷新苗木资源列表失败 */
	public static final int REFRESH_SEEDLING_LIST_SYNC_FAILURE = 10116;
	/** 加载苗木资源列表成功 */
	public static final int LOAD_MORE_SEEDLING_LIST_SUCCESS = 10120;
	/** 加载苗木资源列表失败 */
	public static final int LOAD_MORE_SEEDLING_LIST_FAILURE = 10121;
	/** 获取苗木资源详情成功 */
	public static final int GET_SEEDLING_DETAIL_SUCCESS = 10125;
	/** 获取苗木资源详情失败 */
	public static final int GET_SEEDLING_DETAIL_FAILURE = 10126;
	/** 获取苗木基本信息成功 */
	public static final int GET_SEEDLING_BASE_INFO_SUCCESS = 10130;
	/** 获取苗木基本信息失败 */
	public static final int GET_SEEDLING_BASE_INFO_FAILURE = 10131;

	// **************************订阅********************************
	/** 添加订阅成功 */
	public static final int HANDLER_ADD_SUBSCRIBE_SUCCESS = 10140;
	/** 添加订阅失败 */
	public static final int HANDLER_ADD_SUBSCRIBE_FAILURE = 10141;
	/** 添加订阅已存在 */
	public static final int HANDLER_ADD_SUBSCRIBE_EXISTING = 10142;
	/** 添加订阅不存在 */
	public static final int HANDLER_ADD_SUBSCRIBE_NO_EXISTING = 10143;
	/** 删除订阅成功 */
	public static final int HANDLER_DEL_SUBSCRIBE_SUCCESS = 10145;
	/** 删除订阅失败 */
	public static final int HANDLER_DEL_SUBSCRIBE_FAILURE = 10146;
	/** 获取订阅列表成功 */
	public static final int GET_SUBSCRIBE_LIST_SUCCEESS = 10147;
	/** 获取订阅列表失败 */
	public static final int GET_SUBSCRIBE_LIST_FAILURE = 10148;

	// *************************发布采购信息***************************
	/** 发布采购信息成功 */
	public static final int HANDLER_PUBLISH_PURCHASE_SUCCESS = 10160;
	/** 发布采购信息失败 */
	public static final int HANDLER_PUBLISH_PURCHASE_FAILUER = 10161;

	// *************************管理采购信息***************************
	/** 刷新采购信息列表成功 */
	public static final int REFRESH_PURCHASE_LIST_SYNC_SUCCESS = 10200;
	/** 刷新采购信息列表失败 */
	public static final int REFRESH_PURCHASE_LIST_SYNC_FAILURE = 10201;
	/** 加载采购信息列表成功 */
	public static final int LOAD_MORE_PURCHASE_LIST_SUCCESS = 10205;
	/** 加载采购信息列表失败 */
	public static final int LOAD_MORE_PURCHASE_LIST_FAILURE = 10206;
	/** 刷新采购信息项目明细列表成功 */
	public static final int REFRESH_PURCHASE_DETAIL_LIST_SUCCESS = 10210;
	/** 刷新采购信息项目明细列表失败 */
	public static final int REFRESH_PURCHASE_DETAIL_LIST_FAILURE = 10211;
	/** 刷新采购信息项目信息成功 */
	public static final int REFRESH_PURCHASE_SUCCESS = 10215;
	/** 刷新采购信息项目信息失败 */
	public static final int REFRESH_PURCHASE_FAILURE = 10216;
	/** 采购信息项目修改信息成功 */
	public static final int MODIFY_PURCHASE_SUCCESS = 10220;
	/** 采购信息项目修改信息失败 */
	public static final int MODIFY_PURCHASE_FAILURE = 10221;
	/** 判断是否可以关闭采购信息成功 */
	public static final int TO_CLOSE_PURCHASE_SUCCESS = 10222;
	/** 判断是否可以广播采购信息失败 */
	public static final int TO_CLOSE_PURCHASE_FAILURE = 10223;
	/** 刷新采购信息品种信息成功 */
	public static final int REFRESH_PURCHASE_SEED_SUCCESS = 10225;
	/** 刷新采购信息品种信息失败 */
	public static final int REFRESH_PURCHASE_SEED_FAILURE = 10226;
	/** 采购信息修改品种信息成功 */
	public static final int MODIFY_PURCHASE_SEED_SUCCESS = 10230;
	/** 采购信息修改品种信息失败 */
	public static final int MODIFY_PURCHASE_SEED_FAILURE = 10231;
	/** 采购信息关闭品种信息失败 */
	public static final int CLOSE_PURCHASE_SEED_FAILURE = 10232;
	/** 刷新采购信息报价列表成功 */
	public static final int GET_PURCHASE_PRICE_LIST_SUCCESS = 10240;
	/** 刷新采购信息报价列表失败 */
	public static final int GET_PURCHASE_PRICE_LIST_FAILURE = 10241;
	/** 加载采购信息报价列表成功 */
	public static final int LOAD_PURCHASE_PRICE_LIST_SUCCESS = 10245;
	/** 加载采购信息报价列表失败 */
	public static final int LOAD_PURCHASE_PRICE_LIST_FAILURE = 10246;

	// ****************************购物车************************************
	/** 加入购物车成功 */
	public static final int CREATE_SHOPPING_SUCCESS = 11000;
	/** 加入购物车失败 */
	public static final int CREATE_SHOPPING_FAILURE = 11001;
	/** 刷新购物车列表成功 */
	public static final int REFRESH_SHOPCAR_LIST_SUCCESS = 11010;
	/** 刷新购物车列表失败 */
	public static final int REFRESH_SHOPCAR_LIST_FAILURE = 11011;
	/** 加载购物车列表成功 */
	public static final int LOAD_MORE_SHOPCAR_LIST_SUCCESS = 11015;
	/** 加载购物车列表失败 */
	public static final int LOAD_MORE_SHOPCAR_LIST_FAILURE = 11016;
	/** 加载购物车详情成功 */
	public static final int GET_SHOPCAR_DETAIL_SUCCESS = 11020;
	/** 加载购物车详情失败 */
	public static final int GET_SHOPCAR_DETAIL_FAILURE = 11021;
	/** 购物车详情修改成功 */
	public static final int MODIFY_SHOPCAR_DETAIL_SUCCESS = 11025;
	/** 购物车详情修改失败 */
	public static final int MODIFY_SHOPCAR_DETAIL_FAILURE = 11026;
	/** 删除购物车成功 */
	public static final int DEL_SHOPCAR_DETAIL_SUCCESS = 11030;
	/** 删除购物车失败 */
	public static final int DEL_SHOPCAR_DETAIL_FAILURE = 11031;
	/** 刷新购物车数量 */
	public static final int GET_CART_NUM_FINISH = 11050;

	/** 加入采购订单成功 */
	public static final int CREATE_PURCHASE_ORDER_SUCCESS = 12000;
	/** 加入采购订单失败 */
	public static final int CREATE_PURCHASE_ORDER_FAILURE = 12001;

	// *************************管理采购订单***************************
	/** 刷新采购订单成功 */
	public static final int REFRESH_PURCHASE_ORDER_LIST_SYNC_SUCCESS = 20001;
	/** 刷新采购订单失败 */
	public static final int REFRESH_PURCHASE_ORDER_LIST_SYNC_FAILURE = 20002;
	/** 加载采购订单成功 */
	public static final int LOAD_MORE_PURCHASE_ORDER_LIST_SUCCESS = 20010;
	/** 加载采购订单失败 */
	public static final int LOAD_MORE_PURCHASE_ORDER_LIST_FAILURE = 20011;
	/** 获取采购订单详情成功 */
	public static final int GET_PURCHASE_ORDER_DETAIL_SUCCESS = 20020;
	/** 获取采购订单详情失败 */
	public static final int GET_PURCHASE_ORDER_DETAIL_FAILURE = 20021;
	/** 获取订单收货地址成功 */
	public static final int GET_ORDER_ADDRESS_LIST_SUCCESS = 21000;
	/** 获取订单收货地址失败 */
	public static final int GET_ORDER_ADDRESS_LIST_FAILURE = 21001;
	/** 添加订单收货地址成功 */
	public static final int ADD_ORDER_ADDRESS_SUCCESS = 21100;
	/** 添加订单收货地址失败 */
	public static final int ADD_ORDER_ADDRESS_FAILURE = 21101;
	/** 修改订单收货地址成功 */
	public static final int MODIFY_ORDER_ADDRESS_SUCCESS = 21200;
	/** 修改订单收货地址失败 */
	public static final int MODIFY_ORDER_ADDRESS_FAILURE = 21201;
	/** 删除订单收货地址成功 */
	public static final int DEL_ORDER_ADDRESS_SUCCESS = 21300;
	/** 删除订单收货地址失败 */
	public static final int DEL_ORDER_ADDRESS_FAILURE = 21301;
	/** 获取装车信息成功 */
	public static final int GET_ORDER_CAR_INFO_LIST_SUCCESS = 22010;
	/** 获取装车信息失败 */
	public static final int GET_ORDER_CAR_INFO_LIST_FAILURE = 22011;

	// *************************管理采购订单***************************
	/** 管理采购订单付款成功 */
	public static final int PURCHASE_ORDER_PAY_SUCCESS = 20030;
	/** 管理采购订单付款失败 */
	public static final int PURCHASE_ORDER_PAY_FAILURE = 20031;
	/** 获取帐户信息成功 */
	public static final int GET_PLAT_ACCOUNT_SUCCESS = 20032;
	/** 获取帐户信息失败 */
	public static final int GET_PLAT_ACCOUNT_FAILURE = 20033;
	/** 更新采购订单成功 */
	public static final int UPDATE_PURCHASE_ORDER_SUCCESS = 20040;
	/** 更新采购订单失败 */
	public static final int UPDATE_PURCHASE_ORDER_FAILURE = 20041;
	/** 添加采购苗木成功 */
	public static final int ADD_PURCHASE_FLOWER_SUCCESS = 20050;
	/** 添加采购苗木失败 */
	public static final int ADD_PURCHASE_FLOWER_FAILURE = 20051;
	/** 修改采购苗木成功 */
	public static final int MODIFY_PURCHASE_FLOWER_SUCCESS = 20055;
	/** 修改采购苗木失败 */
	public static final int MODIFY_PURCHASE_FLOWER_FAILURE = 20056;
	/** 获取订单报价信息成功 */
	public static final int GET_ORDER_QUOTE_INFO_SUCCESS = 20060;
	/** 获取订单报价信息失败 */
	public static final int GET_ORDER_QUOTE_INFO_FAILURE = 20061;
	/** 修改装车信息成功 */
	public static final int MODIFY_CARINFO_SUCCESS = 20070;
	/** 修改装车信息失败 */
	public static final int MODIFY_CARINFO_FAILURE = 20071;

	// *************************首页搜索***************************
	/** 首页搜索成功 */
	public static final int HOME_SEARCH_RESULT_SUCCESS = 20101;
	/** 首页搜索失败 */
	public static final int HOME_SEARCH_RESULT_FAILURE = 20102;
	/** 卖家苗木资源搜索成功 */
	public static final int SELLER_SEEDLING_SEARCH_SUCCESS = 20103;
	/** 卖家苗木资源搜索失败 */
	public static final int SELLER_SEEDLING_SEARCH_FAILURE = 20104;

	// *************************查看销售订单***************************
	/** 刷新销售订单成功 */
	public static final int REFRESH_SELLER_ORDER_LIST_SUCCESS = 20200;
	/** 刷新销售订单失败 */
	public static final int REFRESH_SELLER_ORDER_LIST_FAILURE = 20201;
	/** 加载销售订单成功 */
	public static final int LOAD_SELLER_ORDER_LIST_SUCCESS = 20202;
	/** 加载销售订单失败 */
	public static final int LOAD_SELLER_ORDER_LIST_FAILURE = 20203;
	/** 获取销售订单详情成功 */
	public static final int GET_SELLER_ORDER_ORDER_DETAIL_SUCCESS = 20210;
	/** 获取销售订单失败 */
	public static final int GET_SELLER_ORDER_ORDER_DETAIL_FAILURE = 20211;

	// *************************我的苗木经纪***************************
	/** 刷新苗木经纪成功 */
	public static final int REFRESH_USER_AGENT_LIST_SUCCESS = 20220;
	/** 刷新苗木经纪失败 */
	public static final int REFRESH_USER_AGENT_LIST_FAILURE = 20221;
	/** 加载苗木经纪成功 */
	public static final int LOAD_MORE_USER_AGENT_LIST_SUCCESS = 20222;
	/** 加载苗木经纪失败 */
	public static final int LOAD_MORE_USER_AGENT_LIST_FAILURE = 20223;
	/** 获取我的苗木经纪成功 */
	public static final int GET_USER_AGENT_SUCCESS = 20224;
	/** 获取我的苗木经纪失败 */
	public static final int GET_USER_AGENT_FAILURE = 20225;
	/** 绑定我的苗木经纪成功 */
	public static final int BIND_USER_AGENT_SUCCESS = 20226;
	/** 绑定我的苗木经纪失败 */
	public static final int BIND_USER_AGENT_FAILURE = 20227;

	// *************************我的苗木经纪***************************
	/** 发布苗木资源添加苗木成功 */
	public static final int PUBLISH_FLOWER_INFO_ADD_FLOWER_SUCCESS = 20300;
	/** 发布苗木资源添加苗木失败 */
	public static final int PUBLISH_FLOWER_INFO_ADD_FLOWER_FAILURE = 20301;
	/** 发布苗木资源成功 */
	public static final int PUBLISH_FLOWER_INFO_SUCCESS = 20310;
	/** 发布苗木资源失败 */
	public static final int PUBLISH_FLOWER_INFO_FAILURE = 20311;

	// *************************保证金信息***************************
	/** 刷新冻结保证金列表成功 */
	public static final int REFRESH_FROZEN_FUND_LIST_SUCCESS = 30001;
	/** 刷新冻结保证金列表失败 */
	public static final int REFRESH_FROZEN_FUND_LIST_FAILURE = 30002;
	/** 加载冻结保证金列表成功 */
	public static final int LOAD_MORE_FROZEN_FUND_LIST_SUCCESS = 30003;
	/** 加载冻结保证金列表失败 */
	public static final int LOAD_MORE_FROZEN_FUND_LIST_FAILURE = 30004;
	/** 刷新保证金明细列表成功 */
	public static final int REFRESH_FUND_DETAIL_LIST_SUCCESS = 30011;
	/** 刷新保证金明细列表失败 */
	public static final int REFRESH_FUND_DETAIL_LIST_FAILURE = 30012;
	/** 加载保证金明细列表成功 */
	public static final int LOAD_MORE_FUND_DETAIL_LIST_SUCCESS = 30013;
	/** 加载保证金明细列表失败 */
	public static final int LOAD_MORE_FUND_DETAIL_LIST_FAILURE = 30014;
	/** 保证金缴纳成功 */
	public static final int FUND_PAY_SUCCESS = 30015;
	/** 保证金缴纳失败 */
	public static final int FUND_PAY_FAILURE = 30016;
	/** 保证金退回成功 */
	public static final int DEPOSIT_FUND_RETURN_SUCCESS = 30050;
	/** 保证金退回失败 */
	public static final int DEPOSIT_FUND_RETURN_FAILURE = 30051;
	/** 查询单个银行帐号成功 */
	public static final int SELECT_SINGLE_BANKINFO_SUCCESS = 30060;
	/** 查询单个银行帐号失败 */
	public static final int SELECT_SINGLE_BANKINFO_FAILURE = 30061;
	// ************************报价结果查看***************************
	/** 刷新报价结果列表成功 */
	public static final int REFRESH_PRICE_RESULT_LIST_SUCCESS = 30200;
	/** 刷新报价结果列表失败 */
	public static final int REFRESH_PRICE_RESULT_LIST_FAILURE = 30201;
	/** 加载报价结果列表成功 */
	public static final int LOAD_MORE_PRICE_RESULT_LIST_SUCCESS = 30205;
	/** 加载报价结果列表失败 */
	public static final int LOAD_MORE_PRICE_RESULT_LIST_FAILURE = 30206;
	/** 查询报价结果详情成功 */
	public static final int GET_PRICE_RESULT_DETAIL_SUCCESS = 30210;
	/** 查询报价结果详情失败 */
	public static final int GET_PRICE_RESULT_DETAIL_FAILURE = 30211;
	// ************************采购信息报价*****************************
	/** 刷新采购信息列表成功（卖家） */
	public static final int REFRESH_SELLER_PURCHASE_LIST_SUCCESS = 30300;
	/** 刷新采购信息列表失败（卖家） */
	public static final int REFRESH_SELLER_PURCHASE_LIST_FAILURE = 30301;
	/** 加载采购信息列表成功（卖家） */
	public static final int LOAD_MORE_SELLER_PURCHASE_LIST_SUCCESS = 30305;
	/** 加载采购信息列表失败（卖家） */
	public static final int LOAD_MORE_SELLER_PURCHASE_LIST_FAILURE = 30206;
	/** 查询采购信息报价详情成功（卖家） */
	public static final int GET_SELLER_DETAIL_PURCHASE_SUCCESS = 30310;
	/** 查询采购信息报价详情失败（卖家） */
	public static final int GET_SELLER_DETAIL_PURCHASE_FAILURE = 30311;
	/** 修改采购报价成功（卖家） */
	public static final int MODIFY_SELLER_PRICE_SUCCESS = 30410;
	/** 修改采购报价失败（卖家） */
	public static final int MODIFY_SELLER_PRICE_FAILURE = 30411;
	/** 添加采购报价成功（卖家） */
	public static final int ADD_SELLER_PRICE_SUCCESS = 30510;
	/** 添加采购报价失败（卖家） */
	public static final int ADD_SELLER_PRICE_FAILURE = 30511;
	/** 添加采购报价图片成功（卖家） */
	public static final int ADD_SELLER_PRICE_PIC_SUCCESS = 30610;
	/** 添加采购报价图片失败（卖家） */
	public static final int ADD_SELLER_PRICE_PIC_FAILURE = 30611;
	// *************************管理苗木资源*******************************
	/** 刷新苗木资源列表成功（卖家） */
	public static final int REFRESH_SELLER_SEED_LIST_SUCCESS = 30400;
	/** 刷新苗木资源列表失败（卖家） */
	public static final int REFRESH_SELLER_SEED_LIST_FAILURE = 30401;
	/** 加载苗木资源列表成功（卖家） */
	public static final int LOAD_MORE_SELLER_SEED_LIST_SUCCESS = 30405;
	/** 加载采苗木资源列表失败（卖家） */
	public static final int LOAD_MORE_SELLER_SEED_LIST_FAILURE = 30406;
	/** 获取苗木资源详情成功（卖家） */
	public static final int GET_SELLER_SEED_DETAIL_SUCCESS = 30505;
	/** 获取苗木资源详情失败（卖家） */
	public static final int GET_SELLER_SEED_DETAIL_FAILURE = 30506;
	/** 修改苗木资源详情成功（卖家） */
	public static final int MODIFY_SELLER_SEED_DETAIL_SUCCESS = 30605;
	/** 修改苗木资源详情失败（卖家） */
	public static final int MODIFY_SELLER_SEED_DETAIL_FAILURE = 30606;
	// *************************站内信***************************
	/** 获取站内信类型成功 */
	public static final int GET_SIMPLE_MESSAGE_LIST_SUCCESS = 40001;
	/** 获取站内信类型失败 */
	public static final int GET_SIMPLE_MESSAGE_LIST_FAILURE = 40002;
	/** 刷新站内信列表成功 */
	public static final int REFRESH_MESSAGE_LIST_SUCCESS = 40011;
	/** 刷新站内信列表失败 */
	public static final int REFRESH_MESSAGE_LIST_FAILURE = 40012;
	/** 加载站内信列表成功 */
	public static final int LOAD_MORE_MESSAGE_LIST_SUCCESS = 40013;
	/** 加载站内信列表失败 */
	public static final int LOAD_MORE_MESSAGE_LIST_FAILURE = 40014;
	/** 删除站内信成功 */
	public static final int DELETE_MESSAGE_SUCCESS = 40015;
	/** 删除站内信成功 */
	public static final int DELETE_MESSAGE_FAILURE = 40016;
	// *************************站内信设置***************************
	/** 获取push设置成功 */
	public static final int GET_PUSH_SET_SUCCESS = 40201;
	/** 获取push设置失败 */
	public static final int GET_PUSH_SET_FAILURES = 40202;
	/** 设置push设置成功 */
	public static final int MODIFY_PUSH_SET_SUCCESS = 40203;
	/** 设置push设置失败 */
	public static final int MODIFY_PUSH_SET_FAILURE = 40204;

	// *************************我的子帐户***************************
	/** 刷新我的子帐户成功 */
	public static final int REFRESH_MY_CHILD_ACCOUNT_LIST_SUCCESS = 40400;
	/** 刷新我的子帐户失败 */
	public static final int REFRESH_MY_CHILD_ACCOUNT_LIST_FAILURE = 40401;
	/** 加载我的子帐户成功 */
	public static final int LOAD_MORE_MY_CHILD_ACCOUNT_LIST_SUCCESS = 40402;
	/** 加载我的子帐户失败 */
	public static final int LOAD_MORE_MY_CHILD_ACCOUNT_LIST_FAILURE = 40403;
	/** 添加帐户成功 */
	public static final int ADD_CHILD_ACCOUNT_SUCCESS = 40404;
	/** 添加帐户失败 */
	public static final int ADD_CHILD_ACCOUNT_FAILURE = 40405;
	/** 获取子帐户详情成功 */
	public static final int GET_CHILD_ACCOUNT_DETAIL_SUCCESS = 40406;
	/** 获取子帐户详情失败 */
	public static final int GET_CHILD_ACCOUNT_DETAIL_FAILURE = 40407;
	/** 添加帐户成员成功 */
	public static final int ADD_CHILD_ACCOUNT_MEMBER_SUCCESS = 40408;
	/** 添加帐户成员失败 */
	public static final int ADD_CHILD_ACCOUNT_MEMBER_FAILURE = 40409;
	/** 更新子帐户成功 */
	public static final int UPDATE_CHILD_ACCOUNT_SUCCESS = 40410;
	/** 更新子帐户失败 */
	public static final int UPDATE_CHILD_ACCOUNT_FAILURE = 40411;

	/** 刷新全部帐户（除自己）成功 */
	public static final int REFRESH_ALL_ACCOUNT_LIST_SUCCESS = 40500;
	/** 刷新全部帐户（除自己）失败 */
	public static final int REFRESH_ALL_ACCOUNT_LIST_FAILURE = 40501;
	/** 加载全部帐户（除自己）成功 */
	public static final int LOAD_MORE_ALL_ACCOUNT_LIST_SUCCESS = 40502;
	/** 加载全部帐户（除自己）失败 */
	public static final int LOAD_MORE_ALL_ACCOUNT_LIST_FAILURE = 40503;

	// *************************收藏夹***************************
	/** 刷新收藏夹成功 */
	public static final int REFRESH_FAVORITE_LIST_SUCCESS = 40600;
	/** 刷新收藏夹失败 */
	public static final int REFRESH_FAVORITE_LIST_FAILURE = 40601;
	/** 加载收藏夹成功 */
	public static final int LOAD_MORE_FAVORITE_LIST_SUCCESS = 40602;
	/** 加载收藏夹失败 */
	public static final int LOAD_MORE_FAVORITE_LIST_FAILURE = 40603;
	/** 加入收藏夹成功 */
	public static final int ADD_FAVORITE_SUCCESS = 40604;
	/** 加入收藏夹失败 */
	public static final int ADD_FAVORITE_FAILURE = 40605;
	/** 取消收藏夹成功 */
	public static final int DELETE_FAVORITE_SUCCESS = 40606;
	/** 取消收藏夹失败 */
	public static final int DELETE_FAVORITE_FAILURE = 40607;
	
	// *************************拍照获取图片***************************
	/**拍照获取图片失败*/
	public static final int PHOTO_TAKE_PIC_FAILURE = 40700;

}
